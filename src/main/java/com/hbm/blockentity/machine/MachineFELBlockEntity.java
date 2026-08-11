package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: this block entity assumes MachineFEL extends DummyableBlock and stores its facing
 * direction via DummyableBlock.FACING, same as the multiblock system used elsewhere (e.g. Refinery).
 * TODO: NtmBlockEntityTypes.FEL is not registered yet - add the registry entry once MachineFEL block exists.
 * TODO: SILEX detection/linking is stubbed out entirely - MachineSILEX/SilexBlockEntity don't exist
 *       in the 1.21.1 port yet. Re-enable once they are ported.
 * TODO: ItemFELCrystal doesn't exist yet - EnumWavelengths below is a temporary local copy just to keep
 *       the laser logic working. Replace with ItemFELCrystal.EnumWavelengths once that item is ported,
 *       and make sure enum ORDER stays identical (used for power-cost scaling via ordinal()).
 * TODO: ContaminationUtil calls kept with the 1.7.10 signature - verify against the ported ContaminationUtil.java.
 * TODO: createMenu() has no menu class to return to yet (no MachineFELMenu) - returns null for now.
 */
public class MachineFELBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2 {

    // TODO: temporary stand-in for ItemFELCrystal.EnumWavelengths
    public enum EnumWavelengths {
        NULL, VISIBLE, IR, UV, GAMMA, DRX
    }

    public long power;
    public static final long maxPower = 20_000_000;
    public static final int powerReq = 1250;

    public EnumWavelengths mode = EnumWavelengths.NULL;
    public boolean isOn;
    public boolean missingValidSilex = true;
    public int distance;

    public List<LivingEntity> entities = new ArrayList<>();

    private int audioDuration = 0;
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

        if(!level.isClientSide) {

            Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);

            int x = this.worldPosition.getX();
            int y = this.worldPosition.getY();
            int z = this.worldPosition.getZ();

            this.trySubscribe(level, new DirPos(
                    x + dir.getStepX() * -5,
                    y + 1,
                    z + dir.getStepZ() * -5,
                    dir.getOpposite()
            ));

