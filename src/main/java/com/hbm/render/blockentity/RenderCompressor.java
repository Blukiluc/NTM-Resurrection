package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineCompressorBlockEntity;
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

public class RenderCompressor extends BlockEntityRendererNT<MachineCompressorBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineCompressorBlockEntity> create(Context context) {
        return new RenderCompressor();
    }

    @Override
    public void render(MachineCompressorBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }

        RenderSystem.disableCull();
        bindTexture(ResourceManager.COMPRESSOR_TEX);
        ResourceManager.compressor.renderPart("Compressor");

        float lift = Mth.lerp(partialTicks, be.prevPiston, be.piston);
        float fan = Mth.lerp(partialTicks, be.prevFanSpin, be.fanSpin);

        RenderContext.pushPose();
        RenderContext.translate(0F, lift * 3F - 3F, 0F);
        ResourceManager.compressor.renderPart("Pump");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(fan));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.compressor.renderPart("Fan");
        RenderContext.popPose();

        RenderSystem.enableCull();
    }

    @Override
    public AABB getRenderBoundingBox(MachineCompressorBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 9, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_COMPRESSOR.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderSystem.disableCull();
                RenderContext.scale(0.5F, 0.5F, 0.5F);

                bindTexture(ResourceManager.COMPRESSOR_TEX);
                ResourceManager.compressor.renderPart("Compressor");

                double lift = (System.currentTimeMillis() * 0.005D) % 9D;
                if(lift > 3D) lift = 3D - (lift - 3D) / 2D;

                RenderContext.pushPose();
                RenderContext.translate(0F, (float) -lift, 0F);
                ResourceManager.compressor.renderPart("Pump");
                RenderContext.popPose();

                RenderContext.pushPose();
                RenderContext.translate(0F, 1.5F, 0F);
                RenderContext.mulPose(Axis.XP.rotationDegrees((System.currentTimeMillis() * 0.25F) % 360F));
                RenderContext.translate(0F, -1.5F, 0F);
                ResourceManager.compressor.renderPart("Fan");
                RenderContext.popPose();

                RenderSystem.enableCull();
            }
        };
    }
}
