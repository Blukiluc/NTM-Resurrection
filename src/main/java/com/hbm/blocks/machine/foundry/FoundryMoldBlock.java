package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.machine.foundry.FoundryMoldBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FoundryMoldBlock extends FoundryCastingBlock {

    public FoundryMoldBlock(Properties properties) {
        super(properties, Block.box(0, 0, 0, 16, 8, 16));
    }

    public static final MapCodec<FoundryMoldBlock> CODEC = simpleCodec(FoundryMoldBlock::new);
    @Override protected MapCodec<FoundryMoldBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoundryMoldBlockEntity(pos, state);
    }
}
