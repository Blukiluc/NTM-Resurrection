package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineCompressorMenu;
import com.hbm.inventory.recipes.CompressorRecipes;
import com.hbm.inventory.recipes.CompressorRecipes.CompressorRecipe;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class MachineCompressorBaseBlockEntity extends MachineBaseBlockEntity implements IControlReceiver, IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IFluidCopiable {

    public static final long MAX_POWER = 100_000L;
    public static final int PROCESS_TIME_BASE = 100;
    public static final int POWER_REQUIREMENT_BASE = 2_500;

    public final FluidTank[] tanks;
    public long power;
    public boolean isOn;
    public int progress;
    public int processTime = PROCESS_TIME_BASE;
    public int powerRequirement;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    protected MachineCompressorBaseBlockEntity(BlockEntityType<? extends MachineCompressorBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 4);
        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.NONE, 16_000),
                new FluidTank(Fluids.NONE, 16_000).withPressure(1)
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_compressor");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            if(this.level.getGameTime() % 20 == 0) {
                this.updateConnections();
            }

            this.power = Library.chargeTEFromItems(this.slots, 1, this.power, MAX_POWER);
            this.tanks[0].setType(0, this.slots);
            this.setupTanks();

            this.upgradeManager.checkSlots(this.slots, 2, 3);

            int speedLevel = this.upgradeManager.getLevel(UpgradeType.SPEED);
            int powerLevel = this.upgradeManager.getLevel(UpgradeType.POWER);
            int overLevel = this.upgradeManager.getLevel(UpgradeType.OVERDRIVE);

            CompressorRecipe recipe = CompressorRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType(), this.tanks[0].getPressure());
            int timeBase = recipe != null ? recipe.duration : PROCESS_TIME_BASE;

            if(recipe == null) {
                this.processTime = speedLevel == 3 ? 10 : speedLevel == 2 ? 20 : speedLevel == 1 ? 60 : timeBase;
            } else {
                this.processTime = timeBase / (speedLevel + 1);
            }

            this.powerRequirement = POWER_REQUIREMENT_BASE / (powerLevel + 1);
            this.processTime /= overLevel + 1;
            this.powerRequirement *= overLevel * 2 + 1;

            if(this.processTime <= 0) this.processTime = 1;

            if(this.canProcess()) {
                this.progress++;
                this.isOn = true;
                this.power -= this.powerRequirement;

                if(this.progress >= this.processTime) {
                    this.progress = 0;
                    this.process();
                    this.setChanged();
                }
            } else {
                this.progress = 0;
                this.isOn = false;
            }

            if(this.tanks[1].getFill() > 0) {
                for(DirPos pos : this.getConPos()) {
                    this.tryProvide(this.tanks[1], this.level, pos);
                }
            }

            this.networkPackNT(100);
        }
    }

    protected void updateConnections() {
        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(this.level, pos);
            this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
        }
    }

    public abstract DirPos[] getConPos();

    public boolean canProcess() {
        if(this.power <= this.powerRequirement) return false;

        CompressorRecipe recipe = CompressorRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType(), this.tanks[0].getPressure());

        if(recipe == null) {
            return this.tanks[0].getFill() >= 1_000 && this.tanks[1].getFill() + 1_000 <= this.tanks[1].getMaxFill();
        }

        return this.tanks[0].getFill() >= recipe.inputAmount && this.tanks[1].getFill() + recipe.output.fill <= this.tanks[1].getMaxFill();
    }

    protected void process() {
        CompressorRecipe recipe = CompressorRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType(), this.tanks[0].getPressure());

        if(recipe == null) {
            this.tanks[0].setFill(this.tanks[0].getFill() - 1_000);
            this.tanks[1].setFill(this.tanks[1].getFill() + 1_000);
        } else {
            this.tanks[0].setFill(this.tanks[0].getFill() - recipe.inputAmount);
            this.tanks[1].setFill(this.tanks[1].getFill() + recipe.output.fill);
        }
    }

    protected void setupTanks() {
        CompressorRecipe recipe = CompressorRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType(), this.tanks[0].getPressure());

        if(recipe == null) {
            this.tanks[1].withPressure(this.tanks[0].getPressure() + 1).setTankType(this.tanks[0].getTankType());
        } else {
            this.tanks[1].withPressure(recipe.output.pressure).setTankType(recipe.output.type);
        }
    }

    private void setCompression(int compression) {
        if(compression == this.tanks[0].getPressure()) return;

        this.tanks[0].withPressure(compression);
        CompressorRecipe recipe = CompressorRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType(), compression);

        if(recipe == null) {
            this.tanks[1].withPressure(compression + 1);
        } else {
            this.tanks[1].withPressure(recipe.output.pressure).setTankType(recipe.output.type);
        }

        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return stack.getItem() instanceof IItemFluidIdentifier;
        if(slot == 1) return stack.getItem() instanceof IBatteryItem;
        return (slot == 2 || slot == 3) && stack.getItem() instanceof MachineUpgradeItem;
    }

    @Override
    public int[] getSlotsForFace(net.minecraft.core.Direction direction) {
        return new int[] { 0, 1, 2, 3 };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.progress = tag.getInt("progress");
        this.tanks[0].readFromNBT(tag, "0");
        this.tanks[1].readFromNBT(tag, "1");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("progress", this.progress);
        this.tanks[0].writeToNBT(tag, "0");
        this.tanks[1].writeToNBT(tag, "1");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.progress);
        buf.writeInt(this.processTime);
        buf.writeInt(this.powerRequirement);
        buf.writeLong(this.power);
        this.tanks[0].serialize(buf);
        this.tanks[1].serialize(buf);
        buf.writeBoolean(this.isOn);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.progress = buf.readInt();
        this.processTime = buf.readInt();
        this.powerRequirement = buf.readInt();
        this.power = buf.readLong();
        this.tanks[0].deserialize(buf);
        this.tanks[1].deserialize(buf);
        this.isOn = buf.readBoolean();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineCompressorMenu(id, inventory, this);
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("compression")) this.setCompression(tag.getInt("compression"));
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
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.tanks[1] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0] };
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_COMPRESSOR.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(ChatFormatting.GREEN + "Generic compression: " + I18nUtil.resolveKey(this.KEY_DELAY, "-" + (level == 3 ? 90 : level == 2 ? 80 : level == 1 ? 40 : 0) + "%"));
            info.add(ChatFormatting.GREEN + "Recipe: " + I18nUtil.resolveKey(this.KEY_DELAY, "-" + (100 - 100 / (level + 1)) + "%"));
        }
        if(type == UpgradeType.POWER) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(this.KEY_CONSUMPTION, "-" + (100 - 100 / (level + 1)) + "%"));
        }
        if(type == UpgradeType.OVERDRIVE) {
            info.add((BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_GRAY) + "YES");
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 9);
        return upgrades;
    }

    @Override
    public CompoundTag getSettings(Level level, BlockPos pos) {
        CompoundTag tag = IFluidCopiable.super.getSettings(level, pos);
        tag.putInt("compression", this.tanks[0].getPressure());
        return tag;
    }

    @Override
    public void pasteSettings(CompoundTag tag, int index, Level level, Player player, BlockPos pos) {
        if(tag.contains("compression")) this.setCompression(tag.getInt("compression"));
        IFluidCopiable.super.pasteSettings(tag, index, level, player, pos);
    }
}
