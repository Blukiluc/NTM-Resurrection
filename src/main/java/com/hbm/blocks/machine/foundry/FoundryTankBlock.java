package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.machine.foundry.FoundryTankBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FoundryTankBlock extends FoundryMaterialBlock {

    public FoundryTankBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<FoundryTankBlock> CODEC = simpleCodec(FoundryTankBlock::new);
    @Override protected MapCodec<FoundryTankBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoundryTankBlockEntity(pos, state);
    }
}
