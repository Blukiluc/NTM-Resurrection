package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.MachineElectricFurnaceBlock;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.menus.MachineElectricFurnaceMenu;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MachineElectricFurnaceBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IUpgradeInfoProvider {

    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_INPUT = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_UPGRADE = 3;

    public static final long MAX_POWER = 100_000L;
    public static final int BASE_PROGRESS = 100;
    public static final int BASE_CONSUMPTION = 50;

    private static final int[] SLOTS_IO = new int[] { SLOT_BATTERY, SLOT_INPUT, SLOT_OUTPUT };

    public long power;
    public int progress;
    public int maxProgress = BASE_PROGRESS;
    public int consumption = BASE_CONSUMPTION;
    private int cooldown;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachineElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.ELECTRIC_FURNACE.get(), pos, state, 4);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_electric_furnace");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        long previousPower = this.power;
        int previousProgress = this.progress;

        if(this.cooldown > 0) this.cooldown--;

        this.power = Library.chargeTEFromItems(this.slots, SLOT_BATTERY, this.power, MAX_POWER);

        for(Direction direction : Direction.values()) {
            this.trySubscribe(this.level, new DirPos(this.worldPosition.relative(direction), direction));
        }

        this.consumption = BASE_CONSUMPTION;
        this.maxProgress = BASE_PROGRESS;

        this.upgradeManager.checkSlots(this.slots, SLOT_UPGRADE, SLOT_UPGRADE);
        int speedLevel = this.upgradeManager.getLevel(UpgradeType.SPEED);
        int powerLevel = this.upgradeManager.getLevel(UpgradeType.POWER);

        this.maxProgress -= speedLevel * 25;
        this.consumption += speedLevel * 50;
        this.maxProgress += powerLevel * 10;
        this.consumption -= powerLevel * 15;

        if(!this.hasPower()) this.cooldown = 20;

        boolean processing = this.hasPower() && this.canProcess();
        if(processing) {
            this.progress++;
            this.power -= this.consumption;

            if(this.level.getGameTime() % 20 == 0) {
                PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionType.SOOT, PollutionHandler.SOOT_PER_SECOND);
            }

            if(this.progress >= this.maxProgress) {
                this.progress = 0;
                this.processItem();
            }
        } else {
            this.progress = 0;
        }

        BlockState state = this.getBlockState();
        if(state.hasProperty(MachineElectricFurnaceBlock.LIT)
                && state.getValue(MachineElectricFurnaceBlock.LIT) != processing) {
            this.level.setBlock(this.worldPosition, state.setValue(MachineElectricFurnaceBlock.LIT, processing), Block.UPDATE_ALL);
        }

        if(this.power != previousPower || this.progress != previousProgress) this.setChanged();
        this.networkPackNT(50);
    }

    public boolean hasPower() {
        return this.power >= this.consumption;
    }

    public boolean canProcess() {
        ItemStack input = this.slots.get(SLOT_INPUT);
        if(input.isEmpty() || this.cooldown > 0) return false;

        ItemStack result = this.getSmeltingResult(input);
        if(result.isEmpty()) return false;

        ItemStack output = this.slots.get(SLOT_OUTPUT);
        if(output.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void processItem() {
        ItemStack input = this.slots.get(SLOT_INPUT);
        ItemStack result = this.getSmeltingResult(input);
        if(result.isEmpty()) return;

        ItemStack output = this.slots.get(SLOT_OUTPUT);
        if(output.isEmpty()) {
            this.slots.set(SLOT_OUTPUT, result.copy());
        } else {
            output.grow(result.getCount());
        }

        input.shrink(1);
        if(input.isEmpty()) this.slots.set(SLOT_INPUT, ItemStack.EMPTY);
        this.setChanged();
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
        if(slot == SLOT_BATTERY) return stack.getItem() instanceof IBatteryItem;
        if(slot == SLOT_INPUT) return !this.getSmeltingResult(stack).isEmpty();
        if(slot == SLOT_UPGRADE) return stack.getItem() instanceof MachineUpgradeItem;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS_IO;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        if(slot == SLOT_BATTERY && stack.getItem() instanceof IBatteryItem battery) {
            return battery.getCharge(stack) == 0;
        }
        return slot == SLOT_OUTPUT;
    }

    @Override
    public long getPower() {
        return this.power;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return MAX_POWER;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineElectricFurnaceMenu(id, inventory, this);
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_ELECTRIC_FURNACE.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(ChatFormatting.GREEN + Component.translatable(KEY_DELAY, "-" + level * 25 + "%").getString());
            info.add(ChatFormatting.RED + Component.translatable(KEY_CONSUMPTION, "+" + level * 100 + "%").getString());
        }
        if(type == UpgradeType.POWER) {
            info.add(ChatFormatting.GREEN + Component.translatable(KEY_CONSUMPTION, "-" + level * 30 + "%").getString());
            info.add(ChatFormatting.RED + Component.translatable(KEY_DELAY, "+" + level * 10 + "%").getString());
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        return upgrades;
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.maxProgress);
        buf.writeInt(this.progress);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.maxProgress = buf.readInt();
        this.progress = buf.readInt();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.progress = tag.getInt("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("progress", this.progress);
    }
}
