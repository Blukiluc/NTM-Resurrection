package com.hbm.render.blockentity;

import api.hbm.block.ICrucibleAcceptor;
import com.hbm.blockentity.machine.foundry.FoundryBaseBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryOutletBlockEntity;
import com.hbm.blocks.machine.foundry.FoundryOutletBlock;
import com.hbm.inventory.material.Mats.MaterialStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RenderFoundryOutlet<T extends FoundryOutletBlockEntity> extends BlockEntityRendererNT<T> {

    @Override
    public BlockEntityRenderer<T> create(Context context) {
        return new RenderFoundryOutlet<>();
    }

    @Override
    public void render(T outlet, MultiBufferSource buffer, float partialTicks) {
        if (outlet.getLevel() == null) return;

        Direction facing = outlet.getBlockState().getValue(FoundryOutletBlock.FACING);
        BlockEntity source = outlet.getLevel().getBlockEntity(outlet.getBlockPos().relative(facing.getOpposite()));
        if (!(source instanceof FoundryBaseBlockEntity foundry) || foundry.getMaterial() == null || foundry.getAmount() <= 0) return;

        MaterialStack sample = new MaterialStack(foundry.getMaterial(), 1);

        int color = foundry.getMaterial().moltenColor;
        float fluidHeight = RenderFoundry.getFluidSurfaceHeight(foundry);
        switch (facing) {
            case NORTH -> RenderFoundry.renderSurface(buffer, 0.375F, 0.625F, 0.625F, 1F, fluidHeight, color, 1F);
            case SOUTH -> RenderFoundry.renderSurface(buffer, 0.375F, 0F, 0.625F, 0.375F, fluidHeight, color, 1F);
            case EAST -> RenderFoundry.renderSurface(buffer, 0F, 0.375F, 0.375F, 0.625F, fluidHeight, color, 1F);
            case WEST -> RenderFoundry.renderSurface(buffer, 0.625F, 0.375F, 1F, 0.625F, fluidHeight, color, 1F);
            default -> { }
        }

        FlowTarget target = outlet.canAcceptPartialFlow(outlet.getLevel(), facing.getOpposite(), sample)
                ? this.findFlowTarget(outlet, sample) : null;
        if (target != null) {
            RenderFoundry.renderFallingStream(buffer, fluidHeight - 0.001F,
                    -target.distance + target.surfaceHeight + 0.02F, color);
        } else {
            RenderFoundry.renderStoppedFaucetFace(buffer, color, facing, fluidHeight);
        }
    }

    private FlowTarget findFlowTarget(T outlet, MaterialStack sample) {
        for (int distance = 1; distance <= 4; distance++) {
            BlockPos targetPos = outlet.getBlockPos().below(distance);
            BlockEntity target = outlet.getLevel().getBlockEntity(targetPos);
            if (target instanceof ICrucibleAcceptor acceptor) {
                Vec3 hit = Vec3.atCenterOf(targetPos).add(0, 0.5, 0);
                if (!acceptor.canAcceptPartialPour(outlet.getLevel(), hit, Direction.UP, sample)) return null;

                float surfaceHeight = target instanceof FoundryBaseBlockEntity foundry
                        ? RenderFoundry.getFluidSurfaceHeight(foundry) : 0.875F;
                return new FlowTarget(distance, surfaceHeight);
            }

            BlockState state = outlet.getLevel().getBlockState(targetPos);
            if (!state.getFluidState().isEmpty() || !state.getCollisionShape(outlet.getLevel(), targetPos).isEmpty()) return null;
        }
        return null;
    }

    private record FlowTarget(int distance, float surfaceHeight) { }

    @Override
    public AABB getRenderBoundingBox(T be) {
        BlockPos pos = be.getBlockPos();
        return new AABB(pos.getX(), pos.getY() - 4, pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }
}
