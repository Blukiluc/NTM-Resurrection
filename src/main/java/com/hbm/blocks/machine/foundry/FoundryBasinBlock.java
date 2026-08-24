package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.machine.foundry.FoundryBasinBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FoundryBasinBlock extends FoundryCastingBlock {

    public FoundryBasinBlock(Properties properties) {
        super(properties, Block.box(0, 0, 0, 16, 16, 16));
    }

    public static final MapCodec<FoundryBasinBlock> CODEC = simpleCodec(FoundryBasinBlock::new);
    @Override protected MapCodec<FoundryBasinBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoundryBasinBlockEntity(pos, state);
    }
}
