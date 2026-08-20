package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidReceiverMK2;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.handler.PollutionHandler;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class MachineSmokestackBaseBlockEntity extends LoadedBaseBlockEntity implements IFluidReceiverMK2, ITickable {

    public int onTicks;

    protected MachineSmokestackBaseBlockEntity(BlockEntityType<? extends MachineSmokestackBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected abstract double getPollutionModifier();
    protected abstract double getParticleHeight();
    protected abstract float getParticleBaseScale();

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        if(this.level.getGameTime() % 20 == 0) {
            FluidType[] types = new FluidType[] {Fluids.SMOKE, Fluids.SMOKE_LEADED, Fluids.SMOKE_POISON};
            for(FluidType type : types) {
                for(DirPos pos : this.getConnectionPositions()) this.trySubscribe(type, this.level, pos);
            }
        }

        if(this.onTicks > 0) {
            this.onTicks--;
            if(this.level.getGameTime() % 2 == 0) this.spawnParticle();
        }

        this.networkPackNT(150);
    }

    private DirPos[] getConnectionPositions() {
        BlockPos pos = this.worldPosition;
        return new DirPos[] {
                new DirPos(pos.east(2), Direction.EAST),
                new DirPos(pos.west(2), Direction.WEST),
                new DirPos(pos.south(2), Direction.SOUTH),
                new DirPos(pos.north(2), Direction.NORTH)
        };
    }

    private void spawnParticle() {
        if(!(this.level instanceof ServerLevel serverLevel)) return;

        double x = this.worldPosition.getX() + 0.5D;
        double y = this.worldPosition.getY() + this.getParticleHeight();
        double z = this.worldPosition.getZ() + 0.5D;

        CompoundTag data = new CompoundTag();
        data.putString("type", "tower");
        data.putFloat("lift", 10F);
        data.putFloat("base", this.getParticleBaseScale());
        data.putFloat("max", 3F);
        data.putInt("life", 250 + this.level.random.nextInt(50));
        data.putInt("color", 0x404040);

        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 150D, new AuxParticle(data, x, y, z));
    }

    @Override
    public long transferFluid(FluidType type, int pressure, long fluid) {
        if(type != Fluids.SMOKE && type != Fluids.SMOKE_LEADED && type != Fluids.SMOKE_POISON) return fluid;

        this.onTicks = 20;
        float pollution = (float) (fluid * this.getPollutionModifier() / 100D);

        if(type == Fluids.SMOKE) PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionHandler.PollutionType.SOOT, pollution);
        if(type == Fluids.SMOKE_LEADED) PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionHandler.PollutionType.HEAVYMETAL, pollution);
        if(type == Fluids.SMOKE_POISON) PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionHandler.PollutionType.POISON, pollution);

        this.setChanged();
        return 0;
    }

    @Override
    public long getDemand(FluidType type, int pressure) {
        return type == Fluids.SMOKE || type == Fluids.SMOKE_LEADED || type == Fluids.SMOKE_POISON ? 1_000_000L : 0L;
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[0];
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return direction != null && direction.getAxis().isHorizontal() &&
                (type == Fluids.SMOKE || type == Fluids.SMOKE_LEADED || type == Fluids.SMOKE_POISON);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.onTicks);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.onTicks = buf.readInt();
    }
}
