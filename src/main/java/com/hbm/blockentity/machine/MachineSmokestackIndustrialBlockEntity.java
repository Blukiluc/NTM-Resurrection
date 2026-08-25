package com.hbm.blockentity.machine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MachineSmokestackIndustrialBlockEntity extends MachineSmokestackBaseBlockEntity {

    public MachineSmokestackIndustrialBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.SMOKESTACK_INDUSTRIAL.get(), pos, state);
    }

    @Override
    protected double getPollutionModifier() {
        return 0.1D;
    }

    @Override
    protected double getParticleHeight() {
        return 22D;
    }

    @Override
    protected float getParticleBaseScale() {
        return 0.75F;
    }
}
