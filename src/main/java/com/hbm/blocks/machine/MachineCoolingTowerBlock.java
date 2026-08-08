package com.hbm.blocks.machine;

import com.hbm.blockentity.machine.tower.AbstractCoolingTowerBlockEntity;
import com.hbm.blockentity.machine.tower.MachineCoolingTowerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCoolingTowerBlock extends AbstractCoolingTowerBlock {

    private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[] {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST
    };
    public static final MapCodec<MachineCoolingTowerBlock> CODEC = simpleCodec(MachineCoolingTowerBlock::new);

    public MachineCoolingTowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<MachineCoolingTowerBlock> codec() {
        return CODEC;
    }

    @Override
    protected AbstractCoolingTowerBlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new MachineCoolingTowerBlockEntity(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {12, 0, 4, 4, 4, 4};
    }

    @Override
    public int getOffset() {
        return 4;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);
        for(Direction direction : HORIZONTAL_DIRECTIONS) {
            Direction side = direction.getClockWise();
            this.makeExtra(level, corePos.relative(direction, 4));
            this.makeExtra(level, corePos.relative(direction, 4).relative(side, 3));
            this.makeExtra(level, corePos.relative(direction, 4).relative(side.getOpposite(), 3));
        }
    }
}
