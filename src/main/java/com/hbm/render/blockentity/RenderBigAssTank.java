package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.storage.MachineBigAssTankBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.DiamondPronter;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class RenderBigAssTank extends BlockEntityRendererNT<MachineBigAssTankBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineBigAssTankBlockEntity> create(Context context) {
        return new RenderBigAssTank();
    }

    @Override
    public void render(MachineBigAssTankBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int tankPackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(5));
        RenderContext.setup(poseStack, tankPackedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);

        if(be.tilted) {
            RenderContext.translate(0F, -1F, 0F);
            RenderContext.mulPose(Axis.ZP.rotationDegrees(10F));
            RenderContext.mulPose(Axis.YP.rotationDegrees(5F));
        }

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
        }

        bindTexture(ResourceManager.BIG_ASS_TANK_TEX);
        ResourceManager.big_ass_tank.renderAll();

        FluidType type = be.tank.getTankType();
        if(type != Fluids.NONE) {
            RenderContext.pushPose();
            RenderContext.mulPose(Axis.YP.rotationDegrees(22.5F));
            for(int i = 0; i < 2; i++) {
                RenderContext.pushPose();
                RenderContext.translate(5.5F, 2F, 0F);
                DiamondPronter.pront(buffer, type.poison, type.flammability, type.reactivity, type.symbol);
                RenderContext.popPose();
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            RenderContext.popPose();

            renderFluid(be, buffer, type, partialTicks);
        }

        RenderContext.end();
    }

    private void renderFluid(MachineBigAssTankBlockEntity be, MultiBufferSource buffer, FluidType type, float partialTicks) {
        float height = be.tank.getFill() * 1.5F / be.tank.getMaxFill();
        float offset = 5.9375F;
        float scaleFactor = 0.5F;
        float minU = -((be.getLevel().getGameTime() % 250F + partialTicks) / 250F) % 1F;
        float maxU = minU + scaleFactor;
        float maxV = -height * 2F * scaleFactor;

        VertexConsumer consumer = buffer.getBuffer(NtmRenderTypes.entitySmoth(type.getTexture()));
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int light = RenderContext.light();
        int overlay = RenderContext.overlay();

        consumer.addVertex(matrix, -offset, 1.75F, -0.25F).setColor(1F, 1F, 1F, 1F).setUv(minU, 0F).setOverlay(overlay).setLight(light).setNormal(-1F, 0F, 0F);
        consumer.addVertex(matrix, -offset, 1.75F + height, -0.25F).setColor(1F, 1F, 1F, 1F).setUv(minU, maxV).setOverlay(overlay).setLight(light).setNormal(-1F, 0F, 0F);
        consumer.addVertex(matrix, -offset, 1.75F + height, 0.25F).setColor(1F, 1F, 1F, 1F).setUv(maxU, maxV).setOverlay(overlay).setLight(light).setNormal(-1F, 0F, 0F);
        consumer.addVertex(matrix, -offset, 1.75F, 0.25F).setColor(1F, 1F, 1F, 1F).setUv(maxU, 0F).setOverlay(overlay).setLight(light).setNormal(-1F, 0F, 0F);

        consumer.addVertex(matrix, offset, 1.75F, -0.25F).setColor(1F, 1F, 1F, 1F).setUv(maxU, 0F).setOverlay(overlay).setLight(light).setNormal(1F, 0F, 0F);
        consumer.addVertex(matrix, offset, 1.75F + height, -0.25F).setColor(1F, 1F, 1F, 1F).setUv(maxU, maxV).setOverlay(overlay).setLight(light).setNormal(1F, 0F, 0F);
        consumer.addVertex(matrix, offset, 1.75F + height, 0.25F).setColor(1F, 1F, 1F, 1F).setUv(minU, maxV).setOverlay(overlay).setLight(light).setNormal(1F, 0F, 0F);
        consumer.addVertex(matrix, offset, 1.75F, 0.25F).setColor(1F, 1F, 1F, 1F).setUv(minU, 0F).setOverlay(overlay).setLight(light).setNormal(1F, 0F, 0F);
    }

    @Override
    public AABB getRenderBoundingBox(MachineBigAssTankBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 6, y, z - 6, x + 7, y + 6, z + 7);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_BIG_ASS_TANK.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1F, 0F);
                RenderContext.scale(2.5F, 2.5F, 2.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                RenderSystem.enableCull();
                bindTexture(ResourceManager.BIG_ASS_TANK_TEX);
                ResourceManager.big_ass_tank.renderAll();
            }
        };
    }
}
