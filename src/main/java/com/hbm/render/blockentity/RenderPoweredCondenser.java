package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineCondenserPoweredBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
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

public class RenderPoweredCondenser extends BlockEntityRendererNT<MachineCondenserPoweredBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineCondenserPoweredBlockEntity> create(Context context) {
        return new RenderPoweredCondenser();
    }

    @Override
    public void render(MachineCondenserPoweredBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        this.renderCommon(Mth.lerp(partialTicks, be.lastFanRotation, be.fanRotation));
    }

    private void renderCommon(float rotation) {
        RenderSystem.disableCull();
        bindTexture(ResourceManager.CONDENSER_TEX);
        ResourceManager.condenser.renderPart("Condenser");

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(rotation));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.condenser.renderPart("Fan1");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.XN.rotationDegrees(rotation));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.condenser.renderPart("Fan2");
        RenderContext.popPose();
        RenderSystem.enableCull();
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }
    }

    @Override
    public AABB getRenderBoundingBox(MachineCondenserPoweredBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 4, y, z - 4, x + 5, y + 4, z + 5);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CONDENSER_POWERED.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(-1F, -1F, 0F);
                RenderContext.scale(2.75F, 2.75F, 2.75F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                RenderContext.translate(0.5F, 0F, 0F);
                RenderPoweredCondenser.this.renderCommon((System.currentTimeMillis() % 3_600L) * 0.1F);
            }
        };
    }
}
