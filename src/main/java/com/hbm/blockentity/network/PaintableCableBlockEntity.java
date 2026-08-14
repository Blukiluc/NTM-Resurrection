package com.hbm.blockentity.network;

import com.hbm.interfaces.ICopiable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;

public class PaintableCableBlockEntity extends CableBaseBlockEntity implements ICopiable {

    public static final ModelProperty<BlockState> PAINTED_STATE = new ModelProperty<>();
    public static final ModelProperty<Boolean> PORTS_VISIBLE = new ModelProperty<>();

    @Nullable
    private BlockState paintedState;
    private boolean portsVisible = true;

    public PaintableCableBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.networkPackNT(150);
    }

    @Nullable
    public BlockState getPaintedState() {
        return this.paintedState;
    }

    public void setPaintedState(@Nullable BlockState paintedState) {
        this.paintedState = paintedState;
        this.syncModel();
    }

    public boolean arePortsVisible() {
        return this.portsVisible;
    }

    public void setPortsVisible(boolean portsVisible) {
        this.portsVisible = portsVisible;
        this.syncModel();
    }

    private void syncModel() {
        this.setChanged();
        this.requestModelDataUpdate();
        if(this.level != null) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    @Override
    public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder().with(PORTS_VISIBLE, this.portsVisible);
        if(this.paintedState != null) builder.with(PAINTED_STATE, this.paintedState);
        return builder.build();
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeVarInt(this.paintedState == null ? 0 : Block.getId(this.paintedState));
        buf.writeBoolean(this.portsVisible);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        int stateId = buf.readVarInt();
        BlockState state = Block.stateById(stateId);
        this.paintedState = stateId == 0 || state.isAir() ? null : state;
        this.portsVisible = buf.readBoolean();
        this.requestModelDataUpdate();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.paintedState = tag.contains("PaintedState")
                ? NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("PaintedState"))
                : null;
        if(this.paintedState != null && this.paintedState.is(Blocks.AIR)) this.paintedState = null;
        this.portsVisible = !tag.contains("PortsVisible") || tag.getBoolean("PortsVisible");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(this.paintedState != null) tag.put("PaintedState", NbtUtils.writeBlockState(this.paintedState));
        tag.putBoolean("PortsVisible", this.portsVisible);
    }

    @Override
    public CompoundTag getSettings(Level level, BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        if(this.paintedState != null) tag.put("PaintedState", NbtUtils.writeBlockState(this.paintedState));
        return tag;
    }

    @Override
    public void pasteSettings(CompoundTag tag, int index, Level level, Player player, BlockPos pos) {
        if(!tag.contains("PaintedState")) return;
        BlockState state = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), tag.getCompound("PaintedState"));
        this.paintedState = state != null && state.isAir() ? null : state;
        this.syncModel();
    }
}
