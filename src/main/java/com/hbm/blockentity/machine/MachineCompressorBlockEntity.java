package com.hbm.blockentity.machine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCompressorBlockEntity extends MachineCompressorBaseBlockEntity {

    public float fanSpin;
    public float prevFanSpin;
    public float piston;
    public float prevPiston;
    public boolean pistonDir;
    private float randSpeed = 0.1F;

    public MachineCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.COMPRESSOR.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if(this.level != null && this.level.isClientSide) {
            this.prevFanSpin = this.fanSpin;
            this.prevPiston = this.piston;

            if(this.isOn) {
                this.fanSpin += 15F;

                if(this.fanSpin >= 360F) {
                    this.prevFanSpin -= 360F;
                    this.fanSpin -= 360F;
                }

                if(this.pistonDir) {
                    this.piston -= this.randSpeed;
                    if(this.piston <= 0F) {
                        this.pistonDir = false;
                    }
                } else {
                    this.piston += 0.05F;
                    if(this.piston >= 1F) {
                        this.randSpeed = 0.085F + this.level.random.nextFloat() * 0.03F;
                        this.pistonDir = true;
                    }
                }

                this.piston = Mth.clamp(this.piston, 0F, 1F);
            }
        }
    }

    @Override
    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getCounterClockWise(Direction.Axis.Y);

        return new DirPos[] {
                new DirPos(pos.getX() + rot.getStepX() * 2, pos.getY(), pos.getZ() + rot.getStepZ() * 2, rot),
                new DirPos(pos.getX() - rot.getStepX() * 2, pos.getY(), pos.getZ() - rot.getStepZ() * 2, rot.getOpposite()),
                new DirPos(pos.getX() - dir.getStepX() * 2, pos.getY(), pos.getZ() - dir.getStepZ() * 2, dir.getOpposite())
        };
    }
}
