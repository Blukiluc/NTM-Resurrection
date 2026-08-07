package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineCentrifugeBlockEntity;
import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
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

public class RenderCentrifuge extends BlockEntityRendererNT<MachineCentrifugeBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineCentrifugeBlockEntity> create(Context context) { return new RenderCentrifuge(); }

    @Override
    public void render(MachineCentrifugeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int tPackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(3));
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

        RenderSystem.disableCull();
        bindTexture(ResourceManager.CENTRIFUGE_TEX);
        ResourceManager.centrifuge.renderAll();
        RenderSystem.enableCull();

        RenderContext.end();
    }

    @Override
    public AABB getRenderBoundingBox(MachineCentrifugeBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x, y, z, x+1, y + 4, z+1);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CENTRIFUGE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(4.5F, 4.5F, 4.5F);
            }

//            @Override
//            public void renderNonInv(ItemStack stack, MultiBufferSource buffer, boolean rightHand) {
//                RenderContext.scale(1.25F, 1.25F, 1.25F);
//            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                bindTexture(ResourceManager.CENTRIFUGE_TEX);
                ResourceManager.centrifuge.renderAll();
            }
        };
    }
}
