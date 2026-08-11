package com.hbm.blockentity.machine.foundry;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.material.Mats.MaterialStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FoundrySlagtapBlockEntity extends FoundryOutletBlockEntity {

    public FoundrySlagtapBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FOUNDRY_SLAGTAP.get(), pos, state);
    }

    @Override
    public MaterialStack flow(net.minecraft.world.level.Level level, Direction side, MaterialStack stack) {
        if (!this.canAcceptPartialFlow(level, side, stack)) return stack;

        BlockPos placement = null;
        for (int i = 1; i <= 15; i++) {
            BlockPos target = this.worldPosition.below(i);
            BlockEntity be = level.getBlockEntity(target);
            if (be instanceof SlagBlockEntity slag && slag.canAccept(stack)) return slag.accept(stack);
            if (!level.getBlockState(target).canBeReplaced()) {
                placement = target.above();
                break;
            }
        }

        if (placement != null && placement.getY() < this.worldPosition.getY() && level.getBlockState(placement).canBeReplaced()) {
            level.setBlock(placement, NtmBlocks.MOLTEN_SLAG.get().defaultBlockState(), 3);
            if (level.getBlockEntity(placement) instanceof SlagBlockEntity slag) return slag.accept(stack);
        }
        return stack;
    }
}
