package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineMixerMenu;
import com.hbm.inventory.recipes.MixerRecipes;
import com.hbm.inventory.recipes.MixerRecipes.MixerRecipe;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;

public class MachineMixerBlockEntity extends MachineBaseBlockEntity implements IControlReceiver, IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IFluidCopiable {

    public static final long MAX_POWER = 10_000L;

    public long power;
    public int progress;
    public int processTime;
    public int recipeIndex;

    public float rotation;
    public float prevRotation;
    public boolean wasOn;

    private int consumption = 50;

    public final FluidTank[] tanks;
    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachineMixerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MIXER.get(), pos, state, 5);
        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.NONE, 16_000),
                new FluidTank(Fluids.NONE, 16_000),
                new FluidTank(Fluids.NONE, 24_000)
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_mixer");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            this.power = Library.chargeTEFromItems(this.slots, 0, this.power, MAX_POWER);
            this.tanks[2].setType(2, this.slots);

            this.upgradeManager.checkSlots(this.slots, 3, 4);
            int speedLevel = this.upgradeManager.getLevel(UpgradeType.SPEED);
            int powerLevel = this.upgradeManager.getLevel(UpgradeType.POWER);
            int overLevel = this.upgradeManager.getLevel(UpgradeType.OVERDRIVE);

            int baseConsumption = 50 + speedLevel * 150;
            baseConsumption = (int) (baseConsumption * (1D - powerLevel * 0.25D));
            this.consumption = Math.max(1, baseConsumption * (overLevel * 3 + 1));

            for(DirPos pos : this.getConPos()) {
                this.trySubscribe(this.level, pos);
                if(this.tanks[0].getTankType() != Fluids.NONE) this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
                if(this.tanks[1].getTankType() != Fluids.NONE) this.trySubscribe(this.tanks[1].getTankType(), this.level, pos);
            }

            this.wasOn = this.canProcess();

            if(this.wasOn) {
                this.progress++;
                this.power -= this.getConsumption();

                this.processTime -= this.processTime * speedLevel / 4;
                this.processTime /= overLevel + 1;
                if(this.processTime <= 0) this.processTime = 1;

                if(this.progress >= this.processTime) {
                    this.process();
                    this.progress = 0;
                }
            } else {
                this.progress = 0;
            }

            for(DirPos pos : this.getConPos()) {
                if(this.tanks[2].getFill() > 0) this.tryProvide(this.tanks[2], this.level, pos);
            }

            this.setChanged();
            this.networkPackNT(50);
        } else {
            this.prevRotation = this.rotation;
            if(this.wasOn) this.rotation += 20F;

            if(this.rotation >= 360F) {
                this.rotation -= 360F;
                this.prevRotation -= 360F;
            }
        }
    }

    public boolean canProcess() {
        MixerRecipe[] recipes = MixerRecipes.getOutput(this.tanks[2].getTankType());
        if(recipes == null || recipes.length == 0) {
            this.recipeIndex = 0;
            return false;
        }

        this.recipeIndex = Math.floorMod(this.recipeIndex, recipes.length);
        MixerRecipe recipe = recipes[this.recipeIndex];
        if(recipe == null) {
            this.recipeIndex = 0;
            return false;
        }

        this.tanks[0].setTankType(recipe.input1 != null ? recipe.input1.type : Fluids.NONE);
        this.tanks[1].setTankType(recipe.input2 != null ? recipe.input2.type : Fluids.NONE);

        if(recipe.input1 != null && this.tanks[0].getFill() < recipe.input1.fill) return false;
        if(recipe.input2 != null && this.tanks[1].getFill() < recipe.input2.fill) return false;
        if(this.power < this.getConsumption()) return false;
        if(recipe.output + this.tanks[2].getFill() > this.tanks[2].getMaxFill()) return false;

        if(recipe.solidInput != null) {
            ItemStack input = this.slots.get(1);
            if(input.isEmpty()) return false;
            if(!recipe.solidInput.matchesRecipe(input, true) || recipe.solidInput.stacksize > input.getCount()) return false;
        }

        this.processTime = recipe.processTime;
        return true;
    }

    protected void process() {
        MixerRecipe recipe = MixerRecipes.getOutput(this.tanks[2].getTankType(), this.recipeIndex);
        if(recipe == null) return;

        if(recipe.input1 != null) this.tanks[0].setFill(this.tanks[0].getFill() - recipe.input1.fill);
        if(recipe.input2 != null) this.tanks[1].setFill(this.tanks[1].getFill() - recipe.input2.fill);
        if(recipe.solidInput != null) this.slots.get(1).shrink(recipe.solidInput.stacksize);
        this.tanks[2].setFill(this.tanks[2].getFill() + recipe.output);
    }

    public int getConsumption() {
        return this.consumption;
    }

    protected DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.below(), Direction.DOWN),
                new DirPos(pos.east(), Direction.EAST),
                new DirPos(pos.west(), Direction.WEST),
                new DirPos(pos.south(), Direction.SOUTH),
                new DirPos(pos.north(), Direction.NORTH)
        };
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 1 };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return stack.getItem() instanceof IBatteryItem;
        if(slot == 2) return stack.getItem() instanceof IItemFluidIdentifier;
        if(slot == 3 || slot == 4) return stack.getItem() instanceof MachineUpgradeItem;

        if(slot == 1) {
            MixerRecipe recipe = MixerRecipes.getOutput(this.tanks[2].getTankType(), this.recipeIndex);
            return recipe != null && recipe.solidInput != null && recipe.solidInput.matchesRecipe(stack, true);
        }

        return false;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.progress = tag.getInt("progress");
        this.processTime = tag.getInt("processTime");
        this.recipeIndex = tag.getInt("recipe");
        this.wasOn = tag.getBoolean("wasOn");
        for(int i = 0; i < this.tanks.length; i++) this.tanks[i].readFromNBT(tag, "tank" + i);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("progress", this.progress);
        tag.putInt("processTime", this.processTime);
        tag.putInt("recipe", this.recipeIndex);
        tag.putBoolean("wasOn", this.wasOn);
        for(int i = 0; i < this.tanks.length; i++) this.tanks[i].writeToNBT(tag, "tank" + i);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.processTime);
        buf.writeInt(this.progress);
        buf.writeInt(this.recipeIndex);
        buf.writeBoolean(this.wasOn);
        for(FluidTank tank : this.tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.processTime = buf.readInt();
        this.progress = buf.readInt();
        this.recipeIndex = buf.readInt();
        this.wasOn = buf.readBoolean();
        for(FluidTank tank : this.tanks) tank.deserialize(buf);
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
        return new FluidTank[] { this.tanks[2] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0], this.tanks[1] };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineMixerMenu(id, inventory, this);
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("toggle")) {
            this.recipeIndex++;
            this.progress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_MIXER.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(this.KEY_DELAY, "-" + level * 25 + "%"));
            info.add(ChatFormatting.RED + I18nUtil.resolveKey(this.KEY_CONSUMPTION, "+" + level * 300 + "%"));
        }
        if(type == UpgradeType.POWER) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(this.KEY_CONSUMPTION, "-" + level * 25 + "%"));
        }
        if(type == UpgradeType.OVERDRIVE) {
            info.add((BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_GRAY) + "YES");
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 6);
        return upgrades;
    }

    @Override
    public FluidTank getTankToPaste() {
        return this.tanks[2];
    }
}
