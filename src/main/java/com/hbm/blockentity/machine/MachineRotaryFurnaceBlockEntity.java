package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IConditionalInvAccess;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineRotaryFurnaceMenu;
import com.hbm.inventory.recipes.RotaryFurnaceRecipe;
import com.hbm.inventory.recipes.RotaryFurnaceRecipes;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.modules.ModuleBurnTime;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class MachineRotaryFurnaceBlockEntity extends MachineBaseBlockEntity implements IFluidStandardTransceiverMK2, IFluidCopiable, IConditionalInvAccess {

    public static final int SLOT_INPUT_1 = 0;
    public static final int SLOT_INPUT_2 = 1;
    public static final int SLOT_INPUT_3 = 2;
    public static final int SLOT_FLUID_IDENTIFIER = 3;
    public static final int SLOT_FUEL = 4;
    public static final int SLOT_OUTPUT = 5;

    public static final ModuleBurnTime burnModule = new ModuleBurnTime()
            .setCokeTimeMod(1.25D)
            .setRocketTimeMod(1.5D)
            .setSolidTimeMod(1.5D)
            .setBalefireTimeMod(1.5D)
            .setSolidHeatMod(1.5D)
            .setRocketHeatMod(3.0D)
            .setBalefireHeatMod(10.0D);

    public final FluidTank[] tanks;
    public final FluidTank smoke;
    public boolean isProgressing;
    public boolean isVenting;
    public float progress;
    public int burnTime;
    public int maxBurnTime;
    public double burnHeat = 1.0D;
    public int steamUsed;
    public float anim;
    public float lastAnim;

    public MachineRotaryFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_ROTARY_FURNACE.get(), pos, state, 6);
        RotaryFurnaceRecipes.INSTANCE.registerDefaults();
        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.NONE, 16_000),
                new FluidTank(Fluids.STEAM, 12_000),
                new FluidTank(Fluids.SPENTSTEAM, 120)
        };
        this.smoke = new FluidTank(Fluids.SMOKE, 100);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_rotary_furnace");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.updateClientAnimation();
            return;
        }

        int inputFill = this.tanks[0].getFill();
        int steamFill = this.tanks[1].getFill();
        int spentFill = this.tanks[2].getFill();
        int smokeFill = this.smoke.getFill();
        int previousBurnTime = this.burnTime;
        float previousProgress = this.progress;
        boolean previousProgressing = this.isProgressing;
        boolean previousVenting = this.isVenting;

        this.tanks[0].setType(SLOT_FLUID_IDENTIFIER, this.slots);

        for(DirPos pos : this.getSteamPos()) {
            this.trySubscribe(this.tanks[1].getTankType(), this.level, pos);
            if(this.tanks[2].getFill() > 0) this.tryProvide(this.tanks[2], this.level, pos);
        }

        if(this.tanks[0].getTankType() != Fluids.NONE) {
            for(DirPos pos : this.getFluidPos()) {
                this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
            }
        }

        if(this.smoke.getFill() > 0) this.tryProvide(this.smoke, this.level, this.getSmokePos());

        RotaryFurnaceRecipe recipe = RotaryFurnaceRecipes.INSTANCE.getRecipe(
                this.slots.get(SLOT_INPUT_1),
                this.slots.get(SLOT_INPUT_2),
                this.slots.get(SLOT_INPUT_3),
                this.tanks[0].getTankType()
        );

        this.isProgressing = false;
        this.isVenting = false;

        if(recipe != null) {
            if(this.burnTime <= 0) this.tryStartFuel();

            float processSpeed = (float) Math.max(this.burnHeat, 1.0D);
            int steamUse = this.getSteamUse(recipe, processSpeed);

            if(this.canProcess(recipe, steamUse)) {
                this.progress += processSpeed / Math.max(recipe.duration, 1);
                this.tanks[1].setFill(this.tanks[1].getFill() - steamUse);
                this.steamUsed += steamUse;
                this.returnSpentSteam();
                this.isProgressing = true;

                if(this.progress >= 1.0F) {
                    this.progress -= 1.0F;
                    this.process(recipe);
                }

                if(this.burnTime > 0) {
                    this.pollute(PollutionHandler.SOOT_PER_SECOND / 10.0F);
                    this.burnTime--;
                }
            } else {
                this.progress = 0.0F;
            }
        } else {
            this.progress = 0.0F;
        }

        if(inputFill != this.tanks[0].getFill()
                || steamFill != this.tanks[1].getFill()
                || spentFill != this.tanks[2].getFill()
                || smokeFill != this.smoke.getFill()
                || previousBurnTime != this.burnTime
                || previousProgress != this.progress
                || previousProgressing != this.isProgressing
                || previousVenting != this.isVenting) {
            this.setChanged();
        }

        this.networkPackNT(50);
    }

    private void tryStartFuel() {
        ItemStack fuel = this.slots.get(SLOT_FUEL);
        if(fuel.isEmpty()) return;

        int time = burnModule.getBurnTime(fuel) / 2;
        if(time <= 0) return;

        this.burnHeat = burnModule.getBurnHeat(1_000, fuel) / 1_000.0D;
        this.maxBurnTime = time;
        this.burnTime = time;

        ItemStack remainder = fuel.hasCraftingRemainingItem() ? fuel.getCraftingRemainingItem().copy() : ItemStack.EMPTY;
        fuel.shrink(1);
        if(fuel.isEmpty()) this.slots.set(SLOT_FUEL, remainder);
    }

    private int getSteamUse(RotaryFurnaceRecipe recipe, float processSpeed) {
        double multiplier = 10.0D * Math.log10(Math.max(processSpeed, 1.0F)) + 1.0D;
        return Math.max(1, (int) Math.ceil(recipe.steam * multiplier));
    }

    private boolean canProcess(RotaryFurnaceRecipe recipe, int steamUse) {
        if(this.burnTime <= 0) return false;
        if(!RotaryFurnaceRecipes.matchesInputs(recipe, this.getInputStacks(), false)) return false;

        if(recipe.inputFluid != null && recipe.inputFluid.length > 0) {
            if(this.tanks[0].getTankType() != recipe.inputFluid[0].type) return false;
            if(this.tanks[0].getFill() < recipe.inputFluid[0].fill) return false;
        }

        if(this.tanks[1].getFill() < steamUse) return false;

        int spentToCreate = (this.steamUsed + steamUse) / 100;
        if(this.tanks[2].getMaxFill() - this.tanks[2].getFill() < spentToCreate) return false;

        ItemStack output = this.getOutput(recipe);
        if(output.isEmpty()) return false;

        ItemStack current = this.slots.get(SLOT_OUTPUT);
        return current.isEmpty() || ItemStack.isSameItemSameComponents(current, output)
                && current.getCount() + output.getCount() <= current.getMaxStackSize();
    }

    private void process(RotaryFurnaceRecipe recipe) {
        if(recipe.inputItem != null) {
            for(AStack ingredient : recipe.inputItem) {
                for(int slot = SLOT_INPUT_1; slot <= SLOT_INPUT_3; slot++) {
                    ItemStack input = this.slots.get(slot);
                    if(ingredient.matchesRecipe(input, false)) {
                        input.shrink(ingredient.stacksize);
                        if(input.isEmpty()) this.slots.set(slot, ItemStack.EMPTY);
                        break;
                    }
                }
            }
        }

        if(recipe.inputFluid != null && recipe.inputFluid.length > 0) {
            this.tanks[0].setFill(this.tanks[0].getFill() - recipe.inputFluid[0].fill);
        }

        ItemStack output = this.getOutput(recipe);
        ItemStack current = this.slots.get(SLOT_OUTPUT);
        if(current.isEmpty()) {
            this.slots.set(SLOT_OUTPUT, output);
        } else {
            current.grow(output.getCount());
        }
    }

    private ItemStack getOutput(RotaryFurnaceRecipe recipe) {
        if(recipe.outputItem == null || recipe.outputItem.length == 0) return ItemStack.EMPTY;
        ItemStack output = recipe.outputItem[0].getSingle();
        return output == null ? ItemStack.EMPTY : output.copy();
    }

    private ItemStack[] getInputStacks() {
        return new ItemStack[] {
                this.slots.get(SLOT_INPUT_1),
                this.slots.get(SLOT_INPUT_2),
                this.slots.get(SLOT_INPUT_3)
        };
    }

    private void returnSpentSteam() {
        int steamReturn = this.steamUsed / 100;
        int canReturn = this.tanks[2].getMaxFill() - this.tanks[2].getFill();
        int returned = Math.min(steamReturn, canReturn);

        if(returned > 0) {
            this.steamUsed -= returned * 100;
            this.tanks[2].setFill(this.tanks[2].getFill() + returned);
        }
    }

    private void pollute(float amount) {
        int fluidAmount = (int) Math.ceil(amount * 100.0F);
        this.smoke.setFill(this.smoke.getFill() + fluidAmount);

        if(this.smoke.getFill() > this.smoke.getMaxFill()) {
            int overflow = this.smoke.getFill() - this.smoke.getMaxFill();
            this.smoke.setFill(this.smoke.getMaxFill());
            PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionType.SOOT, overflow / 100.0F);
            this.isVenting = true;
        }
    }

    private void updateClientAnimation() {
        this.lastAnim = this.anim;
        if(this.isProgressing) this.anim += (float) Math.max(this.burnHeat, 1.0D);

        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = dir.getClockWise();

        if(this.burnTime > 0 && this.level.random.nextBoolean()) {
            this.level.addParticle(
                    ParticleTypes.FLAME,
                    this.worldPosition.getX() + 0.5D + dir.getStepX() * 0.5D + side.getStepX() + this.level.random.nextGaussian() * 0.25D,
                    this.worldPosition.getY() + 0.375D,
                    this.worldPosition.getZ() + 0.5D + dir.getStepZ() * 0.5D + side.getStepZ() + this.level.random.nextGaussian() * 0.25D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }

        if(this.isVenting && this.level.getGameTime() % 2 == 0) {
            this.level.addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.worldPosition.getX() + 0.5D + side.getStepX(),
                    this.worldPosition.getY() + 5.0D,
                    this.worldPosition.getZ() + 0.5D + side.getStepZ(),
                    0.0D,
                    0.05D,
                    0.0D
            );
        }
    }

    public DirPos[] getSteamPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = dir.getClockWise();
        BlockPos first = this.worldPosition.relative(dir, -2).relative(side, -2);
        BlockPos second = this.worldPosition.relative(dir, -2).relative(side, -1);

        return new DirPos[] {
                new DirPos(first, dir.getOpposite()),
                new DirPos(second, dir.getOpposite())
        };
    }

    public DirPos[] getFluidPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = dir.getClockWise();
        BlockPos first = this.worldPosition.relative(dir).relative(side, 3);
        BlockPos second = this.worldPosition.relative(dir, -1).relative(side, 3);

        return new DirPos[] {
                new DirPos(first, side),
                new DirPos(second, side)
        };
    }

    public DirPos getSmokePos() {
        Direction side = this.getBlockState().getValue(DummyableBlock.FACING).getClockWise();
        return new DirPos(this.worldPosition.relative(side).above(5), Direction.UP);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot >= SLOT_INPUT_1 && slot <= SLOT_INPUT_3) return RotaryFurnaceRecipes.INSTANCE.isItemValid(stack);
        if(slot == SLOT_FLUID_IDENTIFIER) return stack.getItem() instanceof IItemFluidIdentifier;
        if(slot == SLOT_FUEL) return burnModule.getBurnTime(stack) > 0;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {SLOT_INPUT_1, SLOT_INPUT_2, SLOT_INPUT_3, SLOT_FLUID_IDENTIFIER, SLOT_FUEL, SLOT_OUTPUT};
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == SLOT_OUTPUT;
    }

    @Override
    public boolean isItemValidForSlot(BlockPos pos, int slot, ItemStack stack) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canExtractItem(BlockPos pos, int slot, ItemStack stack, Direction direction) {
        return slot == SLOT_OUTPUT;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(BlockPos pos, Direction direction) {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = dir.getClockWise();

        if(direction == dir.getOpposite()) {
            if(pos.equals(this.worldPosition.relative(dir, -1).relative(side, 2))) return new int[] {SLOT_INPUT_1};
            if(pos.equals(this.worldPosition.relative(dir, -1).relative(side))) return new int[] {SLOT_INPUT_2};
            if(pos.equals(this.worldPosition.relative(dir, -1))) return new int[] {SLOT_INPUT_3};
        }

        if(direction == dir && pos.equals(this.worldPosition.relative(dir).relative(side))) return new int[] {SLOT_FUEL};
        if(pos.equals(this.worldPosition.relative(dir).relative(side, 2))) return new int[] {SLOT_OUTPUT};

        return new int[0];
    }

    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {this.tanks[0], this.tanks[1]}; }
    @Override public FluidTank[] getSendingTanks() { return new FluidTank[] {this.tanks[2], this.smoke}; }
    @Override public FluidTank[] getAllTanks() { return new FluidTank[] {this.tanks[0], this.tanks[1], this.tanks[2], this.smoke}; }
    @Override public FluidTank getTankToPaste() { return this.tanks[0]; }

    public int getProgressScaled(int pixels) {
        return Math.round(this.progress * pixels);
    }

    public int getBurnScaled(int pixels) {
        if(this.maxBurnTime <= 0) return 0;
        return this.burnTime * pixels / this.maxBurnTime;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "steam");
        this.tanks[2].readFromNBT(tag, "spent");
        this.smoke.readFromNBT(tag, "smoke");
        this.progress = tag.getFloat("progress");
        this.burnTime = tag.getInt("burnTime");
        this.maxBurnTime = tag.getInt("maxBurnTime");
        this.burnHeat = tag.getDouble("burnHeat");
        this.steamUsed = tag.getInt("steamUsed");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "steam");
        this.tanks[2].writeToNBT(tag, "spent");
        this.smoke.writeToNBT(tag, "smoke");
        tag.putFloat("progress", this.progress);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("maxBurnTime", this.maxBurnTime);
        tag.putDouble("burnHeat", this.burnHeat);
        tag.putInt("steamUsed", this.steamUsed);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.tanks[0].serialize(buf);
        this.tanks[1].serialize(buf);
        this.tanks[2].serialize(buf);
        this.smoke.serialize(buf);
        buf.writeBoolean(this.isVenting);
        buf.writeBoolean(this.isProgressing);
        buf.writeFloat(this.progress);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.maxBurnTime);
        buf.writeDouble(this.burnHeat);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.tanks[0].deserialize(buf);
        this.tanks[1].deserialize(buf);
        this.tanks[2].deserialize(buf);
        this.smoke.deserialize(buf);
        this.isVenting = buf.readBoolean();
        this.isProgressing = buf.readBoolean();
        this.progress = buf.readFloat();
        this.burnTime = buf.readInt();
        this.maxBurnTime = buf.readInt();
        this.burnHeat = buf.readDouble();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineRotaryFurnaceMenu(id, inventory, this);
    }
}
