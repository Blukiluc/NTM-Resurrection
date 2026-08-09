package com.hbm.blocks.network;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.network.CableBaseBlockEntity;
import com.hbm.lib.Library;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.context.BlockPlaceContext;

public class CableBlock extends Block implements EntityBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST =  BlockStateProperties.EAST;
    public static final BooleanProperty WEST =  BlockStateProperties.WEST;
    public static final BooleanProperty UP =    BlockStateProperties.UP;
    public static final BooleanProperty DOWN =  BlockStateProperties.DOWN;

    private final double diameter;

    public CableBlock(Properties properties) {
        this(properties, 5.0D);
    }

    protected CableBlock(Properties properties, double diameter) {
        super(properties);
        this.diameter = diameter;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(EAST,  Boolean.FALSE)
                .setValue(WEST,  Boolean.FALSE)
                .setValue(UP,    Boolean.FALSE)
                .setValue(DOWN,  Boolean.FALSE)
        );
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        return this.defaultBlockState()
                .setValue(NORTH, Library.canConnect(level, pos.north(), Direction.NORTH))
                .setValue(SOUTH, Library.canConnect(level, pos.south(), Direction.SOUTH))
                .setValue(EAST,  Library.canConnect(level, pos.east(), Direction.EAST))
                .setValue(WEST,  Library.canConnect(level, pos.west(), Direction.WEST))
                .setValue(UP,    Library.canConnect(level, pos.above(), Direction.UP))
                .setValue(DOWN,  Library.canConnect(level, pos.below(), Direction.DOWN));
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (!level.isClientSide) {
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

            for (Direction dir : Direction.values()) {
                level.sendBlockUpdated(
                        pos.relative(dir),
                        level.getBlockState(pos.relative(dir)),
                        level.getBlockState(pos.relative(dir)),
                        Block.UPDATE_ALL
                );
            }
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    public static final MapCodec<CableBlock> CODEC = simpleCodec(CableBlock::new);
    @Override protected MapCodec<CableBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBaseBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        state = state
                .setValue(NORTH, Library.canConnect(level, pos.relative(Direction.NORTH), Direction.NORTH))
                .setValue(SOUTH, Library.canConnect(level, pos.relative(Direction.SOUTH), Direction.SOUTH))
                .setValue(EAST,  Library.canConnect(level, pos.relative(Direction.EAST), Direction.EAST))
                .setValue(WEST,  Library.canConnect(level, pos.relative(Direction.WEST), Direction.WEST))
                .setValue(UP,    Library.canConnect(level, pos.relative(Direction.UP), Direction.UP))
                .setValue(DOWN,  Library.canConnect(level, pos.relative(Direction.DOWN), Direction.DOWN));
        return state;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        boolean posX = state.getValue(EAST);
        boolean negX = state.getValue(WEST);
        boolean posY = state.getValue(UP);
        boolean negY = state.getValue(DOWN);
        boolean posZ = state.getValue(SOUTH);
        boolean negZ = state.getValue(NORTH);

        return this.getBlockBounds(posX, negX, posY, negY, posZ, negZ);
    }

    protected VoxelShape getBlockBounds(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        double min = (16.0D - this.diameter) / 32.0D;
        double max = 1.0D - min;
        return Shapes.box(
                negX ? 0.0D : min,
                negY ? 0.0D : min,
                negZ ? 0.0D : min,
                posX ? 1.0D : max,
                posY ? 1.0D : max,
                posZ ? 1.0D : max
        );
    }
}
