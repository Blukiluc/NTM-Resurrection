package com.hbm.blockentity.machine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCompressorCompactBlockEntity extends MachineCompressorBaseBlockEntity {

    public float fanSpin;
    public float prevFanSpin;

    public MachineCompressorCompactBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.COMPRESSOR_COMPACT.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if(this.level != null && this.level.isClientSide) {
            this.prevFanSpin = this.fanSpin;

            if(this.isOn) {
                this.fanSpin += 45F;

                if(this.fanSpin >= 360F) {
                    this.prevFanSpin -= 360F;
                    this.fanSpin -= 360F;
                }
            }
        }
    }

    @Override
    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getCounterClockWise(Direction.Axis.Y);

        return new DirPos[] {
                new DirPos(pos.getX() + rot.getStepX() * 4, pos.getY() + 1, pos.getZ() + rot.getStepZ() * 4, rot),
                new DirPos(pos.getX() - rot.getStepX() * 4, pos.getY() + 1, pos.getZ() - rot.getStepZ() * 4, rot.getOpposite()),
                new DirPos(pos.getX() + dir.getStepX() * 2 - rot.getStepX(), pos.getY() + 1, pos.getZ() + dir.getStepZ() * 2 - rot.getStepZ(), dir),
                new DirPos(pos.getX() + dir.getStepX() * 2 + rot.getStepX(), pos.getY() + 1, pos.getZ() + dir.getStepZ() * 2 + rot.getStepZ(), dir),
                new DirPos(pos.getX() - dir.getStepX() * 2 - rot.getStepX(), pos.getY() + 1, pos.getZ() - dir.getStepZ() * 2 - rot.getStepZ(), dir.getOpposite()),
                new DirPos(pos.getX() - dir.getStepX() * 2 + rot.getStepX(), pos.getY() + 1, pos.getZ() - dir.getStepZ() * 2 + rot.getStepZ(), dir.getOpposite())
        };
    }
}
