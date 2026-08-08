package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Combustible;
import com.hbm.inventory.fluid.trait.FT_Combustible.FuelGrade;
import com.hbm.inventory.fluid.trait.FT_Polluting;
import com.hbm.inventory.menus.MachineDieselMenu;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.lib.Library;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

public class MachineDieselBlockEntity extends MachineBaseBlockEntity implements IEnergyProviderMK2, IFluidStandardTransceiverMK2, IControlReceiver, IFluidCopiable {

    public static final long MAX_POWER = 50_000L;
    public static final int FUEL_CAPACITY = 16_000;
    public static final LinkedHashMap<FuelGrade, Double> FUEL_EFFICIENCY = new LinkedHashMap<>();

    static {
        FUEL_EFFICIENCY.put(FuelGrade.MEDIUM, 0.5D);
        FUEL_EFFICIENCY.put(FuelGrade.HIGH, 0.75D);
        FUEL_EFFICIENCY.put(FuelGrade.AERO, 0.1D);
    }

    public boolean isOn;
    public boolean wasOn;
    public long power;
    public final FluidTank tank = new FluidTank(Fluids.DIESEL, FUEL_CAPACITY);
    public final FluidTank smoke = new FluidTank(Fluids.SMOKE, 100);
    public final FluidTank smokeLeaded = new FluidTank(Fluids.SMOKE_LEADED, 100);
    public final FluidTank smokePoison = new FluidTank(Fluids.SMOKE_POISON, 100);

    private AudioWrapper audio;

