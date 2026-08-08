package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineCompressorCompactBlockEntity;
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

public class RenderCompressorCompact extends BlockEntityRendererNT<MachineCompressorCompactBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineCompressorCompactBlockEntity> create(Context context) {
        return new RenderCompressorCompact();
    }

    @Override
    public void render(MachineCompressorCompactBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }

        RenderSystem.disableCull();
        bindTexture(ResourceManager.COMPRESSOR_COMPACT_TEX);
        ResourceManager.compressor_compact.renderPart("Condenser");

        float rotation = Mth.lerp(partialTicks, be.prevFanSpin, be.fanSpin);

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(rotation));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.compressor_compact.renderPart("Fan1");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.XN.rotationDegrees(rotation));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.compressor_compact.renderPart("Fan2");
        RenderContext.popPose();

        RenderSystem.enableCull();
    }

    @Override
    public AABB getRenderBoundingBox(MachineCompressorCompactBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 3, y, z - 3, x + 4, y + 3, z + 4);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_COMPRESSOR_COMPACT.asItem();
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
                bindTexture(ResourceManager.COMPRESSOR_COMPACT_TEX);
                ResourceManager.compressor_compact.renderAll();
            }
        };
    }
}
