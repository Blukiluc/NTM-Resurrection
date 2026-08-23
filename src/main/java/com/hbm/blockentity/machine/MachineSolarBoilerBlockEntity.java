package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class MachineSolarBoilerBlockEntity extends LoadedBaseBlockEntity implements ITickable, IFluidStandardTransceiverMK2, IFluidCopiable {

    public final FluidTank water = new FluidTank(Fluids.WATER, 100);
    public final FluidTank steam = new FluidTank(Fluids.STEAM, 10_000);
    public int display;
    private int heat;

    public MachineSolarBoilerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_SOLAR_BOILER.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        for(DirPos connection : this.getConnections()) {
            this.trySubscribe(Fluids.WATER, this.level, connection);
        }

        int process = this.heat / 50;
        this.display = process;
        process = Math.min(process, this.water.getFill());
        process = Math.min(process, (this.steam.getMaxFill() - this.steam.getFill()) / 100);
        process = Math.max(process, 0);

        this.water.setFill(this.water.getFill() - process);
        this.steam.setFill(this.steam.getFill() + process * 100);

        if(this.steam.getFill() > 0) {
            for(DirPos connection : this.getConnections()) {
                this.tryProvide(this.steam, this.level, connection);
            }
        }

        this.heat = 0;
        this.setChanged();
        this.networkPackNT(50);
    }

    public void addHeat(int amount) {
        if(amount <= 0) return;
        this.heat += amount;
        this.setChanged();
    }

    private DirPos[] getConnections() {
        return new DirPos[] {
                new DirPos(this.worldPosition.above(3), Direction.UP),
                new DirPos(this.worldPosition.below(), Direction.DOWN)
        };
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return direction != null && direction.getAxis().isVertical() && (type == Fluids.WATER || type == Fluids.STEAM);
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] {this.steam};
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] {this.water};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] {this.water, this.steam};
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.water.readFromNBT(tag, "water");
        this.steam.readFromNBT(tag, "steam");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.water.writeToNBT(tag, "water");
        this.steam.writeToNBT(tag, "steam");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.display);
        this.water.serialize(buf);
        this.steam.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.display = buf.readInt();
        this.water.deserialize(buf);
        this.steam.deserialize(buf);
    }
}
