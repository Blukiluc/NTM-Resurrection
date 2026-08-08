package com.hbm.blockentity.machine.tower;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class AbstractCoolingTowerBlockEntity extends LoadedBaseBlockEntity implements ITickable, IFluidStandardTransceiverMK2, IFluidCopiable {

    public final FluidTank[] tanks;
    public int waterTimer;

    protected AbstractCoolingTowerBlockEntity(BlockEntityType<? extends AbstractCoolingTowerBlockEntity> type, BlockPos pos, BlockState state, int capacity) {
        super(type, pos, state);
        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.SPENTSTEAM, capacity),
                new FluidTank(Fluids.WATER, capacity)
        };
    }

    protected abstract DirPos[] getConnectionPositions();
    protected abstract ParticleSettings getParticleSettings();

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        if(this.waterTimer > 0) this.waterTimer--;

        int converted = Math.min(this.tanks[0].getFill(), this.tanks[1].getMaxFill() - this.tanks[1].getFill());
        if(converted > 0) {
            this.tanks[0].setFill(this.tanks[0].getFill() - converted);
            this.tanks[1].setFill(this.tanks[1].getFill() + converted);
            this.waterTimer = 20;
        }

        for(DirPos pos : this.getConnectionPositions()) {
            this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
            if(this.tanks[1].getFill() > 0) this.tryProvide(this.tanks[1], this.level, pos);
        }

        ParticleSettings settings = this.getParticleSettings();
        if(this.waterTimer > 0 && this.level.getGameTime() % settings.interval() == 0) {
            this.spawnParticle(settings);
        }

        this.setChanged();
        this.networkPackNT(150);
    }

    private void spawnParticle(ParticleSettings settings) {
        if(!(this.level instanceof ServerLevel serverLevel)) return;

        double x = this.worldPosition.getX() + 0.5 + (this.level.random.nextDouble() * 2D - 1D) * settings.spread();
        double y = this.worldPosition.getY() + settings.yOffset();
        double z = this.worldPosition.getZ() + 0.5 + (this.level.random.nextDouble() * 2D - 1D) * settings.spread();

        CompoundTag data = new CompoundTag();
        data.putString("type", "tower");
        data.putFloat("lift", settings.lift());
        data.putFloat("base", settings.baseScale());
        data.putFloat("max", settings.maxScale());
        data.putInt("life", settings.minimumLife() + this.level.random.nextInt(settings.randomLife()));

        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 150D, new AuxParticle(data, x, y, z));
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] {this.tanks[1]};
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] {this.tanks[0]};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    @Override
    public boolean canConnect(FluidType type, Direction direction) {
        return direction != null && direction.getAxis().isHorizontal() &&
                (type == this.tanks[0].getTankType() || type == this.tanks[1].getTankType());
    }

    @Override
    public FluidTank getTankToPaste() {
        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "output");
        this.waterTimer = tag.getInt("waterTimer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "output");
        tag.putInt("waterTimer", this.waterTimer);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.tanks[0].serialize(buf);
        this.tanks[1].serialize(buf);
        buf.writeByte(this.waterTimer);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.tanks[0].deserialize(buf);
        this.tanks[1].deserialize(buf);
        this.waterTimer = buf.readUnsignedByte();
    }

    protected record ParticleSettings(int interval, double yOffset, float lift, float baseScale, float maxScale, int minimumLife, int randomLife, double spread) { }
}
