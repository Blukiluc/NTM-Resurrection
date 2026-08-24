package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineOreAcidizerMenu;
import com.hbm.inventory.recipes.OreAcidizerRecipes;
import com.hbm.inventory.recipes.OreAcidizerRecipes.OreAcidizerRecipe;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.lib.ModAttachments;
import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.BobMathUtil;
import com.hbm.util.SoundUtils;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.LinkedHashMap;
import java.util.List;

public class MachineOreAcidizerBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardReceiverMK2, IUpgradeInfoProvider, IFluidCopiable {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_BATTERY = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_FLUID_INPUT = 3;
    public static final int SLOT_FLUID_OUTPUT = 4;
    public static final int SLOT_UPGRADE_1 = 5;
    public static final int SLOT_UPGRADE_2 = 6;
    public static final int SLOT_FLUID_ID = 7;

    public static final long MAX_POWER = 1_000_000L;
    public static final int BASE_DEMAND = 1_000;

    public long power;
    public int progress;
    public int duration = 600;
    public boolean isOn;

    public float angle;
    public float prevAngle;
    private AudioWrapper audio;

    public final FluidTank tank = new FluidTank(Fluids.PEROXIDE, 8_000);
    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachineOreAcidizerBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.ORE_ACIDIZER.get(), pos, blockState, 8);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.ore_acidizer");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            this.isOn = false;

            for(DirPos pos : this.getConPos()) {
                this.trySubscribe(this.level, pos);
                this.trySubscribe(this.tank.getTankType(), this.level, pos);
            }

            this.power = Library.chargeTEFromItems(this.slots, SLOT_BATTERY, this.power, MAX_POWER);
            this.tank.setType(SLOT_FLUID_ID, this.slots);
            this.tank.loadTank(this.level, SLOT_FLUID_INPUT, SLOT_FLUID_OUTPUT, this.slots);
            this.upgradeManager.checkSlots(this.slots, SLOT_UPGRADE_1, SLOT_UPGRADE_2);
            this.duration = this.getDuration();

            for(int i = 0; i < this.getCycleCount(); i++) {
                if(this.canProcess()) {
                    this.progress++;
                    this.power -= this.getPowerRequired();
                    this.isOn = true;

                    if(this.progress > this.duration) {
                        this.progress = 0;
                        this.processItem();
                    }
                } else {
                    this.progress = 0;
                }
            }

            this.setChanged();
            this.networkPackNT(25);
        } else {
            this.prevAngle = this.angle;

            if(this.isOn) {
                this.angle += 5F * this.getCycleCount();

                if(this.angle >= 360F) {
                    this.angle -= 360F;
                    this.prevAngle -= 360F;
                }

                if(this.level.random.nextInt(20) == 0 && NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getCenter().add(0, 6, 0)) < 2_500) {
                    this.level.addParticle(ParticleTypes.CLOUD, this.worldPosition.getX() + this.level.random.nextDouble(), this.worldPosition.getY() + 6.5D, this.worldPosition.getZ() + this.level.random.nextDouble(), 0.0D, 0.1D, 0.0D);
                }

                if(NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getCenter()) < 625) {
                    if(this.audio == null) {
                        this.audio = this.createAudioLoop();
                        this.audio.startSound();
                    } else if(!this.audio.isPlaying()) {
                        this.audio = this.rebootAudio(this.audio);
                    }
                    this.audio.keepAlive();
                    this.audio.updateVolume(this.getVolume(1F));
                    this.audio.updatePitch(0.75F);
                } else {
                    this.stopAudio();
                }
            } else {
                this.stopAudio();
            }
        }

        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise(Axis.Y);
        BlockPos pos = this.getBlockPos();
        AABB ladder = new AABB(pos.getX() + 0.25D, pos.getY() + 1D, pos.getZ() + 0.25D, pos.getX() + 0.75D, pos.getY() + 6D, pos.getZ() + 0.75D).move(rot.getStepX() * 1.5D, 0D, rot.getStepZ() * 1.5D);

        for(Player player : this.level.getEntitiesOfClass(Player.class, ladder)) {
            HbmPlayerAttachments props = HbmPlayerAttachments.getData(player);
            props.isOnLadder = true;
            player.setData(ModAttachments.PLAYER_ATTACHMENT.get(), props);
        }
    }

    protected DirPos[] getConPos() {
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();

        return new DirPos[] {
                new DirPos(x + 2, y, z + 1, Library.POS_X),
                new DirPos(x + 2, y, z - 1, Library.POS_X),
                new DirPos(x - 2, y, z + 1, Library.NEG_X),
                new DirPos(x - 2, y, z - 1, Library.NEG_X),
                new DirPos(x + 1, y, z + 2, Library.POS_Z),
                new DirPos(x - 1, y, z + 2, Library.POS_Z),
                new DirPos(x + 1, y, z - 2, Library.NEG_Z),
                new DirPos(x - 1, y, z - 2, Library.NEG_Z)
        };
    }

    private void processItem() {
        OreAcidizerRecipe recipe = OreAcidizerRecipes.INSTANCE.getOutput(this.slots.get(SLOT_INPUT), this.tank.getTankType());
        if(recipe == null) return;

        ItemStack output = recipe.output.copy();
        ItemStack outputSlot = this.slots.get(SLOT_OUTPUT);

        if(outputSlot.isEmpty()) {
            this.slots.set(SLOT_OUTPUT, output);
        } else {
            outputSlot.grow(output.getCount());
        }

        this.tank.setFill(this.tank.getFill() - this.getRequiredAcid(recipe.acidAmount));

        float freeChance = this.getFreeChance(recipe);
        if(freeChance == 0F || freeChance < this.level.random.nextFloat()) {
            this.slots.get(SLOT_INPUT).shrink(recipe.itemAmount);
        }
    }

    private boolean canProcess() {
        ItemStack input = this.slots.get(SLOT_INPUT);
        if(input.isEmpty()) return false;
        if(this.power < this.getPowerRequired()) return false;

        OreAcidizerRecipe recipe = OreAcidizerRecipes.INSTANCE.getOutput(input, this.tank.getTankType());
        if(recipe == null) return false;
        if(input.getCount() < recipe.itemAmount) return false;
        if(this.tank.getFill() < this.getRequiredAcid(recipe.acidAmount)) return false;

        ItemStack outputSlot = this.slots.get(SLOT_OUTPUT);
        if(!outputSlot.isEmpty() && !ItemStack.isSameItemSameComponents(outputSlot, recipe.output)) return false;
        return outputSlot.isEmpty() || outputSlot.getCount() + recipe.output.getCount() <= outputSlot.getMaxStackSize();
    }

    public int getRequiredAcid(int base) {
        return base;
    }

    public float getFreeChance(OreAcidizerRecipe recipe) {
        int efficiency = this.upgradeManager.getLevel(UpgradeType.EFFECT);
        return efficiency > 0 ? Math.min(efficiency * recipe.productivity, 0.99F) : 0F;
    }

    public int getDuration() {
        OreAcidizerRecipe recipe = OreAcidizerRecipes.INSTANCE.getOutput(this.slots.get(SLOT_INPUT), this.tank.getTankType());
        int base = recipe != null ? recipe.duration : 600;
        int speed = this.upgradeManager.getLevel(UpgradeType.SPEED);
        return speed > 0 ? (int) Math.ceil(base * Math.max(1F - 0.25F * speed, 0.25F)) : base;
    }

    public int getPowerRequired() {
        int speed = this.upgradeManager.getLevel(UpgradeType.SPEED);
        int effect = this.upgradeManager.getLevel(UpgradeType.EFFECT);
        return BASE_DEMAND + speed * BASE_DEMAND + effect * BASE_DEMAND * 2;
    }

    public int getCycleCount() {
        int overdrive = this.upgradeManager.getLevel(UpgradeType.OVERDRIVE);
        return Math.min(1 + overdrive * 2, 7);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.progress);
        buf.writeInt(this.duration);
        buf.writeLong(this.power);
        buf.writeBoolean(this.isOn);
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.progress = buf.readInt();
        this.duration = buf.readInt();
        this.power = buf.readLong();
        this.isOn = buf.readBoolean();
        this.tank.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.progress = tag.getInt("progress");
        this.duration = tag.getInt("duration");
        this.power = tag.getLong("power");
        this.isOn = tag.getBoolean("isOn");
        this.tank.readFromNBT(tag, "tank");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("progress", this.progress);
        tag.putInt("duration", this.duration);
        tag.putLong("power", this.power);
        tag.putBoolean("isOn", this.isOn);
        this.tank.writeToNBT(tag, "tank");
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == SLOT_INPUT) return OreAcidizerRecipes.INSTANCE.getOutput(stack, this.tank.getTankType()) != null;
        if(slot == SLOT_BATTERY) return stack.getItem() instanceof IBatteryItem;
        if(slot == SLOT_FLUID_INPUT) return FluidContainerRegistry.getFluidContent(stack, this.tank.getTankType()) > 0;
        if(slot == SLOT_UPGRADE_1 || slot == SLOT_UPGRADE_2) return stack.getItem() instanceof MachineUpgradeItem;
        if(slot == SLOT_FLUID_ID) return stack.getItem() instanceof IItemFluidIdentifier;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {SLOT_INPUT, SLOT_OUTPUT};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_INPUT && this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_OUTPUT;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);

        if(this.level != null && index >= SLOT_UPGRADE_1 && index <= SLOT_UPGRADE_2 && stack.getItem() instanceof MachineUpgradeItem) {
            SoundUtils.playAtVec3(this.level, this.getBlockPos().getCenter(), NtmSoundEvents.UPGRADE_PLUG.get(), SoundSource.BLOCKS);
        }
    }

    @Override
    public long getPower() {
        return this.power;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return MAX_POWER;
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] {this.tank};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] {this.tank};
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineOreAcidizerMenu(id, inventory, this);
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.CHEMICAL_PLANT_LOOP.get(), SoundSource.BLOCKS, this, 1F, 15F, 0.75F, 15);
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
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.EFFECT || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_ORE_ACIDIZER.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(this.KEY_DELAY, "-" + level * 25 + "%"));
            info.add(ChatFormatting.RED + I18nUtil.resolveKey(this.KEY_CONSUMPTION, "+" + level * 100 + "%"));
        }
        if(type == UpgradeType.EFFECT) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(this.KEY_EFFICIENCY, "x" + level));
            info.add(ChatFormatting.RED + I18nUtil.resolveKey(this.KEY_CONSUMPTION, "+" + level * 200 + "%"));
        }
        if(type == UpgradeType.OVERDRIVE) {
            info.add((BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_GRAY) + "YES");
        }
    }

    @Override
    public LinkedHashMap<UpgradeType, Integer> getValidUpgrades() {
        LinkedHashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.EFFECT, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    @Override
    public int[] getFluidIDToCopy() {
        return new int[] {this.tank.getTankType().getID()};
    }

    @Override
    public FluidTank getTankToPaste() {
        return this.tank;
    }
}
