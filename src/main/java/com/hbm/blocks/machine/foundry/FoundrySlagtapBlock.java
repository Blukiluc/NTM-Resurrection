package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.machine.foundry.FoundrySlagtapBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FoundrySlagtapBlock extends FoundryOutletBlock {

    public FoundrySlagtapBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<FoundrySlagtapBlock> CODEC = simpleCodec(FoundrySlagtapBlock::new);
    @Override protected MapCodec<FoundrySlagtapBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoundrySlagtapBlockEntity(pos, state);
    }
}
