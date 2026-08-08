package com.hbm.blocks.machine;

import com.hbm.blockentity.machine.oil.FractioningSpacerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FractioningSpacerBlock extends DummyableBlock {

    public static final MapCodec<FractioningSpacerBlock> CODEC = simpleCodec(FractioningSpacerBlock::new);

    public FractioningSpacerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(TYPE) == DummyBlockType.CORE ? new FractioningSpacerBlockEntity(pos, state) : null;
    }

    @Override
    public MapCodec<FractioningSpacerBlock> codec() {
        return CODEC;
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 0, 0, 1, 1, 1, 1 };
    }

    @Override
    public int getOffset() {
        return 1;
    }
}
