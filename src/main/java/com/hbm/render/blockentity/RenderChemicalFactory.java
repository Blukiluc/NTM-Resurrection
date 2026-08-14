package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineChemicalFactoryBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class RenderChemicalFactory extends BlockEntityRendererNT<MachineChemicalFactoryBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineChemicalFactoryBlockEntity> create(Context context) {
        return new RenderChemicalFactory();
    }

    @Override
    public void render(MachineChemicalFactoryBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
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

        float animation = BobMathUtil.interp(be.prevAnim, be.anim, partialTicks);

        bindTexture(ResourceManager.CHEMICAL_FACTORY_TEX);
        ResourceManager.chemical_factory.renderPart("Base");
        if(be.frame) ResourceManager.chemical_factory.renderPart("Frame");

        RenderContext.pushPose();
        RenderContext.translate(1F, 0F, 0F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(-animation * 45F % 360F));
        RenderContext.translate(-1F, 0F, 0F);
        ResourceManager.chemical_factory.renderPart("Fan1");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(-1F, 0F, 0F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(-animation * 45F % 360F));
        RenderContext.translate(1F, 0F, 0F);
        ResourceManager.chemical_factory.renderPart("Fan2");
        RenderContext.popPose();

        RenderContext.end();
    }

    @Override
    public AABB getRenderBoundingBox(MachineChemicalFactoryBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CHEMICAL_FACTORY.asItem();
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
                bindTexture(ResourceManager.CHEMICAL_FACTORY_TEX);
                ResourceManager.chemical_factory.renderPart("Base");
                ResourceManager.chemical_factory.renderPart("Frame");
                ResourceManager.chemical_factory.renderPart("Fan1");
                ResourceManager.chemical_factory.renderPart("Fan2");
            }
        };
    }
}
