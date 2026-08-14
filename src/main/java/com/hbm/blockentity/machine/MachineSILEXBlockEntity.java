package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineSILEXMenu;
import com.hbm.inventory.recipes.SILEXRecipes;
import com.hbm.inventory.recipes.SILEXRecipes.SILEXRecipe;
import com.hbm.inventory.recipes.SILEXRecipes.WeightedOutput;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFELCrystal.EnumWavelengths;
import com.hbm.util.WeightedRandom;
import com.hbm.util.fauxpointtwelve.DirPos;
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

public class MachineSILEXBlockEntity extends MachineBaseBlockEntity implements IFluidStandardReceiverMK2, IFluidCopiable, IControlReceiver {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FLUID_ID = 1;
    public static final int SLOT_FLUID_INPUT = 2;
    public static final int SLOT_FLUID_OUTPUT = 3;
    public static final int SLOT_OUTPUT = 4;
    public static final int SLOT_QUEUE_START = 5;
    public static final int SLOT_QUEUE_END = 11;

    public static final int MAX_FILL = 16_000;
    public static final int PROCESS_TIME = 100;
    public static final int PRIME = 137;

    public EnumWavelengths mode = EnumWavelengths.NULL;
    public final FluidTank tank = new FluidTank(Fluids.PEROXIDE, 16_000);
    public ItemStack current = ItemStack.EMPTY;
    public int currentFill;
    public int progress;
    public int recipeIndex;

    private int loadDelay;

