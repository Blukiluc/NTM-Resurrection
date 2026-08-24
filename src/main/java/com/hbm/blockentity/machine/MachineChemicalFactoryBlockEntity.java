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
import com.hbm.inventory.menus.MachineChemicalFactoryMenu;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.module.machine.ModuleChemicalPlant;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.List;

public class MachineChemicalFactoryBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IControlReceiver, IProxyDelegateProvider, IConditionalInvAccess, IFactoryPortProvider {

    private static final int[] ACCESSIBLE_SLOTS = {
            5, 6, 7, 8, 9, 10,
            12, 13, 14, 15, 16, 17,
            19, 20, 21, 22, 23, 24,
            26, 27, 28, 29, 30, 31
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
    public int anim;
    public int prevAnim;
    private AudioWrapper audio;

    public final ModuleChemicalPlant[] chemplantModule;
    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    private final DelegateChemicalFactory delegate = new DelegateChemicalFactory();
    private DirPos[] coolantLine;

    public MachineChemicalFactoryBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.CHEMICAL_FACTORY.get(), pos, blockState, 32);

        this.inputTanks = new FluidTank[12];
        this.outputTanks = new FluidTank[12];
        for(int i = 0; i < 12; i++) {
            this.inputTanks[i] = new FluidTank(Fluids.NONE, 24_000);
            this.outputTanks[i] = new FluidTank(Fluids.NONE, 24_000);
        }

        this.water = new FluidTank(Fluids.WATER, 4_000);
        this.lps = new FluidTank(Fluids.SPENTSTEAM, 4_000);

        this.allTanks = new FluidTank[this.inputTanks.length + this.outputTanks.length + 2];
        for(int i = 0; i < inputTanks.length; i++) this.allTanks[i] = this.inputTanks[i];
        for(int i = 0; i < outputTanks.length; i++) this.allTanks[i + this.inputTanks.length] = this.outputTanks[i];
        this.allTanks[this.allTanks.length - 2] = this.water;
        this.allTanks[this.allTanks.length - 1] = this.lps;

