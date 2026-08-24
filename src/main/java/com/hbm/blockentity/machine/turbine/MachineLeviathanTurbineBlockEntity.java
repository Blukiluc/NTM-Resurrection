package com.hbm.blockentity.machine.turbine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;

public class MachineLeviathanTurbineBlockEntity extends AbstractTurbineBlockEntity {

    public static final int INPUT_CAPACITY = 1_000_000_000;
    public static final int OUTPUT_CAPACITY = 1_000_000_000;

    public int turnTimer;
    public float rotor;
    public float lastRotor;
    public float fanAcceleration;

    private final float audioDesync = (float)Math.random() * 0.05F;
    private AudioWrapper audio;

    public MachineLeviathanTurbineBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_CHUNGUS.get(), pos, state, INPUT_CAPACITY, OUTPUT_CAPACITY);
    }

    @Override
    protected double getEfficiency() {
        return 0.85D;
    }

    @Override
    protected double getConsumptionPercent() {
        return 1D;
    }

    @Override
    protected int getNetworkRange() {
        return 200;
    }

    @Override
    protected void onServerTick() {
        if(this.turnTimer > 0) this.turnTimer--;
        if(this.operational) this.turnTimer = 25;
    }

    @Override
    protected void onClientTick() {
        this.lastRotor = this.rotor;
        this.rotor += this.fanAcceleration;

        if(this.rotor >= 360F) {
            this.rotor -= 360F;
            this.lastRotor -= 360F;
        }

        if(this.turnTimer > 0) {
            this.fanAcceleration = Math.max(0F, Math.min(25F, this.fanAcceleration + 0.075F + this.audioDesync));
            this.spawnSteamParticles();

            if(this.audio == null) {
                this.audio = AudioWrapper.getLoopedSound(NtmSoundEvents.TURBINE_LEVIATHAN_LOOP.get(), SoundSource.BLOCKS, this, 1F, 20F, 1F, 20);
                this.audio.startSound();
            } else if(!this.audio.isPlaying()) {
                this.audio = this.rebootAudio(this.audio);
            }

            float speed = this.fanAcceleration / 25F;
            this.audio.updateVolume(this.getVolume(0.5F * speed));
            this.audio.updatePitch(0.25F + 0.75F * speed);
            this.audio.keepAlive();
        } else {
            this.fanAcceleration = Math.max(0F, this.fanAcceleration - 0.1F);

            if(this.audio != null) {
                if(this.fanAcceleration > 0F) {
                    float speed = this.fanAcceleration / 25F;
                    this.audio.updateVolume(this.getVolume(0.5F * speed));
                    this.audio.updatePitch(0.25F + 0.75F * speed);
                    this.audio.keepAlive();
                } else {
                    this.stopAudio();
                }
            }
        }
    }

    private void spawnSteamParticles() {
        if(this.level == null) return;

        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();

        for(int i = 0; i < 10; i++) {
            double sideOffset = this.level.random.nextGaussian() * 0.65D;
            this.level.addParticle(
                    ParticleTypes.CLOUD,
                    this.getBlockPos().getX() + 0.5D + facing.getStepX() * (this.level.random.nextDouble() + 1.25D) + side.getStepX() * sideOffset,
                    this.getBlockPos().getY() + 2.5D + this.level.random.nextGaussian() * 0.65D,
                    this.getBlockPos().getZ() + 0.5D + facing.getStepZ() * (this.level.random.nextDouble() + 1.25D) + side.getStepZ() * sideOffset,
                    -facing.getStepX() * 0.08D,
                    0D,
                    -facing.getStepZ() * 0.08D
            );
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.TURBINE_LEVIATHAN_LOOP.get(), SoundSource.BLOCKS, this, 0.5F, 20F, 1F, 20);
    }

    @Override
    protected DirPos[] getConnectionPositions() {
        BlockPos pos = this.getBlockPos();
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();

        return new DirPos[] {
                new DirPos(pos.relative(facing, 5).above(2), facing),
                new DirPos(pos.relative(side, 3), side),
                new DirPos(pos.relative(side.getOpposite(), 3), side.getOpposite())
        };
    }

    @Override
    protected DirPos[] getPowerPositions() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return new DirPos[] { new DirPos(this.getBlockPos().relative(facing.getOpposite(), 11), facing.getOpposite()) };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.turnTimer);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.turnTimer = buf.readInt();
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
}
