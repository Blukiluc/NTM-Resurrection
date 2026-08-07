package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderPress extends BlockEntityRendererNT<MachinePressBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachinePressBlockEntity> create(Context context) { return new RenderPress(); }

    @Override
    public void render(MachinePressBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int tPackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(2));
        RenderContext.setup(poseStack, tPackedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
        }

        bindTexture(ResourceManager.PRESS_TEX);
        ResourceManager.press.renderPart("Body");

        float interpolatedPress = Mth.lerp(partialTicks, (float) be.lastPress, (float) be.renderPress);
        float pressRatio = Mth.clamp(interpolatedPress / MachinePressBlockEntity.maxPress, 0F, 1F);

        float headAmplitude = 0.9F;
        float headY = 0.9F - pressRatio * headAmplitude;

        RenderContext.pushPose(); {
            RenderContext.translate(0F, headY, 0F);
            ResourceManager.press.renderPart("Head");
        } RenderContext.popPose();

        ItemStack ingredient = be.slots.get(0);
        if(!ingredient.isEmpty()) {
            RenderContext.pushPose(); {
                RenderContext.translate(0F, 1F, 0F);

                ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
                BakedModel model = renderer.getModel(ingredient, be.getLevel(), null, 0);

                if(!model.isGui3d()) {
                    RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                    RenderContext.scale(0.6F, 0.6F, 0.6F);
                } else {
                    RenderContext.scale(0.6F, 0.6F, 0.6F);
                    RenderContext.mulPose(Axis.ZP.rotationDegrees(90F));
                }

                renderer.render(ingredient, ItemDisplayContext.FIXED, false,
                        RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);
            } RenderContext.popPose();
        }

        RenderContext.end();
    }

    @Override
    public AABB getRenderBoundingBox(MachinePressBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x, y, z, x+1, y + 3, z+1);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_PRESS.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(6F, 6F, 6F);
            }

            @Override
            public void renderNonInv(ItemStack stack, MultiBufferSource buffer, boolean rightHand) {
                RenderContext.scale(1.25F, 1.25F, 1.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                bindTexture(ResourceManager.PRESS_TEX);
                ResourceManager.press.renderAll();
            }
        };
    }
}
