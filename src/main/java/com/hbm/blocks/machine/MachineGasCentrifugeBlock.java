package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.MachineGasCentrifugeBlockEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class MachineGasCentrifugeBlock extends DummyableBlock {

    public MachineGasCentrifugeBlock(Properties properties) {
        super(properties);
        bounding.add(new AABB(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5));
        bounding.add(new AABB(-0.4375, 1.0, -0.4375, 0.4375, 4.0, 0.4375));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new MachineGasCentrifugeBlockEntity(pos, state);
            default -> null;
        };
    }

    @Override public int[] getDimensions() { return new int[] {3, 0, 0, 0, 0, 0}; }
    @Override public int getOffset() { return 0; }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineGasCentrifugeBlock> CODEC = simpleCodec(MachineGasCentrifugeBlock::new);
    @Override public MapCodec<MachineGasCentrifugeBlock> codec() { return CODEC; }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
