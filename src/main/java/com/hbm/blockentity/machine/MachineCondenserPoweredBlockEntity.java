package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCondenserPoweredBlockEntity extends MachineCondenserBlockEntity implements IEnergyReceiverMK2 {

    public long power;
    public int waterTimer;
    public float fanRotation;
    public float lastFanRotation;

    public MachineCondenserPoweredBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CONDENSER_POWERED.get(), pos, state, 1_000_000, 1_000_000);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.lastFanRotation = this.fanRotation;
            if(this.waterTimer > 0) this.fanRotation = (this.fanRotation + 30F) % 360F;
            return;
        }

        int maxByPower = (int)Math.min(Integer.MAX_VALUE, this.power / 10L);
        int converted = Math.min(Math.min(this.spentSteam.getFill(), this.water.getMaxFill() - this.water.getFill()), maxByPower);
        this.waterTimer = converted > 0 ? 5 : Math.max(0, this.waterTimer - 1);
        if(converted > 0) {
            this.spentSteam.setFill(this.spentSteam.getFill() - converted);
            this.water.setFill(this.water.getFill() + converted);
            this.power -= converted * 10L;
        }

        for(Connection connection : this.getConnections()) {
            this.trySubscribe(this.level, connection.pos, connection.direction);
            this.trySubscribe(Fluids.SPENTSTEAM, this.level, connection.pos, connection.direction);
            this.tryProvide(this.water, this.level, connection.pos, connection.direction);
        }

        this.setChanged();
        this.networkPackNT(100);
    }

    private Connection[] getConnections() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();
        BlockPos center = this.worldPosition.above();
        return new Connection[] {
                new Connection(center.relative(side, 4), side),
                new Connection(center.relative(side.getOpposite(), 4), side.getOpposite()),
                new Connection(center.relative(facing, 2).relative(side, 3), facing),
                new Connection(center.relative(facing, 2).relative(side.getOpposite(), 3), facing),
                new Connection(center.relative(facing.getOpposite(), 2).relative(side, 3), facing.getOpposite()),
                new Connection(center.relative(facing.getOpposite(), 2).relative(side.getOpposite(), 3), facing.getOpposite())
        };
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
        return 10_000_000L;
    }

    @Override
    public boolean canConnect(Direction direction) {
        return direction != null && direction.getAxis().isHorizontal();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.waterTimer = tag.getInt("waterTimer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("waterTimer", this.waterTimer);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.waterTimer);
        buf.writeFloat(this.fanRotation);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.waterTimer = buf.readInt();
        this.fanRotation = buf.readFloat();
    }

    private record Connection(BlockPos pos, Direction direction) { }
}
