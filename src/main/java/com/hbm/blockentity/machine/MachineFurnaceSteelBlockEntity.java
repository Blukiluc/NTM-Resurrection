package com.hbm.blockentity.machine;

import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;
import com.hbm.inventory.menus.MachineFurnaceSteelMenu;
import com.hbm.items.NtmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class MachineFurnaceSteelBlockEntity extends MachineBaseBlockEntity {

    public static final int SLOT_INPUT_1 = 0;
    public static final int SLOT_INPUT_2 = 1;
    public static final int SLOT_INPUT_3 = 2;
    public static final int SLOT_OUTPUT_1 = 3;
    public static final int SLOT_OUTPUT_2 = 4;
    public static final int SLOT_OUTPUT_3 = 5;

    public static final int PROCESS_TIME = 40_000;
    public static final int MAX_HEAT = 100_000;
    public static final double DIFFUSION = 0.05D;

    public int[] progress = new int[3];
    public int[] bonus = new int[3];
    public int heat;
    public boolean wasOn;

    private final ItemStack[] lastItems = new ItemStack[3];

    public MachineFurnaceSteelBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FURNACE_STEEL.get(), pos, state, 6);
        Arrays.fill(this.lastItems, ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furnace_steel");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.spawnWorkingParticles();
            return;
        }

        int previousHeat = this.heat;
        this.tryPullHeat();
        this.wasOn = false;
        int burn = (this.heat - MAX_HEAT / 3) / 10;
        boolean changed = this.heat != previousHeat;

        for(int index = 0; index < 3; index++) {
            ItemStack input = this.slots.get(index);
            if(input.isEmpty() || this.lastItems[index].isEmpty() || !ItemStack.isSameItemSameComponents(input, this.lastItems[index])) {
                this.progress[index] = 0;
                this.bonus[index] = 0;
            }

            if(this.canSmelt(index)) {
                this.progress[index] += burn;
                this.heat -= burn;
                this.wasOn = true;
                changed = true;

                if(this.level.getGameTime() % 20 == 0) {
                    PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionType.SOOT, PollutionHandler.SOOT_PER_SECOND * 2.0F);
                }
            }

            this.lastItems[index] = input.copy();

            if(this.progress[index] >= PROCESS_TIME) {
                this.smeltItem(index);
                this.progress[index] = 0;
                changed = true;
            }
        }

        if(changed) this.setChanged();
        this.networkPackNT(50);
    }

    private void spawnWorkingParticles() {
        if(!this.wasOn) return;

        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();

        this.level.addParticle(
                ParticleTypes.SMOKE,
                this.worldPosition.getX() + 0.5D - dir.getStepX() * 1.125D - rot.getStepX() * 0.75D,
                this.worldPosition.getY() + 2.625D,
                this.worldPosition.getZ() + 0.5D - dir.getStepZ() * 1.125D - rot.getStepZ() * 0.75D,
                0.0D,
                0.05D,
                0.0D
        );

        if(this.level.random.nextInt(20) == 0) {
            this.level.addParticle(
                    ParticleTypes.CLOUD,
                    this.worldPosition.getX() + 0.5D + dir.getStepX() * 0.75D,
                    this.worldPosition.getY() + 2.0D,
                    this.worldPosition.getZ() + 0.5D + dir.getStepZ() * 0.75D,
                    0.0D,
                    0.05D,
                    0.0D
            );
        }

        if(this.level.random.nextInt(15) == 0) {
            this.level.addParticle(
                    ParticleTypes.LAVA,
                    this.worldPosition.getX() + 0.5D + dir.getStepX() * 1.5D + rot.getStepX() * (this.level.random.nextDouble() - 0.5D),
                    this.worldPosition.getY() + 0.75D,
                    this.worldPosition.getZ() + 0.5D + dir.getStepZ() * 1.5D + rot.getStepZ() * (this.level.random.nextDouble() - 0.5D),
                    dir.getStepX() * 0.5D,
                    0.05D,
                    dir.getStepZ() * 0.5D
            );
        }
    }

    public boolean canSmelt(int index) {
        if(this.heat < MAX_HEAT / 3) return false;

        ItemStack result = this.getSmeltingResult(this.slots.get(index));
        if(result.isEmpty()) return false;

        ItemStack output = this.slots.get(index + 3);
        if(output.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void smeltItem(int index) {
        ItemStack input = this.slots.get(index);
        ItemStack result = this.getSmeltingResult(input);
        if(result.isEmpty()) return;

        ItemStack output = this.slots.get(index + 3);
        if(output.isEmpty()) {
            this.slots.set(index + 3, result.copy());
            output = this.slots.get(index + 3);
        } else {
            output.grow(result.getCount());
        }

        this.addBonus(input, index);
        while(this.bonus[index] >= 100) {
            output.setCount(Math.min(output.getMaxStackSize(), output.getCount() + result.getCount()));
            this.bonus[index] -= 100;
        }

        input.shrink(1);
        if(input.isEmpty()) this.slots.set(index, ItemStack.EMPTY);
    }

    private void addBonus(ItemStack stack, int index) {
        String itemPath = stack.getItem().builtInRegistryHolder().key().location().getPath();
        boolean ore = itemPath.startsWith("ore_") || itemPath.endsWith("_ore") || stack.getTags().anyMatch(tag -> {
            String namespace = tag.location().getNamespace();
            String path = tag.location().getPath();
            return namespace.equals("c") && (path.equals("ores") || path.startsWith("ores/"));
        });

        if(ore) {
            this.bonus[index] += 25;
            return;
        }

        if(stack.is(ItemTags.LOGS)) {
            this.bonus[index] += 50;
            return;
        }

        if(stack.is(NtmItems.TAR_OIL.get())
                || stack.is(NtmItems.TAR_CRACK_OIL.get())
                || stack.is(NtmItems.TAR_COAL.get())
                || stack.is(NtmItems.TAR_WOOD.get())) {
            this.bonus[index] += 50;
        }
    }

    private void tryPullHeat() {
        if(this.heat < MAX_HEAT) {
            BlockEntity blockEntity = this.level.getBlockEntity(this.worldPosition.below());
            if(blockEntity instanceof IHeatSource source) {
                int diff = source.getHeatStored() - this.heat;
                if(diff == 0) return;

                if(diff > 0) {
                    int pulled = (int) Math.ceil(diff * DIFFUSION);
                    int accepted = Math.min(pulled, MAX_HEAT - this.heat);
                    source.useUpHeat(accepted);
                    this.heat += accepted;
                    return;
                }
            }
        }

        this.heat = Math.max(this.heat - Math.max(this.heat / 1000, 1), 0);
    }

    private ItemStack getSmeltingResult(ItemStack stack) {
        if(this.level == null || stack.isEmpty()) return ItemStack.EMPTY;

        SingleRecipeInput input = new SingleRecipeInput(stack);
        return this.level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, input, this.level)
                .map(recipe -> recipe.value().assemble(input, this.level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot < 3 && !this.getSmeltingResult(stack).isEmpty();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { SLOT_INPUT_1, SLOT_INPUT_2, SLOT_INPUT_3, SLOT_OUTPUT_1, SLOT_OUTPUT_2, SLOT_OUTPUT_3 };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= SLOT_OUTPUT_1;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        int[] savedProgress = tag.getIntArray("progress");
        int[] savedBonus = tag.getIntArray("bonus");
        if(savedProgress.length == 3) this.progress = savedProgress;
        if(savedBonus.length == 3) this.bonus = savedBonus;
        this.heat = tag.getInt("heat");
        this.wasOn = tag.getBoolean("wasOn");

        Arrays.fill(this.lastItems, ItemStack.EMPTY);
        ListTag list = tag.getList("lastItems", Tag.TAG_COMPOUND);
        for(int i = 0; i < list.size(); i++) {
            CompoundTag itemTag = list.getCompound(i);
            int slot = itemTag.getByte("lastItem");
            if(slot >= 0 && slot < this.lastItems.length) {
                this.lastItems[slot] = ItemStack.parse(registries, itemTag).orElse(ItemStack.EMPTY);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putIntArray("progress", this.progress);
        tag.putIntArray("bonus", this.bonus);
        tag.putInt("heat", this.heat);
        tag.putBoolean("wasOn", this.wasOn);

        ListTag list = new ListTag();
        for(int i = 0; i < this.lastItems.length; i++) {
            ItemStack lastItem = this.lastItems[i];
            if(lastItem.isEmpty()) continue;

            CompoundTag itemTag = new CompoundTag();
            itemTag.putByte("lastItem", (byte) i);
            lastItem.save(registries, itemTag);
            list.add(itemTag);
        }
        tag.put("lastItems", list);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeVarIntArray(this.progress);
        buf.writeVarIntArray(this.bonus);
        buf.writeInt(this.heat);
        buf.writeBoolean(this.wasOn);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.progress = buf.readVarIntArray(3);
        this.bonus = buf.readVarIntArray(3);
        this.heat = buf.readInt();
        this.wasOn = buf.readBoolean();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineFurnaceSteelMenu(id, inventory, this);
    }
}
