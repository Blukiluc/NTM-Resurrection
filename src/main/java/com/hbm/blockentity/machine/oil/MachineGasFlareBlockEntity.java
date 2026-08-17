package com.hbm.blockentity.machine.oil;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.energymk2.IEnergyReceiverMK2.ConnectionPriority;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.LoadedBaseBlockEntity.TiltType;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.fluid.trait.FT_Polluting;
import com.hbm.inventory.fluid.trait.FluidTrait.FluidReleaseType;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Gaseous;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Gaseous_ART;
import com.hbm.inventory.menus.MachineGasFlareMenu;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.ParticleUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MachineGasFlareBlockEntity extends MachineBaseBlockEntity implements IEnergyProviderMK2, IFluidStandardReceiverMK2, IControlReceiver, IUpgradeInfoProvider, IFluidCopiable {

    public static final long MAX_POWER = 100_000L;
    public static final int TANK_CAPACITY = 64_000;

    public long power;
    public final FluidTank tank = new FluidTank(Fluids.GAS, TANK_CAPACITY);
    public boolean isOn;
    public boolean doesBurn;
    public int fluidUsed;
    public int output;
    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachineGasFlareBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.GAS_FLARE.get(), pos, state, 6);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_flare");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        this.checkTilt(TiltType.CONFIG, false);
        this.fluidUsed = 0;
        this.output = 0;

        for(DirPos pos : this.getConnectionPositions()) {
            this.tryProvide(this.level, pos.makeCompat(), pos.getDir());
            this.trySubscribe(this.tank.getTankType(), this.level, pos);
        }

        this.tank.setType(3, this.slots);
        this.tank.loadTank(this.level, 1, 2, this.slots);

        int maxVent = 50;
        int maxBurn = 10;

        if(this.isOn && this.tank.getFill() > 0 && !this.tilted) {
            this.upgradeManager.checkSlots(this.slots, 4, 5);
            int speed = this.upgradeManager.getLevel(UpgradeType.SPEED);
            int efficiency = this.upgradeManager.getLevel(UpgradeType.EFFECT);

            maxVent += maxVent * speed;
            maxBurn += maxBurn * speed;

            if(!this.doesBurn || !this.tank.getTankType().hasTrait(FT_Flammable.class)) {
                this.vent(maxVent);
            } else {
                this.burn(maxBurn, efficiency);
            }
        }

        this.power = Library.chargeItemsFromTE(this.slots, 0, this.power, MAX_POWER);
        this.setChanged();
        this.networkPackNT(50);
    }

    private void vent(int maxVent) {
        FluidType type = this.tank.getTankType();
        if(!this.isGas(type)) return;

        int eject = Math.min(maxVent, this.tank.getFill());
        if(eject <= 0) return;

        this.fluidUsed = eject;
        this.tank.setFill(this.tank.getFill() - eject);
        type.onFluidRelease(this, this.tank, eject);

        if(this.level.getGameTime() % 7 == 0) {
            this.level.playSound(null, this.worldPosition.above(11), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, this.getVolume(1.5F), 0.5F);
        }
        if(this.level.getGameTime() % 5 == 0) {
            FT_Polluting.pollute(this.level, this.worldPosition, type, FluidReleaseType.SPILL, eject * 5F);
        }

        this.spawnVentParticle(type);
    }

    private void burn(int maxBurn, int efficiency) {
        FluidType type = this.tank.getTankType();
        FT_Flammable flammable = type.getTrait(FT_Flammable.class);
        if(flammable == null) return;

        int eject = Math.min(maxBurn, this.tank.getFill());
        if(eject <= 0) return;

        this.fluidUsed = eject;
        this.tank.setFill(this.tank.getFill() - eject);

        int penalty = this.isGas(type) ? 5 : 10;
        long generated = flammable.getHeatEnergy() * eject / 1_000L / penalty;
        generated += generated * efficiency / 3L;
        this.output = (int) Math.min(generated, Integer.MAX_VALUE);
        this.power = Math.min(this.power + generated, MAX_POWER);

        ParticleUtil.spawnGasFlame(this.level, this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 11.75D, this.worldPosition.getZ() + 0.5D, this.level.random.nextGaussian() * 0.15D, 0.2D, this.level.random.nextGaussian() * 0.15D);

        AABB fireBox = new AABB(
                this.worldPosition.getX() - 1D,
                this.worldPosition.getY() + 12D,
                this.worldPosition.getZ() - 2D,
                this.worldPosition.getX() + 2D,
                this.worldPosition.getY() + 17D,
                this.worldPosition.getZ() + 2D
        );
        for(Entity entity : this.level.getEntitiesOfClass(Entity.class, fireBox)) {
            entity.igniteForSeconds(5F);
            entity.hurt(entity.damageSources().onFire(), 5F);
        }

        if(this.level.getGameTime() % 3 == 0) {
            this.level.playSound(null, this.worldPosition.above(11), NtmSoundEvents.FLAMETHROWER_SHOOT.get(), SoundSource.BLOCKS, this.getVolume(1.5F), 0.75F);
        }
        if(this.level.getGameTime() % 5 == 0) {
            FT_Polluting.pollute(this.level, this.worldPosition, type, FluidReleaseType.BURN, eject * 5F);
        }

        if(this.level instanceof ServerLevel serverLevel) {
            boolean even = this.level.getGameTime() % 2 == 0;
            double x = this.worldPosition.getX() + (even ? 1.5D : 1.125D);
            double y = this.worldPosition.getY() + (even ? 10.75D : 11.75D);
            double z = this.worldPosition.getZ() + (even ? 1.5D : -0.5D);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, x, y, z, 1, 0D, 0D, 0D, 0D);
        }
    }

    private void spawnVentParticle(FluidType type) {
        if(!(this.level instanceof ServerLevel serverLevel)) return;

        double x = this.worldPosition.getX() + 0.5D;
        double y = this.worldPosition.getY() + 11D;
        double z = this.worldPosition.getZ() + 0.5D;
        CompoundTag data = new CompoundTag();
        data.putString("type", "tower");
        data.putFloat("lift", 1F);
        data.putFloat("base", 0.25F);
        data.putFloat("max", 3F);
        data.putInt("life", 150 + this.level.random.nextInt(20));
        data.putInt("color", type.getColor());
        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 50D, new AuxParticle(data, x, y, z));
    }

    private boolean isGas(FluidType type) {
        return type.hasTrait(FT_Gaseous.class) || type.hasTrait(FT_Gaseous_ART.class);
    }

    private DirPos[] getConnectionPositions() {
        BlockPos pos = this.worldPosition;
        return new DirPos[] {
                new DirPos(pos.east(2), Direction.EAST),
                new DirPos(pos.west(2), Direction.WEST),
                new DirPos(pos.south(2), Direction.SOUTH),
                new DirPos(pos.north(2), Direction.NORTH)
        };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineGasFlareMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return stack.getItem() instanceof IBatteryItem;
        if(slot == 1) return FluidContainerRegistry.getFluidContent(stack, this.tank.getTankType()) > 0;
        if(slot == 3) return stack.getItem() instanceof IItemFluidIdentifier;
        return (slot == 4 || slot == 5) && stack.getItem() instanceof MachineUpgradeItem;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 0 || index == 2;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {0, 1, 2, 3, 4, 5};
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.distanceToSqr(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ()) <= 256D;
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("valve")) this.isOn = !this.isOn;
        if(tag.contains("dial")) this.doesBurn = !this.doesBurn;
        this.setChanged();
    }

    @Override
    public CompoundTag getSettings(Level level, BlockPos pos) {
        CompoundTag tag = IFluidCopiable.super.getSettings(level, pos);
        tag.putBoolean("isOn", this.isOn);
        tag.putBoolean("doesBurn", this.doesBurn);
        return tag;
    }

    @Override
    public void pasteSettings(CompoundTag tag, int index, Level level, Player player, BlockPos pos) {
        IFluidCopiable.super.pasteSettings(tag, index, level, player, pos);
        if(tag.contains("isOn")) this.isOn = tag.getBoolean("isOn");
        if(tag.contains("doesBurn")) this.doesBurn = tag.getBoolean("doesBurn");
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.isOn = tag.getBoolean("isOn");
        this.doesBurn = tag.getBoolean("doesBurn");
        this.tank.readFromNBT(tag, "tank");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putBoolean("isOn", this.isOn);
        tag.putBoolean("doesBurn", this.doesBurn);
        this.tank.writeToNBT(tag, "tank");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeBoolean(this.isOn);
        buf.writeBoolean(this.doesBurn);
        buf.writeInt(this.fluidUsed);
        buf.writeInt(this.output);
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.isOn = buf.readBoolean();
        this.doesBurn = buf.readBoolean();
        this.fluidUsed = buf.readInt();
        this.output = buf.readInt();
        this.tank.deserialize(buf);
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
        return new FluidTank[] {this.tank};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] {this.tank};
    }

    @Override
    public ConnectionPriority getFluidPriority() {
        return ConnectionPriority.LOW;
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return direction != null && direction.getAxis().isHorizontal();
    }

    @Override
    public int getFloorCount() {
        return 4;
    }

    @Override
    public BlockPos getFloorPosFromIndex(int index) {
        return this.standardFloor3x3(index);
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.EFFECT;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_FLARE.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(KEY_CONSUMPTION, "+" + (level * 100) + "%"));
        }
        if(type == UpgradeType.EFFECT) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(KEY_EFFICIENCY, "+" + (100 * level / 3) + "%"));
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new LinkedHashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.EFFECT, 3);
        return upgrades;
    }
}
