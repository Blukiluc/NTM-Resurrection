package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class MachineGroundwaterPumpBlockEntity extends LoadedBaseBlockEntity implements ITickable, IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IFluidCopiable {

    public final FluidTank water = new FluidTank(Fluids.WATER, 1_000_000);
    public final FluidTank steam = new FluidTank(Fluids.STEAM, 1_000);
    public final FluidTank spentSteam = new FluidTank(Fluids.SPENTSTEAM, 100);
    public long power;
    public boolean isOn;
    public boolean onGround;
    public float rotor;
    public float lastRotor;
    private int groundCheckDelay;

    public MachineGroundwaterPumpBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.GROUNDWATER_PUMP.get(), pos, state);
    }

    private boolean isElectric() {
        return this.getBlockState().is(NtmBlocks.PUMP_ELECTRIC.get());
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.lastRotor = this.rotor;
            if(this.isOn) this.rotor = (this.rotor + 10F) % 360F;
            return;
        }

        if(this.groundCheckDelay-- <= 0) {
            this.onGround = this.checkGround();
            this.groundCheckDelay = 100;
        }

        this.isOn = false;
        if(this.onGround && this.worldPosition.getY() <= 70) {
            int space = this.water.getMaxFill() - this.water.getFill();
            if(this.isElectric()) {
                if(this.power >= 1_000L && space >= 10_000) {
                    this.power -= 1_000L;
                    this.water.setFill(this.water.getFill() + 10_000);
                    this.isOn = true;
                }
            } else if(this.steam.getFill() >= 100 && this.spentSteam.getFill() < this.spentSteam.getMaxFill() && space >= 1_000) {
                this.steam.setFill(this.steam.getFill() - 100);
                this.spentSteam.setFill(this.spentSteam.getFill() + 1);
                this.water.setFill(this.water.getFill() + 1_000);
                this.isOn = true;
            }
        }

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos connection = this.worldPosition.relative(direction, 2);
            this.tryProvide(this.water, this.level, connection, direction);
            if(this.isElectric()) {
                this.trySubscribe(this.level, connection, direction);
            } else {
                this.trySubscribe(Fluids.STEAM, this.level, connection, direction);
                this.tryProvide(this.spentSteam, this.level, connection, direction);
            }
        }

        this.setChanged();
        this.networkPackNT(50);
    }

    private boolean checkGround() {
        if(this.level == null || !this.level.dimensionType().hasSkyLight()) return false;
        int valid = 0;
        int checked = 0;
        for(int x = -2; x <= 2; x++) {
            for(int z = -2; z <= 2; z++) {
                BlockPos ground = this.worldPosition.offset(x, -1, z);
                checked++;
                if(this.level.getBlockState(ground).isFaceSturdy(this.level, ground, Direction.UP)) valid++;
            }
        }
        return valid * 2 >= checked;
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
        return 10_000L;
    }

    @Override
    public boolean canConnect(Direction direction) {
        return this.isElectric() && direction != null && direction.getAxis().isHorizontal();
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        if(direction == null || !direction.getAxis().isHorizontal()) return false;
        return type == Fluids.WATER || !this.isElectric() && (type == Fluids.STEAM || type == Fluids.SPENTSTEAM);
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return this.isElectric() ? new FluidTank[] {this.water} : new FluidTank[] {this.water, this.spentSteam};
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return this.isElectric() ? FluidTank.EMPTY_ARRAY : new FluidTank[] {this.steam};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.isElectric() ? new FluidTank[] {this.water} : new FluidTank[] {this.water, this.steam, this.spentSteam};
    }

    @Override
    public FluidTank getTankToPaste() {
        return this.isElectric() ? null : this.steam;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.onGround = tag.getBoolean("onGround");
        this.water.readFromNBT(tag, "water");
        this.steam.readFromNBT(tag, "steam");
        this.spentSteam.readFromNBT(tag, "spentSteam");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putBoolean("onGround", this.onGround);
        this.water.writeToNBT(tag, "water");
        this.steam.writeToNBT(tag, "steam");
        this.spentSteam.writeToNBT(tag, "spentSteam");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.water.serialize(buf);
        this.steam.serialize(buf);
        this.spentSteam.serialize(buf);
        buf.writeLong(this.power);
        buf.writeBoolean(this.isOn);
        buf.writeBoolean(this.onGround);
        buf.writeFloat(this.rotor);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.water.deserialize(buf);
        this.steam.deserialize(buf);
        this.spentSteam.deserialize(buf);
        this.power = buf.readLong();
        this.isOn = buf.readBoolean();
        this.onGround = buf.readBoolean();
        this.rotor = buf.readFloat();
    }
}
