package com.hbm.blockentity.network;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.network.ElectricityConnectorBlock;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PylonConnectorBlockEntity extends PylonBaseBlockEntity {

    public PylonConnectorBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.PYLON_CONNECTOR.get(), pos, state);
    }

    private boolean isHeavy() {
        return this.getBlockState().is(NtmBlocks.RED_CONNECTOR_SUPER.get());
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.SINGLE;
    }

    @Override
    public int getMaxWireLength() {
        return this.isHeavy() ? 100 : 10;
    }

    @Override
    public Vec3[] getMountPositions() {
        Direction facing = this.getBlockState().getValue(ElectricityConnectorBlock.FACING);
        double distance = this.isHeavy() ? 0.375D : 0D;
        return new Vec3[] {new Vec3(0.5D + facing.getStepX() * distance, 0.5D + facing.getStepY() * distance, 0.5D + facing.getStepZ() * distance)};
    }

    @Override
    protected DirPos[] getLocalConnections() {
        Direction facing = this.getBlockState().getValue(ElectricityConnectorBlock.FACING);
        return new DirPos[] {new DirPos(this.worldPosition.relative(facing.getOpposite()), facing.getOpposite())};
    }

    @Override
    public boolean canConnect(Direction direction) {
        return direction == this.getBlockState().getValue(ElectricityConnectorBlock.FACING).getOpposite();
    }
}
