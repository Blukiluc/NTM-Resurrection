package com.hbm.blocks.network;

import net.minecraft.world.phys.shapes.VoxelShape;

public class BoxCableBlock extends CableBlock {

    private final double diameter;

    public BoxCableBlock(Properties properties, double diameter) {
        super(properties, diameter);
        this.diameter = diameter;
    }

    @Override
    protected VoxelShape getBlockBounds(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        return NetworkShapeUtils.connectedShape(posX, negX, posY, negY, posZ, negZ, this.diameter, this.diameter);
    }
}
