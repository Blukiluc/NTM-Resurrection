package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.PollutionHandler;
import com.hbm.handler.PollutionHandler.PollutionType;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Combustible;
import com.hbm.inventory.fluid.trait.FT_Combustible.FuelGrade;
import com.hbm.inventory.fluid.trait.FT_Polluting;
import com.hbm.inventory.menus.MachineTurbofanMenu;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import java.util.Map;

public class MachineTurbofanBlockEntity extends MachineBaseBlockEntity implements IEnergyProviderMK2, IFluidStandardTransceiverMK2, IFluidCopiable, IControlReceiver {

    public static final long MAX_POWER = 1_000_000L;
    public static final int FUEL_CAPACITY = 24_000;

    public long power;
    public final FluidTank tank = new FluidTank(Fluids.KEROSENE, FUEL_CAPACITY);
    public final FluidTank blood = new FluidTank(Fluids.BLOOD, FUEL_CAPACITY);
    public final FluidTank smoke = new FluidTank(Fluids.SMOKE, 100);
    public final FluidTank smokeLeaded = new FluidTank(Fluids.SMOKE_LEADED, 100);
    public final FluidTank smokePoison = new FluidTank(Fluids.SMOKE_POISON, 100);

    // TODO upgrade system
    public int afterburner = 0;

    public boolean wasOn;
    public boolean showBlood = false;
    protected int output;
    protected int consumption;

    public float spin;
    public float lastSpin;
    public int momentum = 0;

    private AudioWrapper audio;

