package com.hbm.blockentity.network;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.network.ElectricityPylonBlock;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PylonBlockEntity extends PylonBaseBlockEntity {

    public PylonBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.PYLON.get(), pos, state);
    }

    public ElectricityPylonBlock.Variant getVariant() {
        if(this.getBlockState().getBlock() instanceof ElectricityPylonBlock block) return block.getVariant();
        return ElectricityPylonBlock.Variant.WOOD;
    }

    @Override
    public ConnectionType getConnectionType() {
        return switch(this.getVariant()) {
            case WOOD, STEEL -> ConnectionType.SINGLE;
            case MEDIUM_WOOD, MEDIUM_WOOD_TRANSFORMER, MEDIUM_STEEL, MEDIUM_STEEL_TRANSFORMER -> ConnectionType.TRIPLE;
            case LARGE, SUBSTATION -> ConnectionType.QUADRUPLE;
        };
    }

    @Override
    public int getMaxWireLength() {
        return ((ElectricityPylonBlock)this.getBlockState().getBlock()).getWireLength();
    }

    @Override
    public Vec3[] getMountPositions() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return switch(this.getVariant()) {
            case WOOD, STEEL -> new Vec3[] {new Vec3(0.5D, 5.5D, 0.5D)};
            case MEDIUM_WOOD, MEDIUM_WOOD_TRANSFORMER, MEDIUM_STEEL, MEDIUM_STEEL_TRANSFORMER -> new Vec3[] {
                    new Vec3(0.5D, 7.5D, 0.5D),
                    new Vec3(0.5D + facing.getStepX(), 7.5D, 0.5D + facing.getStepZ()),
                    new Vec3(0.5D + facing.getStepX() * 2D, 7.5D, 0.5D + facing.getStepZ() * 2D)
            };
            case LARGE -> this.getLargeMountPositions(facing);
            case SUBSTATION -> this.getSubstationMountPositions(facing);
        };
    }

    private Vec3[] getLargeMountPositions(Direction facing) {
        double diagonal = Math.sqrt(0.5D);
        Vec3 axis = switch(facing) {
            case EAST -> new Vec3(-diagonal, 0D, -diagonal);
            case SOUTH -> new Vec3(0D, 0D, -1D);
            case WEST -> new Vec3(diagonal, 0D, -diagonal);
            default -> new Vec3(1D, 0D, 0D);
        };
        double sideOffset = 3.375D;
        double topOffset = 0.8125D;
        return new Vec3[] {
                new Vec3(0.5D + axis.x * sideOffset, 11.5D + topOffset, 0.5D + axis.z * sideOffset),
                new Vec3(0.5D + axis.x * sideOffset, 11.5D - topOffset, 0.5D + axis.z * sideOffset),
                new Vec3(0.5D - axis.x * sideOffset, 11.5D + topOffset, 0.5D - axis.z * sideOffset),
                new Vec3(0.5D - axis.x * sideOffset, 11.5D - topOffset, 0.5D - axis.z * sideOffset)
        };
    }

    private Vec3[] getSubstationMountPositions(Direction facing) {
        boolean alongX = facing == Direction.NORTH || facing == Direction.SOUTH;
        Vec3 axis = alongX ? new Vec3(1D, 0D, 0D) : new Vec3(0D, 0D, -1D);
        return new Vec3[] {
                new Vec3(0.5D + axis.x * 0.5D, 5.25D, 0.5D + axis.z * 0.5D),
                new Vec3(0.5D + axis.x * 1.5D, 5.25D, 0.5D + axis.z * 1.5D),
                new Vec3(0.5D - axis.x * 0.5D, 5.25D, 0.5D - axis.z * 0.5D),
                new Vec3(0.5D - axis.x * 1.5D, 5.25D, 0.5D - axis.z * 1.5D)
        };
    }

    @Override
    protected BlockPos[] getNodePositions() {
        if(this.getVariant() != ElectricityPylonBlock.Variant.SUBSTATION) return super.getNodePositions();
        return new BlockPos[] {
                this.worldPosition,
                this.worldPosition.offset(1, 0, 1),
                this.worldPosition.offset(1, 0, -1),
                this.worldPosition.offset(-1, 0, 1),
                this.worldPosition.offset(-1, 0, -1)
        };
    }

    @Override
    protected DirPos[] getLocalConnections() {
        List<DirPos> positions = new ArrayList<>();
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        ElectricityPylonBlock.Variant variant = this.getVariant();

        if(variant == ElectricityPylonBlock.Variant.WOOD || variant == ElectricityPylonBlock.Variant.STEEL) {
            for(Direction direction : Direction.Plane.HORIZONTAL) positions.add(new DirPos(this.worldPosition.relative(direction), direction));
        }

        if(variant == ElectricityPylonBlock.Variant.MEDIUM_WOOD_TRANSFORMER || variant == ElectricityPylonBlock.Variant.MEDIUM_STEEL_TRANSFORMER) {
            positions.add(new DirPos(this.worldPosition.relative(facing.getOpposite()), facing.getOpposite()));
        }

        if(variant == ElectricityPylonBlock.Variant.SUBSTATION) {
            positions.add(new DirPos(this.worldPosition.offset(2, 0, -1), Direction.EAST));
            positions.add(new DirPos(this.worldPosition.offset(2, 0, 1), Direction.EAST));
            positions.add(new DirPos(this.worldPosition.offset(-2, 0, -1), Direction.WEST));
            positions.add(new DirPos(this.worldPosition.offset(-2, 0, 1), Direction.WEST));
            positions.add(new DirPos(this.worldPosition.offset(-1, 0, 2), Direction.SOUTH));
            positions.add(new DirPos(this.worldPosition.offset(1, 0, 2), Direction.SOUTH));
            positions.add(new DirPos(this.worldPosition.offset(-1, 0, -2), Direction.NORTH));
            positions.add(new DirPos(this.worldPosition.offset(1, 0, -2), Direction.NORTH));
        }

        return positions.toArray(DirPos[]::new);
    }

    @Override
    public boolean canConnect(Direction direction) {
        if(direction == null || !direction.getAxis().isHorizontal()) return false;
        ElectricityPylonBlock.Variant variant = this.getVariant();
        if(variant == ElectricityPylonBlock.Variant.LARGE) return false;
        if(variant == ElectricityPylonBlock.Variant.MEDIUM_WOOD || variant == ElectricityPylonBlock.Variant.MEDIUM_STEEL) return false;
        if(variant == ElectricityPylonBlock.Variant.MEDIUM_WOOD_TRANSFORMER || variant == ElectricityPylonBlock.Variant.MEDIUM_STEEL_TRANSFORMER) {
            return direction == this.getBlockState().getValue(DummyableBlock.FACING).getOpposite();
        }
        return true;
    }
}
