package com.hbm.blockentity.machine.foundry;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.foundry.DynamicSlagBlock;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.material.NTMMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SlagBlockEntity extends LoadedBaseBlockEntity implements ITickable {

    private NTMMaterial material;
    private int amount;

    public SlagBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MOLTEN_SLAG.get(), pos, state);
    }

    public NTMMaterial getMaterial() {
        return this.material;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean canAccept(MaterialStack stack) {
        return stack != null && stack.material != null && stack.amount > 0
                && (this.material == null || this.material == stack.material)
                && this.amount < MaterialShapes.BLOCK.q(16);
    }

    public MaterialStack accept(MaterialStack stack) {
        if (!this.canAccept(stack)) return stack;
        int accepted = Math.min(stack.amount, MaterialShapes.BLOCK.q(16) - this.amount);
        this.material = stack.material;
        this.amount += accepted;
        stack.amount -= accepted;
        this.updateState();
        this.setChanged();
        return stack.amount > 0 ? stack : null;
    }

    public MaterialStack removeMaterial(int requested) {
        if (this.material == null || this.amount <= 0 || requested <= 0) return null;
        int removed = Math.min(requested, this.amount);
        MaterialStack result = new MaterialStack(this.material, removed);
        this.amount -= removed;
        if (this.amount <= 0) {
            this.amount = 0;
            this.material = null;
        }
        this.updateState();
        this.setChanged();
        return result;
    }

    @Override
    public void updateEntity() {
        if (this.level == null || this.level.isClientSide) return;
        this.networkPackNT(32);
        if (this.material == null || this.amount <= 0 || this.level.getGameTime() % 5 != 0) return;

        BlockPos belowPos = this.worldPosition.below();
        BlockEntity below = this.level.getBlockEntity(belowPos);
        if (below instanceof SlagBlockEntity slag && slag.canAccept(new MaterialStack(this.material, 1))) {
            MaterialStack moving = this.removeMaterial(this.amount);
            MaterialStack left = slag.accept(moving);
            if (left != null) this.accept(left);
        } else if (this.level.getBlockState(belowPos).canBeReplaced()) {
            MaterialStack moving = this.removeMaterial(this.amount);
            this.level.setBlock(belowPos, NtmBlocks.MOLTEN_SLAG.get().defaultBlockState(), 3);
            if (this.level.getBlockEntity(belowPos) instanceof SlagBlockEntity slag) slag.accept(moving);
            this.level.removeBlock(this.worldPosition, false);
            return;
        }

        if (this.amount > MaterialShapes.BLOCK.q(1)) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos target = this.worldPosition.relative(direction);
                BlockEntity be = this.level.getBlockEntity(target);
                if (be instanceof SlagBlockEntity slag && slag.canAccept(new MaterialStack(this.material, 1))) {
                    MaterialStack moving = this.removeMaterial(Math.min(MaterialShapes.BLOCK.q(1), this.amount - MaterialShapes.BLOCK.q(1)));
                    MaterialStack left = slag.accept(moving);
                    if (left != null) this.accept(left);
                    break;
                }
                if (this.level.getBlockState(target).canBeReplaced()) {
                    MaterialStack moving = this.removeMaterial(Math.min(MaterialShapes.BLOCK.q(1), this.amount - MaterialShapes.BLOCK.q(1)));
                    this.level.setBlock(target, NtmBlocks.MOLTEN_SLAG.get().defaultBlockState(), 3);
                    if (this.level.getBlockEntity(target) instanceof SlagBlockEntity slag) slag.accept(moving);
                    break;
                }
            }
        }
    }

    private void updateState() {
        if (this.level == null || this.amount <= 0 || !this.getBlockState().hasProperty(DynamicSlagBlock.LAYERS)) return;
        int layers = Math.max(1, Math.min(16, (this.amount + MaterialShapes.BLOCK.q(1) - 1) / MaterialShapes.BLOCK.q(1)));
        if (this.getBlockState().getValue(DynamicSlagBlock.LAYERS) != layers) {
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(DynamicSlagBlock.LAYERS, layers), 3);
        }
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
        this.amount = this.material == null ? 0 : tag.getInt("amount");
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