    public MachineTurbofanBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.TURBOFAN.get(), pos, state, 5);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_turbofan");
    }

    private DirPos[] getConnectionPositions() {
        Direction direction = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = direction.getClockWise();

        return new DirPos[] {
                new DirPos(this.worldPosition.relative(side, 2), side),
                new DirPos(this.worldPosition.relative(side, 2).relative(direction.getOpposite()), side),
                new DirPos(this.worldPosition.relative(side.getOpposite(), 2), side.getOpposite()),
                new DirPos(this.worldPosition.relative(side.getOpposite(), 2).relative(direction.getOpposite()), side.getOpposite())
        };
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            this.output = 0;
            this.consumption = 0;
            this.wasOn = false;

            boolean changed = false;

            if(this.tank.setType(4, this.slots)) changed = true;
            if(this.tank.loadTank(this.level, 0, 1, this.slots)) changed = true;
            this.blood.setTankType(Fluids.BLOOD);

            // TODO flame_pony
            // TODO upgrades

            long burnValue = 0;
            int amount = 1 + this.afterburner;
            int amountToBurn = Math.min(amount, this.tank.getFill());

            boolean redstone = false;
            for(DirPos pos : this.getConnectionPositions()) {
                BlockPos target = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
                if(this.level.hasNeighborSignal(target)) {
                    redstone = true;
                    break;
                }
            }

            if(!redstone) {

                FT_Combustible trait = this.tank.getTankType().getTrait(FT_Combustible.class);
                if(trait != null && trait.getGrade() == FuelGrade.AERO) {
                    burnValue = trait.getCombustionEnergy() / 1_000;
                }

                if(amountToBurn > 0) {
                    this.wasOn = true;
                    this.tank.setFill(this.tank.getFill() - amountToBurn);
                    this.output = (int) (burnValue * amountToBurn * (1 + Math.min(this.afterburner / 3D, 4)));
                    this.power += this.output;
                    this.consumption = amountToBurn;
                    changed = true;

                    if(this.level.getGameTime() % 20 == 0) this.pollute(this.tank.getTankType(), amountToBurn * 5F);
                }
            }

            long previousPower = this.power;
            this.power = Library.chargeItemsFromTE(this.slots, 3, this.power, MAX_POWER);
            if(this.power != previousPower) changed = true;

            for(DirPos pos : this.getConnectionPositions()) {
                BlockPos target = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
                this.tryProvide(this.level, target, pos.getDir());
                this.trySubscribe(this.tank.getTankType(), this.level, pos);
                if(this.blood.getFill() > 0) this.tryProvide(this.blood, this.level, pos);
                if(this.smoke.getFill() > 0) this.tryProvide(this.smoke, this.level, pos);
                if(this.smokeLeaded.getFill() > 0) this.tryProvide(this.smokeLeaded, this.level, pos);
                if(this.smokePoison.getFill() > 0) this.tryProvide(this.smokePoison, this.level, pos);
            }

            if(burnValue > 0 && amountToBurn > 0) {
                this.handleExhaust();
            }

            if(this.power > MAX_POWER) {
                this.power = MAX_POWER;
                changed = true;
            }

            if(changed) this.setChanged();
            this.networkPackNT(150);

        } else {

            this.lastSpin = this.spin;

            if(this.wasOn) {
                if(this.momentum < 100) this.momentum++;
            } else {
                if(this.momentum > 0) this.momentum--;
            }

            this.spin += this.momentum / 2F;

            if(this.spin >= 360F) {
                this.spin -= 360F;
                this.lastSpin -= 360F;
            }

            if(this.momentum > 0) {
                if(this.audio == null) {
                    this.audio = this.createAudioLoop();
                    if(this.audio != null) this.audio.startSound();
                } else if(!this.audio.isPlaying()) {
                    this.audio = this.rebootAudio(this.audio);
                }

                if(this.audio != null) {
                    this.audio.keepAlive();
                    this.audio.updateVolume(this.getVolume(this.momentum / 50F));
                    this.audio.updatePitch(this.momentum / 200F + 0.5F + this.afterburner * 0.16F);
                }
            } else if(this.audio != null) {
                this.audio.stopSound();
                this.audio = null;
            }
        }
    }

    private void handleExhaust() {
        Direction direction = this.getBlockState().getValue(DummyableBlock.FACING).getClockWise();
        Direction side = direction.getClockWise();

        // TODO: afterburner particle effects (gas fire jets, damage sound, afterburner flame stack)

        double cx = this.worldPosition.getX() + 0.5;
        double cy = this.worldPosition.getY();
        double cz = this.worldPosition.getZ() + 0.5;

        AABB pullZone = this.buildZone(cx, cy, cz, direction, side, -3.5, -19.5, -1.5, 3, 20);
        for(Entity e : this.level.getEntitiesOfClass(Entity.class, pullZone)) {
            if(this.afterburner > 0) {
                e.igniteForSeconds(5.0F);
                e.hurt(this.level.damageSources().inFire(), 5F);
            }
            e.setDeltaMovement(e.getDeltaMovement().subtract(direction.getStepX() * 0.2, 0, direction.getStepZ() * 0.2));
        }

        AABB intakeZone = this.buildZone(cx, cy, cz, direction.getOpposite(), side, -3.5, -8.5, -1.5, 3, 9);
        for(Entity e : this.level.getEntitiesOfClass(Entity.class, intakeZone)) {
            e.setDeltaMovement(e.getDeltaMovement().subtract(direction.getStepX() * 0.2, 0, direction.getStepZ() * 0.2));
        }

        AABB killZone = this.buildZone(cx, cy, cz, direction.getOpposite(), side, -3.5, -3.75, -1.5, 3, 1);
        // TODO replace generic with a dedicated turbofan damage type
        DamageSource turbofanDamage = this.level.damageSources().generic();
        for(Entity e : this.level.getEntitiesOfClass(Entity.class, killZone)) {
            e.hurt(turbofanDamage, 1000F);

            if(!e.isAlive() && e instanceof LivingEntity) {
                // TODO giblets particle effect (AuxParticlePacketNT "giblets") not ported yet
                this.level.playSound(null, e.blockPosition(), SoundEvents.PLAYER_HURT, SoundSource.BLOCKS, 2.0F, 0.95F + this.level.random.nextFloat() * 0.2F);

                this.blood.setFill(Math.min(this.blood.getFill() + 50, this.blood.getMaxFill()));
                this.showBlood = true;
            }
        }
    }

    private AABB buildZone(double cx, double cy, double cz, Direction dir, Direction side, double nearOffset, double farOffset, double sideOffset, double height, double sideWidth) {
        double minX = cx + dir.getStepX() * nearOffset - side.getStepX() * sideOffset;
        double maxX = cx + dir.getStepX() * farOffset + side.getStepX() * sideOffset;
        double minZ = cz + dir.getStepZ() * nearOffset - side.getStepZ() * sideOffset;
        double maxZ = cz + dir.getStepZ() * farOffset + side.getStepZ() * sideOffset;

        return new AABB(Math.min(minX, maxX), cy, Math.min(minZ, maxZ), Math.max(minX, maxX), cy + height, Math.max(minZ, maxZ));
    }

    private void pollute(FluidType type, float consumed) {
        FT_Polluting trait = type.getTrait(FT_Polluting.class);
        if(trait == null) return;

        for(Map.Entry<PollutionType, Float> entry : trait.burnMap.entrySet()) {
            this.pollute(entry.getKey(), entry.getValue() * consumed);
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
        }
    }

    public long getPowerScaled(long i) {
        return (this.power * i) / MAX_POWER;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineTurbofanMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return com.hbm.inventory.FluidContainerRegistry.getFluidContent(stack, this.tank.getTankType()) > 0;
        if(slot == 3) return stack.getItem() instanceof IBatteryItem;
        if(slot == 4) return stack.getItem() instanceof IItemFluidIdentifier;
        // slot 2: upgrade slot, restore item check once the upgrade system is ported
        return slot == 2;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if(direction == Direction.DOWN) return new int[] { 1, 3 };
        if(direction == Direction.UP) return new int[] { 0 };
        return new int[] { 2, 3, 4 };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        if(slot == 1) return true;
        if(slot == 3 && stack.getItem() instanceof IBatteryItem battery) {
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
        return new FluidTank[] { this.blood, this.smoke, this.smokeLeaded, this.smokePoison };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.tank, this.blood, this.smoke, this.smokeLeaded, this.smokePoison };
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.distanceToSqr(
                this.worldPosition.getX() + 0.5,
                this.worldPosition.getY() + 0.5,
                this.worldPosition.getZ() + 0.5
        ) < 625;
    }

    @Override
    public void receiveControl(CompoundTag tag) { }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.tank.readFromNBT(tag, "fuel");
        this.blood.readFromNBT(tag, "blood");
        this.smoke.readFromNBT(tag, "smoke0");
        this.smokeLeaded.readFromNBT(tag, "smoke1");
        this.smokePoison.readFromNBT(tag, "smoke2");
        this.showBlood = tag.getBoolean("showBlood");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        this.tank.writeToNBT(tag, "fuel");
        this.blood.writeToNBT(tag, "blood");
        this.smoke.writeToNBT(tag, "smoke0");
        this.smokeLeaded.writeToNBT(tag, "smoke1");
        this.smokePoison.writeToNBT(tag, "smoke2");
        tag.putBoolean("showBlood", this.showBlood);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeByte((byte) this.afterburner);
        buf.writeBoolean(this.wasOn);
        buf.writeBoolean(this.showBlood);
        this.tank.serialize(buf);
        this.blood.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.afterburner = buf.readByte();
        this.wasOn = buf.readBoolean();
        this.showBlood = buf.readBoolean();
        this.tank.deserialize(buf);
        this.blood.deserialize(buf);
    }

    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.TURBOFAN_LOOP.get(), SoundSource.BLOCKS, this, 1.0F, 50F, 1.0F, 20);
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
}