package com.hbm.blockentity.machine.foundry;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats.MaterialStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class FoundryChannelBlockEntity extends FoundryBaseBlockEntity {

    public FoundryChannelBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FOUNDRY_CHANNEL.get(), pos, state);
    }

    @Override
    public int getCapacity() {
        return MaterialShapes.INGOT.q(2);
    }

    @Override
    protected boolean canAccept(MaterialStack stack) {
        return super.canAccept(stack) && this.networkAccepts(stack);
    }

    private boolean networkAccepts(MaterialStack stack) {
        if (this.level == null) return false;
        ArrayDeque<BlockPos> open = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        open.add(this.worldPosition);

        while (!open.isEmpty()) {
            BlockPos current = open.removeFirst();
            if (!visited.add(current)) continue;
            BlockEntity be = this.level.getBlockEntity(current);
            if (!(be instanceof FoundryChannelBlockEntity channel)) continue;
            if (channel.material != null && channel.material != stack.material) return false;
            for (Direction direction : Direction.Plane.HORIZONTAL) open.add(current.relative(direction));
        }
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.level == null || this.level.isClientSide || this.amount <= 0 || this.level.getGameTime() % 5 != 0) return;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.material == null || this.amount <= 0) return;
            BlockEntity target = this.level.getBlockEntity(this.worldPosition.relative(direction));
            if (target instanceof api.hbm.block.ICrucibleAcceptor acceptor && !(target instanceof FoundryChannelBlockEntity)) {
                int transfer = Math.min(MaterialShapes.NUGGET.q(1), this.amount);
                MaterialStack moving = new MaterialStack(this.material, transfer);
                MaterialStack left = acceptor.flow(this.level, direction.getOpposite(), moving);
                int accepted = transfer - (left == null ? 0 : left.amount);
                if (accepted > 0) this.removeMaterial(accepted);
            }
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.material == null || this.amount <= 0) return;
            BlockEntity target = this.level.getBlockEntity(this.worldPosition.relative(direction));
            if (target instanceof FoundryChannelBlockEntity channel && channel.material == this.material && channel.amount + 1 < this.amount) {
                int transfer = Math.min(MaterialShapes.NUGGET.q(1), (this.amount - channel.amount) / 2);
                if (transfer > 0) {
                    MaterialStack left = channel.accept(new MaterialStack(this.material, transfer));
                    this.removeMaterial(transfer - (left == null ? 0 : left.amount));
                }
            }
        }
    }
}
