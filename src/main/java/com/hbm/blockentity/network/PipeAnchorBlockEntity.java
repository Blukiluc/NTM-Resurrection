package com.hbm.blockentity.network;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.network.PipeAnchorBlock;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PipeAnchorBlockEntity extends PipelineBaseBlockEntity {

    public PipeAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.PIPE_ANCHOR.get(), pos, state);
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.SMALL;
    }

    @Override
    public Vec3 getMountPos() {
        return new Vec3(0.5D, 0.5D, 0.5D);
    }

    @Override
    public int getMaxPipeLength() {
        return 10;
    }

    @Override
    protected DirPos[] getLocalConnections() {
        Direction direction = this.getBlockState().getValue(PipeAnchorBlock.FACING).getOpposite();
        return new DirPos[] {new DirPos(this.worldPosition.relative(direction), direction)};
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return direction == this.getBlockState().getValue(PipeAnchorBlock.FACING).getOpposite() && type == this.type;
    }
}
