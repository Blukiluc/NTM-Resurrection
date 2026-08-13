package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MachineHephaestusBlockEntity extends LoadedBaseBlockEntity implements ITickable, IFluidStandardTransceiverMK2, IFluidCopiable {

    public static final int TANK_CAPACITY = 24_000;
    private static final int SCAN_DEPTH = 10;
    private static final int SCAN_RANGE = 7;
    private static final ResourceLocation VOLCANO_ORE_ID = NuclearTechMod.withDefaultNamespace("ore_volcano");

    public final FluidTank input = new FluidTank(Fluids.OIL, TANK_CAPACITY);
    public final FluidTank output = new FluidTank(Fluids.HOTOIL, TANK_CAPACITY);
    public int bufferedHeat;
    public float rot;
    public float prevRot;

    private final int[] heat = new int[SCAN_DEPTH];
    private long fissureScanTime = -20L;
    private Block volcanoOre;
    private boolean volcanoOreResolved;
    private AudioWrapper audio;

    public MachineHephaestusBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_HEPHAESTUS.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.updateClient();
            return;
        }

        this.setupTanks();

        if(this.level.getGameTime() % 20L == 0L) {
            this.updateConnections();
        }

        this.scanHeatLayer();
        this.heatFluid();

        if(this.output.getFill() > 0) {
            for(DirPos pos : this.getConnectionPositions()) {
                this.tryProvide(this.output, this.level, pos);
            }
        }

        this.bufferedHeat = this.getTotalHeat();
        this.setChanged();
        this.networkPackNT(150);
    }

    private void updateClient() {
        this.prevRot = this.rot;

        if(this.bufferedHeat > 0) {
            this.rot += 0.5F;
            if(this.rot >= 360F) {
                this.prevRot -= 360F;
                this.rot -= 360F;
            }

            if(this.level.random.nextInt(7) == 0) {
                double x = this.level.random.nextGaussian() * 2D;
                double y = this.level.random.nextGaussian() * 3D;
                double z = this.level.random.nextGaussian() * 2D;
                this.level.addParticle(
                        ParticleTypes.CLOUD,
                        this.worldPosition.getX() + 0.5D + x,
                        this.worldPosition.getY() + 6D + y,
                        this.worldPosition.getZ() + 0.5D + z,
                        0D,
                        0D,
                        0D
                );
            }

            if(this.audio == null) {
                this.audio = this.createAudioLoop();
                this.audio.startSound();
            } else if(!this.audio.isPlaying()) {
                this.audio = this.rebootAudio(this.audio);
            }

            this.audio.keepAlive();
            this.audio.updateVolume(this.getVolume(0.75F));
        } else {
            this.stopAudio();
        }
    }

    private void setupTanks() {
        FluidType type = this.input.getTankType();
        FT_Heatable trait = type.getTrait(FT_Heatable.class);

        if(trait != null && trait.hasSteps() && trait.getEfficiency(FT_Heatable.HeatingType.HEATEXCHANGER) > 0D) {
            this.output.setTankType(trait.getFirstStep().typeProduced);
            return;
        }

        this.input.setTankType(Fluids.NONE);
        this.output.setTankType(Fluids.NONE);
    }

    private void updateConnections() {
        if(this.input.getTankType() == Fluids.NONE) return;

        for(DirPos pos : this.getConnectionPositions()) {
            this.trySubscribe(this.input.getTankType(), this.level, pos);
        }
    }

    private void scanHeatLayer() {
        if(!this.volcanoOreResolved) {
            this.volcanoOre = BuiltInRegistries.BLOCK.getOptional(VOLCANO_ORE_ID).orElse(null);
            this.volcanoOreResolved = true;
        }

        int height = (int)(this.level.getGameTime() % SCAN_DEPTH);
        int y = this.worldPosition.getY() - 1 - height;
        this.heat[height] = 0;

        if(y < this.level.getMinBuildHeight()) return;

        BlockPos.MutableBlockPos scanPos = new BlockPos.MutableBlockPos();
        for(int x = -SCAN_RANGE; x <= SCAN_RANGE; x++) {
            for(int z = -SCAN_RANGE; z <= SCAN_RANGE; z++) {
                scanPos.set(this.worldPosition.getX() + x, y, this.worldPosition.getZ() + z);
                this.heat[height] += this.heatFromBlock(scanPos);
            }
        }
    }

    private int heatFromBlock(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);

        if(state.is(Blocks.LAVA)) return 5;
        if(state.is(NtmBlocks.VOLCANIC_LAVA.get())) return 150;

        if(this.volcanoOre != null && state.is(this.volcanoOre)) {
            this.fissureScanTime = this.level.getGameTime();
            return 300;
        }

        return 0;
    }

    private boolean heatFluid() {
        FT_Heatable trait = this.input.getTankType().getTrait(FT_Heatable.class);
        if(trait == null || !trait.hasSteps()) return false;

        FT_Heatable.HeatingStep step = trait.getFirstStep();
        int inputOps = this.input.getFill() / step.amountReq;
        int outputOps = (this.output.getMaxFill() - this.output.getFill()) / step.amountProduced;
        int heatOps = this.getTotalHeat() / step.heatReq;
        int ops = Math.min(inputOps, Math.min(outputOps, heatOps));
        if(ops <= 0) return false;

        this.input.setFill(this.input.getFill() - step.amountReq * ops);
        this.output.setFill(this.output.getFill() + step.amountProduced * ops);
        return true;
    }

    public int getTotalHeat() {
        int total = 0;
        for(int layerHeat : this.heat) total += layerHeat;

        if(this.level != null && this.level.getGameTime() - this.fissureScanTime < 20L) {
            total *= 3;
        }

        return total;
    }

    private DirPos[] getConnectionPositions() {
        BlockPos pos = this.getBlockPos();

        return new DirPos[] {
                new DirPos(pos.relative(Direction.EAST, 2), Direction.EAST),
                new DirPos(pos.relative(Direction.WEST, 2), Direction.WEST),
                new DirPos(pos.relative(Direction.SOUTH, 2), Direction.SOUTH),
                new DirPos(pos.relative(Direction.NORTH, 2), Direction.NORTH),
                new DirPos(pos.above(11).relative(Direction.EAST, 2), Direction.EAST),
                new DirPos(pos.above(11).relative(Direction.WEST, 2), Direction.WEST),
                new DirPos(pos.above(11).relative(Direction.SOUTH, 2), Direction.SOUTH),
                new DirPos(pos.above(11).relative(Direction.NORTH, 2), Direction.NORTH)
        };
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return direction != null && direction.getAxis().isHorizontal() &&
                (type == this.input.getTankType() || type == this.output.getTankType());
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] {this.output};
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] {this.input};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] {this.input, this.output};
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.input.readFromNBT(tag, "input");
        this.output.readFromNBT(tag, "output");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.input.writeToNBT(tag, "input");
        this.output.writeToNBT(tag, "output");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.input.serialize(buf);
        this.output.serialize(buf);
        buf.writeInt(this.bufferedHeat);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.input.deserialize(buf);
        this.output.deserialize(buf);
        this.bufferedHeat = buf.readInt();
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(
                NtmSoundEvents.HEPHAESTUS_LOOP.get(),
                SoundSource.BLOCKS,
                this.worldPosition.getX(),
                this.worldPosition.getY() + 5F,
                this.worldPosition.getZ(),
                0.75F,
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
}
