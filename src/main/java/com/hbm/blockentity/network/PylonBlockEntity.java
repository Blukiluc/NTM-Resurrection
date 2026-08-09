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
        Direction side = facing.getClockWise();
        return switch(this.getVariant()) {
            case WOOD, STEEL -> new Vec3[] {new Vec3(0.5D, 5.5D, 0.5D)};
            case MEDIUM_WOOD, MEDIUM_WOOD_TRANSFORMER, MEDIUM_STEEL, MEDIUM_STEEL_TRANSFORMER -> new Vec3[] {
                    new Vec3(0.5D - side.getStepX(), 7.5D, 0.5D - side.getStepZ()),
                    new Vec3(0.5D, 7.5D, 0.5D),
                    new Vec3(0.5D + side.getStepX(), 7.5D, 0.5D + side.getStepZ())
            };
            case LARGE -> new Vec3[] {
                    new Vec3(0.5D - side.getStepX() * 3.375D, 13.8125D, 0.5D - side.getStepZ() * 3.375D),
                    new Vec3(0.5D - side.getStepX() * 1.125D, 14.625D, 0.5D - side.getStepZ() * 1.125D),
                    new Vec3(0.5D + side.getStepX() * 1.125D, 14.625D, 0.5D + side.getStepZ() * 1.125D),
                    new Vec3(0.5D + side.getStepX() * 3.375D, 13.8125D, 0.5D + side.getStepZ() * 3.375D)
            };
            case SUBSTATION -> new Vec3[] {
                    new Vec3(0.5D - side.getStepX() * 1.5D - facing.getStepX(), 4.5D, 0.5D - side.getStepZ() * 1.5D - facing.getStepZ()),
                    new Vec3(0.5D - side.getStepX() * 0.5D - facing.getStepX(), 4.5D, 0.5D - side.getStepZ() * 0.5D - facing.getStepZ()),
                    new Vec3(0.5D + side.getStepX() * 0.5D - facing.getStepX(), 4.5D, 0.5D + side.getStepZ() * 0.5D - facing.getStepZ()),
                    new Vec3(0.5D + side.getStepX() * 1.5D - facing.getStepX(), 4.5D, 0.5D + side.getStepZ() * 1.5D - facing.getStepZ())
            };
        };
    }

    @Override
    protected BlockPos[] getNodePositions() {
        if(this.getVariant() != ElectricityPylonBlock.Variant.SUBSTATION) return super.getNodePositions();
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();
        return new BlockPos[] {
                this.worldPosition,
                this.worldPosition.relative(facing).relative(side, 2),
                this.worldPosition.relative(facing).relative(side.getOpposite(), 2),
                this.worldPosition.relative(facing.getOpposite()).relative(side, 2),
                this.worldPosition.relative(facing.getOpposite()).relative(side.getOpposite(), 2)
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
            Direction side = facing.getClockWise();
            for(BlockPos nodePos : this.getNodePositions()) {
                if(nodePos.equals(this.worldPosition)) continue;
                Direction outward = nodePos.getX() == this.worldPosition.getX() ? facing : nodePos.getZ() == this.worldPosition.getZ() ? side : Direction.getNearest(nodePos.getX() - this.worldPosition.getX(), 0, nodePos.getZ() - this.worldPosition.getZ());
                positions.add(new DirPos(nodePos.relative(outward), outward));
            }
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