        this.chemplantModule = new ModuleChemicalPlant[4];
        for(int i = 0; i < 4; i++) {
            this.chemplantModule[i] = new ModuleChemicalPlant(i, this, slots)
                    .itemInput(5 + i * 7)
                    .itemOutput(8 + i * 7)
                    .fluidInput(inputTanks[i * 3], inputTanks[1 + i * 3], inputTanks[2 + i * 3])
                    .fluidOutput(outputTanks[i * 3], outputTanks[1 + i * 3], outputTanks[2 + i * 3]);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_chemical_factory");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(maxPower <= 0) this.maxPower = 10_000_000;

        if(!level.isClientSide) {
            long nextMaxPower = 0;
            for(ModuleChemicalPlant module : chemplantModule) {
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
                this.chemplantModule[i].update(speed * 2D, pow * 2D, canCool(), slots.get(4 + i * 7));
                this.didProcess[i] = this.chemplantModule[i].didProcess;
                markDirty |= this.chemplantModule[i].markDirty;

                if(this.chemplantModule[i].didProcess) {
                    this.water.setFill(this.water.getFill() - 100);
                    this.lps.setFill(this.lps.getFill() + 100);
                }
            }

            // internal fluid sharing logic
            for(FluidTank in : inputTanks) if(in.getTankType() != Fluids.NONE) for(FluidTank out : outputTanks) {
                if(out.getTankType() == Fluids.NONE) continue;
                if(out.getTankType() != in.getTankType()) continue;
                if(out.getPressure() != in.getPressure()) continue;
                int toMove = BobMathUtil.min(in.getMaxFill() - in.getFill(), out.getFill(), 50);
                if(toMove > 0) {
                    in.setFill(in.getFill() + toMove);
                    out.setFill(out.getFill() - toMove);
                }
            }

            if(markDirty) this.setChanged();
            this.networkPackNT(100);
        } else {
            this.prevAnim = this.anim;
            boolean processing = this.isProcessing();
            if(processing) this.anim++;

            if(level.getGameTime() % 20 == 0) {
                frame = !level.getBlockState(this.worldPosition.above(3)).isAir();
            }

            if(processing && Math.sqrt(NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getBottomCenter())) < 50) {
                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }
                audio.keepAlive();
                audio.updateVolume(this.getVolume(1F));
            } else if(audio != null) {
                audio.stopSound();
                audio = null;
            }
        }
    }

    public boolean isProcessing() {
        return didProcess[0] || didProcess[1] || didProcess[2] || didProcess[3];
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.CHEMICAL_PLANT_LOOP.get(), SoundSource.BLOCKS, this, 1F, 15F, 1F, 20);
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

                new DirPos(x + dir.getStepX() * 2 + rot.getStepX() * 2, y + 3, z + dir.getStepZ() * 2 + rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x + dir.getStepX() + rot.getStepX() * 2, y + 3, z + dir.getStepZ() + rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x + rot.getStepX() * 2, y + 3, z + rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x - dir.getStepX() + rot.getStepX() * 2, y + 3, z - dir.getStepZ() + rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x - dir.getStepX() * 2 + rot.getStepX() * 2, y + 3, z - dir.getStepZ() * 2 + rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x + dir.getStepX() * 2 - rot.getStepX() * 2, y + 3, z + dir.getStepZ() * 2 - rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x + dir.getStepX() - rot.getStepX() * 2, y + 3, z + dir.getStepZ() - rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x - rot.getStepX() * 2, y + 3, z - rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x - dir.getStepX() - rot.getStepX() * 2, y + 3, z - dir.getStepZ() - rot.getStepZ() * 2, Library.POS_Y),
                new DirPos(x - dir.getStepX() * 2 - rot.getStepX() * 2, y + 3, z - dir.getStepZ() * 2 - rot.getStepZ() * 2, Library.POS_Y),

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
        for(ModuleChemicalPlant module : chemplantModule) module.serialize(buf);
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
        for(ModuleChemicalPlant module : chemplantModule) module.deserialize(buf);
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
        for(ModuleChemicalPlant module : chemplantModule) module.readFromNBT(tag);
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
        for(ModuleChemicalPlant module : chemplantModule) module.writeToNBT(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true;
        for(int i = 0; i < 4; i++) if(slot == 4 + i * 7 && stack.getItem() == NtmItems.BLUEPRINTS.get()) return true;
        if(slot >= 1 && slot <= 3 && stack.getItem() instanceof MachineUpgradeItem) return true;
        for(ModuleChemicalPlant module : chemplantModule) if(module.isItemValid(slot, stack)) return true;
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        if(slot >= 8 && slot <= 10) return true;
        if(slot >= 15 && slot <= 17) return true;
        if(slot >= 22 && slot <= 24) return true;
        if(slot >= 29 && slot <= 31) return true;
        for(ModuleChemicalPlant module : chemplantModule) if(module.isSlotClogged(slot)) return true;
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
                int[] slots = new int[15];
                slots[0] = 5 + i * 7;
                slots[1] = 6 + i * 7;
                slots[2] = 7 + i * 7;
                int index = 3;
                for(int module = 0; module < 4; module++) {
                    for(int output = 0; output < 3; output++) slots[index++] = 8 + module * 7 + output;
                }
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
        return new MachineChemicalFactoryMenu(id, inventory, this);
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("index") && tag.contains("selection")) {
            int index = tag.getInt("index");
            String selection = tag.getString("selection");
            if(index >= 0 && index < 4) {
                this.chemplantModule[index].recipe = selection;
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
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_CHEMICAL_FACTORY.get()).getString());
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

    public class DelegateChemicalFactory implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2 {
        @Override public long getPower() { return MachineChemicalFactoryBlockEntity.this.getPower(); }
        @Override public void setPower(long power) { MachineChemicalFactoryBlockEntity.this.setPower(power); }
        @Override public long getMaxPower() { return MachineChemicalFactoryBlockEntity.this.getMaxPower(); }
        @Override public boolean isLoaded() { return MachineChemicalFactoryBlockEntity.this.isLoaded(); }
        @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {MachineChemicalFactoryBlockEntity.this.water}; }
        @Override public FluidTank[] getSendingTanks() { return new FluidTank[] {MachineChemicalFactoryBlockEntity.this.lps}; }
        @Override public FluidTank[] getAllTanks() { return new FluidTank[] {MachineChemicalFactoryBlockEntity.this.water, MachineChemicalFactoryBlockEntity.this.lps}; }
    }
}
