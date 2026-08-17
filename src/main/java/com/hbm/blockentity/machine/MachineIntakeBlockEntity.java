package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardSenderMK2;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

public class MachineIntakeBlockEntity extends LoadedBaseBlockEntity implements ITickable, IEnergyReceiverMK2, IFluidStandardSenderMK2 {

    public final FluidTank compair = new FluidTank(Fluids.AIR, 1_000);
    public long power;
    public float fan;
    public float prevFan;

    private AudioWrapper audio;

    public MachineIntakeBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.INTAKE.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(this.power >= this.getMaxPower() / 20L) {
                this.compair.setFill(this.compair.getMaxFill());
                this.power -= this.getMaxPower() / 20L;
            }

            for(DirPos pos : this.getConPos()) {
                if(this.compair.getFill() > 0) this.tryProvide(this.compair, this.level, pos);
                this.trySubscribe(this.level, pos);
            }

            this.setChanged();
            this.networkPackNT(50);

        } else {

            this.prevFan = this.fan;

            if(this.power >= this.getMaxPower() / 20L) {
                this.fan += 45F;

                if(this.fan >= 360F) {
                    this.fan -= 360F;
                    this.prevFan -= 360F;
                }

                if(this.audio == null) {
                    this.audio = this.createAudioLoop();
                    this.audio.startSound();
                } else if(!this.audio.isPlaying()) {
                    this.audio = this.rebootAudio(this.audio);
                }

                this.audio.keepAlive();
                this.audio.updateVolume(this.getVolume(0.25F));

            } else {
                this.stopAudio();
            }
        }
    }

    // Same shape as the 1.7.10 version: dir is the facing direction, rot is the direction
    // rotated 90° around the vertical axis (used to sweep across the intake's width).
    public DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();
        BlockPos pos = this.getBlockPos();

        return new DirPos[] {
                new DirPos(pos.relative(dir), dir),
                new DirPos(pos.relative(dir).relative(rot), dir),

                new DirPos(pos.relative(dir, -2), dir.getOpposite()),
                new DirPos(pos.relative(dir, -2).relative(rot), dir.getOpposite()),

                new DirPos(pos.relative(rot, 2), rot),
                new DirPos(pos.relative(rot, 2).relative(dir, -1), rot),

                new DirPos(pos.relative(rot, -1), rot.getOpposite()),
                new DirPos(pos.relative(rot, -1).relative(dir, -1), rot.getOpposite())
        };
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(
                NtmSoundEvents.ELECTRIC_MOTOR_LOOP.get(),
                SoundSource.BLOCKS,
                this.worldPosition.getX(),
                this.worldPosition.getY(),
                this.worldPosition.getZ(),
                0.25F,
                10F,
                1F,
                20
        );
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.stopAudio();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.stopAudio();
    }

    private void stopAudio() {
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        this.compair.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.compair.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.compair.readFromNBT(tag, "compair");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        this.compair.writeToNBT(tag, "compair");
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return type == Fluids.AIR && direction != null && direction.getAxis().isHorizontal();
    }

    @Override
    public void setPower(long power) { this.power = power; }
    @Override
    public long getPower() { return this.power; }
    @Override
    public long getMaxPower() { return 2_000L; }

    @Override
    public FluidTank[] getAllTanks() { return new FluidTank[] {this.compair}; }
    @Override
    public FluidTank[] getSendingTanks() { return new FluidTank[] {this.compair}; }
}