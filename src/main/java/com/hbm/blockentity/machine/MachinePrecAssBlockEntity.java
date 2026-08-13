package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachinePrecAssMenu;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.module.machine.ModulePrecAss;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MachinePrecAssBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IControlReceiver {

    private static final int[] ACCESSIBLE_SLOTS = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
    private static final double[] NULL_POSITION = {45, -30, 45};
    private static final double[] WORKING_POSITION = {45, -15, -5};

    public final FluidTank inputTank;
    public final FluidTank outputTank;

    public long power;
    public long maxPower = 100_000;
    public boolean didProcess;

    public boolean frame;
    private AudioWrapper audio;

    public final ModulePrecAss assemblerModule;

    public double prevRing;
    public double ring;
    public double ringSpeed;
    public double ringTarget;
    public int ringDelay;

    public final double[] armAngles = {45, -15, -5};
    public final double[] prevArmAngles = {45, -15, -5};
    public final double[] strikers = new double[4];
    public final double[] prevStrikers = new double[4];
    private final boolean[] strikerDir = new boolean[4];
    private int strikerIndex;
    private int strikerDelay;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachinePrecAssBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.PREC_ASS.get(), pos, blockState, 22);

        this.inputTank = new FluidTank(Fluids.NONE, 4_000);
        this.outputTank = new FluidTank(Fluids.NONE, 4_000);

        this.assemblerModule = new ModulePrecAss(0, this, slots)
                .itemInput(4)
                .itemOutput(13)
                .fluidInput(inputTank)
                .fluidOutput(outputTank);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_precass");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(maxPower <= 0) this.maxPower = 1_000_000;

        if(!level.isClientSide) {
            GenericRecipe recipe = assemblerModule.getRecipe();
            if(recipe != null) this.maxPower = recipe.power * 100;
            this.maxPower = BobMathUtil.max(this.power, this.maxPower, 100_000);

            this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);
            this.upgradeManager.checkSlots(slots, 2, 3);

            for(DirPos pos : getConPos()) {
                this.trySubscribe(level, pos);
                if(inputTank.getTankType() != Fluids.NONE) this.trySubscribe(inputTank.getTankType(), level, pos);
                if(outputTank.getFill() > 0) this.tryProvide(outputTank, level, pos);
            }

            double speed = 1D;
            double pow = 1D;

            speed += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3) / 3D;
            speed += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3);

            pow -= Math.min(upgradeManager.getLevel(UpgradeType.POWER), 3) * 0.25D;
            pow += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3) * 1D;
            pow += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3) * 10D / 3D;

            this.assemblerModule.update(speed, pow, true, slots.get(1));
            this.didProcess = this.assemblerModule.didProcess;
            if(this.assemblerModule.markDirty) this.setChanged();

            this.networkPackNT(100);
        } else {
            if(level.getGameTime() % 20 == 0) {
                frame = !level.getBlockState(this.worldPosition.above(3)).isAir();
            }

            if(this.didProcess && NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getBottomCenter()) < 50 * 50) {
                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }
                audio.keepAlive();
                audio.updatePitch(0.75F);
                audio.updateVolume(this.getVolume(0.5F));
            } else if(audio != null) {
                audio.stopSound();
                audio = null;
            }

            System.arraycopy(this.armAngles, 0, this.prevArmAngles, 0, this.armAngles.length);
            System.arraycopy(this.strikers, 0, this.prevStrikers, 0, this.strikers.length);
            this.prevRing = this.ring;

            for(int i = 0; i < strikers.length; i++) {
                if(this.strikerDir[i]) {
                    this.strikers[i] = -0.75D;
                    this.strikerDir[i] = false;
                    if(!this.muffled) NuclearTechMod.proxy.playLocalSound(this.getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_STRIKE.get(), SoundSource.BLOCKS, this.getVolume(0.5F), 1.25F);
                } else {
                    this.strikers[i] = Mth.clamp(this.strikers[i] + 0.5D, -0.75D, 0D);
                }
            }

            if(this.ring != this.ringTarget) {
                double ringDelta = Math.abs(this.ringTarget - this.ring);
                if(ringDelta <= this.ringSpeed) this.ring = this.ringTarget;
                if(this.ringTarget > this.ring) this.ring += this.ringSpeed;
                if(this.ringTarget < this.ring) this.ring -= this.ringSpeed;
                if(this.ringTarget == this.ring) {
                    double sub = ringTarget >= 360 ? -360D : 360D;
                    this.ringTarget += sub;
                    this.ring += sub;
                    this.prevRing += sub;
                    this.ringDelay = 100 + level.random.nextInt(21);
                }
            }

            if(didProcess) {
                if(this.ring == this.ringTarget) {
                    if(this.ringDelay > 0) this.ringDelay--;
                    if(this.ringDelay <= 0) {
                        this.ringTarget += 45 * (level.random.nextBoolean() ? -1 : 1);
                        this.ringSpeed = 10D + level.random.nextDouble() * 5D;
                        if(!this.muffled) NuclearTechMod.proxy.playLocalSound(this.getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_START.get(), SoundSource.BLOCKS, this.getVolume(0.25F), 1.25F + level.random.nextFloat() * 0.25F);
                    }
                }

                if(!isInWorkingPosition(this.armAngles) && canArmsMove()) move(WORKING_POSITION);

                if(isInWorkingPosition(this.armAngles)) {
                    this.strikerDelay--;
                    if(this.strikerDelay <= 0) {
                        this.strikerDir[this.strikerIndex] = true;
                        this.strikerIndex = (this.strikerIndex + 1) % this.strikers.length;
                        this.strikerDelay = this.strikerIndex == 3 ? 10 + level.random.nextInt(3) : 2;
                    }
                }
            } else {
                for(int i = 0; i < strikerDir.length; i++) this.strikerDir[i] = false;
                if(canArmsMove()) move(NULL_POSITION);
            }

            if(this.isInWorkingPosition(prevArmAngles) && !this.isInWorkingPosition(armAngles)) {
                if(!this.muffled) NuclearTechMod.proxy.playLocalSound(this.getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_STOP.get(), SoundSource.BLOCKS, this.getVolume(0.25F), 1.25F + level.random.nextFloat() * 0.25F);
            }
        }
    }

    private boolean canArmsMove() {
        for(double striker : this.strikers) if(striker != 0) return false;
        return true;
    }

    private boolean isInWorkingPosition(double[] arms) {
        for(int i = 0; i < WORKING_POSITION.length; i++) if(arms[i] != WORKING_POSITION[i]) return false;
        return true;
    }

    private boolean move(double[] targetAngles) {
        boolean didMove = false;

        for(int i = 0; i < armAngles.length; i++) {
            if(armAngles[i] == targetAngles[i]) continue;
            didMove = true;
            double angle = armAngles[i];
            double target = targetAngles[i];
            double turn = 15D;
            double delta = Math.abs(angle - target);

            if(delta <= turn) {
                armAngles[i] = targetAngles[i];
                continue;
            }
            if(angle < target) armAngles[i] += turn;
            else armAngles[i] -= turn;
        }
        return !didMove;
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.ELECTRIC_MOTOR_LOOP.get(), SoundSource.BLOCKS, this, 0.5F, 15F, 0.75F, 20);
    }

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

    public DirPos[] getConPos() {
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();

        return new DirPos[] {
                new DirPos(x + 2, y, z - 1, Library.POS_X),
                new DirPos(x + 2, y, z, Library.POS_X),
                new DirPos(x + 2, y, z + 1, Library.POS_X),
                new DirPos(x - 2, y, z - 1, Library.NEG_X),
                new DirPos(x - 2, y, z, Library.NEG_X),
                new DirPos(x - 2, y, z + 1, Library.NEG_X),
                new DirPos(x - 1, y, z + 2, Library.POS_Z),
                new DirPos(x, y, z + 2, Library.POS_Z),
                new DirPos(x + 1, y, z + 2, Library.POS_Z),
                new DirPos(x - 1, y, z - 2, Library.NEG_Z),
                new DirPos(x, y, z - 2, Library.NEG_Z),
                new DirPos(x + 1, y, z - 2, Library.NEG_Z)
        };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.inputTank.serialize(buf);
        this.outputTank.serialize(buf);
        buf.writeLong(power);
        buf.writeLong(maxPower);
        buf.writeBoolean(didProcess);
        this.assemblerModule.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.inputTank.deserialize(buf);
        this.outputTank.deserialize(buf);
        this.power = buf.readLong();
        this.maxPower = buf.readLong();
        this.didProcess = buf.readBoolean();
        this.assemblerModule.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.inputTank.readFromNBT(tag, "i");
        this.outputTank.readFromNBT(tag, "o");
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.assemblerModule.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.inputTank.writeToNBT(tag, "i");
        this.outputTank.writeToNBT(tag, "o");
        tag.putLong("power", power);
        tag.putLong("maxPower", maxPower);
        this.assemblerModule.writeToNBT(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true;
        if(slot == 1 && stack.getItem() == NtmItems.BLUEPRINTS.get()) return true;
        if(slot >= 2 && slot <= 3 && stack.getItem() instanceof MachineUpgradeItem) return true;
        return this.assemblerModule.isItemValid(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot >= 13 || this.assemblerModule.isSlotClogged(slot);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return ACCESSIBLE_SLOTS;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {inputTank}; }
    @Override public FluidTank[] getSendingTanks() { return new FluidTank[] {outputTank}; }
    @Override public FluidTank[] getAllTanks() { return new FluidTank[] {inputTank, outputTank}; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachinePrecAssMenu(id, inventory, this);
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("index") && tag.contains("selection")) {
            int index = tag.getInt("index");
            String selection = tag.getString("selection");
            if(index == 0) {
                this.assemblerModule.recipe = selection;
                this.setChanged();
            }
        }
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) { }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        LinkedHashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    public float getRenderRing(float partialTicks) {
        return BobMathUtil.interp((float) this.prevRing, (float) this.ring, partialTicks);
    }

    public float[] getRenderArmAngles(float partialTicks) {
        return new float[] {
                BobMathUtil.interp((float) this.prevArmAngles[0], (float) this.armAngles[0], partialTicks),
                BobMathUtil.interp((float) this.prevArmAngles[1], (float) this.armAngles[1], partialTicks),
                BobMathUtil.interp((float) this.prevArmAngles[2], (float) this.armAngles[2], partialTicks)
        };
    }

    public float getRenderStriker(int index, float partialTicks) {
        return BobMathUtil.interp((float) this.prevStrikers[index], (float) this.strikers[index], partialTicks);
    }
}
