package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IConditionalInvAccess;
import com.hbm.blockentity.IProxyDelegateProvider;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineAssemblyFactoryMenu;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.module.machine.ModuleAssemblyMachine;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.List;

public class MachineAssemblyFactoryBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IControlReceiver, IProxyDelegateProvider, IConditionalInvAccess, IFactoryPortProvider {

    private static final int[] ACCESSIBLE_SLOTS = {
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
            33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
            47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59
    };

    public final FluidTank[] allTanks;
    public final FluidTank[] inputTanks;
    public final FluidTank[] outputTanks;
    public final FluidTank water;
    public final FluidTank lps;

    public long power;
    public long maxPower = 1_000_000;
    public final boolean[] didProcess = new boolean[4];

    public boolean frame;
    private AudioWrapper audio;

    public final TragicYuri[] animations;
    public final ModuleAssemblyMachine[] assemblerModule;
    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    private final DelegateAssemblyFactory delegate = new DelegateAssemblyFactory();
    private DirPos[] coolantLine;

    public MachineAssemblyFactoryBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.ASSEMBLY_FACTORY.get(), pos, blockState, 60);

        this.animations = new TragicYuri[2];
        for(int i = 0; i < animations.length; i++) animations[i] = new TragicYuri(i);

        this.inputTanks = new FluidTank[4];
        this.outputTanks = new FluidTank[4];
        for(int i = 0; i < 4; i++) {
            this.inputTanks[i] = new FluidTank(Fluids.NONE, 4_000);
            this.outputTanks[i] = new FluidTank(Fluids.NONE, 4_000);
        }

        this.water = new FluidTank(Fluids.WATER, 4_000);
        this.lps = new FluidTank(Fluids.SPENTSTEAM, 4_000);

        this.allTanks = new FluidTank[this.inputTanks.length + this.outputTanks.length + 2];
        for(int i = 0; i < inputTanks.length; i++) this.allTanks[i] = this.inputTanks[i];
        for(int i = 0; i < outputTanks.length; i++) this.allTanks[i + this.inputTanks.length] = this.outputTanks[i];
        this.allTanks[this.allTanks.length - 2] = this.water;
        this.allTanks[this.allTanks.length - 1] = this.lps;

        this.assemblerModule = new ModuleAssemblyMachine[4];
        for(int i = 0; i < 4; i++) {
            this.assemblerModule[i] = new ModuleAssemblyMachine(i, this, slots)
                    .itemInput(5 + i * 14).itemOutput(17 + i * 14)
                    .fluidInput(inputTanks[i]).fluidOutput(outputTanks[i]);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_assembly_factory");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(maxPower <= 0) this.maxPower = 10_000_000;

        if(!level.isClientSide) {
            long nextMaxPower = 0;
            for(ModuleAssemblyMachine module : assemblerModule) {
                GenericRecipe recipe = module.getRecipe();
                if(recipe != null) nextMaxPower += recipe.power * 100;
            }
            this.maxPower = BobMathUtil.max(this.power, nextMaxPower, 1_000_000);

            this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);
            upgradeManager.checkSlots(slots, 1, 3);

            for(DirPos pos : getConPos()) {
                this.trySubscribe(level, pos);
                for(FluidTank tank : inputTanks) if(tank.getTankType() != Fluids.NONE) this.trySubscribe(tank.getTankType(), level, pos);
                for(FluidTank tank : outputTanks) if(tank.getFill() > 0) this.tryProvide(tank, level, pos);
            }

            for(DirPos pos : getCoolPos()) {
                delegate.trySubscribe(level, pos);
                delegate.trySubscribe(water.getTankType(), level, pos);
                if(lps.getFill() > 0) delegate.tryProvide(lps, level, pos);
            }

            double speed = 1D;
            double pow = 1D;

            speed += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3) / 3D;
            speed += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3);

            pow -= Math.min(upgradeManager.getLevel(UpgradeType.POWER), 3) * 0.25D;
            pow += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3) * 1D;
            pow += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3) * 10D / 3D;

            boolean markDirty = false;
            for(int i = 0; i < 4; i++) {
                this.assemblerModule[i].update(speed * 2D, pow * 2D, canCool(), slots.get(4 + i * 14));
                this.didProcess[i] = this.assemblerModule[i].didProcess;
                markDirty |= this.assemblerModule[i].markDirty;

                if(this.assemblerModule[i].didProcess) {
                    this.water.setFill(this.water.getFill() - 100);
                    this.lps.setFill(this.lps.getFill() + 100);
                }
            }

            if(markDirty) this.setChanged();
            this.networkPackNT(100);
        } else {
            boolean processing = this.isProcessing();

            if(processing && Math.sqrt(NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getBottomCenter())) < 50) {
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

            for(TragicYuri animation : animations) animation.update(processing);

            if(level.getGameTime() % 20 == 0) {
                frame = !level.getBlockState(this.worldPosition.above(3)).isAir();
            }
        }
    }

    public boolean isProcessing() {
        return didProcess[0] || didProcess[1] || didProcess[2] || didProcess[3];
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

    public boolean canCool() {
        return water.getFill() >= 100 && lps.getFill() <= lps.getMaxFill() - 100;
    }

    public DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise(Direction.Axis.Y);
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();

        return new DirPos[] {
                new DirPos(x + 3, y, z - 2, Library.POS_X),
                new DirPos(x + 3, y, z, Library.POS_X),
                new DirPos(x + 3, y, z + 2, Library.POS_X),
                new DirPos(x - 3, y, z - 2, Library.NEG_X),
                new DirPos(x - 3, y, z, Library.NEG_X),
                new DirPos(x - 3, y, z + 2, Library.NEG_X),
                new DirPos(x - 2, y, z + 3, Library.POS_Z),
                new DirPos(x, y, z + 3, Library.POS_Z),
                new DirPos(x + 2, y, z + 3, Library.POS_Z),
                new DirPos(x - 2, y, z - 3, Library.NEG_Z),
                new DirPos(x, y, z - 3, Library.NEG_Z),
                new DirPos(x + 2, y, z - 3, Library.NEG_Z),
                new DirPos(x + dir.getStepX() + rot.getStepX() * 3, y, z + dir.getStepZ() + rot.getStepZ() * 3, rot),
                new DirPos(x - dir.getStepX() + rot.getStepX() * 3, y, z - dir.getStepZ() + rot.getStepZ() * 3, rot),
                new DirPos(x + dir.getStepX() - rot.getStepX() * 3, y, z + dir.getStepZ() - rot.getStepZ() * 3, rot.getOpposite()),
                new DirPos(x - dir.getStepX() - rot.getStepX() * 3, y, z - dir.getStepZ() - rot.getStepZ() * 3, rot.getOpposite())
        };
    }

    public DirPos[] getCoolPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise(Direction.Axis.Y);
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();

        return new DirPos[] {
                new DirPos(x + rot.getStepX() + dir.getStepX() * 3, y, z + rot.getStepZ() + dir.getStepZ() * 3, dir),
                new DirPos(x - rot.getStepX() + dir.getStepX() * 3, y, z - rot.getStepZ() + dir.getStepZ() * 3, dir),
                new DirPos(x + rot.getStepX() - dir.getStepX() * 3, y, z + rot.getStepZ() - dir.getStepZ() * 3, dir.getOpposite()),
                new DirPos(x - rot.getStepX() - dir.getStepX() * 3, y, z - rot.getStepZ() - dir.getStepZ() * 3, dir.getOpposite())
        };
    }

    public DirPos[] getIOPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise(Direction.Axis.Y);
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();

        return new DirPos[] {
                new DirPos(x + dir.getStepX() + rot.getStepX() * 3, y, z + dir.getStepZ() + rot.getStepZ() * 3, rot),
                new DirPos(x - dir.getStepX() + rot.getStepX() * 3, y, z - dir.getStepZ() + rot.getStepZ() * 3, rot),
                new DirPos(x + dir.getStepX() - rot.getStepX() * 3, y, z + dir.getStepZ() - rot.getStepZ() * 3, rot.getOpposite()),
                new DirPos(x - dir.getStepX() - rot.getStepX() * 3, y, z - dir.getStepZ() - rot.getStepZ() * 3, rot.getOpposite())
        };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        for(FluidTank tank : inputTanks) tank.serialize(buf);
        for(FluidTank tank : outputTanks) tank.serialize(buf);
        water.serialize(buf);
        lps.serialize(buf);
        buf.writeLong(power);
        buf.writeLong(maxPower);
        for(boolean value : didProcess) buf.writeBoolean(value);
        for(ModuleAssemblyMachine module : assemblerModule) module.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        for(FluidTank tank : inputTanks) tank.deserialize(buf);
        for(FluidTank tank : outputTanks) tank.deserialize(buf);
        water.deserialize(buf);
        lps.deserialize(buf);
        this.power = buf.readLong();
        this.maxPower = buf.readLong();
        for(int i = 0; i < didProcess.length; i++) this.didProcess[i] = buf.readBoolean();
        for(ModuleAssemblyMachine module : assemblerModule) module.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for(int i = 0; i < inputTanks.length; i++) this.inputTanks[i].readFromNBT(tag, "i" + i);
        for(int i = 0; i < outputTanks.length; i++) this.outputTanks[i].readFromNBT(tag, "o" + i);
        this.water.readFromNBT(tag, "w");
        this.lps.readFromNBT(tag, "s");
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        for(ModuleAssemblyMachine module : assemblerModule) module.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for(int i = 0; i < inputTanks.length; i++) this.inputTanks[i].writeToNBT(tag, "i" + i);
        for(int i = 0; i < outputTanks.length; i++) this.outputTanks[i].writeToNBT(tag, "o" + i);
        this.water.writeToNBT(tag, "w");
        this.lps.writeToNBT(tag, "s");
        tag.putLong("power", power);
        tag.putLong("maxPower", maxPower);
        for(ModuleAssemblyMachine module : assemblerModule) module.writeToNBT(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true;
        for(int i = 0; i < 4; i++) if(slot == 4 + i * 14 && stack.getItem() == NtmItems.BLUEPRINTS.get()) return true;
        if(slot >= 1 && slot <= 3 && stack.getItem() instanceof MachineUpgradeItem) return true;
        for(ModuleAssemblyMachine module : assemblerModule) if(module.isItemValid(slot, stack)) return true;
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        for(int i = 0; i < 4; i++) if(slot == 17 + i * 14) return true;
        for(ModuleAssemblyMachine module : assemblerModule) if(module.isSlotClogged(slot)) return true;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return ACCESSIBLE_SLOTS;
    }

    @Override
    public boolean isItemValidForSlot(BlockPos pos, int slot, ItemStack stack) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canExtractItem(BlockPos pos, int slot, ItemStack stack, Direction direction) {
        return this.canTakeItemThroughFace(slot, stack, direction);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(BlockPos pos, Direction direction) {
        DirPos[] io = getIOPos();
        for(int i = 0; i < io.length; i++) {
            DirPos port = io[i];
            if(port.compare(
                    pos.getX() + port.getDir().getStepX(),
                    pos.getY() + port.getDir().getStepY(),
                    pos.getZ() + port.getDir().getStepZ())) {
                int[] slots = new int[16];
                for(int j = 0; j < 12; j++) slots[j] = 5 + i * 14 + j;
                for(int j = 0; j < 4; j++) slots[12 + j] = 17 + j * 14;
                return slots;
            }
        }
        return ACCESSIBLE_SLOTS;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTank[] getReceivingTanks() { return inputTanks; }
    @Override public FluidTank[] getSendingTanks() { return outputTanks; }
    @Override public FluidTank[] getAllTanks() { return allTanks; }
    @Override public FluidTank getWaterTank() { return water; }
    @Override public FluidTank getSpentSteamTank() { return lps; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineAssemblyFactoryMenu(id, inventory, this);
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("index") && tag.contains("selection")) {
            int index = tag.getInt("index");
            String selection = tag.getString("selection");
            if(index >= 0 && index < 4) {
                this.assemblerModule[index].recipe = selection;
                this.setChanged();
            }
        }
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_ASSEMBLY_FACTORY.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(KEY_SPEED, "+" + (level * 100 / 3) + "%"));
            info.add(ChatFormatting.RED + I18nUtil.resolveKey(KEY_CONSUMPTION, "+" + (level * 50) + "%"));
        }
        if(type == UpgradeType.POWER) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(KEY_CONSUMPTION, "-" + (level * 25) + "%"));
        }
        if(type == UpgradeType.OVERDRIVE) {
            info.add((BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_GRAY) + "YES");
        }
    }

    @Override
    public LinkedHashMap<UpgradeType, Integer> getValidUpgrades() {
        LinkedHashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    @Override
    public Object getDelegateForPosition(BlockPos pos) {
        if(coolantLine == null) {
            Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
            Direction rot = dir.getClockWise(Direction.Axis.Y);
            int x = this.worldPosition.getX();
            int y = this.worldPosition.getY();
            int z = this.worldPosition.getZ();

            coolantLine = new DirPos[] {
                    new DirPos(x + rot.getStepX() + dir.getStepX() * 2, y, z + rot.getStepZ() + dir.getStepZ() * 2, dir),
                    new DirPos(x - rot.getStepX() + dir.getStepX() * 2, y, z - rot.getStepZ() + dir.getStepZ() * 2, dir),
                    new DirPos(x + rot.getStepX() - dir.getStepX() * 2, y, z + rot.getStepZ() - dir.getStepZ() * 2, dir.getOpposite()),
                    new DirPos(x - rot.getStepX() - dir.getStepX() * 2, y, z - rot.getStepZ() - dir.getStepZ() * 2, dir.getOpposite())
            };
        }

        for(DirPos coolantPos : coolantLine) if(coolantPos.compare(pos.getX(), pos.getY(), pos.getZ())) return this.delegate;
        return null;
    }

    public class DelegateAssemblyFactory implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2 {
        @Override public long getPower() { return MachineAssemblyFactoryBlockEntity.this.getPower(); }
        @Override public void setPower(long power) { MachineAssemblyFactoryBlockEntity.this.setPower(power); }
        @Override public long getMaxPower() { return MachineAssemblyFactoryBlockEntity.this.getMaxPower(); }
        @Override public boolean isLoaded() { return MachineAssemblyFactoryBlockEntity.this.isLoaded(); }
        @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {MachineAssemblyFactoryBlockEntity.this.water}; }
        @Override public FluidTank[] getSendingTanks() { return new FluidTank[] {MachineAssemblyFactoryBlockEntity.this.lps}; }
        @Override public FluidTank[] getAllTanks() { return new FluidTank[] {MachineAssemblyFactoryBlockEntity.this.water, MachineAssemblyFactoryBlockEntity.this.lps}; }
    }

    /**
     * Carriage consisting of two arms - a striker and a saw
     * Movement of both arms is inverted, one pedestal can only be serviced by one arm at a time
     *
     * @author hbm
     */
    public class TragicYuri {
        public final AssemblerArm striker;
        public final AssemblerArm saw;
        private final RandomSource random = RandomSource.create();
        private YuriState state = YuriState.WORKING;
        private float slider;
        private float prevSlider;
        private boolean direction;
        private int timeUntilReposition;

        public TragicYuri(int group) {
            striker = new AssemblerArm(group == 0 ? 0 : 3);
            saw = new AssemblerArm(group == 0 ? 1 : 2).yepThatsASaw();
            timeUntilReposition = 140 + random.nextInt(161);
        }

        public void update(boolean working) {
            this.prevSlider = this.slider;

            if(didProcess[striker.recipeIndex] || didProcess[saw.recipeIndex]) {
                switch(state) {
                    case WORKING -> {
                        timeUntilReposition--;
                        if(timeUntilReposition <= 0) state = YuriState.RETIRING;
                    }
                    case RETIRING -> {
                        if(striker.state == ArmState.WAIT && saw.state == ArmState.WAIT) {
                            state = YuriState.SLIDING;
                            direction = !direction;
                            if(!muffled) NuclearTechMod.proxy.playLocalSound(getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_START.get(), SoundSource.BLOCKS, getVolume(0.25F), 1.25F + level.random.nextFloat() * 0.25F);
                        }
                    }
                    case SLIDING -> {
                        float sliderSpeed = 1F / 10F;
                        if(direction) {
                            slider += sliderSpeed;
                            if(slider >= 1F) {
                                slider = 1F;
                                state = YuriState.WORKING;
                            }
                        } else {
                            slider -= sliderSpeed;
                            if(slider <= 0F) {
                                slider = 0F;
                                state = YuriState.WORKING;
                            }
                        }
                        if(state == YuriState.WORKING) timeUntilReposition = 140 + random.nextInt(161);
                    }
                }
            }

            striker.updateArm();
            saw.updateArm();
        }

        public float getSlider(float partialTicks) {
            return BobMathUtil.interp(this.prevSlider, this.slider, partialTicks);
        }

        public class AssemblerArm {
            private final float[] angles = new float[4];
            private final float[] prevAngles = new float[4];
            private final float[] targetAngles = new float[4];
            private final float[] speed = new float[4];
            private float sawAngle;
            private float prevSawAngle;
            private final int recipeIndex;
            private ArmState state = ArmState.REPOSITION;
            private int actionDelay;
            private boolean saw;

            public AssemblerArm(int index) {
                this.recipeIndex = index;
                this.resetSpeed();
                this.chooseNewArmPosition();
            }

            public AssemblerArm yepThatsASaw() {
                this.saw = true;
                this.chooseNewArmPosition();
                return this;
            }

            private void resetSpeed() {
                speed[0] = 15F;
                speed[1] = 15F;
                speed[2] = 15F;
                speed[3] = saw ? 0.125F : 0.5F;
            }

            public void updateArm() {
                resetSpeed();
                for(int i = 0; i < angles.length; i++) prevAngles[i] = angles[i];
                prevSawAngle = sawAngle;

                int serviceIndex = recipeIndex;
                if(slider > 0.5F) serviceIndex += serviceIndex % 2 == 0 ? 1 : -1;
                if(!didProcess[serviceIndex]) state = ArmState.RETIRE;
                if(state == ArmState.CUT || state == ArmState.EXTEND) this.sawAngle += 45F;

                if(actionDelay > 0) {
                    actionDelay--;
                    return;
                }

                switch(state) {
                    case REPOSITION -> {
                        if(move()) {
                            actionDelay = 2;
                            state = ArmState.EXTEND;
                            targetAngles[3] = saw ? -0.375F : -0.75F;
                        }
                    }
                    case EXTEND -> {
                        if(move()) {
                            if(saw) {
                                state = ArmState.CUT;
                                targetAngles[2] = -targetAngles[2];
                                if(!muffled) NuclearTechMod.proxy.playLocalSound(getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_CUT.get(), SoundSource.BLOCKS, getVolume(0.5F), 1F + random.nextFloat() * 0.25F);
                            } else {
                                state = ArmState.RETRACT;
                                targetAngles[3] = 0F;
                                if(!muffled) NuclearTechMod.proxy.playLocalSound(getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_STRIKE.get(), SoundSource.BLOCKS, getVolume(0.5F), 1F);
                            }
                        }
                    }
                    case CUT -> {
                        speed[2] = Math.abs(targetAngles[2] / 20F);
                        if(move()) {
                            state = ArmState.RETRACT;
                            targetAngles[3] = 0F;
                        }
                    }
                    case RETRACT -> {
                        if(move()) {
                            actionDelay = 2 + random.nextInt(5);
                            chooseNewArmPosition();
                            state = TragicYuri.this.state == YuriState.RETIRING ? ArmState.RETIRE : ArmState.REPOSITION;
                        }
                    }
                    case RETIRE -> {
                        for(int i = 0; i < targetAngles.length; i++) targetAngles[i] = 0F;
                        if(move()) {
                            actionDelay = 2 + random.nextInt(5);
                            chooseNewArmPosition();
                            state = ArmState.WAIT;
                        }
                    }
                    case WAIT -> {
                        if(TragicYuri.this.state == YuriState.WORKING) this.state = ArmState.REPOSITION;
                    }
                }
            }

            private void chooseNewArmPosition() {
                float[][] positions = !saw ? new float[][] {
                        {10, 10, -10}, {15, 15, -15}, {25, 10, -15}, {30, 0, -10}, {-10, 10, 0}, {-20, 30, -15}
                } : new float[][] {
                        {-15, 15, -10}, {-15, 15, -15}, {-15, 15, 10}, {-15, 15, 15}, {-15, 15, 2}, {-15, 15, -2}
                };

                int chosen = random.nextInt(positions.length);
                this.targetAngles[0] = positions[chosen][0];
                this.targetAngles[1] = positions[chosen][1];
                this.targetAngles[2] = positions[chosen][2];
            }

            private boolean move() {
                boolean didMove = false;
                for(int i = 0; i < angles.length; i++) {
                    if(angles[i] == targetAngles[i]) continue;
                    didMove = true;
                    float delta = Math.abs(angles[i] - targetAngles[i]);
                    if(delta <= speed[i]) {
                        angles[i] = targetAngles[i];
                    } else if(angles[i] < targetAngles[i]) {
                        angles[i] += speed[i];
                    } else {
                        angles[i] -= speed[i];
                    }
                }
                return !didMove;
            }

            public float[] getPositions(float partialTicks) {
                return new float[] {
                        BobMathUtil.interp(this.prevAngles[0], this.angles[0], partialTicks),
                        BobMathUtil.interp(this.prevAngles[1], this.angles[1], partialTicks),
                        BobMathUtil.interp(this.prevAngles[2], this.angles[2], partialTicks),
                        BobMathUtil.interp(this.prevAngles[3], this.angles[3], partialTicks),
                        BobMathUtil.interp(this.prevSawAngle, this.sawAngle, partialTicks)
                };
            }
        }
    }

    public enum YuriState {
        WORKING,
        RETIRING,
        SLIDING
    }

    public enum ArmState {
        REPOSITION,
        EXTEND,
        CUT,
        RETRACT,
        RETIRE,
        WAIT
    }
}
