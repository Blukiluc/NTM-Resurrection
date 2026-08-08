package com.hbm.blockentity.machine.turbine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.trait.FT_Coolable;
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

public class MachineIndustrialTurbineBlockEntity extends AbstractTurbineBlockEntity {

    public static final int INPUT_CAPACITY = 750_000;
    public static final int OUTPUT_CAPACITY = 3_000_000;
    public static final double FLYWHEEL_MAX_ENERGY = 50_000_000D;

    public float rotor;
    public float lastRotor;
    public double spin;
    public long maxPower;
    public long lastPowerTarget;
    public long flywheelEnergy;

    private final float audioDesync = (float)Math.random() * 0.05F;
    private AudioWrapper audio;

    public MachineIndustrialTurbineBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_INDUSTRIAL_TURBINE.get(), pos, state, INPUT_CAPACITY, OUTPUT_CAPACITY);
    }

    @Override
    protected double getEfficiency() {
        return 1D;
    }

    @Override
    protected double getConsumptionPercent() {
        return 0.2D;
    }

    @Override
    protected boolean resizesForSteamType() {
        return true;
    }

    @Override
    protected void generatePower(long power, int steamConsumed) {
        FT_Coolable trait = this.tanks[0].getTankType().getTrait(FT_Coolable.class);
        double efficiency = trait.getEfficiency(FT_Coolable.CoolingType.TURBINE) * this.getEfficiency();
        int maxOperations = (int)Math.ceil(this.tanks[0].getMaxFill() * this.getConsumptionPercent() / trait.amountReq);
        this.maxPower = (long)(maxOperations * (double)trait.heatEnergy * efficiency);
        this.flywheelEnergy += power;
    }

    @Override
    protected void onServerTick() {
        this.spin = this.flywheelEnergy / FLYWHEEL_MAX_ENERGY;
        this.lastPowerTarget = Math.min((long)(Math.max(this.spin, 0.05D) * this.maxPower), this.flywheelEnergy);
        this.flywheelEnergy -= this.lastPowerTarget;
        this.powerBuffer = this.lastPowerTarget;
    }

    @Override
    protected void onClientTick() {
        this.lastRotor = this.rotor;
        float speed = this.spin >= 0.5D ? 30F : (float)(Math.sqrt(this.spin * 2D) * 30D);
        this.rotor += speed;

        if(this.rotor >= 360F) {
            this.lastRotor -= 360F;
            this.rotor -= 360F;
        }

        if(this.spin > 0D) {
            float spinLevel = (float)Math.min(1D, this.spin * 2D);
            float volume = this.getVolume(0.25F + spinLevel * 0.75F);
            float pitch = 0.5F + spinLevel * 0.5F + this.audioDesync;

            if(this.audio == null) {
                this.audio = AudioWrapper.getLoopedSound(NtmSoundEvents.TURBINE_INDUSTRIAL_LOOP.get(), SoundSource.BLOCKS, this, volume, 20F, pitch, 20);
                this.audio.startSound();
            } else if(!this.audio.isPlaying()) {
                this.audio = this.rebootAudio(this.audio);
            }

            this.audio.updatePitch(pitch);
            this.audio.updateVolume(volume);
            this.audio.keepAlive();
        } else {
            this.stopAudio();
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.TURBINE_INDUSTRIAL_LOOP.get(), SoundSource.BLOCKS, this, 0.25F, 20F, 0.5F + this.audioDesync, 20);
    }

    @Override
    protected DirPos[] getConnectionPositions() {
        BlockPos pos = this.getBlockPos();
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();

        return new DirPos[] {
                new DirPos(pos.relative(facing, 3).relative(side, 2), side),
                new DirPos(pos.relative(facing, 3).relative(side.getOpposite(), 2), side.getOpposite()),
                new DirPos(pos.relative(facing.getOpposite()).relative(side, 2), side),
                new DirPos(pos.relative(facing.getOpposite()).relative(side.getOpposite(), 2), side.getOpposite()),
                new DirPos(pos.relative(facing, 3).above(3), Direction.UP),
                new DirPos(pos.relative(facing.getOpposite()).above(3), Direction.UP)
        };
    }

    @Override
    protected DirPos[] getPowerPositions() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return new DirPos[] { new DirPos(this.getBlockPos().relative(facing.getOpposite(), 4).above(), facing.getOpposite()) };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.lastPowerTarget = tag.getLong("lastPowerTarget");
        this.flywheelEnergy = tag.getLong("flywheelEnergy");
        this.maxPower = tag.getLong("maxPower");
        this.spin = tag.getDouble("spin");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("lastPowerTarget", this.lastPowerTarget);
        tag.putLong("flywheelEnergy", this.flywheelEnergy);
        tag.putLong("maxPower", this.maxPower);
        tag.putDouble("spin", this.spin);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeDouble(this.spin);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.spin = buf.readDouble();
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
