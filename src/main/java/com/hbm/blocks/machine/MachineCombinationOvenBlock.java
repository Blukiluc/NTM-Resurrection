package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineCombinationOvenBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MachineCombinationOvenBlock extends DummyableBlock {

    public static final MapCodec<MachineCombinationOvenBlock> CODEC = simpleCodec(MachineCombinationOvenBlock::new);

    public MachineCombinationOvenBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<MachineCombinationOvenBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineCombinationOvenBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().fluid();
            default -> new ProxyComboBlockEntity(pos, state).inventory().fluid();
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override public int[] getDimensions() { return new int[] { 1, 0, 1, 1, 1, 1 }; }
    @Override public int getOffset() { return 1; }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
