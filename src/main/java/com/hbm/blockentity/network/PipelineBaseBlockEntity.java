package com.hbm.blockentity.network;

import api.hbm.fluidmk2.FluidNode;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class PipelineBaseBlockEntity extends PipeBaseBlockEntity {

    public enum ConnectionType {
        SMALL
    }

    public enum ConnectionResult {
        CONNECTED,
        INCOMPATIBLE,
        SAME_ANCHOR,
        TOO_FAR,
        FLUID_MISMATCH,
        ALREADY_CONNECTED
    }

    protected final List<BlockPos> connected = new ArrayList<>();

    protected PipelineBaseBlockEntity(BlockEntityType<? extends PipelineBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract ConnectionType getConnectionType();

    public abstract Vec3 getMountPos();

    public abstract int getMaxPipeLength();

    protected DirPos[] getLocalConnections() {
        return new DirPos[0];
    }

    public Vec3 getConnectionPoint() {
        return this.getMountPos().add(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());
    }

    public List<BlockPos> getConnected() {
        return this.connected;
    }

    @Override
    public FluidNode createNode(FluidType type) {
        List<DirPos> connections = new ArrayList<>();
        connections.add(new DirPos(this.worldPosition, null));
        connections.addAll(List.of(this.getLocalConnections()));
        for(BlockPos remote : this.connected) connections.add(new DirPos(remote, null));
        return new FluidNode(type.getNetworkProvider(), this.worldPosition).setConnections(connections.toArray(DirPos[]::new));
    }

    public ConnectionResult connectTo(PipelineBaseBlockEntity other) {
        if(other.getConnectionType() != this.getConnectionType()) return ConnectionResult.INCOMPATIBLE;
        if(other == this) return ConnectionResult.SAME_ANCHOR;
        if(this.connected.contains(other.worldPosition)) return ConnectionResult.ALREADY_CONNECTED;

        if(this.type == Fluids.NONE && other.type != Fluids.NONE) this.setFluidType(other.type);
        if(other.type == Fluids.NONE && this.type != Fluids.NONE) other.setFluidType(this.type);
        if(this.type != other.type) return ConnectionResult.FLUID_MISMATCH;

        double range = Math.min(this.getMaxPipeLength(), other.getMaxPipeLength());
        if(this.getConnectionPoint().distanceTo(other.getConnectionPoint()) > range) return ConnectionResult.TOO_FAR;

        this.connected.add(other.worldPosition.immutable());
        other.connected.add(this.worldPosition.immutable());
        this.refreshNode();
        other.refreshNode();
        this.setChanged();
        other.setChanged();
        this.networkPackNT(Math.max(100, this.getMaxPipeLength() + 25));
        other.networkPackNT(Math.max(100, other.getMaxPipeLength() + 25));
        return ConnectionResult.CONNECTED;
    }

    protected void refreshNode() {
        if(this.level != null && !this.level.isClientSide && this.node != null) {
            UniNodespace.destroyNode(this.level, this.worldPosition, this.type.getNetworkProvider());
            this.node = null;
        }
    }

    @Override
    public void setRemoved() {
        if(this.level != null && !this.level.isClientSide) {
            for(BlockPos remotePos : List.copyOf(this.connected)) {
                if(this.level.getBlockEntity(remotePos) instanceof PipelineBaseBlockEntity remote) {
                    remote.connected.remove(this.worldPosition);
                    remote.refreshNode();
                    remote.setChanged();
                    remote.networkPackNT(Math.max(100, remote.getMaxPipeLength() + 25));
                }
            }
        }
        super.setRemoved();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.connected.clear();
        for(long value : tag.getLongArray("connections")) this.connected.add(BlockPos.of(value));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLongArray("connections", this.connected.stream().mapToLong(BlockPos::asLong).toArray());
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeVarInt(this.connected.size());
        for(BlockPos pos : this.connected) buf.writeBlockPos(pos);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.connected.clear();
        int count = buf.readVarInt();
        for(int i = 0; i < count; i++) this.connected.add(buf.readBlockPos());
    }
}
