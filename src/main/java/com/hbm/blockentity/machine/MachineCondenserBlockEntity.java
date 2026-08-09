package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCondenserBlockEntity extends LoadedBaseBlockEntity implements ITickable, IFluidStandardTransceiverMK2, IFluidCopiable {

    public final FluidTank spentSteam;
    public final FluidTank water;

    public MachineCondenserBlockEntity(BlockPos pos, BlockState state) {
        this(NtmBlockEntityTypes.CONDENSER.get(), pos, state, 100, 100);
    }

    protected MachineCondenserBlockEntity(BlockEntityType<? extends MachineCondenserBlockEntity> type, BlockPos pos, BlockState state, int inputCapacity, int outputCapacity) {
        super(type, pos, state);
        this.spentSteam = new FluidTank(Fluids.SPENTSTEAM, inputCapacity);
        this.water = new FluidTank(Fluids.WATER, outputCapacity);
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        int converted = Math.min(this.spentSteam.getFill(), this.water.getMaxFill() - this.water.getFill());
        if(converted > 0) {
            this.spentSteam.setFill(this.spentSteam.getFill() - converted);
            this.water.setFill(this.water.getFill() + converted);
        }

        for(Direction direction : Direction.values()) {
            BlockPos connection = this.worldPosition.relative(direction);
            this.trySubscribe(Fluids.SPENTSTEAM, this.level, connection, direction);
            this.tryProvide(this.water, this.level, connection, direction);
        }

        this.setChanged();
        this.networkPackNT(25);
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return type == Fluids.SPENTSTEAM || type == Fluids.WATER;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] {this.water};
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] {this.spentSteam};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] {this.spentSteam, this.water};
    }

    @Override
    public FluidTank getTankToPaste() {
        return this.spentSteam;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.spentSteam.readFromNBT(tag, "spentSteam");
        this.water.readFromNBT(tag, "water");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.spentSteam.writeToNBT(tag, "spentSteam");
        this.water.writeToNBT(tag, "water");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.spentSteam.serialize(buf);
        this.water.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.spentSteam.deserialize(buf);
        this.water.deserialize(buf);
    }
}
