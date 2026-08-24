package com.hbm.blockentity.machine.foundry;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FoundryBasinBlockEntity extends FoundryCastingBlockEntity {

    public FoundryBasinBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FOUNDRY_BASIN.get(), pos, state);
    }

    @Override
    public int getMoldSize() {
        return 1;
    }
}
