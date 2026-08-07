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
import com.hbm.inventory.menus.MachineChemicalPlantMenu;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
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

import java.util.HashMap;
import java.util.List;

public class MachineChemicalPlantBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IControlReceiver {

    public FluidTank[] tanks = new FluidTank[6];

    public long power;
    public long maxPower = 100_000;
    public boolean didProcess = false;

    public boolean frame = false;
    private AudioWrapper audio;

    public ModuleChemicalPlant assemblerModule;

    public float prevSpin;
    public float spin;
    public float spinSpeed = 6F;

    public float prevSlide;
    public float slide;
    private int slideTick = 0;
    private static final float SLIDE_AMPLITUDE = 0.3F;
    private static final float SLIDE_FREQ = 0.15F;

    public UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachineChemicalPlantBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.CHEMICAL_PLANT.get(), pos, blockState, 22);

        for(int i = 0; i < tanks.length; i++) {
            tanks[i] = new FluidTank(Fluids.NONE, 24_000);
        }

        this.assemblerModule = new ModuleChemicalPlant(0, this, slots)
                .itemInput(4)
                .itemOutput(7)
                .fluidInput(tanks[0], tanks[1], tanks[2])
                .fluidOutput(tanks[3], tanks[4], tanks[5]);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_chemical_plant");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(maxPower <= 0) this.maxPower = 1_000_000;

        if(!level.isClientSide) {
            GenericRecipe recipe = ChemicalPlantRecipes.INSTANCE.recipeNameMap.get(assemblerModule.recipe);
            if(recipe != null) {
                this.maxPower = recipe.power * 100;
            }
            this.maxPower = BobMathUtil.max(this.power, this.maxPower, 100_000);

            this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);
            upgradeManager.checkSlots(slots, 2, 3);

            for(int i = 0; i < 3; i++) {
                tanks[i].loadTank(level, 10 + i, 13 + i, slots);
            }
            for(int i = 3; i < 6; i++) {
                tanks[i].unloadTank(level, 16 + (i - 3), 19 + (i - 3), slots);
            }

            for(DirPos pos : getConPos()) {
                this.trySubscribe(level, pos);
                for(int i = 0; i < 6; i++) {
                    if(tanks[i].getTankType() != Fluids.NONE) this.trySubscribe(tanks[i].getTankType(), level, pos);
                }
                for(int i = 3; i < 6; i++) {
                    if(tanks[i].getFill() > 0) this.tryProvide(tanks[i], level, pos);
                }
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

//            if(didProcess) {
//                if(slots[0] != null && slots[0].getItem() == ModItems.meteorite_sword_alloyed)
//                    slots[0] = new ItemStack(ModItems.meteorite_sword_machined);
//            }

            this.networkPackNT(100);
        } else {

            if(level.getGameTime() % 20 == 0) {
                frame = !level.getBlockState(this.worldPosition.above(3)).isAir();
            }

            if(this.didProcess && Math.sqrt(NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getBottomCenter())) < 50) {
                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }
                audio.keepAlive();
                audio.updatePitch(0.75F);
                audio.updateVolume(this.getVolume(0.5F));

            } else {
                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }
            }

            this.prevSpin = this.spin;
            this.prevSlide = this.slide;

            if(didProcess) {
                this.spin += spinSpeed;
                if(this.spin >= 360F) this.spin -= 360F;

                this.slideTick++;
                this.slide = (float) (Math.sin(this.slideTick * SLIDE_FREQ) * SLIDE_AMPLITUDE);
            }
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.CHEMICAL_PLANT_LOOP.get(), SoundSource.BLOCKS, this, 0.5F, 15F, 0.75F, 20);
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
                new DirPos(x + 2, y, z + 0, Library.POS_X),
                new DirPos(x + 2, y, z + 1, Library.POS_X),
                new DirPos(x - 2, y, z - 1, Library.NEG_X),
                new DirPos(x - 2, y, z + 0, Library.NEG_X),
                new DirPos(x - 2, y, z + 1, Library.NEG_X),
                new DirPos(x - 1, y, z + 2, Library.POS_Z),
                new DirPos(x + 0, y, z + 2, Library.POS_Z),
                new DirPos(x + 1, y, z + 2, Library.POS_Z),
                new DirPos(x - 1, y, z - 2, Library.NEG_Z),
                new DirPos(x + 0, y, z - 2, Library.NEG_Z),
                new DirPos(x + 1, y, z - 2, Library.NEG_Z),
        };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        for(FluidTank tank : tanks) tank.serialize(buf);
        buf.writeLong(power);
        buf.writeLong(maxPower);
        buf.writeBoolean(didProcess);
        this.assemblerModule.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        boolean wasProcessing = this.didProcess;
        for(FluidTank tank : tanks) tank.deserialize(buf);
        this.power = buf.readLong();
        this.maxPower = buf.readLong();
        this.didProcess = buf.readBoolean();
        this.assemblerModule.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for(int i = 0; i < tanks.length; i++) tanks[i].readFromNBT(tag, "t" + i);
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.assemblerModule.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for(int i = 0; i < tanks.length; i++) tanks[i].writeToNBT(tag, "t" + i);
        tag.putLong("power", power);
        tag.putLong("maxPower", maxPower);
        this.assemblerModule.writeToNBT(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true; // battery
        if(slot == 1 && stack.getItem() == NtmItems.BLUEPRINTS.get()) return true;
        if(slot >= 2 && slot <= 3 && stack.getItem() instanceof MachineUpgradeItem) return true; // upgrades
        if(slot >= 10 && slot <= 12) return true; // input fluid in
        if(slot >= 16 && slot <= 18) return true; // output fluid out
        if(this.assemblerModule.isItemValid(slot, stack)) return true; // recipe input crap
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {tanks[0], tanks[1], tanks[2]}; }
    @Override public FluidTank[] getSendingTanks() { return new FluidTank[] {tanks[3], tanks[4], tanks[5]}; }
    @Override public FluidTank[] getAllTanks() { return tanks; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineChemicalPlantMenu(id, inventory, this);
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
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        //info.add(IUpgradeInfoProvider.getStandardLabel(ModBlocks.machine_assembly_machine));
        //
        // if(type == UpgradeType.SPEED) {
        //
        //    info.add(EnumChatFormatting.GREEN + I18nUtil.resolveKey(KEY_SPEED, "+" + (level * 100 / 3) + "%"));
        //
        //    info.add(EnumChatFormatting.RED + I18nUtil.resolveKey(KEY_CONSUMPTION, "+" + (level * 50) + "%"));
        //
        // }
        //
        // if(type == UpgradeType.POWER) {
        //
        //    info.add(EnumChatFormatting.GREEN + I18nUtil.resolveKey(KEY_CONSUMPTION, "-" + (level * 25) + "%"));
        //
        // }
        //
        // if(type == UpgradeType.OVERDRIVE) {
        //
        //    info.add((BobMathUtil.getBlink() ? EnumChatFormatting.RED : EnumChatFormatting.DARK_GRAY) + "YES");
        //
        // }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }
}
