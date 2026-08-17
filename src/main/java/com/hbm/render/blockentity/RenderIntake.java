package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineIntakeBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class RenderIntake extends BlockEntityRendererNT<MachineIntakeBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineIntakeBlockEntity> create(Context context) {
        return new RenderIntake();
    }

    @Override
    public void render(MachineIntakeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderSystem.disableCull();

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }

        RenderContext.translate(-0.5F, 0F, 0.5F);

        bindTexture(ResourceManager.INTAKE_TEX);
        ResourceManager.intake.renderPart("Base");

        float rot = Mth.lerp(partialTicks, be.prevFan, be.fan);

        RenderContext.pushPose();
        RenderContext.mulPose(Axis.YP.rotationDegrees(-rot));
        ResourceManager.intake.renderPart("Fan");
        RenderContext.popPose();

        RenderSystem.enableCull();
        RenderContext.end();
    }

    @Override
    public AABB getRenderBoundingBox(MachineIntakeBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 1, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_INTAKE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.INTAKE_TEX);
                ResourceManager.intake.renderAll();
            }
        };
    }
}