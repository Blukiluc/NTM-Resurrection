package com.hbm.blockentity.machine.turbine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class MachineSteamEngineBlockEntity extends AbstractTurbineBlockEntity {

    public float rotor;
    public float lastRotor;
    public float acceleration;

    public MachineSteamEngineBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.STEAM_ENGINE.get(), pos, state, 2_000, 20);
    }

    @Override
    protected double getEfficiency() {
        return 0.85D;
    }

    @Override
    protected double getConsumptionPercent() {
        return 1D;
    }

    @Override
    protected DirPos[] getConnectionPositions() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();
        BlockPos center = this.worldPosition.relative(side, 2).above();
        return new DirPos[] {
                new DirPos(center, side),
                new DirPos(center.relative(facing), side),
                new DirPos(center.relative(facing.getOpposite()), side)
        };
    }

    @Override
    protected DirPos[] getPowerPositions() {
        return this.getConnectionPositions();
    }

    @Override
    protected void onServerTick() {
        if(this.operational) this.acceleration = Math.min(40F, this.acceleration + 0.1F);
        else this.acceleration = Math.max(0F, this.acceleration - 0.1F);
        this.lastRotor = this.rotor;
        this.rotor = (this.rotor + this.acceleration) % 360F;
    }

    @Override
    protected void onClientTick() {
        this.lastRotor = this.rotor;
        this.rotor = (this.rotor + this.acceleration) % 360F;
    }

    @Override
    public boolean canConnect(Direction direction) {
        return direction != null && direction.getAxis().isHorizontal();
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeFloat(this.rotor);
        buf.writeFloat(this.acceleration);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.rotor = buf.readFloat();
        this.acceleration = buf.readFloat();
    }
}
