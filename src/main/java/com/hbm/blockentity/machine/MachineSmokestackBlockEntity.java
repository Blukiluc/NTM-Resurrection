package com.hbm.blockentity.machine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MachineSmokestackBlockEntity extends MachineSmokestackBaseBlockEntity {

    public MachineSmokestackBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CHIMNEY_BRICK.get(), pos, state);
    }

    @Override
    protected double getPollutionModifier() {
        return 0.25D;
    }

    @Override
    protected double getParticleHeight() {
        return 12D;
    }

    @Override
    protected float getParticleBaseScale() {
        return 0.5F;
    }
}
