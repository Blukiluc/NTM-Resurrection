package com.hbm.render.blockentity;

import com.hbm.blockentity.network.PipeAnchorBlockEntity;
import com.hbm.blockentity.network.PipelineBaseBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.network.PipeAnchorBlock;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RenderPipeAnchor extends BlockEntityRendererNT<PipeAnchorBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<PipeAnchorBlockEntity> create(Context context) {
        return new RenderPipeAnchor();
    }

    @Override
    public void render(PipeAnchorBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        this.bindTexture(ResourceManager.PIPE_ANCHOR_TEX);

        RenderContext.pushPose();
        RenderContext.translate(0.5F, 0.5F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(PipeAnchorBlock.FACING));
        RenderContext.translate(0F, -0.5F, 0F);
        ResourceManager.pipe_anchor.renderPart("Anchor");
        RenderContext.popPose();

        if(be.getLevel() == null) return;

        for(BlockPos remotePos : be.getConnected()) {
            BlockEntity remoteEntity = be.getLevel().getBlockEntity(remotePos);
            if(!(remoteEntity instanceof PipelineBaseBlockEntity remote) || be.getFluidType() != remote.getFluidType()) continue;

            Vec3 start = be.getConnectionPoint();
            Vec3 end = remote.getConnectionPoint();
            if(!isDominant(start, end)) continue;

            double deltaX = end.x - start.x;
            double deltaY = end.y - start.y;
            double deltaZ = end.z - start.z;
            double horizontal = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            double yaw = Math.toDegrees(Math.atan2(deltaX, deltaZ));
            double pitch = Math.toDegrees(Math.atan2(deltaY, horizontal));
            double length = start.distanceTo(end);

            RenderContext.pushPose();
            RenderContext.translate(0.5F, 0.5F, 0.5F);
            RenderContext.mulPose(Axis.YP.rotationDegrees((float)yaw));
            RenderContext.mulPose(Axis.XP.rotationDegrees((float)(90D - pitch)));

            RenderContext.pushPose();
            RenderContext.scale(1F, (float)length, 1F);
            RenderContext.translate(0F, -0.5F, 0F);
            int color = be.getFluidType().getColor();
            RenderContext.setColor(lighten((color >> 16) & 255), lighten((color >> 8) & 255), lighten(color & 255), 1F);
            ResourceManager.pipe_anchor.renderPart("Pipe");
            RenderContext.setColor(1F, 1F, 1F, 1F);
            RenderContext.popPose();

            RenderContext.pushPose();
            RenderContext.translate(0F, (float)(length / 2D - 1.5D), 0F);
            ResourceManager.pipe_anchor.renderPart("Ring");
            RenderContext.popPose();

            RenderContext.popPose();
        }
    }

    private static float lighten(int component) {
        return (float)(component + (255 - component) * 0.25D) / 255F;
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case DOWN -> RenderContext.mulPose(Axis.XP.rotationDegrees(180F));
            case NORTH -> {
                RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.ZP.rotationDegrees(180F));
            }
            case SOUTH -> RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
            case WEST -> {
                RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.ZP.rotationDegrees(90F));
            }
            case EAST -> {
                RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.ZP.rotationDegrees(270F));
            }
            default -> { }
        }
    }

    public static boolean isDominant(Vec3 first, Vec3 second) {
        if(first.x < second.x) return true;
        if(first.x > second.x) return false;
        if(first.y < second.y) return true;
        if(first.y > second.y) return false;
        return first.z < second.z;
    }

    @Override
    public AABB getRenderBoundingBox(PipeAnchorBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int range = be.getMaxPipeLength() + 1;
        return new AABB(x - range, y - range, z - range, x + range + 1, y + range + 1, z + range + 1);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.PIPE_ANCHOR.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -3.5F, 0F);
                RenderContext.scale(10F, 10F, 10F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.PIPE_ANCHOR_TEX);
                ResourceManager.pipe_anchor.renderPart("Anchor");
            }
        };
    }
}
