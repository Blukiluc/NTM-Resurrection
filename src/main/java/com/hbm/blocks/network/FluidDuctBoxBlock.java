package com.hbm.blocks.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FluidDuctBoxBlock extends FluidDuctStandardBlock {

    private final double diameter;
    private final double junctionDiameter;

    public FluidDuctBoxBlock(Properties properties, double diameter, double junctionDiameter) {
        super(properties);
        this.diameter = diameter;
        this.junctionDiameter = junctionDiameter;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getDuctShape(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getDuctShape(state);
    }

    private VoxelShape getDuctShape(BlockState state) {
        return NetworkShapeUtils.connectedShape(
                state.getValue(EAST),
                state.getValue(WEST),
                state.getValue(UP),
                state.getValue(DOWN),
                state.getValue(SOUTH),
                state.getValue(NORTH),
                this.diameter,
                this.junctionDiameter
        );
    }
}
