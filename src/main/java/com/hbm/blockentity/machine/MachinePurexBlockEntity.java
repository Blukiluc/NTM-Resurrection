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
import com.hbm.inventory.menus.MachinePurexMenu;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.module.machine.ModuleMachinePUREX;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MachinePurexBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IControlReceiver {

    private static final int[] ACCESSIBLE_SLOTS = {4, 5, 6, 7, 8, 9, 10, 11, 12};

    public final FluidTank[] inputTanks = new FluidTank[3];
    public final FluidTank[] outputTanks = new FluidTank[1];

    public long power;
    public long maxPower = 1_000_000;
    public boolean didProcess;

    public boolean frame;
    public int anim;
    public int prevAnim;

    private AudioWrapper audio;

    public final ModuleMachinePUREX purexModule;
    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachinePurexBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.PUREX.get(), pos, blockState, 13);

        for(int i = 0; i < inputTanks.length; i++) {
            inputTanks[i] = new FluidTank(Fluids.NONE, 24_000);
        }
        outputTanks[0] = new FluidTank(Fluids.NONE, 24_000);

        this.purexModule = new ModuleMachinePUREX(0, this, slots)
                .itemInput(4)
                .itemOutput(7)
                .fluidInput(inputTanks[0], inputTanks[1], inputTanks[2])
                .fluidOutput(outputTanks[0]);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_purex");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.maxPower <= 0) this.maxPower = 1_000_000;

        if(!level.isClientSide) {
            GenericRecipe recipe = purexModule.getRecipe();
            if(recipe != null) this.maxPower = recipe.power * 100;
            this.maxPower = BobMathUtil.max(this.power, this.maxPower, 1_000_000);

            this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);
            this.upgradeManager.checkSlots(slots, 2, 3);

            for(DirPos pos : getConPos()) {
                this.trySubscribe(level, pos);
                for(FluidTank tank : inputTanks) {
                    if(tank.getTankType() != Fluids.NONE) this.trySubscribe(tank.getTankType(), level, pos);
                }
                for(FluidTank tank : outputTanks) {
                    if(tank.getFill() > 0) this.tryProvide(tank, level, pos);
                }
            }

            double speed = 1D;
            double pow = 1D;

            speed += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3) / 3D;
            speed += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3);

            pow -= Math.min(upgradeManager.getLevel(UpgradeType.POWER), 3) * 0.25D;
            pow += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3);
            pow += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3) * 10D / 3D;

            this.purexModule.update(speed, pow, true, slots.get(1));
            this.didProcess = this.purexModule.didProcess;
            if(this.purexModule.markDirty) this.setChanged();

            this.networkPackNT(100);
        } else {
            this.prevAnim = this.anim;

            if(level.getGameTime() % 20 == 0) {
                this.frame = !level.getBlockState(this.worldPosition.above(5)).isAir();
            }

            if(this.didProcess) this.anim++;

            if(this.didProcess && NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getBottomCenter()) < 25 * 25) {
                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }
                audio.keepAlive();
                audio.updateVolume(this.getVolume(1F));
                audio.updatePitch(0.75F);
            } else if(audio != null) {
                audio.stopSound();
                audio = null;
            }
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.CHEMICAL_PLANT_LOOP.get(), SoundSource.BLOCKS, this, 1F, 15F, 0.75F, 15);
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
                new DirPos(x + 3, y, z - 2, Library.POS_X),
                new DirPos(x + 3, y, z - 1, Library.POS_X),
                new DirPos(x + 3, y, z, Library.POS_X),
                new DirPos(x + 3, y, z + 1, Library.POS_X),
                new DirPos(x + 3, y, z + 2, Library.POS_X),
                new DirPos(x - 3, y, z - 2, Library.NEG_X),
                new DirPos(x - 3, y, z - 1, Library.NEG_X),
                new DirPos(x - 3, y, z, Library.NEG_X),
                new DirPos(x - 3, y, z + 1, Library.NEG_X),
                new DirPos(x - 3, y, z + 2, Library.NEG_X),
                new DirPos(x - 2, y, z + 3, Library.POS_Z),
                new DirPos(x - 1, y, z + 3, Library.POS_Z),
                new DirPos(x, y, z + 3, Library.POS_Z),
                new DirPos(x + 1, y, z + 3, Library.POS_Z),
                new DirPos(x + 2, y, z + 3, Library.POS_Z),
                new DirPos(x - 2, y, z - 3, Library.NEG_Z),
                new DirPos(x - 1, y, z - 3, Library.NEG_Z),
                new DirPos(x, y, z - 3, Library.NEG_Z),
                new DirPos(x + 1, y, z - 3, Library.NEG_Z),
                new DirPos(x + 2, y, z - 3, Library.NEG_Z)
        };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        for(FluidTank tank : inputTanks) tank.serialize(buf);
        for(FluidTank tank : outputTanks) tank.serialize(buf);
        buf.writeLong(power);
        buf.writeLong(maxPower);
        buf.writeBoolean(didProcess);
        this.purexModule.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        for(FluidTank tank : inputTanks) tank.deserialize(buf);
        for(FluidTank tank : outputTanks) tank.deserialize(buf);
        this.power = buf.readLong();
        this.maxPower = buf.readLong();
        this.didProcess = buf.readBoolean();
        this.purexModule.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for(int i = 0; i < inputTanks.length; i++) inputTanks[i].readFromNBT(tag, "i" + i);
        outputTanks[0].readFromNBT(tag, "o0");
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.purexModule.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for(int i = 0; i < inputTanks.length; i++) inputTanks[i].writeToNBT(tag, "i" + i);
        outputTanks[0].writeToNBT(tag, "o0");
        tag.putLong("power", power);
        tag.putLong("maxPower", maxPower);
        this.purexModule.writeToNBT(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true; // battery
        if(slot == 1 && stack.getItem() == NtmItems.BLUEPRINTS.get()) return true;
        if(slot >= 2 && slot <= 3 && stack.getItem() instanceof MachineUpgradeItem) return true; // upgrades
        return this.purexModule.isItemValid(slot, stack); // recipe input crap
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot >= 7 || this.purexModule.isSlotClogged(slot);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return ACCESSIBLE_SLOTS;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTank[] getReceivingTanks() { return inputTanks; }
    @Override public FluidTank[] getSendingTanks() { return outputTanks; }
    @Override public FluidTank[] getAllTanks() { return new FluidTank[] {inputTanks[0], inputTanks[1], inputTanks[2], outputTanks[0]}; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachinePurexMenu(id, inventory, this);
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("index") && tag.contains("selection")) {
            int index = tag.getInt("index");
            String selection = tag.getString("selection");
            if(index == 0) {
                this.purexModule.recipe = selection;
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
}
