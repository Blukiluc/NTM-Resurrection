package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.MachineSILEXBlock;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.menus.MachineFELMenu;
import com.hbm.items.machine.ItemFELCrystal;
import com.hbm.items.machine.ItemFELCrystal.EnumWavelengths;
import com.hbm.lib.Library;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MachineFELBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IControlReceiver {

    public static final long MAX_POWER = 20_000_000L;
    public static final int BASE_POWER_REQUIREMENT = 1_250;
    public static final int RANGE = 24;

    public long power;
    public EnumWavelengths mode = EnumWavelengths.NULL;
    public boolean isOn;
    public boolean missingValidSilex = true;
    public int distance;

    private int audioDuration;
    private AudioWrapper audio;

    public MachineFELBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.FEL.get(), pos, blockState, 2);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machineFEL");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            Direction direction = this.getBlockState().getValue(DummyableBlock.FACING);
            BlockPos connection = this.worldPosition.relative(direction, -5).above();
            this.trySubscribe(this.level, new DirPos(connection, direction.getOpposite()));

            this.power = Library.chargeTEFromItems(this.slots, 0, this.power, MAX_POWER);
            this.mode = this.isOn && this.slots.get(1).getItem() instanceof ItemFELCrystal crystal
                    ? crystal.wavelength
                    : EnumWavelengths.NULL;

            this.missingValidSilex = true;
            this.distance = 0;

            int requirement = getPowerRequirement(this.mode);
            if(this.isOn && this.mode != EnumWavelengths.NULL && this.power < requirement) {
                this.power = 0;
            }

            if(this.isOn && this.mode != EnumWavelengths.NULL && this.power >= requirement) {
                this.distance = this.scanLaser(direction);
                this.affectEntities(direction);
                this.power -= requirement;
            }

            this.setChanged();
            this.networkPackNT(250);
        } else {
            if(this.isBeamActive()) {
                this.audioDuration += 2;
            } else {
                this.audioDuration -= 3;
            }

            this.audioDuration = Mth.clamp(this.audioDuration, 0, 60);

            if(this.audioDuration > 10) {
                if(this.audio == null) {
                    this.audio = this.createAudioLoop();
                    this.audio.startSound();
                } else if(!this.audio.isPlaying()) {
                    this.audio = this.rebootAudio(this.audio);
                }

                this.audio.updateVolume(this.getVolume(2F));
                this.audio.updatePitch((this.audioDuration - 10) / 100F + 0.5F);
                this.audio.keepAlive();
            } else {
                this.stopAudio();
            }
        }
    }

    private int scanLaser(Direction direction) {
        int distance = RANGE;
        boolean silexSpacing = false;
        Set<BlockPos> processedSilex = new HashSet<>();

        for(int i = 3; i < RANGE; i++) {
            BlockPos beamPos = this.worldPosition.relative(direction, i).above();
            BlockState state = this.level.getBlockState(beamPos);

            if(state.is(NtmBlocks.MACHINE_SILEX.get())) {
                MachineSILEXBlock block = (MachineSILEXBlock) state.getBlock();
                BlockPos corePos = block.findCore(this.level, beamPos);

                if(corePos != null && processedSilex.add(corePos)) {
                    BlockEntity blockEntity = this.level.getBlockEntity(corePos);
                    if(blockEntity instanceof MachineSILEXBlockEntity silex) {
                        Direction silexDirection = silex.getBlockState().getValue(DummyableBlock.FACING);
                        if(i >= 5 && !silexSpacing && this.rotationIsValid(silexDirection, direction)) {
                            silex.mode = this.mode;
                            this.missingValidSilex = false;
                            silexSpacing = true;
                        } else {
                            this.level.destroyBlock(corePos, true);
                        }
                    }
                }

                continue;
            }

            if(!state.getFluidState().isEmpty()) {
                distance = i;
                this.level.playSound(null, beamPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1F, 1F);
                this.level.removeBlock(beamPos, false);
                break;
            }

            if(state.isAir() || state.canBeReplaced()) {
                silexSpacing = false;
                continue;
            }

            distance = i;
            float hardness = state.getBlock().getExplosionResistance();
            if(hardness < 75F && this.level.getRandom().nextInt(5) == 0) {
                this.level.playSound(null, beamPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1F, 1F);
                BlockState fire = this.mode == EnumWavelengths.DRX
                        ? NtmBlocks.FIRE_DIGAMMA.get().defaultBlockState()
                        : Blocks.FIRE.defaultBlockState();
                this.level.setBlockAndUpdate(beamPos, fire);
            }
            break;
        }

        return distance;
    }

    private void affectEntities(Direction direction) {
        if(this.distance <= 3) return;

        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        int beamDistance = this.distance - 1;

        double minX = Math.min(x, x + direction.getStepX() * beamDistance) + 0.2D;
        double maxX = Math.max(x, x + direction.getStepX() * beamDistance) + 0.8D;
        double minZ = Math.min(z, z + direction.getStepZ() * beamDistance) + 0.2D;
        double maxZ = Math.max(z, z + direction.getStepZ() * beamDistance) + 0.8D;

        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class,
                new AABB(minX, y + 0.2D, minZ, maxX, y + 1.8D, maxZ));

        for(LivingEntity entity : entities) {
            switch(this.mode) {
                case VISIBLE -> {
                    entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60 * 60 * 65_536, 0));
                    entity.igniteForSeconds(10);
                }
                case IR, UV -> entity.igniteForSeconds(10);
                case GAMMA -> ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 25);
                case DRX -> ContaminationUtil.applyDigammaData(entity, 0.1F);
            }
        }
    }

    public boolean rotationIsValid(Direction silexDirection, Direction felDirection) {
        return silexDirection == felDirection || silexDirection == felDirection.getOpposite();
    }

    public static int getPowerRequirement(EnumWavelengths wavelength) {
        return wavelength == EnumWavelengths.NULL
                ? 0
                : (int) (BASE_POWER_REQUIREMENT * Math.pow(3, wavelength.ordinal()));
    }

    public boolean isBeamActive() {
        return this.isOn
                && this.mode != EnumWavelengths.NULL
                && this.distance > 3;
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.FEL_LOOP.get(), SoundSource.BLOCKS, this, 2F, 10F, 2F, 20);
    }

    private void stopAudio() {
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
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

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeEnum(this.mode);
        buf.writeBoolean(this.isOn);
        buf.writeBoolean(this.missingValidSilex);
        buf.writeInt(this.distance);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.mode = buf.readEnum(EnumWavelengths.class);
        this.isOn = buf.readBoolean();
        this.missingValidSilex = buf.readBoolean();
        this.distance = buf.readInt();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        try {
            this.mode = EnumWavelengths.valueOf(tag.getString("mode"));
        } catch(IllegalArgumentException ignored) {
            this.mode = EnumWavelengths.NULL;
        }
        this.isOn = tag.getBoolean("isOn");
        this.missingValidSilex = tag.getBoolean("valid");
        this.distance = tag.getInt("distance");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putString("mode", this.mode.name());
        tag.putBoolean("isOn", this.isOn);
        tag.putBoolean("valid", this.missingValidSilex);
        tag.putInt("distance", this.distance);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return stack.getItem() instanceof IBatteryItem;
        if(slot == 1) return stack.getItem() instanceof ItemFELCrystal;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {0, 1};
    }

    @Override public long getPower() { return this.power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return MAX_POWER; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineFELMenu(id, inventory, this);
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("toggle")) {
            this.isOn = !this.isOn;
            this.setChanged();
        }
    }
}
