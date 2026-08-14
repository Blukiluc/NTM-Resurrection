package com.hbm.blockentity.network;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.Nodespace;
import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class PylonBaseBlockEntity extends LoadedBaseBlockEntity implements ITickable, IEnergyConductorMK2 {

    public enum ConnectionType {
        SINGLE,
        TRIPLE,
        QUADRUPLE
    }

    public final List<BlockPos> connected = new ArrayList<>();
    public int color = 0x7A3F28;
    protected PowerNode node;

    protected PylonBaseBlockEntity(BlockEntityType<? extends PylonBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract ConnectionType getConnectionType();
    public abstract int getMaxWireLength();
    public abstract Vec3[] getMountPositions();

    protected BlockPos[] getNodePositions() {
        return new BlockPos[] {this.worldPosition};
    }

    protected DirPos[] getLocalConnections() {
        return new DirPos[0];
    }

    @Override
    public PowerNode createNode() {
        List<DirPos> connections = new ArrayList<>();
        connections.add(new DirPos(this.worldPosition, null));
        for(BlockPos remote : this.connected) connections.add(new DirPos(remote, null));
        connections.addAll(List.of(this.getLocalConnections()));
        return new PowerNode(this.getNodePositions()).setConnections(connections.toArray(DirPos[]::new));
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;
        if(!this.level.isClientSide && (this.node == null || this.node.expired)) {
            this.node = Nodespace.getNode(this.level, this.worldPosition);
            if(this.node == null || this.node.expired) {
                this.node = this.createNode();
                Nodespace.createNode(this.level, this.node);
            }
            this.networkPackNT(Math.max(100, this.getMaxWireLength() + 25));
        }
    }

    public boolean canConnectTo(PylonBaseBlockEntity other) {
        if(other == this || other.getConnectionType() != this.getConnectionType()) return false;
        double range = Math.min(this.getMaxWireLength(), other.getMaxWireLength());
        return Vec3.atCenterOf(this.worldPosition).distanceTo(Vec3.atCenterOf(other.worldPosition)) <= range;
    }

    public boolean connectTo(PylonBaseBlockEntity other) {
        if(!this.canConnectTo(other) || this.connected.contains(other.worldPosition)) return false;
        this.connected.add(other.worldPosition.immutable());
        other.connected.add(this.worldPosition.immutable());
        this.refreshNode();
        other.refreshNode();
        this.setChanged();
        other.setChanged();
        this.networkPackNT(Math.max(100, this.getMaxWireLength() + 25));
        other.networkPackNT(Math.max(100, other.getMaxWireLength() + 25));
        return true;
    }

    protected void refreshNode() {
        if(this.level != null && !this.level.isClientSide && this.node != null) {
            Nodespace.destroyNode(this.level, this.worldPosition);
            this.node = null;
        }
    }

    public boolean applyDye(ItemStack stack, Player player) {
        if(!(stack.getItem() instanceof DyeItem dye)) return false;
        if(this.level != null && !this.level.isClientSide) {
            this.color = dye.getDyeColor().getTextureDiffuseColor();
            this.setChanged();
            this.networkPackNT(Math.max(100, this.getMaxWireLength() + 25));
            if(!player.hasInfiniteMaterials()) stack.shrink(1);
        }
        return true;
    }

    @Override
    public void setRemoved() {
        if(this.level != null && !this.level.isClientSide) {
            for(BlockPos remotePos : List.copyOf(this.connected)) {
                if(this.level.getBlockEntity(remotePos) instanceof PylonBaseBlockEntity remote) {
                    remote.connected.remove(this.worldPosition);
                    remote.refreshNode();
                    remote.setChanged();
                }
            }
            if(this.node != null) Nodespace.destroyNode(this.level, this.worldPosition);
        }
        super.setRemoved();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("color")) this.color = tag.getInt("color");
        this.connected.clear();
        for(long value : tag.getLongArray("connections")) this.connected.add(BlockPos.of(value));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("color", this.color);
        tag.putLongArray("connections", this.connected.stream().mapToLong(BlockPos::asLong).toArray());
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.color);
        buf.writeVarInt(this.connected.size());
        for(BlockPos pos : this.connected) buf.writeBlockPos(pos);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.color = buf.readInt();
        this.connected.clear();
        int count = buf.readVarInt();
        for(int i = 0; i < count; i++) this.connected.add(buf.readBlockPos());
    }
}