    public MachineSILEXBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.SILEX.get(), pos, blockState, 11);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machineSILEX");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        this.tank.setType(SLOT_FLUID_ID, this.slots);
        this.tank.loadTank(this.level, SLOT_FLUID_INPUT, SLOT_FLUID_OUTPUT, this.slots);

        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(this.tank.getTankType(), this.level, pos);
        }

        this.loadFluid();

        if(!this.process()) {
            this.progress = 0;
        }

        this.dequeue();

        if(this.currentFill <= 0) {
            this.currentFill = 0;
            this.current = ItemStack.EMPTY;
        }

        this.setChanged();
        this.networkPackNT(50);
        this.mode = EnumWavelengths.NULL;
    }

    private DirPos[] getConPos() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise(Direction.Axis.Y);
        BlockPos pos = this.getBlockPos().above().relative(side, 2);
        BlockPos opposite = this.getBlockPos().above().relative(side.getOpposite(), 2);

        return new DirPos[] {
                new DirPos(pos, side),
                new DirPos(opposite, side.getOpposite())
        };
    }

    private void loadFluid() {
        FluidType type = this.tank.getTankType();
        if(type != Fluids.NONE) {
            ItemStack converted = SILEXRecipes.fluidInput(type);
            if(SILEXRecipes.INSTANCE.getOutput(converted) != null) {
                if(this.currentFill == 0) this.current = converted;

                if(ItemStack.isSameItemSameComponents(this.current, converted)) {
                    int toFill = Math.min(50, Math.min(MAX_FILL - this.currentFill, this.tank.getFill()));
                    this.currentFill += toFill;
                    this.tank.setFill(this.tank.getFill() - toFill);
                }
            }
        }

        this.loadDelay++;
        if(this.loadDelay > 20) this.loadDelay = 0;

        ItemStack input = this.slots.get(SLOT_INPUT);
        if(this.loadDelay != 0 || input.isEmpty() || this.tank.getTankType() != Fluids.PEROXIDE) return;

        ItemStack singular = input.copyWithCount(1);
        if(!this.current.isEmpty() && !ItemStack.isSameItemSameComponents(this.current, singular)) return;

        SILEXRecipe recipe = SILEXRecipes.INSTANCE.getOutput(input);
        if(recipe == null) return;

        int load = recipe.fluidProduced;
        if(load > MAX_FILL - this.currentFill || load > this.tank.getFill()) return;

        this.currentFill += load;
        this.current = singular;
        this.tank.setFill(this.tank.getFill() - load);
        input.shrink(1);
    }

    private boolean process() {
        if(this.current.isEmpty() || this.currentFill <= 0) return false;

        SILEXRecipe recipe = SILEXRecipes.INSTANCE.getOutput(this.current);
        if(recipe == null) return false;
        if(recipe.laserStrength.ordinal() > this.mode.ordinal()) return false;
        if(this.currentFill < recipe.fluidConsumed) return false;
        if(!this.slots.get(SLOT_OUTPUT).isEmpty()) return false;

        int difference = this.mode.ordinal() - recipe.laserStrength.ordinal();
        this.progress += 1 << difference;

        if(this.progress >= PROCESS_TIME) {
            this.currentFill -= recipe.fluidConsumed;

            int totalWeight = WeightedRandom.getTotalWeight(recipe.outputs);
            if(totalWeight > 0) {
                int weight = Math.floorMod(this.recipeIndex, totalWeight);
                WeightedOutput output = (WeightedOutput) WeightedRandom.getItem(recipe.outputs, weight);
                if(output != null) this.slots.set(SLOT_OUTPUT, output.stack.copy());
            }

            this.progress = 0;
            this.recipeIndex += PRIME;
        }

        return true;
    }

    private void dequeue() {
        ItemStack output = this.slots.get(SLOT_OUTPUT);
        if(output.isEmpty()) return;

        for(int slot = SLOT_QUEUE_START; slot < SLOT_QUEUE_END && !output.isEmpty(); slot++) {
            ItemStack queued = this.slots.get(slot);
            if(queued.isEmpty() || !ItemStack.isSameItemSameComponents(output, queued)) continue;

            int transfer = Math.min(output.getCount(), queued.getMaxStackSize() - queued.getCount());
            if(transfer > 0) {
                queued.grow(transfer);
                output.shrink(transfer);
            }
        }

        for(int slot = SLOT_QUEUE_START; slot < SLOT_QUEUE_END && !output.isEmpty(); slot++) {
            if(!this.slots.get(slot).isEmpty()) continue;
            this.slots.set(slot, output.copy());
            this.slots.set(SLOT_OUTPUT, ItemStack.EMPTY);
            return;
        }

        if(output.isEmpty()) this.slots.set(SLOT_OUTPUT, ItemStack.EMPTY);
    }

    public int getProgressScaled(int scale) {
        return this.progress * scale / PROCESS_TIME;
    }

    public int getFluidScaled(int scale) {
        return this.tank.getFill() * scale / this.tank.getMaxFill();
    }

    public int getFillScaled(int scale) {
        return this.currentFill * scale / MAX_FILL;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {SLOT_INPUT, 5, 6, 7, 8, 9, 10};
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == SLOT_INPUT) return SILEXRecipes.INSTANCE.getOutput(stack) != null;
        if(slot == SLOT_FLUID_ID) return stack.getItem() instanceof IItemFluidIdentifier;
        if(slot == SLOT_FLUID_INPUT) return FluidContainerRegistry.getFluidContent(stack, this.tank.getTankType()) > 0;
        return false;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_INPUT && this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= SLOT_QUEUE_START && index < SLOT_QUEUE_END;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tank.readFromNBT(tag, "tank");
        this.currentFill = tag.getInt("fill");
        this.progress = tag.getInt("progress");
        this.recipeIndex = tag.getInt("recipeIndex");
        try {
            this.mode = EnumWavelengths.valueOf(tag.getString("mode"));
        } catch(IllegalArgumentException ignored) {
            this.mode = EnumWavelengths.NULL;
        }
        this.current = tag.contains("current") ? ItemStack.parseOptional(registries, tag.getCompound("current")) : ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.tank.writeToNBT(tag, "tank");
        tag.putInt("fill", this.currentFill);
        tag.putInt("progress", this.progress);
        tag.putInt("recipeIndex", this.recipeIndex);
        tag.putString("mode", this.mode.name());
        if(!this.current.isEmpty()) tag.put("current", this.current.save(registries));
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.tank.serialize(buf);
        buf.writeInt(this.currentFill);
        buf.writeInt(this.progress);
        buf.writeInt(this.recipeIndex);
        buf.writeEnum(this.mode);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, this.current);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.tank.deserialize(buf);
        this.currentFill = buf.readInt();
        this.progress = buf.readInt();
        this.recipeIndex = buf.readInt();
        this.mode = buf.readEnum(EnumWavelengths.class);
        this.current = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
    }

    @Override public FluidTank[] getAllTanks() { return new FluidTank[] {this.tank}; }
    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {this.tank}; }
    @Override public FluidTank getTankToPaste() { return this.tank; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineSILEXMenu(id, inventory, this);
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("void")) {
            this.currentFill = 0;
            this.current = ItemStack.EMPTY;
            this.progress = 0;
            this.setChanged();
        }
    }
}