            this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);

            // TODO: was previously reading the wavelength from ItemFELCrystal in slot 1.
            // Kept the same slot check, just without the real item class for now.
            if(this.isOn && !slots.get(1).isEmpty()) {
                // TODO: replace with real crystal item lookup once ItemFELCrystal is ported
                this.mode = this.mode; // placeholder - mode must be set externally/by GUI for now
            } else {
                this.mode = EnumWavelengths.NULL;
            }

            int range = 24;
            boolean silexSpacing = false;

            int req = (int) (powerReq * ((mode.ordinal() == 0) ? 0 : Math.pow(3, mode.ordinal())));

            if(this.isOn && this.mode != EnumWavelengths.NULL && power < req) {
                this.power = 0;
            }

            if(this.isOn && power >= req && this.mode != EnumWavelengths.NULL) {

                int dist = this.distance - 1;
                double blx = Math.min(x, x + dir.getStepX() * dist) + 0.2;
                double bux = Math.max(x, x + dir.getStepX() * dist) + 0.8;
                double bly = Math.min(y, 1 + y + 0 * dist) + 0.2; // vertical beam offset unused in original (offsetY always 0 here)
                double buy = Math.max(y, 1 + y) + 0.8;
                double blz = Math.min(z, z + dir.getStepZ() * dist) + 0.2;
                double buz = Math.max(z, z + dir.getStepZ() * dist) + 0.8;

                List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(blx, bly, blz, bux, buy, buz));

                for(LivingEntity entity : list) {
                    switch(this.mode) {
                        case VISIBLE:
                            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60 * 60 * 65536, 0));
                            // fall through, matches original switch fallthrough behavior
                        case IR:
                        case UV:
                            entity.igniteForSeconds(10);
                            break;
                        case GAMMA:
                            // TODO: verify signature against ported ContaminationUtil
                            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 25);
                            break;
                        case DRX:
                            // TODO: verify signature against ported ContaminationUtil
                            ContaminationUtil.applyDigammaData(entity, 0.1F);
                            break;
                        default:
                            break;
                    }
                }

                power -= req;

                for(int i = 3; i < range; i++) {

                    int bx = x + dir.getStepX() * i;
                    int by = y + 1;
                    int bz = z + dir.getStepZ() * i;
                    BlockPos beamPos = new BlockPos(bx, by, bz);

                    BlockState state = level.getBlockState(beamPos);

                    // TODO: 1.7.10 used Material#isOpaque()/isLiquid() - canOcclude()/getFluidState()
                    // are the closest modern equivalents but may need tuning.
                    boolean isOpaque = state.canOcclude();
                    boolean isLiquid = !level.getFluidState(beamPos).isEmpty();

                    if(!isOpaque && !state.is(Blocks.TNT)) {
                        this.distance = range;
                        silexSpacing = false;
                        continue;
                    }

                    // TODO: SILEX linking disabled - MachineSILEX/SilexBlockEntity not ported yet.
                    // Original code checked `state.getBlock() == ModBlocks.machine_silex` here and
                    // synchronized `mode` between this FEL and the found SILEX block entity.

                    if(isOpaque || state.is(Blocks.TNT)) {

                        this.distance = i;

                        if(isLiquid) {
                            level.playSound(null, bx + 0.5, by + 0.5, bz + 0.5, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                            level.removeBlock(beamPos, false);
                            break;
                        }

                        // TODO: getExplosionResistance signature differs in 1.21.1 - approximate with default resistance for now.
                        float hardness = state.getBlock().getExplosionResistance();
                        if(hardness < 75 && level.getRandom().nextInt(5) == 0) {
                            level.playSound(null, bx + 0.5, by + 0.5, bz + 0.5, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);

                            // TODO: original had an easter-egg swap to ModBlocks.digamma_matter based on
                            // MainRegistry.polaroidID - not ported, always uses fire/fire_digamma for now.
                            BlockState fireBlock = (this.mode != EnumWavelengths.DRX)
                                    ? Blocks.FIRE.defaultBlockState()
                                    : NtmBlocks.FIRE_DIGAMMA.get().defaultBlockState();

                            level.setBlockAndUpdate(beamPos, fireBlock);

                            if(this.mode == EnumWavelengths.DRX) {
//                                level.setBlockAndUpdate(beamPos.below(), NtmBlocks.ASH_DIGAMMA.get().defaultBlockState());
                                level.setBlockAndUpdate(beamPos.below(), NtmBlocks.FIRE_DIGAMMA.get().defaultBlockState());
                            }
                        }
                        break;
                    }
                }
            }

            this.networkPackNT(250);

        } else {

            if(power > powerReq * Math.pow(2, mode.ordinal()) && isOn && mode != EnumWavelengths.NULL && distance - 3 > 0) {
                audioDuration += 2;
            } else {
                audioDuration -= 3;
            }

            audioDuration = Mth.clamp(audioDuration, 0, 60);

            if(audioDuration > 10) {

                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }

                audio.updateVolume(getVolume(2F));
                audio.updatePitch((audioDuration - 10) / 100F + 0.5F);

            } else {

                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }
            }
        }
    }

//    @Override
//    public AudioWrapper createAudioLoop() {
//        // TODO: NtmSoundEvents.FEL_LOOP is not registered yet - add it alongside the other machine loop sounds.
//        return AudioWrapper.getLoopedSound(NtmSoundEvents.FEL_LOOP.get(), SoundSource.BLOCKS, this, 2.0F, 10F, 2.0F, 20);
//    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(power);
        buf.writeEnum(mode);
        buf.writeBoolean(isOn);
        buf.writeBoolean(missingValidSilex);
        buf.writeInt(distance);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        power = buf.readLong();
        mode = buf.readEnum(EnumWavelengths.class);
        isOn = buf.readBoolean();
        missingValidSilex = buf.readBoolean();
        distance = buf.readInt();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        power = tag.getLong("power");
        mode = EnumWavelengths.valueOf(tag.getString("mode"));
        isOn = tag.getBoolean("isOn");
        missingValidSilex = tag.getBoolean("valid");
        distance = tag.getInt("distance");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", power);
        tag.putString("mode", mode.name());
        tag.putBoolean("isOn", isOn);
        tag.putBoolean("valid", missingValidSilex);
        tag.putInt("distance", distance);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true; // battery
        // TODO: slot 1 should only accept ItemFELCrystal once it's ported
        if(slot == 1) return true;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {0, 1};
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        // TODO: no MachineFELMenu ported yet - GUI/container out of scope for this pass.
        return null;
    }
}