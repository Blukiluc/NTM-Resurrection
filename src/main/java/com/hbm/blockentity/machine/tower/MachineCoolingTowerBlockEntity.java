package com.hbm.blockentity.machine.tower;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCoolingTowerBlockEntity extends AbstractCoolingTowerBlockEntity {

    private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[] {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST
    };
    private static final ParticleSettings PARTICLE_SETTINGS = new ParticleSettings(4, 1D, 0.5F, 1F, 10F, 750, 250, 1.5D);

    public MachineCoolingTowerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_TOWER_LARGE.get(), pos, state, 10_000);
    }

    @Override
    protected DirPos[] getConnectionPositions() {
        DirPos[] positions = new DirPos[12];
        BlockPos pos = this.getBlockPos();
        int index = 0;

        for(Direction direction : HORIZONTAL_DIRECTIONS) {
            Direction side = direction.getClockWise();
            positions[index++] = new DirPos(pos.relative(direction, 5), direction);
            positions[index++] = new DirPos(pos.relative(direction, 5).relative(side, 3), direction);
            positions[index++] = new DirPos(pos.relative(direction, 5).relative(side.getOpposite(), 3), direction);
        }

        return positions;
    }

    @Override
    protected ParticleSettings getParticleSettings() {
        return PARTICLE_SETTINGS;
    }
}
