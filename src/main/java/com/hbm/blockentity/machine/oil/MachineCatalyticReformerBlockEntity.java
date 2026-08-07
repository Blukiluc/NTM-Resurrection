package com.hbm.blockentity.machine.oil;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineCatalyticReformerMenu;
import com.hbm.inventory.recipes.CatalyticReformerRecipes;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.Tuple;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.lib.Library;
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

public class MachineCatalyticReformerBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IPersistentNBT {

    public static final long MAX_POWER = 1_000_000L;
    public static final long REFORM_POWER_COST = 20_000L;
    public static final int REFORM_FLUID_COST = 100;

    public long power;
    public final FluidTank[] tanks;

    // Slots: 0=battery, 1=fluid ID, 2=catalytic converter,
    //        3=input full, 4=input empty,
    //        5=output1 full, 6=output2 full, 7=output3 full,
    //        8=output1 empty, 9=output2 empty, 10=output3 empty
    public MachineCatalyticReformerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CATALYTIC_REFORMER.get(), pos, state, 11);

        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.NAPHTHA, 64_000),
                new FluidTank(Fluids.REFORMATE, 24_000),
                new FluidTank(Fluids.PETROLEUM, 24_000),
                new FluidTank(Fluids.HYDROGEN, 24_000)
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_catalytic_reformer");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(this.level.getGameTime() % 20 == 0) {
                for(DirPos pos : this.getConPos()) {
                    this.trySubscribe(this.level, pos);
                    this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
                }
            }

            this.power = Library.chargeTEFromItems(this.slots, 0, this.power, MAX_POWER);
            this.tanks[0].setType(1, this.slots);
            this.tanks[0].loadTank(this.level, 3, 4, this.slots);

            this.reform();

            this.tanks[1].unloadTank(this.level, 5, 8, this.slots);
            this.tanks[2].unloadTank(this.level, 6, 9, this.slots);
            this.tanks[3].unloadTank(this.level, 7, 10, this.slots);

            for(DirPos pos : this.getConPos()) {
                for(int i = 1; i < this.tanks.length; i++) {
                    if(this.tanks[i].getFill() > 0) {
                        this.tryProvide(this.tanks[i], this.level, pos);
                    }
                }
            }

            this.networkPackNT(150);
        }
    }

    private void reform() {
        Tuple.Triplet<FluidStack, FluidStack, FluidStack> out = CatalyticReformerRecipes.INSTANCE.getOutput(this.tanks[0].getTankType());

        if(out == null) {
            this.tanks[1].setTankType(Fluids.NONE);
            this.tanks[2].setTankType(Fluids.NONE);
            this.tanks[3].setTankType(Fluids.NONE);
            return;
        }

        this.tanks[1].setTankType(out.getX().type);
        this.tanks[2].setTankType(out.getY().type);
        this.tanks[3].setTankType(out.getZ().type);

        if(this.power < REFORM_POWER_COST) return;
        if(this.tanks[0].getFill() < REFORM_FLUID_COST) return;

        ItemStack catalyst = this.getItem(2);
        if(catalyst.isEmpty() || !catalyst.is(NtmItems.CATALYTIC_CONVERTER.get())) return;

        if(this.tanks[1].getFill() + out.getX().fill > this.tanks[1].getMaxFill()) return;
        if(this.tanks[2].getFill() + out.getY().fill > this.tanks[2].getMaxFill()) return;
        if(this.tanks[3].getFill() + out.getZ().fill > this.tanks[3].getMaxFill()) return;

        this.tanks[0].setFill(this.tanks[0].getFill() - REFORM_FLUID_COST);
        this.tanks[1].setFill(this.tanks[1].getFill() + out.getX().fill);
        this.tanks[2].setFill(this.tanks[2].getFill() + out.getY().fill);
        this.tanks[3].setFill(this.tanks[3].getFill() + out.getZ().fill);

        this.power -= REFORM_POWER_COST;
    }

    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise(Direction.Axis.Y);

        return new DirPos[] {
                new DirPos(pos.getX() + dir.getStepX() * 2 + rot.getStepX(), pos.getY(), pos.getZ() + dir.getStepZ() * 2 + rot.getStepZ(), dir),
                new DirPos(pos.getX() + dir.getStepX() * 2 - rot.getStepX(), pos.getY(), pos.getZ() + dir.getStepZ() * 2 - rot.getStepZ(), dir),
                new DirPos(pos.getX() - dir.getStepX() * 2 + rot.getStepX(), pos.getY(), pos.getZ() - dir.getStepZ() * 2 + rot.getStepZ(), dir.getOpposite()),
                new DirPos(pos.getX() - dir.getStepX() * 2 - rot.getStepX(), pos.getY(), pos.getZ() - dir.getStepZ() * 2 - rot.getStepZ(), dir.getOpposite()),
                new DirPos(pos.getX() + rot.getStepX() * 3, pos.getY(), pos.getZ() + rot.getStepZ() * 3, rot),
                new DirPos(pos.getX() - rot.getStepX() * 3, pos.getY(), pos.getZ() - rot.getStepZ() * 3, rot.getOpposite())
        };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) {
            return stack.getItem() instanceof api.hbm.energymk2.IBatteryItem;
        }

        if(slot == 1) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        }

        if(slot == 2) {
            return stack.is(NtmItems.CATALYTIC_CONVERTER.get());
        }

        if(slot == 3) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[0].getTankType()).isEmpty();
        }

        if(slot == 5) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[1].getTankType()).isEmpty();
        }

        if(slot == 6) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[2].getTankType()).isEmpty();
        }

        if(slot == 7) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[3].getTankType()).isEmpty();
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 4 || index == 8 || index == 9 || index == 10;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "o1");
        this.tanks[2].readFromNBT(tag, "o2");
        this.tanks[3].readFromNBT(tag, "o3");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "o1");
        this.tanks[2].writeToNBT(tag, "o2");
        this.tanks[3].writeToNBT(tag, "o3");
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        if(this.tanks[0].getFill() == 0 && this.tanks[1].getFill() == 0 && this.tanks[2].getFill() == 0 && this.tanks[3].getFill() == 0) return;

        CompoundTag tag = new CompoundTag();
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].writeToNBT(tag, "" + i);
        }
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].readFromNBT(tag, "" + i);
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        for(FluidTank tank : this.tanks) {
            tank.serialize(buf);
        }
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        for(FluidTank tank : this.tanks) {
            tank.deserialize(buf);
        }
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
        return dir != null && dir != Direction.DOWN;
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && dir != Direction.DOWN;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.tanks[1], this.tanks[2], this.tanks[3] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineCatalyticReformerMenu(id, inventory, this);
    }
}