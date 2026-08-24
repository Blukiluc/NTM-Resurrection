package com.hbm.blocks.machine;

import com.hbm.blockentity.machine.tower.AbstractCoolingTowerBlockEntity;
import com.hbm.blockentity.machine.tower.MachineAuxiliaryCoolingTowerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MachineAuxiliaryCoolingTowerBlock extends AbstractCoolingTowerBlock {

    public static final MapCodec<MachineAuxiliaryCoolingTowerBlock> CODEC = simpleCodec(MachineAuxiliaryCoolingTowerBlock::new);

    public MachineAuxiliaryCoolingTowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<MachineAuxiliaryCoolingTowerBlock> codec() {
        return CODEC;
    }

    @Override
    protected AbstractCoolingTowerBlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new MachineAuxiliaryCoolingTowerBlockEntity(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {18, 0, 2, 2, 2, 2};
    }

    @Override
    public int getOffset() {
        return 2;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);
        this.makeExtra(level, corePos.relative(Direction.NORTH, 2));
        this.makeExtra(level, corePos.relative(Direction.SOUTH, 2));
        this.makeExtra(level, corePos.relative(Direction.WEST, 2));
        this.makeExtra(level, corePos.relative(Direction.EAST, 2));
    }
}
