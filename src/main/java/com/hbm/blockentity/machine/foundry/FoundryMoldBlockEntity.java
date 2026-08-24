package com.hbm.blockentity.machine.foundry;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FoundryMoldBlockEntity extends FoundryCastingBlockEntity {

    public FoundryMoldBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FOUNDRY_MOLD.get(), pos, state);
    }

    @Override
    public int getMoldSize() {
        return 0;
    }
}
