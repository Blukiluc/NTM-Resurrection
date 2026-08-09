package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.MachineStirlingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MachineStirlingBlockEntity extends LoadedBaseBlockEntity implements ITickable, IEnergyProviderMK2 {

    public long powerBuffer;
    public int heat;
    public int overspeed;
    public boolean hasGear = true;
    public float spin;
    public float lastSpin;

    public MachineStirlingBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.STIRLING.get(), pos, state);
    }

    private MachineStirlingBlock.Variant getVariant() {
        if(this.getBlockState().is(NtmBlocks.MACHINE_STIRLING_STEEL.get())) return MachineStirlingBlock.Variant.HEAVY;
        if(this.getBlockState().is(NtmBlocks.MACHINE_STIRLING_CREATIVE.get())) return MachineStirlingBlock.Variant.CREATIVE;
        return MachineStirlingBlock.Variant.STANDARD;
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        MachineStirlingBlock.Variant variant = this.getVariant();
        if(this.level.isClientSide) {
            this.lastSpin = this.spin;
            float target = this.hasGear ? Math.min(45F, this.powerBuffer * 45F / Math.max(this.getMaxHeat(), 1)) : 0F;
            this.spin += (target - this.spin) * 0.2F;
            return;
        }

        this.powerBuffer = 0;
        if(variant == MachineStirlingBlock.Variant.CREATIVE) {
            this.hasGear = true;
        }

        if(this.hasGear) {
            this.drawHeat();
            this.powerBuffer = variant == MachineStirlingBlock.Variant.CREATIVE ? this.heat : this.heat / 2L;

            if(variant != MachineStirlingBlock.Variant.CREATIVE && this.heat > this.getMaxHeat()) {
                this.overspeed++;
                if(this.overspeed > 300) {
                    this.hasGear = false;
                    this.overspeed = 0;
                    this.level.explode(null, this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1D, this.worldPosition.getZ() + 0.5D, 2F, Level.ExplosionInteraction.BLOCK);
                }
            } else {
                this.overspeed = Math.max(0, this.overspeed - 1);
            }
        }

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos output = this.worldPosition.relative(direction, 2);
            this.tryProvide(this.level, output, direction);
        }

        this.heat = 0;
        this.setChanged();
        this.networkPackNT(50);
    }

    private void drawHeat() {
        if(this.level == null) return;
        if(this.level.getBlockEntity(this.worldPosition.below()) instanceof IHeatSource source) {
            int accepted = Math.max(1, source.getHeatStored() / 10);
            source.useUpHeat(accepted);
            this.heat += accepted;
        }
    }

    private int getMaxHeat() {
        return this.getVariant() == MachineStirlingBlock.Variant.HEAVY ? 1_500 : 300;
    }

    @Override
    public long getPower() {
        return this.powerBuffer;
    }

    @Override
    public void setPower(long power) {
        this.powerBuffer = power;
    }

    @Override
    public long getMaxPower() {
        return Math.max(this.powerBuffer, this.getMaxHeat());
    }

    @Override
    public boolean canConnect(Direction direction) {
        return direction != null && direction.getAxis().isHorizontal();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.powerBuffer = tag.getLong("power");
        this.heat = tag.getInt("heat");
        this.overspeed = tag.getInt("overspeed");
        this.hasGear = tag.getBoolean("hasGear");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.powerBuffer);
        tag.putInt("heat", this.heat);
        tag.putInt("overspeed", this.overspeed);
        tag.putBoolean("hasGear", this.hasGear);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.powerBuffer);
        buf.writeInt(this.heat);
        buf.writeBoolean(this.hasGear);
        buf.writeFloat(this.spin);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.powerBuffer = buf.readLong();
        this.heat = buf.readInt();
        this.hasGear = buf.readBoolean();
        this.spin = buf.readFloat();
    }
}
