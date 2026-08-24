package com.hbm.blockentity.machine.foundry;

import api.hbm.block.ICrucibleAcceptor;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats.MaterialStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FoundryTankBlockEntity extends FoundryBaseBlockEntity {

    public FoundryTankBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FOUNDRY_TANK.get(), pos, state);
    }

    @Override
    public int getCapacity() {
        return MaterialShapes.BLOCK.q(4);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.level == null || this.level.isClientSide || this.amount <= 0 || this.level.getGameTime() % 5 != 0) return;

        BlockEntity below = this.level.getBlockEntity(this.worldPosition.below());
        if (below instanceof ICrucibleAcceptor acceptor) this.transfer(acceptor, Direction.UP, Math.min(MaterialShapes.INGOT.q(1), this.amount));
        if (this.material == null || this.amount <= 0) return;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.material == null || this.amount <= 0) return;
            BlockEntity target = this.level.getBlockEntity(this.worldPosition.relative(direction));
            if (target instanceof ICrucibleAcceptor acceptor && !(target instanceof FoundryTankBlockEntity)) {
                this.transfer(acceptor, direction.getOpposite(), Math.min(MaterialShapes.NUGGET.q(1), this.amount));
            } else if (target instanceof FoundryTankBlockEntity tank && tank.material == this.material && tank.amount + 1 < this.amount) {
                int transfer = Math.min(MaterialShapes.NUGGET.q(1), (this.amount - tank.amount) / 2);
                MaterialStack left = tank.accept(new MaterialStack(this.material, transfer));
                this.removeMaterial(transfer - (left == null ? 0 : left.amount));
            }
        }
    }

    private void transfer(ICrucibleAcceptor acceptor, Direction side, int transfer) {
        if (transfer <= 0) return;
        MaterialStack left = acceptor.flow(this.level, side, new MaterialStack(this.material, transfer));
        this.removeMaterial(transfer - (left == null ? 0 : left.amount));
    }
}
