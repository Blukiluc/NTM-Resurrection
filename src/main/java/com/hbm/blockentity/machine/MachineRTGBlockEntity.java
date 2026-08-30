package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyProviderMK2;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.menus.MachineRTGMenu;
import com.hbm.items.machine.RTGPelletItem;
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
import net.minecraft.world.level.block.state.BlockState;

public class MachineRTGBlockEntity extends MachineBaseBlockEntity implements IEnergyProviderMK2 {

    public static final int SLOT_COUNT = 15;
    public static final int HEAT_MAX = 200;
    public static final long MAX_POWER = 100_000L;

    private static final int[] SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

    public int heat;
    public long power;

    public MachineRTGBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.RTG.get(), pos, state, SLOT_COUNT);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_rtg");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        int previousHeat = this.heat;
        long previousPower = this.power;

        for(Direction direction : Direction.values()) {
            if(this.power > 0) this.tryProvide(this.level, this.worldPosition.relative(direction), direction);
        }

        this.heat = 0;
        for(ItemStack stack : this.slots) {
            if(stack.getItem() instanceof RTGPelletItem pellet) {
                this.heat += pellet.getHeat();
            }
        }

        this.heat = Math.min(this.heat, HEAT_MAX);
        this.power = Math.min(this.power + this.heat * 5L, MAX_POWER);

        if(this.heat != previousHeat || this.power != previousPower) this.setChanged();
        this.networkPackNT(50);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof RTGPelletItem;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return false;
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
        return new MachineRTGMenu(id, inventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.heat = tag.getInt("heat");
        this.power = tag.getLong("power");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("heat", this.heat);
        tag.putLong("power", this.power);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.heat);
        buf.writeLong(this.power);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.heat = buf.readInt();
        this.power = buf.readLong();
    }
}
