package com.hbm.blockentity.machine.foundry;

import api.hbm.block.ICrucibleAcceptor;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.machine.foundry.FoundryOutletBlock;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.material.NTMMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FoundryOutletBlockEntity extends LoadedBaseBlockEntity implements ICrucibleAcceptor {

    protected NTMMaterial filter;
    protected boolean invertFilter;
    protected boolean invertRedstone;

    public FoundryOutletBlockEntity(BlockPos pos, BlockState state) {
        this(NtmBlockEntityTypes.FOUNDRY_OUTLET.get(), pos, state);
    }

    protected FoundryOutletBlockEntity(BlockEntityType<? extends FoundryOutletBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public NTMMaterial getFilter() {
        return this.filter;
    }

    public boolean isInvertFilter() {
        return this.invertFilter;
    }

    public boolean isInvertRedstone() {
        return this.invertRedstone;
    }

    public boolean isClosed() {
        if (this.level == null) return false;
        if (this.level.isClientSide && this.getBlockState().hasProperty(FoundryOutletBlock.CLOSED)) {
            return this.getBlockState().getValue(FoundryOutletBlock.CLOSED);
        }
        return this.invertRedstone ^ this.level.hasNeighborSignal(this.worldPosition);
    }

    public void setFilter(NTMMaterial filter) {
        this.filter = filter;
        this.setChanged();
        this.networkPackNT(32);
    }

    public void clearFilter() {
        this.filter = null;
        this.invertFilter = false;
        this.setChanged();
        this.networkPackNT(32);
    }

    public void toggleFilter() {
        this.invertFilter = !this.invertFilter;
        this.setChanged();
        this.networkPackNT(32);
    }

    public void toggleRedstone() {
        this.invertRedstone = !this.invertRedstone;
        this.setChanged();
        this.syncClosedState();
    }

    public void syncClosedState() {
        if (this.level == null || this.level.isClientSide) return;
        BlockState state = this.getBlockState();
        boolean closed = this.invertRedstone ^ this.level.hasNeighborSignal(this.worldPosition);
        if (state.hasProperty(FoundryOutletBlock.CLOSED) && state.getValue(FoundryOutletBlock.CLOSED) != closed) {
            this.level.setBlock(this.worldPosition, state.setValue(FoundryOutletBlock.CLOSED, closed), Block.UPDATE_CLIENTS);
        }
        this.networkPackNT(32);
    }

    protected boolean allows(MaterialStack stack) {
        if (this.level == null || stack == null || stack.material == null || stack.amount <= 0) return false;
        Direction facing = this.getBlockState().getValue(FoundryOutletBlock.FACING);
        boolean materialAllowed = this.filter == null || ((this.filter == stack.material) != this.invertFilter);
        return !this.isClosed() && materialAllowed && facing != Direction.UP && facing != Direction.DOWN;
    }

    protected MaterialStack sendDown(MaterialStack stack, int range) {
        if (this.level == null) return stack;
        for (int i = 1; i <= range; i++) {
            BlockPos targetPos = this.worldPosition.below(i);
            BlockEntity target = this.level.getBlockEntity(targetPos);
            if (target instanceof ICrucibleAcceptor acceptor) {
                Vec3 hit = Vec3.atCenterOf(targetPos).add(0, 0.5, 0);
                return acceptor.canAcceptPartialPour(this.level, hit, Direction.UP, stack)
                        ? acceptor.pour(this.level, hit, Direction.UP, stack) : stack;
            }
            BlockState state = this.level.getBlockState(targetPos);
            if (!state.getFluidState().isEmpty() || !state.getCollisionShape(this.level, targetPos).isEmpty()) break;
        }
        return stack;
    }

    @Override
    public boolean canAcceptPartialPour(Level level, Vec3 hit, Direction side, MaterialStack stack) {
        return false;
    }

    @Override
    public MaterialStack pour(Level level, Vec3 hit, Direction side, MaterialStack stack) {
        return stack;
    }

    @Override
    public boolean canAcceptPartialFlow(Level level, Direction side, MaterialStack stack) {
        Direction facing = this.getBlockState().getValue(FoundryOutletBlock.FACING);
        return side == facing.getOpposite() && this.allows(stack);
    }

    @Override
    public MaterialStack flow(Level level, Direction side, MaterialStack stack) {
        return this.canAcceptPartialFlow(level, side, stack) ? this.sendDown(stack, 4) : stack;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.filter != null) tag.putString("filterName", this.filter.getCanonicalName());
        tag.putBoolean("invertFilter", this.invertFilter);
        tag.putBoolean("invertRedstone", this.invertRedstone);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.filter = tag.contains("filterName")
                ? Mats.matByName.get(tag.getString("filterName"))
                : tag.contains("filter") ? Mats.matById.get(tag.getInt("filter")) : null;
        this.invertFilter = tag.getBoolean("invertFilter");
        this.invertRedstone = tag.getBoolean("invertRedstone");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeUtf(this.filter == null ? "" : this.filter.getCanonicalName());
        buf.writeBoolean(this.invertFilter);
        buf.writeBoolean(this.invertRedstone);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.filter = Mats.matByName.get(buf.readUtf());
        this.invertFilter = buf.readBoolean();
        this.invertRedstone = buf.readBoolean();
    }
}
