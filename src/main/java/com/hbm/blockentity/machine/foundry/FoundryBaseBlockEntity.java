package com.hbm.blockentity.machine.foundry;

import api.hbm.block.ICrucibleAcceptor;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.material.NTMMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class FoundryBaseBlockEntity extends LoadedBaseBlockEntity implements ICrucibleAcceptor, ITickable {

    protected NTMMaterial material;
    protected int amount;

    protected FoundryBaseBlockEntity(BlockEntityType<? extends FoundryBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getCapacity();

    public NTMMaterial getMaterial() {
        return this.material;
    }

    public int getAmount() {
        return this.amount;
    }

    public float getFill() {
        int capacity = this.getCapacity();
        return capacity <= 0 ? 0F : (float) this.amount / capacity;
    }

    public MaterialStack removeMaterial(int requested) {
        if (this.material == null || this.amount <= 0 || requested <= 0) return null;
        int removed = Math.min(requested, this.amount);
        MaterialStack stack = new MaterialStack(this.material, removed);
        this.amount -= removed;
        if (this.amount <= 0) {
            this.amount = 0;
            this.material = null;
        }
        this.setChanged();
        return stack;
    }

    protected boolean canAccept(MaterialStack stack) {
        return stack != null && stack.material != null && stack.amount > 0
                && (this.material == null || this.material == stack.material)
                && this.amount < this.getCapacity();
    }

    protected MaterialStack accept(MaterialStack stack) {
        if (!this.canAccept(stack)) return stack;
        int accepted = Math.min(stack.amount, this.getCapacity() - this.amount);
        this.material = stack.material;
        this.amount += accepted;
        stack.amount -= accepted;
        this.setChanged();
        return stack.amount > 0 ? stack : null;
    }

    @Override
    public boolean canAcceptPartialPour(Level level, Vec3 hit, Direction side, MaterialStack stack) {
        return side == Direction.UP && this.canAccept(stack);
    }

    @Override
    public MaterialStack pour(Level level, Vec3 hit, Direction side, MaterialStack stack) {
        return this.canAcceptPartialPour(level, hit, side, stack) ? this.accept(stack) : stack;
    }

    @Override
    public boolean canAcceptPartialFlow(Level level, Direction side, MaterialStack stack) {
        return this.canAccept(stack);
    }

    @Override
    public MaterialStack flow(Level level, Direction side, MaterialStack stack) {
        return this.canAcceptPartialFlow(level, side, stack) ? this.accept(stack) : stack;
    }

    @Override
    public void updateEntity() {
        if (this.level == null) return;
        if (!this.level.isClientSide) this.networkPackNT(32);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.material != null) tag.putInt("material", this.material.id);
        tag.putInt("amount", this.amount);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.material = Mats.matById.get(tag.getInt("material"));
        this.amount = this.material == null ? 0 : Math.max(0, tag.getInt("amount"));
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.material == null ? -1 : this.material.id);
        buf.writeVarInt(this.amount);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.material = Mats.matById.get(buf.readInt());
        int receivedAmount = buf.readVarInt();
        this.amount = this.material == null ? 0 : receivedAmount;
    }
}
