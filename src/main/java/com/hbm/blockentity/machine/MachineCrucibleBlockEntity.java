package com.hbm.blockentity.machine;

import api.hbm.block.ICrucibleAcceptor;
import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.material.MatDistribution;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.inventory.menus.MachineCrucibleMenu;
import com.hbm.inventory.recipes.CrucibleRecipe;
import com.hbm.inventory.recipes.CrucibleRecipes;
import com.hbm.util.CrucibleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MachineCrucibleBlockEntity extends MachineBaseBlockEntity implements IControlReceiver, ICrucibleAcceptor {

    public static final int RECIPE_CAPACITY = MaterialShapes.BLOCK.q(16);
    public static final int WASTE_CAPACITY = MaterialShapes.BLOCK.q(16);
    public static final int PROCESS_TIME = 20_000;
    public static final int MAX_HEAT = 100_000;
    public static final double DIFFUSION = 0.25D;

    public int heat;
    public int progress;
    public String recipe = "null";
    public final List<MaterialStack> recipeStack = new ArrayList<>();
    public final List<MaterialStack> wasteStack = new ArrayList<>();

    public MachineCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CRUCIBLE.get(), pos, state, 10);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_crucible");
    }

    @Override
    public void updateEntity() {
        if (this.level == null) return;
        if (this.level.isClientSide) return;

        int previousHeat = this.heat;
        int previousProgress = this.progress;
        int previousRecipeAmount = this.getAmount(this.recipeStack, null);
        int previousWasteAmount = this.getAmount(this.wasteStack, null);

        this.pullHeat();
        if (this.level.getGameTime() % 5 == 0) this.collectItems();
        if (!this.trySmelt()) this.progress = 0;
        boolean processedRecipe = this.tryRecipe();
        this.pourStacks();
        this.damageEntities();
        this.recipeStack.removeIf(stack -> stack.amount <= 0);
        this.wasteStack.removeIf(stack -> stack.amount <= 0);
        if (processedRecipe || previousHeat != this.heat || previousProgress != this.progress
                || previousRecipeAmount != this.getAmount(this.recipeStack, null)
                || previousWasteAmount != this.getAmount(this.wasteStack, null)) this.setChanged();
        this.networkPackNT(50);
    }

    private void pullHeat() {
        if (this.heat >= MAX_HEAT) return;
        BlockEntity blockEntity = this.level.getBlockEntity(this.worldPosition.below());
        if (blockEntity instanceof IHeatSource source) {
            int difference = Math.min(source.getHeatStored() - this.heat, MAX_HEAT - this.heat);
            if (difference > 0) {
                int transfer = (int) Math.ceil(difference * DIFFUSION);
                source.useUpHeat(transfer);
                this.heat = Math.min(MAX_HEAT, this.heat + transfer);
                return;
            }
        }
        this.heat = Math.max(0, this.heat - Math.max(this.heat / 1000, 1));
    }

    private void collectItems() {
        AABB collection = new AABB(this.worldPosition.getX() - 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() - 0.5,
                this.worldPosition.getX() + 1.5, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 1.5);
        for (ItemEntity entity : this.level.getEntitiesOfClass(ItemEntity.class, collection)) {
            ItemStack stack = entity.getItem();
            while (!stack.isEmpty() && this.isItemSmeltable(stack)) {
                int slot = this.getFirstEmptySlot();
                if (slot < 0) break;
                this.slots.set(slot, stack.copyWithCount(1));
                stack.shrink(1);
                this.setChanged();
            }
            if (stack.isEmpty()) entity.discard();
            else entity.setItem(stack);
        }
    }

    private boolean trySmelt() {
        if (this.heat < MAX_HEAT / 2) return false;
        int slot = this.getFirstSmeltableSlot();
        if (slot < 0) return false;

        int difference = (int) ((this.heat - MAX_HEAT / 2) * 0.05D);
        if (difference <= 0) return false;
        this.progress += difference;
        this.heat -= difference;

        if (this.progress >= PROCESS_TIME) {
            this.progress = 0;
            for (MaterialStack material : MatDistribution.getSmeltingMaterials(this.slots.get(slot))) {
                if (this.isRecipeMaterial(material.material)) this.addToStack(this.recipeStack, material);
                else this.addToStack(this.wasteStack, material);
            }
            this.removeItem(slot, 1);
        }
        return true;
    }

    private boolean tryRecipe() {
        CrucibleRecipe loaded = this.getLoadedRecipe();
        if (loaded == null || this.level.getGameTime() % loaded.frequency != 0) return false;
        for (MaterialStack input : loaded.input) {
            if (this.getAmount(this.recipeStack, input.material) < input.amount) return false;
        }
        for (MaterialStack input : loaded.input) this.removeFromStack(this.recipeStack, input.material, input.amount);
        for (MaterialStack output : loaded.output) this.addToStack(this.recipeStack, output);
        return true;
    }

    private void pourStacks() {
        Direction direction = this.getBlockState().getValue(DummyableBlock.FACING);
        List<MaterialStack> output = this.getRecipeOutput();
        if (!output.isEmpty()) {
            CrucibleUtil.pourFullStack(this.level,
                    this.worldPosition.getX() + 0.5 + direction.getStepX() * 1.875,
                    this.worldPosition.getY() + 0.25,
                    this.worldPosition.getZ() + 0.5 + direction.getStepZ() * 1.875,
                    6, output, MaterialShapes.NUGGET.q(3));
        }
        Direction opposite = direction.getOpposite();
        if (!this.wasteStack.isEmpty()) {
            CrucibleUtil.pourFullStack(this.level,
                    this.worldPosition.getX() + 0.5 + opposite.getStepX() * 1.875,
                    this.worldPosition.getY() + 0.25,
                    this.worldPosition.getZ() + 0.5 + opposite.getStepZ() * 1.875,
                    6, this.wasteStack, MaterialShapes.NUGGET.q(3));
        }
    }

    private List<MaterialStack> getRecipeOutput() {
        CrucibleRecipe loaded = this.getLoadedRecipe();
        if (loaded == null) return this.recipeStack;
        List<MaterialStack> output = new ArrayList<>();
        for (MaterialStack stored : this.recipeStack) {
            if (this.getAmount(loaded.output, stored.material) > 0) output.add(stored);
        }
        return output;
    }

    private void damageEntities() {
        int total = this.getAmount(this.recipeStack, null) + this.getAmount(this.wasteStack, null);
        if (total <= 0) return;
        double height = (double) total / (RECIPE_CAPACITY + WASTE_CAPACITY) * 0.875D;
        AABB molten = new AABB(this.worldPosition.getX() - 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() - 0.5,
                this.worldPosition.getX() + 1.5, this.worldPosition.getY() + 0.5 + height, this.worldPosition.getZ() + 1.5);
        for (Entity entity : this.level.getEntities((Entity) null, molten, Entity::isAlive)) {
            entity.hurt(this.level.damageSources().lava(), 5F);
            entity.setRemainingFireTicks(Math.max(entity.getRemainingFireTicks(), 100));
        }
    }

    private int getFirstSmeltableSlot() {
        for (int slot = 1; slot < 10; slot++) if (this.isItemSmeltable(this.slots.get(slot))) return slot;
        return -1;
    }

    private int getFirstEmptySlot() {
        for (int slot = 1; slot < 10; slot++) if (this.slots.get(slot).isEmpty()) return slot;
        return -1;
    }

    public boolean isItemSmeltable(ItemStack stack) {
        List<MaterialStack> materials = MatDistribution.getSmeltingMaterials(stack);
        if (materials.isEmpty()) return false;
        CrucibleRecipe loaded = this.getLoadedRecipe();
        boolean matchesRecipe = loaded == null;
        int recipeContent = loaded == null ? 0 : loaded.getInputAmount();
        int recipeAmount = this.getAmount(this.recipeStack, null);
        int wasteAmount = this.getAmount(this.wasteStack, null);
        Map<NTMMaterial, Integer> additions = new LinkedHashMap<>();

        for (MaterialStack material : materials) {
            int inputRequired = loaded == null ? 0 : this.getAmount(loaded.input, material.material);
            if (loaded != null && this.getAmount(loaded.output, material.material) > 0) {
                recipeAmount += material.amount;
                additions.merge(material.material, material.amount, Integer::sum);
                matchesRecipe = true;
            } else if (inputRequired == 0) {
                wasteAmount += material.amount;
            } else {
                int maximum = inputRequired * RECIPE_CAPACITY / recipeContent;
                int alreadyAdded = additions.getOrDefault(material.material, 0);
                if (this.getAmount(this.recipeStack, material.material) + alreadyAdded + material.amount > maximum) return false;
                recipeAmount += material.amount;
                additions.merge(material.material, material.amount, Integer::sum);
                matchesRecipe = true;
            }
        }
        return recipeAmount <= RECIPE_CAPACITY && wasteAmount <= WASTE_CAPACITY && matchesRecipe;
    }

    private boolean isRecipeMaterial(NTMMaterial material) {
        CrucibleRecipe loaded = this.getLoadedRecipe();
        return loaded != null && (this.getAmount(loaded.input, material) > 0 || this.getAmount(loaded.output, material) > 0);
    }

    private void addToStack(List<MaterialStack> materials, MaterialStack incoming) {
        for (MaterialStack stored : materials) {
            if (stored.material == incoming.material) {
                stored.amount += incoming.amount;
                return;
            }
        }
        materials.add(incoming.copy());
    }

    private void removeFromStack(List<MaterialStack> materials, NTMMaterial material, int amount) {
        for (MaterialStack stored : materials) {
            if (stored.material == material) {
                stored.amount -= amount;
                return;
            }
        }
    }

    public int getAmount(List<MaterialStack> materials, NTMMaterial material) {
        int amount = 0;
        for (MaterialStack stack : materials) {
            if (material == null || stack.material == material) amount += stack.amount;
        }
        return amount;
    }

    private int getAmount(MaterialStack[] materials, NTMMaterial material) {
        int amount = 0;
        for (MaterialStack stack : materials) if (material == null || stack.material == material) amount += stack.amount;
        return amount;
    }

    public CrucibleRecipe getLoadedRecipe() {
        return CrucibleRecipes.INSTANCE.recipeNameMap.get(this.recipe);
    }

    public List<MaterialStack> drainAllMaterials() {
        List<MaterialStack> result = new ArrayList<>();
        for (MaterialStack stack : this.recipeStack) result.add(stack.copy());
        for (MaterialStack stack : this.wasteStack) result.add(stack.copy());
        this.recipeStack.clear();
        this.wasteStack.clear();
        this.setChanged();
        return result;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot > 0 && this.isItemSmeltable(stack);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("recipe", this.recipe);
        tag.putInt("progress", this.progress);
        tag.putInt("heat", this.heat);
        tag.put("recipeMaterials", this.writeMaterials(this.recipeStack));
        tag.put("wasteMaterials", this.writeMaterials(this.wasteStack));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        String loadedRecipe = tag.getString("recipe");
        this.recipe = loadedRecipe.isEmpty() ? "null" : loadedRecipe;
        this.progress = tag.getInt("progress");
        this.heat = tag.getInt("heat");
        if (tag.contains("recipeMaterials")) this.readMaterials(tag.getCompound("recipeMaterials"), this.recipeStack);
        else this.readLegacyMaterials(tag.getIntArray("recipeStack"), this.recipeStack);
        if (tag.contains("wasteMaterials")) this.readMaterials(tag.getCompound("wasteMaterials"), this.wasteStack);
        else this.readLegacyMaterials(tag.getIntArray("wasteStack"), this.wasteStack);
    }

    private CompoundTag writeMaterials(List<MaterialStack> materials) {
        CompoundTag data = new CompoundTag();
        for (MaterialStack material : materials) data.putInt(material.material.getCanonicalName(), material.amount);
        return data;
    }

    private void readMaterials(CompoundTag data, List<MaterialStack> materials) {
        materials.clear();
        for (String name : data.getAllKeys()) {
            NTMMaterial material = Mats.matByName.get(name);
            int amount = data.getInt(name);
            if (material != null && amount > 0) materials.add(new MaterialStack(material, amount));
        }
    }

    private void readLegacyMaterials(int[] data, List<MaterialStack> materials) {
        materials.clear();
        for (int i = 0; i + 1 < data.length; i += 2) {
            NTMMaterial material = Mats.matById.get(data[i]);
            if (material != null && data[i + 1] > 0) materials.add(new MaterialStack(material, data[i + 1]));
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeVarInt(this.progress);
        buf.writeVarInt(this.heat);
        buf.writeUtf(this.recipe);
        this.writeMaterials(buf, this.recipeStack);
        this.writeMaterials(buf, this.wasteStack);
    }

    private void writeMaterials(RegistryFriendlyByteBuf buf, List<MaterialStack> materials) {
        buf.writeVarInt(materials.size());
        for (MaterialStack material : materials) {
            buf.writeUtf(material.material.getCanonicalName());
            buf.writeVarInt(material.amount);
        }
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.progress = buf.readVarInt();
        this.heat = buf.readVarInt();
        this.recipe = buf.readUtf();
        this.readMaterials(buf, this.recipeStack);
        this.readMaterials(buf, this.wasteStack);
    }

    private void readMaterials(RegistryFriendlyByteBuf buf, List<MaterialStack> materials) {
        materials.clear();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            NTMMaterial material = Mats.matByName.get(buf.readUtf());
            int amount = buf.readVarInt();
            if (material != null && amount > 0) materials.add(new MaterialStack(material, amount));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineCrucibleMenu(id, inventory, this);
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if (tag.contains("index") && tag.contains("selection") && tag.getInt("index") == 0) {
            this.recipe = tag.getString("selection");
            this.progress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canAcceptPartialPour(Level level, Vec3 hit, Direction side, MaterialStack stack) {
        return side == Direction.UP && this.getMoltenAcceptance(stack) > 0;
    }

    @Override
    public MaterialStack pour(Level level, Vec3 hit, Direction side, MaterialStack stack) {
        if (!this.canAcceptPartialPour(level, hit, side, stack)) return stack;
        int accepted = this.getMoltenAcceptance(stack);
        if (accepted <= 0) return stack;
        List<MaterialStack> destination = this.getLoadedRecipe() == null ? this.wasteStack : this.recipeStack;
        this.addToStack(destination, new MaterialStack(stack.material, accepted));
        stack.amount -= accepted;
        this.setChanged();
        return stack.amount > 0 ? stack : null;
    }

    @Override
    public boolean canAcceptPartialFlow(Level level, Direction side, MaterialStack stack) {
        return false;
    }

    @Override
    public MaterialStack flow(Level level, Direction side, MaterialStack stack) {
        return stack;
    }

    private int getMoltenAcceptance(MaterialStack stack) {
        if (stack == null || stack.material == null || stack.amount <= 0) return 0;
        CrucibleRecipe loaded = this.getLoadedRecipe();
        if (loaded == null) return Math.min(stack.amount, WASTE_CAPACITY - this.getAmount(this.wasteStack, null));
        int required = this.getAmount(loaded.input, stack.material);
        if (required <= 0) return 0;
        int maximum = required * RECIPE_CAPACITY / loaded.getInputAmount();
        int availableForMaterial = maximum - this.getAmount(this.recipeStack, stack.material);
        int availableOverall = RECIPE_CAPACITY - this.getAmount(this.recipeStack, null);
        return Math.max(0, Math.min(stack.amount, Math.min(availableForMaterial, availableOverall)));
    }
}
