package com.hbm.blockentity.machine;

import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.menus.MachineFurnaceIronMenu;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.modules.ModuleBurnTime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MachineFurnaceIronBlockEntity extends MachineBaseBlockEntity implements IUpgradeInfoProvider {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL_1 = 1;
    public static final int SLOT_FUEL_2 = 2;
    public static final int SLOT_OUTPUT = 3;
    public static final int SLOT_UPGRADE = 4;

    public static final int BASE_TIME = 160;

    public static final ModuleBurnTime burnModule = new ModuleBurnTime()
            .setLigniteTimeMod(1.25D)
            .setCoalTimeMod(1.25D)
            .setCokeTimeMod(1.5D)
            .setSolidTimeMod(2.0D)
            .setRocketTimeMod(2.0D)
            .setBalefireTimeMod(2.0D);

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public int maxBurnTime;
    public int burnTime;
    public int progress;
    public int processingTime = BASE_TIME;
    public boolean wasOn;

    public MachineFurnaceIronBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FURNACE_IRON.get(), pos, state, 5);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furnace_iron");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.spawnWorkingParticles();
            return;
        }

        this.upgradeManager.checkSlots(this.slots, SLOT_UPGRADE, SLOT_UPGRADE);
        this.processingTime = BASE_TIME - (BASE_TIME / 2) * this.upgradeManager.getLevel(UpgradeType.SPEED) / 3;
        this.wasOn = false;

        if(this.burnTime <= 0) {
            for(int slot = SLOT_FUEL_1; slot <= SLOT_FUEL_2; slot++) {
                ItemStack fuelStack = this.slots.get(slot);
                if(fuelStack.isEmpty()) continue;

                int fuel = burnModule.getBurnTime(fuelStack);
                if(fuel <= 0) continue;

                this.maxBurnTime = fuel;
                this.burnTime = fuel;

                ItemStack remainder = fuelStack.hasCraftingRemainingItem() ? fuelStack.getCraftingRemainingItem().copy() : ItemStack.EMPTY;
                fuelStack.shrink(1);
                if(fuelStack.isEmpty()) this.slots.set(slot, remainder);
                this.setChanged();
                break;
            }
        }

        if(this.canSmelt()) {
            this.wasOn = true;
            this.progress++;
            this.burnTime--;

            if(this.progress % 15 == 0 && !this.muffled) {
                this.level.playSound(null, this.worldPosition, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 0.5F + this.level.random.nextFloat() * 0.5F);
            }

            if(this.progress >= this.processingTime) {
                this.smeltItem();
                this.progress = 0;
            }

            if(this.level.getGameTime() % 20 == 0) {
                PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionType.SOOT, PollutionHandler.SOOT_PER_SECOND);
            }

            this.setChanged();
        } else if(this.progress != 0) {
            this.progress = 0;
            this.setChanged();
        }

        this.networkPackNT(50);
    }

    private void spawnWorkingParticles() {
        if(this.progress <= 0) return;

        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();
        double offset = this.progress % 2 == 0 ? 1.0D : 0.5D;

        this.level.addParticle(
                ParticleTypes.SMOKE,
                this.worldPosition.getX() + 0.5D - dir.getStepX() * offset - rot.getStepX() * 0.1875D,
                this.worldPosition.getY() + 2.0D,
                this.worldPosition.getZ() + 0.5D - dir.getStepZ() * offset - rot.getStepZ() * 0.1875D,
                0.0D,
                0.01D,
                0.0D
        );

        if(this.progress % 5 == 0) {
            double randomOffset = this.level.random.nextDouble();
            this.level.addParticle(
                    ParticleTypes.FLAME,
                    this.worldPosition.getX() + 0.5D + dir.getStepX() * 0.25D + rot.getStepX() * randomOffset,
                    this.worldPosition.getY() + 0.25D + this.level.random.nextDouble() * 0.25D,
                    this.worldPosition.getZ() + 0.5D + dir.getStepZ() * 0.25D + rot.getStepZ() * randomOffset,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    public boolean canSmelt() {
        if(this.burnTime <= 0) return false;

        ItemStack result = this.getSmeltingResult(this.slots.get(SLOT_INPUT));
        if(result.isEmpty()) return false;

        ItemStack output = this.slots.get(SLOT_OUTPUT);
        if(output.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void smeltItem() {
        ItemStack result = this.getSmeltingResult(this.slots.get(SLOT_INPUT));
        if(result.isEmpty()) return;

        ItemStack output = this.slots.get(SLOT_OUTPUT);
        if(output.isEmpty()) {
            this.slots.set(SLOT_OUTPUT, result.copy());
        } else {
            output.grow(result.getCount());
        }

        ItemStack input = this.slots.get(SLOT_INPUT);
        input.shrink(1);
        if(input.isEmpty()) this.slots.set(SLOT_INPUT, ItemStack.EMPTY);
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
        if(slot == SLOT_INPUT) return !this.getSmeltingResult(stack).isEmpty();
        if(slot == SLOT_FUEL_1 || slot == SLOT_FUEL_2) return burnModule.getBurnTime(stack) > 0;
        if(slot == SLOT_UPGRADE) return stack.getItem() instanceof MachineUpgradeItem;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { SLOT_INPUT, SLOT_FUEL_1, SLOT_FUEL_2, SLOT_OUTPUT };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_OUTPUT;
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.FURNACE_IRON.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(Component.translatable(KEY_DELAY, "-" + level * 50 / 3 + "%").getString());
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        return upgrades;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.maxBurnTime = tag.getInt("maxBurnTime");
        this.burnTime = tag.getInt("burnTime");
        this.progress = tag.getInt("progress");
        this.processingTime = tag.contains("processingTime") ? tag.getInt("processingTime") : BASE_TIME;
        this.wasOn = tag.getBoolean("wasOn");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("maxBurnTime", this.maxBurnTime);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("progress", this.progress);
        tag.putInt("processingTime", this.processingTime);
        tag.putBoolean("wasOn", this.wasOn);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.maxBurnTime);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.progress);
        buf.writeInt(this.processingTime);
        buf.writeBoolean(this.wasOn);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.maxBurnTime = buf.readInt();
        this.burnTime = buf.readInt();
        this.progress = buf.readInt();
        this.processingTime = buf.readInt();
        this.wasOn = buf.readBoolean();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineFurnaceIronMenu(id, inventory, this);
    }
}
