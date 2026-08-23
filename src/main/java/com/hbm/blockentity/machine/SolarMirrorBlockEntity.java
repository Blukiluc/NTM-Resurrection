package com.hbm.blockentity.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SolarMirrorBlockEntity extends LoadedBaseBlockEntity implements ITickable {

    private BlockPos target = BlockPos.ZERO;
    private boolean hasTarget;
    public boolean isOn;

    public SolarMirrorBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.SOLAR_MIRROR.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        boolean previousState = this.isOn;
        this.isOn = false;

        if(this.hasTarget && this.target.getY() > this.worldPosition.getY()) {
            int sunlight = this.level.getBrightness(LightLayer.SKY, this.worldPosition) - this.level.getSkyDarken() - 11;
            if(sunlight > 0 && this.level.canSeeSky(this.worldPosition.above())) {
                BlockEntity blockEntity = this.level.getBlockEntity(this.target.below());
                if(blockEntity instanceof MachineSolarBoilerBlockEntity boiler) {
                    this.isOn = true;
                    boiler.addHeat(sunlight);
                }
            }
        }

        if(previousState != this.isOn) this.setChanged();
        this.networkPackNT(200);
    }

    public void setTarget(BlockPos target) {
        this.target = target.immutable();
        this.hasTarget = true;
        this.setChanged();
        this.networkPackNT(200);
    }

    public BlockPos getTarget() {
        return this.target;
    }

    public boolean hasTarget() {
        return this.hasTarget;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.hasTarget = tag.getBoolean("hasTarget");
        if(this.hasTarget) this.target = BlockPos.of(tag.getLong("target"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("hasTarget", this.hasTarget);
        if(this.hasTarget) tag.putLong("target", this.target.asLong());
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(this.hasTarget);
        if(this.hasTarget) buf.writeBlockPos(this.target);
        buf.writeBoolean(this.isOn);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.hasTarget = buf.readBoolean();
        this.target = this.hasTarget ? buf.readBlockPos() : BlockPos.ZERO;
        this.isOn = buf.readBoolean();
    }
}
