package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.ElectrolyserFluidRecipes;
import com.hbm.inventory.recipes.ElectrolyserFluidRecipes.ElectrolysisRecipe;
import com.hbm.inventory.recipes.ElectrolyserMetalRecipes;
import com.hbm.inventory.recipes.ElectrolyserMetalRecipes.ElectrolysisMetalRecipe;
import com.hbm.inventory.menus.MachineElectrolyserFluidMenu;
import com.hbm.inventory.menus.MachineElectrolyserMetalMenu;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MachineElectrolyserBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IPersistentNBT, IFluidCopiable, IControlReceiver {

    public static final long MAX_POWER = 20_000_000L;
    public static final int USAGE_ORE_BASE = 10_000;
    public static final int USAGE_FLUID_BASE = 10_000;

    // NOTE: assumes MaterialShapes/Mats kept the same API as in the 1.7.10 port.
    // Please confirm against the actual Mats.java/MaterialShapes.java of this repo.
    public static final int MAX_MATERIAL = MaterialShapes.BLOCK.q(16);

    public long power;
    public int usageOre;
    public int usageFluid;

    public int progressFluid;
    public int processFluidTime = 100;
    public int progressOre;
    public int processOreTime = 600;

    public MaterialStack leftStack;
    public MaterialStack rightStack;

    public final FluidTank[] tanks;

    /** 0 = fluid mode GUI, 1 = metal mode GUI. Switched via the toggle button in the screen. */
    private int lastSelectedGUI = 0;

    // TODO: wire the upgrade system once UpgradeManagerNT is ready for this machine
    // public UpgradeManagerNT upgradeManager = new UpgradeManagerNT();

    public MachineElectrolyserBlockEntity(BlockPos pos, BlockState state) {
        //0: Battery
        //1-2: Upgrades (not wired yet)
        //// FLUID
        //3: Fluid ID input
        //4: Fluid ID output
        //5-6: Water in/out
        //7-8: Hydrogen out (canister)
        //9-10: Oxygen out (canister)
        //11-13: Byproducts
        //// METAL
        //14: Crystal input
        //15-20: Metal outputs
        super(NtmBlockEntityTypes.ELECTROLYSER.get(), pos, state, 21);

        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.WATER, 16_000),
                new FluidTank(Fluids.HYDROGEN, 16_000),
                new FluidTank(Fluids.OXYGEN, 16_000),
                new FluidTank(Fluids.NITRIC_ACID, 16_000)
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_electrolyser");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(this.level.getGameTime() % 20 == 0) {
                for(DirPos pos : this.getConPos()) {
                    this.trySubscribe(this.level, pos);
                    this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
                    this.trySubscribe(this.tanks[3].getTankType(), this.level, pos);

                    if(this.tanks[1].getFill() > 0) this.tryProvide(this.tanks[1], this.level, pos);
                    if(this.tanks[2].getFill() > 0) this.tryProvide(this.tanks[2], this.level, pos);
                }
            }

            this.power = Library.chargeTEFromItems(this.slots, 0, this.power, MAX_POWER);
            this.tanks[0].setType(3, 4, this.slots);
            this.tanks[0].loadTank(this.level, 5, 6, this.slots);
            this.tanks[1].unloadTank(this.level, 7, 8, this.slots);
            this.tanks[2].unloadTank(this.level, 9, 10, this.slots);

            // TODO: re-apply upgrade modifiers once UpgradeManagerNT is wired for this machine
            // upgradeManager.checkSlots(this, slots, 1, 2);
            // int speedLevel = upgradeManager.getLevel(UpgradeType.SPEED);
            // int powerLevel = upgradeManager.getLevel(UpgradeType.POWER);
            // usageOre = USAGE_ORE_BASE - USAGE_ORE_BASE * powerLevel / 4 + USAGE_ORE_BASE * speedLevel;
            // usageFluid = USAGE_FLUID_BASE - USAGE_FLUID_BASE * powerLevel / 4 + USAGE_FLUID_BASE * speedLevel;
            this.usageOre = USAGE_ORE_BASE;
            this.usageFluid = USAGE_FLUID_BASE;

            boolean changed = false;

            for(int i = 0; i < this.getCycleCount(); i++) {
                if(this.canProcessFluid()) {
                    this.progressFluid++;
                    this.power -= this.usageFluid;

                    if(this.progressFluid >= this.getDurationFluid()) {
                        this.processFluids();
                        this.progressFluid = 0;
                        changed = true;
                    }
                }

                if(this.canProcessMetal()) {
                    this.progressOre++;
                    this.power -= this.usageOre;

                    if(this.progressOre >= this.getDurationMetal()) {
                        this.processMetal();
                        this.progressOre = 0;
                        changed = true;
                    }
                }
            }

            // TODO: the 1.7.10 version poured leftStack/rightStack into the world via CrucibleUtil
            // here, with particle effects (see the old updateEntity for reference). For now the
            // molten stacks just accumulate up to maxMaterial and processing pauses once full.
            // Re-implement once CrucibleUtil / the particle system is confirmed for 1.21.1.

            if(changed) {
                this.setChanged();
            }

            this.networkPackNT(50);
        }
    }

    private boolean canProcessFluid() {
        if(this.power < this.usageFluid) return false;

        ElectrolysisRecipe recipe = ElectrolyserFluidRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType());
        if(recipe == null) return false;

        this.tanks[1].setTankType(recipe.output1.type);
        this.tanks[2].setTankType(recipe.output2.type);

        if(recipe.amount > this.tanks[0].getFill()) return false;
        if(recipe.output1.fill + this.tanks[1].getFill() > this.tanks[1].getMaxFill()) return false;
        if(recipe.output2.fill + this.tanks[2].getFill() > this.tanks[2].getMaxFill()) return false;

        if(recipe.byproduct != null) {
            for(int i = 0; i < recipe.byproduct.length; i++) {
                ItemStack slot = this.getItem(11 + i);
                ItemStack byproduct = recipe.byproduct[i];

                if(slot.isEmpty()) continue;
                if(!ItemStack.isSameItemSameComponents(slot, byproduct)) return false;
                if(slot.getCount() + byproduct.getCount() > slot.getMaxStackSize()) return false;
            }
        }

        return true;
    }

    private void processFluids() {
        ElectrolysisRecipe recipe = ElectrolyserFluidRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType());
        if(recipe == null) return;

        this.tanks[0].setFill(this.tanks[0].getFill() - recipe.amount);
        this.tanks[1].setTankType(recipe.output1.type);
        this.tanks[2].setTankType(recipe.output2.type);
        this.tanks[1].setFill(this.tanks[1].getFill() + recipe.output1.fill);
        this.tanks[2].setFill(this.tanks[2].getFill() + recipe.output2.fill);

        if(recipe.byproduct != null) {
            for(int i = 0; i < recipe.byproduct.length; i++) {
                ItemStack slot = this.getItem(11 + i);
                ItemStack byproduct = recipe.byproduct[i];

                if(slot.isEmpty()) {
                    this.setItem(11 + i, byproduct.copy());
                } else {
                    ItemStack stack = slot.copy();
                    stack.grow(byproduct.getCount());
                    this.setItem(11 + i, stack);
                }
            }
        }
    }

    private boolean canProcessMetal() {
        ItemStack crystal = this.getItem(14);
        if(crystal.isEmpty()) return false;
        if(this.power < this.usageOre) return false;
        if(this.tanks[3].getFill() < 100) return false;

        ElectrolysisMetalRecipe recipe = ElectrolyserMetalRecipes.INSTANCE.getRecipe(crystal);
        if(recipe == null) return false;

        if(this.leftStack != null && recipe.output1 != null) {
            if(recipe.output1.material != this.leftStack.material) return false;
            if(recipe.output1.amount + this.leftStack.amount > MAX_MATERIAL) return false;
        }

        if(this.rightStack != null && recipe.output2 != null) {
            if(recipe.output2.material != this.rightStack.material) return false;
            if(recipe.output2.amount + this.rightStack.amount > MAX_MATERIAL) return false;
        }

        if(recipe.byproduct != null) {
            for(int i = 0; i < recipe.byproduct.length; i++) {
                ItemStack slot = this.getItem(15 + i);
                ItemStack byproduct = recipe.byproduct[i];

                if(slot.isEmpty()) continue;
                if(!ItemStack.isSameItemSameComponents(slot, byproduct)) return false;
                if(slot.getCount() + byproduct.getCount() > slot.getMaxStackSize()) return false;
            }
        }

        return true;
    }

    private void processMetal() {
        ItemStack crystal = this.getItem(14);
        ElectrolysisMetalRecipe recipe = ElectrolyserMetalRecipes.INSTANCE.getRecipe(crystal);
        if(recipe == null) return;

        // NOTE: MaterialStack.amount is final in this port, so accumulating means rebuilding
        // the stack rather than mutating it in place (unlike the 1.7.10 version).
        if(recipe.output1 != null) {
            if(this.leftStack == null) {
                this.leftStack = new MaterialStack(recipe.output1.material, recipe.output1.amount);
            } else {
                this.leftStack = new MaterialStack(this.leftStack.material, this.leftStack.amount + recipe.output1.amount);
            }
        }

        if(recipe.output2 != null) {
            if(this.rightStack == null) {
                this.rightStack = new MaterialStack(recipe.output2.material, recipe.output2.amount);
            } else {
                this.rightStack = new MaterialStack(this.rightStack.material, this.rightStack.amount + recipe.output2.amount);
            }
        }

        if(recipe.byproduct != null) {
            for(int i = 0; i < recipe.byproduct.length; i++) {
                ItemStack slot = this.getItem(15 + i);
                ItemStack byproduct = recipe.byproduct[i];

                if(slot.isEmpty()) {
                    this.setItem(15 + i, byproduct.copy());
                } else {
                    ItemStack stack = slot.copy();
                    stack.grow(byproduct.getCount());
                    this.setItem(15 + i, stack);
                }
            }
        }

        this.tanks[3].setFill(this.tanks[3].getFill() - 100);
        this.removeItem(14, 1);
    }

    public int getDurationFluid() {
        ElectrolysisRecipe recipe = ElectrolyserFluidRecipes.INSTANCE.getRecipe(this.tanks[0].getTankType());
        return recipe != null ? recipe.duration : this.processFluidTime;
        // TODO: re-apply SPEED/POWER upgrade scaling once wired
    }

    public int getDurationMetal() {
        ElectrolysisMetalRecipe recipe = ElectrolyserMetalRecipes.INSTANCE.getRecipe(this.getItem(14));
        return recipe != null ? recipe.duration : this.processOreTime;
        // TODO: re-apply SPEED/POWER upgrade scaling once wired
    }

    public int getCycleCount() {
        return 1;
        // TODO: re-apply OVERDRIVE upgrade scaling once wired
    }

    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise(Direction.Axis.Y);

        return new DirPos[] {
                new DirPos(pos.getX() - dir.getStepX() * 6, pos.getY(), pos.getZ() - dir.getStepZ() * 6, dir.getOpposite()),
                new DirPos(pos.getX() - dir.getStepX() * 6 + rot.getStepX(), pos.getY(), pos.getZ() - dir.getStepZ() * 6 + rot.getStepZ(), dir.getOpposite()),
                new DirPos(pos.getX() - dir.getStepX() * 6 - rot.getStepX(), pos.getY(), pos.getZ() - dir.getStepZ() * 6 - rot.getStepZ(), dir.getOpposite()),
                new DirPos(pos.getX() + dir.getStepX() * 6, pos.getY(), pos.getZ() + dir.getStepZ() * 6, dir),
                new DirPos(pos.getX() + dir.getStepX() * 6 + rot.getStepX(), pos.getY(), pos.getZ() + dir.getStepZ() * 6 + rot.getStepZ(), dir),
                new DirPos(pos.getX() + dir.getStepX() * 6 - rot.getStepX(), pos.getY(), pos.getZ() + dir.getStepZ() * 6 - rot.getStepZ(), dir)
        };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) {
            return stack.getItem() instanceof IBatteryItem;
        }

        // TODO: re-enable once the upgrade system is wired for this machine
        // if(slot == 1 || slot == 2) {
        //     return stack.getItem() instanceof MachineUpgradeItem;
        // }

        if(slot == 3) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        }

        if(slot == 5) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[0].getTankType()).isEmpty();
        }

        if(slot == 7) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[1].getTankType()).isEmpty();
        }

        if(slot == 9) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[2].getTankType()).isEmpty();
        }

        if(slot == 14) {
            return ElectrolyserMetalRecipes.INSTANCE.getRecipe(stack) != null;
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == 4 || slot == 6 || slot == 8 || slot == 10
                || slot == 11 || slot == 12 || slot == 13
                || (slot >= 15 && slot <= 20);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.progressFluid = tag.getInt("progressFluid");
        this.progressOre = tag.getInt("progressOre");
        this.lastSelectedGUI = tag.getInt("lastSelectedGUI");

        if(tag.contains("leftType")) {
            this.leftStack = new MaterialStack(Mats.matById.get(tag.getInt("leftType")), tag.getInt("leftAmount"));
        } else {
            this.leftStack = null;
        }

        if(tag.contains("rightType")) {
            this.rightStack = new MaterialStack(Mats.matById.get(tag.getInt("rightType")), tag.getInt("rightAmount"));
        } else {
            this.rightStack = null;
        }

        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].readFromNBT(tag, "t" + i);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("progressFluid", this.progressFluid);
        tag.putInt("progressOre", this.progressOre);
        tag.putInt("lastSelectedGUI", this.lastSelectedGUI);

        if(this.leftStack != null) {
            tag.putInt("leftType", this.leftStack.material.id);
            tag.putInt("leftAmount", this.leftStack.amount);
        }

        if(this.rightStack != null) {
            tag.putInt("rightType", this.rightStack.material.id);
            tag.putInt("rightAmount", this.rightStack.amount);
        }

        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].writeToNBT(tag, "t" + i);
        }
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("power", this.power);
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].writeToNBT(tag, "t" + i);
        }
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        this.power = tag.getLong("power");
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].readFromNBT(tag, "t" + i);
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.progressFluid);
        buf.writeInt(this.progressOre);
        buf.writeInt(this.usageOre);
        buf.writeInt(this.usageFluid);
        buf.writeInt(this.lastSelectedGUI);
        for(FluidTank tank : this.tanks) {
            tank.serialize(buf);
        }
        buf.writeBoolean(this.leftStack != null);
        buf.writeBoolean(this.rightStack != null);
        if(this.leftStack != null) {
            buf.writeInt(this.leftStack.material.id);
            buf.writeInt(this.leftStack.amount);
        }
        if(this.rightStack != null) {
            buf.writeInt(this.rightStack.material.id);
            buf.writeInt(this.rightStack.amount);
        }
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.progressFluid = buf.readInt();
        this.progressOre = buf.readInt();
        this.usageOre = buf.readInt();
        this.usageFluid = buf.readInt();
        this.lastSelectedGUI = buf.readInt();
        for(FluidTank tank : this.tanks) {
            tank.deserialize(buf);
        }
        boolean left = buf.readBoolean();
        boolean right = buf.readBoolean();
        this.leftStack = left ? new MaterialStack(Mats.matById.get(buf.readInt()), buf.readInt()) : null;
        this.rightStack = right ? new MaterialStack(Mats.matById.get(buf.readInt()), buf.readInt()) : null;
    }

    @Override
    public long getPower() {
        return Math.max(Math.min(this.power, MAX_POWER), 0);
    }

    @Override
    public void setPower(long i) {
        this.power = i;
    }

    @Override
    public long getMaxPower() {
        return MAX_POWER;
    }

    @Override
    public long transferPower(long power) {
        if(power + this.getPower() <= this.getMaxPower()) {
            this.setPower(power + this.getPower());
            return 0;
        }

        long capacity = this.getMaxPower() - this.getPower();
        long overshoot = power - capacity;
        this.setPower(this.getMaxPower());
        return overshoot;
    }

    @Override
    public boolean canConnect(Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.tanks[1], this.tanks[2] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0], this.tanks[3] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if(this.lastSelectedGUI == 1) {
            return new MachineElectrolyserMetalMenu(id, inventory, this);
        }
        return new MachineElectrolyserFluidMenu(id, inventory, this);
    }

    /*
     * TOGGLE SYSTEM (IControlReceiver)
     * The screens send a CompoundTagControl packet with either "sgf" (switch to fluid) or
     * "sgm" (switch to metal) set. Receiving it here flips lastSelectedGUI and immediately
     * reopens the menu, which picks the right one via createMenu() above - same pattern as
     * DummyableBlock.standardOpenBehavior.
     */

    @Override
    public void receiveControl(CompoundTag data) { }

    @Override
    public void receiveControl(Player player, CompoundTag data) {
        if(data.contains("sgf")) this.lastSelectedGUI = 0;
        if(data.contains("sgm")) this.lastSelectedGUI = 1;

        player.openMenu(new SimpleMenuProvider(this, this.getDisplayName()), this.getBlockPos());
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }
}