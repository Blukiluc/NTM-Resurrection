package com.hbm.blockentity.machine.oil;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class FractioningSpacerBlockEntity extends BlockEntity {

    private AABB bb;

    public FractioningSpacerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FRACTION_SPACER.get(), pos, state);
    }

    public AABB getRenderBoundingBox() {
        if(this.bb == null) {
            BlockPos pos = this.getBlockPos();
            this.bb = new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 1, pos.getZ() + 2);
        }
        return this.bb;
    }

    public double getMaxRenderDistanceSquared() {
        return 65_536.0D;
    }
}