    public MachineDieselBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.DIESEL.get(), pos, state, 4);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_diesel");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            this.wasOn = false;
            boolean changed = false;

            if(this.tank.setType(3, this.slots)) changed = true;
            if(this.tank.loadTank(this.level, 0, 1, this.slots)) changed = true;

            long previousPower = this.power;
            this.power = Library.chargeItemsFromTE(this.slots, 2, this.power, MAX_POWER);
            if(this.power != previousPower) changed = true;

            for(Direction direction : Direction.values()) {
                BlockPos target = this.worldPosition.relative(direction);
                DirPos connection = new DirPos(target, direction);

                if(this.power > 0) this.tryProvide(this.level, target, direction);
                if(this.smoke.getFill() > 0) this.tryProvide(this.smoke, this.level, connection);
                if(this.smokeLeaded.getFill() > 0) this.tryProvide(this.smokeLeaded, this.level, connection);
                if(this.smokePoison.getFill() > 0) this.tryProvide(this.smokePoison, this.level, connection);
                this.trySubscribe(this.tank.getTankType(), this.level, connection);
            }

            if(this.isOn && this.generate()) changed = true;

            if(changed) this.setChanged();
            this.networkPackNT(50);
        } else if(this.wasOn) {
            if(this.audio == null) {
                this.audio = this.createAudioLoop();
                if(this.audio != null) this.audio.startSound();
            } else if(!this.audio.isPlaying()) {
                this.audio = this.rebootAudio(this.audio);
            }

            if(this.audio != null) {
                this.audio.keepAlive();
                this.audio.updateVolume(this.getVolume(1F));
            }
        } else if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    public boolean hasAcceptableFuel() {
        return this.getHEFromFuel() > 0;
    }

    public long getHEFromFuel() {
        return getHEFromFuel(this.tank.getTankType());
    }

    public static long getHEFromFuel(FluidType type) {
        FT_Combustible fuel = type.getTrait(FT_Combustible.class);
        if(fuel == null || fuel.getGrade() == FuelGrade.LOW) return 0;

        double efficiency = FUEL_EFFICIENCY.getOrDefault(fuel.getGrade(), 0D);
        return (long) (fuel.getCombustionEnergy() / 1_000L * efficiency);
    }

    private boolean generate() {
        if(!this.isOn) return false;
        if(this.level == null || this.level.hasNeighborSignal(this.worldPosition)) return false;
        if(!this.hasAcceptableFuel() || this.tank.getFill() <= 0) return false;

        this.wasOn = true;
        this.tank.setFill(Math.max(this.tank.getFill() - 1, 0));

        if(this.level.getGameTime() % 5 == 0) {
            this.pollute(this.tank.getTankType());
        }

        this.power = Math.min(this.power + this.getHEFromFuel(), MAX_POWER);
        return true;
    }

    private void pollute(FluidType type) {
        FT_Polluting trait = type.getTrait(FT_Polluting.class);
        if(trait == null) return;

        for(Map.Entry<PollutionType, Float> entry : trait.burnMap.entrySet()) {
            this.pollute(entry.getKey(), entry.getValue());
        }
    }

    private void pollute(PollutionType type, float amount) {
        FluidTank pollutionTank;
        if(type == PollutionType.SOOT) {
            pollutionTank = this.smoke;
        } else if(type == PollutionType.HEAVYMETAL) {
            pollutionTank = this.smokeLeaded;
        } else if(type == PollutionType.POISON) {
            pollutionTank = this.smokePoison;
        } else {
            return;
        }

        int fluidAmount = (int) Math.ceil(amount * 100);
        pollutionTank.setFill(pollutionTank.getFill() + fluidAmount);

        if(pollutionTank.getFill() > pollutionTank.getMaxFill()) {
            int overflow = pollutionTank.getFill() - pollutionTank.getMaxFill();
            pollutionTank.setFill(pollutionTank.getMaxFill());
            PollutionHandler.incrementPollution(this.level, this.worldPosition, type, overflow / 100F);

            if(this.level.random.nextInt(3) == 0) {
                this.level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.1F, 1.5F);
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineDieselMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return FluidContainerRegistry.getFluidContent(stack, this.tank.getTankType()) > 0;
        if(slot == 2) return stack.getItem() instanceof IBatteryItem;
        if(slot == 3) return stack.getItem() instanceof IItemFluidIdentifier;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if(direction == Direction.DOWN) return new int[] { 1, 2 };
        if(direction == Direction.UP) return new int[] { 0 };
        return new int[] { 2 };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        if(slot == 1) return true;
        if(slot == 2 && stack.getItem() instanceof IBatteryItem battery) {
            return battery.getCharge(stack) == battery.getMaxCharge(stack);
        }
        return false;
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
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.smoke, this.smokeLeaded, this.smokePoison };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.distanceToSqr(
                this.worldPosition.getX() + 0.5,
                this.worldPosition.getY() + 0.5,
                this.worldPosition.getZ() + 0.5
        ) < 25;
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("turnOn")) this.isOn = !this.isOn;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.isOn = tag.getBoolean("isOn");
        this.power = tag.getLong("power");
        this.tank.readFromNBT(tag, "fuel");
        this.smoke.readFromNBT(tag, "smoke0");
        this.smokeLeaded.readFromNBT(tag, "smoke1");
        this.smokePoison.readFromNBT(tag, "smoke2");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isOn", this.isOn);
        tag.putLong("power", this.power);
        this.tank.writeToNBT(tag, "fuel");
        this.smoke.writeToNBT(tag, "smoke0");
        this.smokeLeaded.writeToNBT(tag, "smoke1");
        this.smokePoison.writeToNBT(tag, "smoke2");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeBoolean(this.isOn);
        buf.writeBoolean(this.wasOn);
        this.tank.serialize(buf);
        this.smoke.serialize(buf);
        this.smokeLeaded.serialize(buf);
        this.smokePoison.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.isOn = buf.readBoolean();
        this.wasOn = buf.readBoolean();
        this.tank.deserialize(buf);
        this.smoke.deserialize(buf);
        this.smokeLeaded.deserialize(buf);
        this.smokePoison.deserialize(buf);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.ENGINE_LOOP.get(), SoundSource.BLOCKS, this, 1F, 10F, 1F, 10);
    }
}
