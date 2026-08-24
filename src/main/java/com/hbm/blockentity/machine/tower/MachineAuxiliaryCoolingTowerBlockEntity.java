package com.hbm.blockentity.machine.tower;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class MachineAuxiliaryCoolingTowerBlockEntity extends AbstractCoolingTowerBlockEntity {

    private static final ParticleSettings PARTICLE_SETTINGS = new ParticleSettings(2, 18D, 1F, 0.5F, 4F, 250, 250, 0D);

    public MachineAuxiliaryCoolingTowerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_TOWER_SMALL.get(), pos, state, 1_000);
    }

    @Override
    protected DirPos[] getConnectionPositions() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.relative(Direction.NORTH, 3), Direction.NORTH),
                new DirPos(pos.relative(Direction.SOUTH, 3), Direction.SOUTH),
                new DirPos(pos.relative(Direction.WEST, 3), Direction.WEST),
                new DirPos(pos.relative(Direction.EAST, 3), Direction.EAST)
        };
    }

    @Override
    protected ParticleSettings getParticleSettings() {
        return PARTICLE_SETTINGS;
    }
}
