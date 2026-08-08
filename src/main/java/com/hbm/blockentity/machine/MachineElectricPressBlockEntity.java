package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.menus.MachineElectricPressMenu;
import com.hbm.inventory.recipes.PressRecipes;
import com.hbm.items.machine.ItemStamp;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MachineElectricPressBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IUpgradeInfoProvider {

    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_STAMP = 1;
    public static final int SLOT_INPUT = 2;
    public static final int SLOT_OUTPUT = 3;
    public static final int SLOT_UPGRADE = 4;

    public long power;
    public static final long maxPower = 50_000;

    public int press;
    public double renderPress;
    public double lastPress;
    private int syncPress;
    private int turnProgress;
    public static final int maxPress = 200;
    private boolean isRetracting;
    private int delay;

    public ItemStack syncStack = ItemStack.EMPTY;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachineElectricPressBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.ELECTRIC_PRESS.get(), pos, state, 5);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_electric_press");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(level.isClientSide) {
            this.lastPress = this.renderPress;

            if(this.turnProgress > 0) {
                this.renderPress += (this.syncPress - this.renderPress) / (double) this.turnProgress;
                this.turnProgress--;
            } else {
                this.renderPress = this.syncPress;
            }
            return;
        }

        for(Direction dir : Direction.values()) {
            this.trySubscribe(level, new DirPos(this.worldPosition.relative(dir), dir));
        }

        this.power = Library.chargeTEFromItems(this.slots, SLOT_BATTERY, this.power, maxPower);
        boolean canProcess = this.canProcess();

        if((canProcess || this.isRetracting || this.delay > 0) && this.power >= 100) {
            this.power -= 100;

            if(this.delay <= 0) {
                this.upgradeManager.checkSlots(this.slots, SLOT_UPGRADE, SLOT_UPGRADE);
                int speed = 1 + this.upgradeManager.getLevel(UpgradeType.SPEED);

                int stampSpeed = this.isRetracting ? 20 : 45;
                stampSpeed *= 1D + speed / 4D;

                if(this.isRetracting) {
                    this.press -= stampSpeed;

                    if(this.press <= 0) {
                        this.press = 0;
                        this.isRetracting = false;
                        this.delay = 6 - speed;
                    }
                } else if(canProcess) {
                    this.press += stampSpeed;

                    if(this.press >= maxPress) {
                        this.press = maxPress;
                        level.playSound(null, this.worldPosition, NtmSoundEvents.PRESS_OPERATE.get(),
                                SoundSource.BLOCKS, this.getVolume(1.5F), 1.0F);
                        this.processItem();
                        this.isRetracting = true;
                        this.delay = 6 - speed;
                    }
                } else if(this.press > 0) {
                    this.isRetracting = true;
                }
            } else {
                this.delay--;
            }

            this.setChanged();
        }

        this.networkPackNT(50);
    }

    public boolean canProcess() {
        if(this.power < 100) return false;

        ItemStack stamp = this.slots.get(SLOT_STAMP);
        ItemStack input = this.slots.get(SLOT_INPUT);
        if(stamp.isEmpty() || input.isEmpty()) return false;

        ItemStack output = PressRecipes.getOutput(input, stamp);
        if(output.isEmpty()) return false;

        ItemStack outputSlot = this.slots.get(SLOT_OUTPUT);
        if(outputSlot.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(outputSlot, output)
                && outputSlot.getCount() + output.getCount() <= outputSlot.getMaxStackSize();
    }

    private void processItem() {
        ItemStack stamp = this.slots.get(SLOT_STAMP);
        ItemStack input = this.slots.get(SLOT_INPUT);
        ItemStack output = PressRecipes.getOutput(input, stamp);
        if(output.isEmpty()) return;

        ItemStack outputSlot = this.slots.get(SLOT_OUTPUT);
        if(outputSlot.isEmpty()) {
            this.slots.set(SLOT_OUTPUT, output.copy());
        } else {
            outputSlot.grow(output.getCount());
        }

        input.shrink(1);

        if(stamp.getMaxDamage() > 0) {
            stamp.setDamageValue(stamp.getDamageValue() + 1);
            if(stamp.getDamageValue() >= stamp.getMaxDamage()) {
                this.slots.set(SLOT_STAMP, ItemStack.EMPTY);
            }
        }

        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == SLOT_BATTERY) return stack.getItem() instanceof IBatteryItem;
        if(slot == SLOT_STAMP) return stack.getItem() instanceof ItemStamp;
        if(slot == SLOT_INPUT) return true;
        if(slot == SLOT_UPGRADE) return stack.getItem() instanceof MachineUpgradeItem;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { SLOT_STAMP, SLOT_INPUT, SLOT_OUTPUT };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_OUTPUT;
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
        return maxPower;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineElectricPressMenu(id, inventory, this);
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_ELECTRIC_PRESS.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(Component.translatable(KEY_DELAY, "-" + (50 * level / 3) + "%").getString());
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        return upgrades;
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.press);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, this.slots.get(SLOT_INPUT));
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.syncPress = buf.readInt();
        this.syncStack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
        this.turnProgress = 2;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.press = tag.getInt("press");
        this.power = tag.getLong("power");
        this.isRetracting = tag.getBoolean("ret");
        this.delay = tag.getInt("delay");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("press", this.press);
        tag.putLong("power", this.power);
        tag.putBoolean("ret", this.isRetracting);
        tag.putInt("delay", this.delay);
    }
}
