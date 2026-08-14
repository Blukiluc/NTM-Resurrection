package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MachineBigAssTankBlockEntity extends MachineFluidTankBlockEntity {

    public MachineBigAssTankBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.BIG_ASS_TANK.get(), pos, state, 16_000_000);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.bigAssTank");
    }

    @Override public long getReceiverSpeed(FluidType type, int pressure) { return Math.max(50_000, (tank.getMaxFill() - tank.getFill()) / 100); }
    @Override public long getProviderSpeed(FluidType type, int pressure) { return Math.max(50_000, tank.getFill() / 100); }

    @Override
    public void updateEntity() {
        if(level != null && !level.isClientSide) {
            this.checkTilt(TiltType.UNAVOIDABLE, true);

            if(tank.getFill() > 0 && tank.getTankType().isAntimatter()) {
                BlockPos pos = this.getBlockPos();
                level.destroyBlock(pos, false);
                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10F, true, Level.ExplosionInteraction.BLOCK);
                return;
            }
        }

        super.updateEntity();
    }

    @Override public int getFloorCount() { return 4 * 4; }
    @Override public BlockPos getFloorPosFromIndex(int index) { return this.standardFloor7x7(index); }

    @Override
    protected DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        BlockPos pos = this.getBlockPos();

        return new DirPos[] {
                new DirPos(pos.relative(dir, 7), dir),
                new DirPos(pos.relative(dir.getOpposite(), 7), dir.getOpposite())
        };
    }
}
