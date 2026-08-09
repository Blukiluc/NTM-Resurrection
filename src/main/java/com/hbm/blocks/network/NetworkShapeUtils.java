package com.hbm.blocks.network;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class NetworkShapeUtils {

    private NetworkShapeUtils() {
    }

    public static VoxelShape connectedShape(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ, double diameter, double junctionDiameter) {
        double min = (16.0D - diameter) / 32.0D;
        double max = 1.0D - min;
        double junctionMin = (16.0D - junctionDiameter) / 32.0D;
        double junctionMax = 1.0D - junctionMin;
        int mask = (posX ? 32 : 0) + (negX ? 16 : 0) + (posY ? 8 : 0) + (negY ? 4 : 0) + (posZ ? 2 : 0) + (negZ ? 1 : 0);
        int count = (posX ? 1 : 0) + (negX ? 1 : 0) + (posY ? 1 : 0) + (negY ? 1 : 0) + (posZ ? 1 : 0) + (negZ ? 1 : 0);

        if(mask == 0b100000 || mask == 0b010000 || mask == 0b110000) return Shapes.box(0.0D, min, min, 1.0D, max, max);
        if(mask == 0b001000 || mask == 0b000100 || mask == 0b001100) return Shapes.box(min, 0.0D, min, max, 1.0D, max);
        if(mask == 0b000010 || mask == 0b000001 || mask == 0b000011) return Shapes.box(min, min, 0.0D, max, max, 1.0D);

        double centerMin = count == 2 ? min : junctionMin;
        double centerMax = count == 2 ? max : junctionMax;
        VoxelShape shape = Shapes.box(centerMin, centerMin, centerMin, centerMax, centerMax, centerMax);

        if(posX) shape = Shapes.or(shape, Shapes.box(max, min, min, 1.0D, max, max));
        if(negX) shape = Shapes.or(shape, Shapes.box(0.0D, min, min, min, max, max));
        if(posY) shape = Shapes.or(shape, Shapes.box(min, max, min, max, 1.0D, max));
        if(negY) shape = Shapes.or(shape, Shapes.box(min, 0.0D, min, max, min, max));
        if(posZ) shape = Shapes.or(shape, Shapes.box(min, min, max, max, max, 1.0D));
        if(negZ) shape = Shapes.or(shape, Shapes.box(min, min, 0.0D, max, max, min));

        return shape;
    }
}
