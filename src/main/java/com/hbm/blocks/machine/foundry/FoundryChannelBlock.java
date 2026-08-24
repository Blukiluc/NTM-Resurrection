package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.machine.foundry.FoundryChannelBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.machine.MachineCrucibleBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FoundryChannelBlock extends FoundryMaterialBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    private static final VoxelShape CENTER = Block.box(5, 0, 5, 11, 8, 11);
    private static final VoxelShape NORTH_SHAPE = Block.box(5, 0, 0, 11, 8, 5);
    private static final VoxelShape EAST_SHAPE = Block.box(11, 0, 5, 16, 8, 11);
    private static final VoxelShape SOUTH_SHAPE = Block.box(5, 0, 11, 11, 8, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 0, 5, 5, 8, 11);

    public FoundryChannelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
    }

    public static final MapCodec<FoundryChannelBlock> CODEC = simpleCodec(FoundryChannelBlock::new);
    @Override protected MapCodec<FoundryChannelBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoundryChannelBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BooleanProperty property = switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> null;
        };
        return property == null ? state : state.setValue(property,
                this.connectsTo(level, pos, neighborPos, neighborState, direction));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.updateConnections(context.getLevel(), context.getClickedPos(), this.defaultBlockState());
    }

    public BlockState updateConnections(BlockGetter level, BlockPos channelPos, BlockState state) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BooleanProperty property = switch (direction) {
                case NORTH -> NORTH;
                case EAST -> EAST;
                case SOUTH -> SOUTH;
                case WEST -> WEST;
                default -> null;
            };
            BlockPos neighborPos = channelPos.relative(direction);
            state = state.setValue(property, this.connectsTo(level, channelPos, neighborPos,
                    level.getBlockState(neighborPos), direction));
        }
        return state;
    }

    private boolean connectsTo(BlockGetter level, BlockPos channelPos, BlockPos neighborPos,
                               BlockState state, Direction direction) {
        if (state.getBlock() instanceof FoundryChannelBlock || state.getBlock() instanceof FoundryMoldBlock) return true;
        if (state.getBlock() instanceof FoundryOutletBlock) {
            return state.getValue(FoundryOutletBlock.FACING) == direction;
        }
        if (state.getBlock() instanceof MachineCrucibleBlock crucible) {
            BlockPos corePos = crucible.findCore(level, neighborPos);
            if (corePos == null) return false;
            Direction facing = level.getBlockState(corePos).getValue(DummyableBlock.FACING);
            Direction recipeOutput = facing.getCounterClockWise(Direction.Axis.Y);
            Direction wasteOutput = facing.getClockWise();
            return channelPos.equals(corePos.relative(recipeOutput, 2))
                    || channelPos.equals(corePos.relative(wasteOutput, 2));
        }
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = CENTER;
        if (state.getValue(NORTH)) shape = Shapes.or(shape, NORTH_SHAPE);
        if (state.getValue(EAST)) shape = Shapes.or(shape, EAST_SHAPE);
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(WEST)) shape = Shapes.or(shape, WEST_SHAPE);
        return shape;
    }
}
