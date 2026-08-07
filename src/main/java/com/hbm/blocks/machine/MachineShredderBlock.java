package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.MachineShredderBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MachineShredderBlock extends DummyableBlock {

    public MachineShredderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new MachineShredderBlockEntity(pos, state);
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if (be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override public int[] getDimensions() { return new int[] {0, 0, 0, 0, 0, 0}; }
    @Override public int getOffset() { return 0; }

    @Override
    protected Direction getDirModified(Direction dir) {
        // The shredder's top/bottom textures are fixed - only allow horizontal facings,
        // falling back to north if the player placed it while looking straight up/down.
        return (dir == Direction.UP || dir == Direction.DOWN) ? Direction.NORTH : dir;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        // Simple cube look (no custom 3D model/animation yet) - renders the JSON model directly
        return RenderShape.MODEL;
    }

    public static final MapCodec<MachineShredderBlock> CODEC = simpleCodec(MachineShredderBlock::new);
    @Override public MapCodec<MachineShredderBlock> codec() { return CODEC; }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
