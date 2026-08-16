package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineElectrolyserBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.MultiblockHandlerXR;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MachineElectrolyserBlock extends DummyableBlock {

    public MachineElectrolyserBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineElectrolyserBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineElectrolyserBlock> CODEC = simpleCodec(MachineElectrolyserBlock::new);
    @Override public MapCodec<MachineElectrolyserBlock> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] {0, 0, 5, 5, 1, 3}; }
    @Override public int getOffset() { return 5; }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        BlockPos corePos = pos.relative(dir, offset);

        if(!MultiblockHandlerXR.checkSpace(level, corePos, getDimensions(), pos, dir)) return false;

        if(!MultiblockHandlerXR.checkSpace(level, corePos, new int[] {2, -1, 5, 5, 1, 1}, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos, new int[] {3, -3, 5, 5, 0, 0}, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos, new int[] {3, -1, 4, -4, -3, 3}, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos, new int[] {3, -1, 2, -2, -3, 3}, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos, new int[] {3, -1, 0, 0, -3, 3}, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos, new int[] {3, -1, -2, 2, -3, 3}, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos, new int[] {3, -1, -4, 4, -3, 3}, pos, dir)) return false;

        int[] dummyPole = new int[] {0, 0, 0, 0, -1, 2};

        if(!MultiblockHandlerXR.checkSpace(level, corePos.relative(dir, 4).above(3), dummyPole, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos.relative(dir, 2).above(3), dummyPole, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos.above(3), dummyPole, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos.relative(dir.getOpposite(), 2).above(3), dummyPole, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, corePos.relative(dir.getOpposite(), 4).above(3), dummyPole, pos, dir)) return false;

        return true;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);
        Direction rot = dir.getClockWise(Axis.Y);

        MultiblockHandlerXR.fillSpace(level, corePos, new int[] {2, -1, 5, 5, 1, 1}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[] {3, -3, 5, 5, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[] {3, -1, 4, -4, -3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[] {3, -1, 2, -2, -3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[] {3, -1, 0, 0, -3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[] {3, -1, -2, 2, -3, 3}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[] {3, -1, -4, 4, -3, 3}, this, dir);

        int[] dummyPole = new int[] {0, 0, 0, 0, -1, 2};

        MultiblockHandlerXR.fillSpace(level, corePos.relative(dir, 4).above(3), dummyPole, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos.relative(dir, 2).above(3), dummyPole, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos.above(3), dummyPole, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos.relative(dir.getOpposite(), 2).above(3), dummyPole, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos.relative(dir.getOpposite(), 4).above(3), dummyPole, this, dir);

        // Corner dummy blocks flagged as "extra" (matching the old makeExtra calls)
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 5));
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 5).relative(rot));
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 5).relative(rot.getOpposite()));
        this.makeExtra(level, corePos.relative(dir, 5));
        this.makeExtra(level, corePos.relative(dir, 5).relative(rot));
        this.makeExtra(level, corePos.relative(dir, 5).relative(rot.getOpposite()));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
        // NOTE: this will currently fail when the menu opens, since createMenu() isn't implemented yet
    }
}