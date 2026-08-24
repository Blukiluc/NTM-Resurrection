package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineSawmillBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderSawmill extends BlockEntityRendererNT<MachineSawmillBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineSawmillBlockEntity> create(Context context) {
        return new RenderSawmill();
    }

    @Override
    public void render(MachineSawmillBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        this.renderCommon(Mth.lerp(partialTicks, be.lastBladeRotation, be.bladeRotation), be.hasBlade);
    }

    private void renderCommon(float rotation, boolean hasBlade) {
        bindTexture(ResourceManager.SAWMILL_TEX);
        ResourceManager.sawmill.renderPart("Main");

        if(hasBlade) {
            RenderContext.pushPose();
            RenderContext.translate(0F, 1.375F, 0F);
            RenderContext.mulPose(Axis.ZN.rotationDegrees(rotation * 2F));
            RenderContext.translate(0F, -1.375F, 0F);
            ResourceManager.sawmill.renderPart("Blade");
            RenderContext.popPose();
        }

        RenderContext.pushPose();
        RenderContext.translate(0.5625F, 1.375F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(rotation));
        RenderContext.translate(-0.5625F, -1.375F, 0F);
        ResourceManager.sawmill.renderPart("GearLeft");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(-0.5625F, 1.375F, 0F);
        RenderContext.mulPose(Axis.ZN.rotationDegrees(rotation));
        RenderContext.translate(0.5625F, -1.375F, 0F);
        ResourceManager.sawmill.renderPart("GearRight");
        RenderContext.popPose();
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }
    }

    @Override
    public AABB getRenderBoundingBox(MachineSawmillBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_SAWMILL.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderSawmill.this.renderCommon((System.currentTimeMillis() % 3_600L) * 0.1F, true);
            }
        };
    }
}
