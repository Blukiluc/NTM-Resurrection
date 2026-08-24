package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineTurbofanBlockEntity;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderTurbofan extends BlockEntityRendererNT<MachineTurbofanBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineTurbofanBlockEntity> create(Context context) {
        return new RenderTurbofan();
    }

    @Override
    public void render(MachineTurbofanBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }
        RenderContext.translate(-0.5F, 0F, 0.5F);


        float spin = be.lastSpin + (be.spin - be.lastSpin) * partialTicks;

        RenderContext.pushPose();
        bindTexture(ResourceManager.TURBOFAN_TEX);
        ResourceManager.turbofan.renderPart("Body");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(spin));
        RenderContext.translate(0F, -1.5F, 0F);
        bindTexture(ResourceManager.TURBOFAN_BLADES_TEX);
        ResourceManager.turbofan.renderPart("Blades");
        RenderContext.popPose();

        RenderContext.pushPose();
        bindTexture(ResourceManager.TURBOFAN_AFTERBURNER_TEX);
        ResourceManager.turbofan.renderPart("Afterburner");
        RenderContext.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(MachineTurbofanBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 20, y, z - 20, x + 21, y + 4, z + 21);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_TURBOFAN.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1F, 0F);
                RenderContext.scale(2.75F, 2.75F, 2.75F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                bindTexture(ResourceManager.TURBOFAN_TEX);
                ResourceManager.turbofan.renderAll();
            }
        };
    }
}