package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.menus.MachineWoodBurnerMenu;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.InfiniteFluidItem;
import com.hbm.lib.Library;
import com.hbm.modules.ModuleBurnTime;
import com.hbm.util.ItemStackUtil;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Locale;

public class MachineWoodBurnerBlockEntity extends MachineBaseBlockEntity implements IFluidStandardReceiverMK2, IControlReceiver, IEnergyProviderMK2, IFluidCopiable {

    public static final long MAX_POWER = 100_000L;
    public static final ModuleBurnTime burnModule = new ModuleBurnTime().setLogTimeMod(4).setWoodTimeMod(2);

    public long power;
    public int burnTime;
    public int maxBurnTime;
    public boolean liquidBurn;
    public boolean isOn;
    public int powerGen;

    public final FluidTank tank = new FluidTank(Fluids.WOODOIL, 16_000);

    public int ashLevelWood;
    public int ashLevelCoal;
    public int ashLevelMisc;

    public MachineWoodBurnerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.WOOD_BURNER.get(), pos, state, 6);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_wood_burner");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            boolean changed = false;
            this.powerGen = 0;

            if(this.tank.setType(2, this.slots)) changed = true;
            if(this.tank.loadTank(this.level, 3, 4, this.slots)) changed = true;

            long previousPower = this.power;
            this.power = Library.chargeItemsFromTE(this.slots, 5, this.power, MAX_POWER);
            if(this.power != previousPower) changed = true;
            long powerAfterCharging = this.power;

            for(DirPos pos : this.getConPos()) {
                if(this.power > 0) this.tryProvide(this.level, pos.makeCompat(), pos.getDir());
                if(this.level.getGameTime() % 20 == 0) this.trySubscribe(this.tank.getTankType(), this.level, pos);
            }

            if(!this.liquidBurn) {
                if(this.burnTime <= 0) {
                    ItemStack fuel = this.slots.get(0);
                    if(!fuel.isEmpty()) {
                        int burn = burnModule.getBurnTime(fuel);
                        if(burn > 0) {
                            Item ash = getAshFromFuel(fuel);
                            if(ash == NtmItems.POWDER_ASH_WOOD.get()) this.ashLevelWood += burn;
                            if(ash == NtmItems.POWDER_ASH_COAL.get()) this.ashLevelCoal += burn;
                            if(ash == NtmItems.POWDER_ASH.get()) this.ashLevelMisc += burn;

                            int threshold = 2_000;
                            while(this.processAsh(this.ashLevelWood, NtmItems.POWDER_ASH_WOOD.get(), threshold)) this.ashLevelWood -= threshold;
                            while(this.processAsh(this.ashLevelCoal, NtmItems.POWDER_ASH_COAL.get(), threshold)) this.ashLevelCoal -= threshold;
                            while(this.processAsh(this.ashLevelMisc, NtmItems.POWDER_ASH.get(), threshold)) this.ashLevelMisc -= threshold;

                            this.maxBurnTime = this.burnTime = burn;
                            ItemStack remainder = fuel.hasCraftingRemainingItem() ? fuel.getCraftingRemainingItem().copy() : ItemStack.EMPTY;
                            fuel.shrink(1);
                            if(fuel.isEmpty()) this.slots.set(0, remainder);
                            changed = true;
                        }
                    }
                } else if(this.power < MAX_POWER && this.isOn) {
                    this.burnTime--;
                    this.powerGen += 100;
                    changed = true;
                    if(this.level.getGameTime() % 20 == 0) {
                        PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionType.SOOT, PollutionHandler.SOOT_PER_SECOND);
                    }
                }
            } else if(this.power < MAX_POWER && this.tank.getFill() > 0 && this.isOn) {
                FT_Flammable trait = this.tank.getTankType().getTrait(FT_Flammable.class);

                if(trait != null) {
                    int toBurn = Math.min(this.tank.getFill(), 2);

                    if(toBurn > 0) {
                        this.powerGen += (int) (trait.getHeatEnergy() * toBurn / 2_000L);
                        this.tank.setFill(this.tank.getFill() - toBurn);
                        changed = true;
                        if(this.level.getGameTime() % 20 == 0) {
                            PollutionHandler.incrementPollution(this.level, this.worldPosition, PollutionType.SOOT, PollutionHandler.SOOT_PER_SECOND * toBurn / 2F);
                        }
                    }
                }
            }

            this.power = Math.min(this.power + this.powerGen, MAX_POWER);
            if(this.power != powerAfterCharging) changed = true;
            if(changed) this.setChanged();
            this.networkPackNT(25);
        } else if(this.powerGen > 0) {
            Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
            Direction side = dir.getClockWise();
            this.level.addParticle(
                    ParticleTypes.SMOKE,
                    this.worldPosition.getX() + 0.5 - dir.getStepX() + side.getStepX(),
                    this.worldPosition.getY() + 4,
                    this.worldPosition.getZ() + 0.5 - dir.getStepZ() + side.getStepZ(),
                    0,
                    0.05,
                    0
            );
        }
    }

    private DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = dir.getClockWise();
        BlockPos output = this.worldPosition.relative(dir.getOpposite(), 2);

        return new DirPos[] {
                new DirPos(output, dir.getOpposite()),
                new DirPos(output.relative(side), dir.getOpposite())
        };
    }

    public static Item getAshFromFuel(ItemStack stack) {
        List<String> names = ItemStackUtil.getTags(stack);

        for(String name : names) {
            String lower = name.toLowerCase(Locale.US);
            if(lower.contains("coke")) return NtmItems.POWDER_ASH_COAL.get();
            if(lower.contains("coal")) return NtmItems.POWDER_ASH_COAL.get();
            if(lower.contains("lignite")) return NtmItems.POWDER_ASH_COAL.get();
            if(lower.contains("log")) return NtmItems.POWDER_ASH_WOOD.get();
            if(lower.contains("wood")) return NtmItems.POWDER_ASH_WOOD.get();
            if(lower.contains("sapling")) return NtmItems.POWDER_ASH_WOOD.get();
        }

        return NtmItems.POWDER_ASH.get();
    }

    private boolean processAsh(int level, Item ash, int threshold) {
        if(level < threshold) return false;

        ItemStack output = this.slots.get(1);
        if(output.isEmpty()) {
            this.slots.set(1, new ItemStack(ash));
            return true;
        }

        if(output.is(ash) && output.getCount() < output.getMaxStackSize()) {
            output.grow(1);
            return true;
        }

        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineWoodBurnerMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return burnModule.getBurnTime(stack) > 0;
        if(slot == 2) return stack.getItem() instanceof IItemFluidIdentifier;
        if(slot == 3) return !FluidContainerRegistry.getEmptyContainer(stack).isEmpty() || stack.getItem() instanceof InfiniteFluidItem;
        if(slot == 5) return stack.getItem() instanceof IBatteryItem;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == 1;
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
    public boolean canConnect(Direction dir) {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return dir == facing.getOpposite();
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return dir == facing.getOpposite();
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public FluidTank getTankToPaste() {
        return this.tank;
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("toggle")) this.isOn = !this.isOn;
        if(tag.contains("switch")) this.liquidBurn = !this.liquidBurn;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.burnTime = tag.getInt("burnTime");
        this.maxBurnTime = tag.getInt("maxBurnTime");
        this.isOn = tag.getBoolean("isOn");
        this.liquidBurn = tag.getBoolean("liquidBurn");
        this.ashLevelWood = tag.getInt("ashLevelWood");
        this.ashLevelCoal = tag.getInt("ashLevelCoal");
        this.ashLevelMisc = tag.getInt("ashLevelMisc");
        this.tank.readFromNBT(tag, "tank");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("maxBurnTime", this.maxBurnTime);
        tag.putBoolean("isOn", this.isOn);
        tag.putBoolean("liquidBurn", this.liquidBurn);
        tag.putInt("ashLevelWood", this.ashLevelWood);
        tag.putInt("ashLevelCoal", this.ashLevelCoal);
        tag.putInt("ashLevelMisc", this.ashLevelMisc);
        this.tank.writeToNBT(tag, "tank");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.powerGen);
        buf.writeInt(this.maxBurnTime);
        buf.writeBoolean(this.isOn);
        buf.writeBoolean(this.liquidBurn);
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.burnTime = buf.readInt();
        this.powerGen = buf.readInt();
        this.maxBurnTime = buf.readInt();
        this.isOn = buf.readBoolean();
        this.liquidBurn = buf.readBoolean();
        this.tank.deserialize(buf);
    }
}
