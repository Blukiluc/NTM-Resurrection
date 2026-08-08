package com.hbm.blockentity.machine.turbine;

import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.fluidmk2.IFluidConnectorMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Coolable;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractTurbineBlockEntity extends LoadedBaseBlockEntity implements ITickable, IEnergyProviderMK2, IFluidStandardTransceiverMK2, IFluidConnectorMK2, IFluidCopiable {

    public final FluidTank[] tanks;
    public long powerBuffer;
    public long output;
    public boolean operational;

    protected AbstractTurbineBlockEntity(BlockEntityType<? extends AbstractTurbineBlockEntity> type, BlockPos pos, BlockState state, int inputCapacity, int outputCapacity) {
        super(type, pos, state);
        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.STEAM, inputCapacity),
                new FluidTank(Fluids.SPENTSTEAM, outputCapacity)
        };
    }

    protected abstract double getEfficiency();
    protected abstract double getConsumptionPercent();
    protected abstract DirPos[] getConnectionPositions();
    protected abstract DirPos[] getPowerPositions();

    protected boolean resizesForSteamType() {
        return false;
    }

    protected int getNetworkRange() {
        return 150;
    }

    protected void generatePower(long power, int steamConsumed) {
        this.powerBuffer += power;
    }

    protected void onServerTick() { }

    protected void onClientTick() { }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.onClientTick();
            return;
        }

        this.powerBuffer = 0;
        this.output = 0;
        this.operational = false;

        FluidType inputType = this.tanks[0].getTankType();
        boolean valid = false;

        if(inputType.hasTrait(FT_Coolable.class)) {
            FT_Coolable trait = inputType.getTrait(FT_Coolable.class);
            double efficiency = trait.getEfficiency(FT_Coolable.CoolingType.TURBINE) * this.getEfficiency();

            if(efficiency > 0 && trait.coolsTo != null && trait.amountReq > 0 && trait.amountProduced > 0) {
                this.tanks[1].setTankType(trait.coolsTo);

                int availableInput = (int)Math.min(Math.ceil(this.tanks[0].getFill() * this.getConsumptionPercent()), this.tanks[0].getFill());
                int inputOperations = availableInput / trait.amountReq;
                int outputOperations = (this.tanks[1].getMaxFill() - this.tanks[1].getFill()) / trait.amountProduced;
                int operations = Math.min(inputOperations, outputOperations);

                if(operations > 0) {
                    int consumed = operations * trait.amountReq;
                    this.tanks[0].setFill(this.tanks[0].getFill() - consumed);
                    this.tanks[1].setFill(this.tanks[1].getFill() + operations * trait.amountProduced);
                    this.generatePower((long)(operations * (double)trait.heatEnergy * efficiency), consumed);
                    this.operational = true;
                }

                valid = true;
            }
        }

        this.onServerTick();
        this.output = this.powerBuffer;

        if(!valid) {
            this.tanks[1].setTankType(Fluids.NONE);
        }

        for(DirPos pos : this.getPowerPositions()) {
            this.tryProvide(this.level, pos.makeCompat(), pos.getDir());
        }

        for(DirPos pos : this.getConnectionPositions()) {
            this.tryProvide(this.tanks[1], this.level, pos);
            this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
        }

        this.setChanged();
        this.networkPackNT(this.getNetworkRange());
    }

    public void onLeverPull() {
        FluidType type = this.tanks[0].getTankType();
        boolean resize = this.resizesForSteamType();

        if(type == Fluids.STEAM) {
            this.tanks[0].setTankType(Fluids.HOTSTEAM);
            this.tanks[1].setTankType(Fluids.STEAM);
            if(resize) this.resizeTanks(0.1D);
        } else if(type == Fluids.HOTSTEAM) {
            this.tanks[0].setTankType(Fluids.SUPERHOTSTEAM);
            this.tanks[1].setTankType(Fluids.HOTSTEAM);
            if(resize) this.resizeTanks(0.1D);
        } else if(type == Fluids.SUPERHOTSTEAM) {
            this.tanks[0].setTankType(Fluids.ULTRAHOTSTEAM);
            this.tanks[1].setTankType(Fluids.SUPERHOTSTEAM);
            if(resize) this.resizeTanks(0.1D);
        } else {
            this.tanks[0].setTankType(Fluids.STEAM);
            this.tanks[1].setTankType(Fluids.SPENTSTEAM);
            if(resize && type == Fluids.ULTRAHOTSTEAM) this.resizeTanks(1000D);
        }

        this.setChanged();
    }

    private void resizeTanks(double factor) {
        this.tanks[0].changeTankSize((int)(this.tanks[0].getMaxFill() * factor));
        this.tanks[1].changeTankSize((int)(this.tanks[1].getMaxFill() * factor));
    }

    @Override
    public long getPower() {
        return this.powerBuffer;
    }

    @Override
    public void setPower(long power) {
        this.powerBuffer = power;
    }

    @Override
    public long getMaxPower() {
        return this.powerBuffer;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.tanks[1] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return direction != null && direction.getAxis().isHorizontal() && (type.hasTrait(FT_Coolable.class) || type == this.tanks[1].getTankType());
    }

    @Override
    public FluidTank getTankToPaste() {
        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.powerBuffer = tag.getLong("power");
        this.output = tag.getLong("output");
        this.operational = tag.getBoolean("operational");
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "outputTank");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.powerBuffer);
        tag.putLong("output", this.output);
        tag.putBoolean("operational", this.operational);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "outputTank");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.tanks[0].serialize(buf);
        this.tanks[1].serialize(buf);
        buf.writeLong(this.output);
        buf.writeBoolean(this.operational);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.tanks[0].deserialize(buf);
        this.tanks[1].deserialize(buf);
        this.output = buf.readLong();
        this.operational = buf.readBoolean();
    }
}
