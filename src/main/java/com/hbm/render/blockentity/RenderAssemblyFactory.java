package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineAssemblyFactoryBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class RenderAssemblyFactory extends BlockEntityRendererNT<MachineAssemblyFactoryBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineAssemblyFactoryBlockEntity> create(Context context) {
        return new RenderAssemblyFactory();
    }

    @Override
    public void render(MachineAssemblyFactoryBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int topLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(2));
        RenderContext.setup(poseStack, topLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }

        bindTexture(ResourceManager.ASSEMBLY_FACTORY_TEX);
        ResourceManager.assembly_factory.renderPart("Base");
        if(be.frame) ResourceManager.assembly_factory.renderPart("Frame");

        float slide1 = be.animations[0].getSlider(partialTicks);
        float slide2 = be.animations[1].getSlider(partialTicks);
        float[] arm1 = be.animations[0].striker.getPositions(partialTicks);
        float[] arm2 = be.animations[0].saw.getPositions(partialTicks);
        float[] arm3 = be.animations[1].striker.getPositions(partialTicks);
        float[] arm4 = be.animations[1].saw.getPositions(partialTicks);

        RenderContext.pushPose();
        RenderContext.translate(0.5F - slide1, 0F, 0F);
        ResourceManager.assembly_factory.renderPart("Slider1");
        RenderContext.translate(0F, 1.625F, -0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(-arm1[0]));
        RenderContext.translate(0F, -1.625F, 0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmLower1");
        RenderContext.translate(0F, 2.375F, -0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(-arm1[1]));
        RenderContext.translate(0F, -2.375F, 0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmUpper1");
        RenderContext.translate(0F, 2.375F, -0.4375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(-arm1[2]));
        RenderContext.translate(0F, -2.375F, 0.4375F);
        ResourceManager.assembly_factory.renderPart("Head1");
        RenderContext.translate(0F, arm1[3], 0F);
        ResourceManager.assembly_factory.renderPart("Striker1");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(-0.5F + slide1, 0F, 0F);
        ResourceManager.assembly_factory.renderPart("Slider2");
        RenderContext.translate(0F, 1.625F, 0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm2[0]));
        RenderContext.translate(0F, -1.625F, -0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmLower2");
        RenderContext.translate(0F, 2.375F, 0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm2[1]));
        RenderContext.translate(0F, -2.375F, -0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmUpper2");
        RenderContext.translate(0F, 2.375F, 0.4375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm2[2]));
        RenderContext.translate(0F, -2.375F, -0.4375F);
        ResourceManager.assembly_factory.renderPart("Head2");
        RenderContext.translate(0F, arm2[3], 0F);
        ResourceManager.assembly_factory.renderPart("Striker2");
        RenderContext.translate(0F, 1.625F, 0.3125F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(-arm2[4]));
        RenderContext.translate(0F, -1.625F, -0.3125F);
        ResourceManager.assembly_factory.renderPart("Blade2");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(-0.5F + slide2, 0F, 0F);
        ResourceManager.assembly_factory.renderPart("Slider3");
        RenderContext.translate(0F, 1.625F, 0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm3[0]));
        RenderContext.translate(0F, -1.625F, -0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmLower3");
        RenderContext.translate(0F, 2.375F, 0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm3[1]));
        RenderContext.translate(0F, -2.375F, -0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmUpper3");
        RenderContext.translate(0F, 2.375F, 0.4375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm3[2]));
        RenderContext.translate(0F, -2.375F, -0.4375F);
        ResourceManager.assembly_factory.renderPart("Head3");
        RenderContext.translate(0F, arm3[3], 0F);
        ResourceManager.assembly_factory.renderPart("Striker3");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0.5F - slide2, 0F, 0F);
        ResourceManager.assembly_factory.renderPart("Slider4");
        RenderContext.translate(0F, 1.625F, -0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(-arm4[0]));
        RenderContext.translate(0F, -1.625F, 0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmLower4");
        RenderContext.translate(0F, 2.375F, -0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(-arm4[1]));
        RenderContext.translate(0F, -2.375F, 0.9375F);
        ResourceManager.assembly_factory.renderPart("ArmUpper4");
        RenderContext.translate(0F, 2.375F, -0.4375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(-arm4[2]));
        RenderContext.translate(0F, -2.375F, 0.4375F);
        ResourceManager.assembly_factory.renderPart("Head4");
        RenderContext.translate(0F, arm4[3], 0F);
        ResourceManager.assembly_factory.renderPart("Striker4");
        RenderContext.translate(0F, 1.625F, -0.3125F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm4[4]));
        RenderContext.translate(0F, -1.625F, 0.3125F);
        ResourceManager.assembly_factory.renderPart("Blade4");
        RenderContext.popPose();

        if(NuclearTechMod.proxy.me().distanceToSqr(be.getBlockPos().getBottomCenter().add(0, 1, 0)) < 35 * 35) {
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            for(int i = 0; i < 4; i++) {
                GenericRecipe recipe = be.assemblerModule[i].getRecipe();
                if(recipe == null) continue;

                RenderContext.pushPose();
                RenderContext.translate(1.5F - i, 0F, 0F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.translate(0F, 1.0625F, 0F);

                ItemStack stack = recipe.getIcon();
                BakedModel model = renderer.getModel(stack, null, null, 0);
                if(!model.isGui3d()) {
                    RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));
                    RenderContext.translate(0F, -0.25F, 0F);
                } else {
                    RenderContext.translate(0F, -0.0625F, 0F);
                    RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                }
                RenderContext.scale(1.25F, 1.25F, 1.25F);
                renderer.render(stack, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);
                RenderContext.popPose();
            }

            renderSparks(be, partialTicks, buffer, arm2, arm4, slide1, slide2);
        }

        RenderContext.end();
    }

    private void renderSparks(MachineAssemblyFactoryBlockEntity be, float partialTicks, MultiBufferSource buffer, float[] arm2, float[] arm4, float slide1, float slide2) {
        VertexConsumer consumer = buffer.getBuffer(NtmRenderTypes.entitySmoth(ResourceManager.ASSEMBLY_FACTORY_SPARKS_TEX));
        float wide = 0.1875F;
        float narrow = 0F;
        float length = 1.25F;
        float uMin = (float) ((be.getLevel().getGameTime() / 10D + partialTicks) % 10D);
        float uMax = uMin + 1F;
        float epsilon = 0.01F;

        // renders two layers of sparks, one with regular UV and one with mirrored +0.5 offset
        // render left and right of the blade with small offset to eliminate z-fighting
        if(arm2[3] <= -0.375F) {
            RenderContext.pushPose();
            RenderContext.translate(0.5F + slide1, 1.0625F, -arm2[2] / 45F);
            renderSparkStrip(consumer, length, wide, narrow, epsilon, uMin, uMax);
            RenderContext.popPose();
        }

        if(arm4[3] <= -0.375F) {
            RenderContext.pushPose();
            RenderContext.translate(-0.5F - slide2, 1.0625F, arm4[2] / 45F);
            renderSparkStrip(consumer, -length, wide, narrow, epsilon, uMin, uMax);
            RenderContext.popPose();
        }
    }

    private void renderSparkStrip(VertexConsumer consumer, float length, float wide, float narrow, float epsilon, float uMin, float uMax) {
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int overlay = RenderContext.overlay();

        consumer.addVertex(matrix, -epsilon, -wide, length).setColor(1F, 1F, 1F, 0F).setUv(uMin + 0.5F, 0F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(-1F, 0F, 0F);
        consumer.addVertex(matrix, -epsilon, wide, length).setColor(1F, 1F, 1F, 0F).setUv(uMin + 0.5F, 1F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(-1F, 0F, 0F);
        consumer.addVertex(matrix, -epsilon, narrow, 0F).setColor(1F, 1F, 1F, 1F).setUv(uMax + 0.5F, 1F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(-1F, 0F, 0F);
        consumer.addVertex(matrix, -epsilon, -narrow, 0F).setColor(1F, 1F, 1F, 1F).setUv(uMax + 0.5F, 0F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(-1F, 0F, 0F);

        consumer.addVertex(matrix, epsilon, -wide, length).setColor(1F, 1F, 1F, 0F).setUv(uMin, 1F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(1F, 0F, 0F);
        consumer.addVertex(matrix, epsilon, wide, length).setColor(1F, 1F, 1F, 0F).setUv(uMin, 0F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(1F, 0F, 0F);
        consumer.addVertex(matrix, epsilon, narrow, 0F).setColor(1F, 1F, 1F, 1F).setUv(uMax, 0F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(1F, 0F, 0F);
        consumer.addVertex(matrix, epsilon, -narrow, 0F).setColor(1F, 1F, 1F, 1F).setUv(uMax, 1F).setOverlay(overlay).setLight(LightTexture.FULL_BRIGHT).setNormal(1F, 0F, 0F);
    }

    @Override
    public AABB getRenderBoundingBox(MachineAssemblyFactoryBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_ASSEMBLY_FACTORY.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                bindTexture(ResourceManager.ASSEMBLY_FACTORY_TEX);
                ResourceManager.assembly_factory.renderAll();
            }
        };
    }
}
