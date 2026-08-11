package com.hbm.blockentity.machine.foundry;

import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.items.machine.FoundryMoldItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class FoundryCastingBlockEntity extends FoundryBaseBlockEntity implements Container {

    protected final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    protected int cooloff;

    protected FoundryCastingBlockEntity(BlockEntityType<? extends FoundryCastingBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getMoldSize();

    public ItemStack getMold() {
        return this.items.get(0);
    }

    public ItemStack getResult() {
        return this.items.get(1);
    }

    @Override
    public int getCapacity() {
        return this.getMold().getItem() instanceof FoundryMoldItem mold && mold.getSize() == this.getMoldSize() ? mold.getCost() : 0;
    }

    @Override
    public boolean canAcceptPartialPour(Level level, Vec3 hit, Direction side, MaterialStack stack) {
        return side == Direction.UP && this.getResult().isEmpty() && super.canAcceptPartialPour(level, hit, side, stack);
    }

    @Override
    public boolean canAcceptPartialFlow(Level level, Direction side, MaterialStack stack) {
        return side == Direction.UP && this.getResult().isEmpty() && super.canAcceptPartialFlow(level, side, stack);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.level == null || this.level.isClientSide) return;

        int capacity = this.getCapacity();
        if (capacity > 0 && this.amount >= capacity && this.material != null && this.getResult().isEmpty()) {
            this.cooloff++;
            if (this.cooloff >= 200 && this.getMold().getItem() instanceof FoundryMoldItem mold) {
                ItemStack output = mold.getOutput(this.material);
                if (!output.isEmpty()) {
                    this.items.set(1, output);
                    this.amount = 0;
                    this.material = null;
                    this.cooloff = 0;
                    this.setChanged();
                }
            }
        } else {
            this.cooloff = 0;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("cooloff", this.cooloff);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.cooloff = tag.getInt("cooloff");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, this.items.get(0));
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, this.items.get(1));
        buf.writeVarInt(this.cooloff);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.items.set(0, ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        this.items.set(1, ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        this.cooloff = buf.readVarInt();
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if (!result.isEmpty()) this.setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == 0 && this.amount == 0 && stack.getItem() instanceof FoundryMoldItem mold && mold.getSize() == this.getMoldSize();
    }
}
