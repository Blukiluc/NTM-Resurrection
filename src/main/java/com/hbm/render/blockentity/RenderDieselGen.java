package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineDieselBlockEntity;
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

public class RenderDieselGen extends BlockEntityRendererNT<MachineDieselBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineDieselBlockEntity> create(Context context) {
        return new RenderDieselGen();
    }

    @Override
    public void render(MachineDieselBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }

        bindTexture(ResourceManager.DIESEL_GENERATOR_TEX);
        ResourceManager.diesel_generator.renderPart("Generator");

        RenderContext.pushPose();
        if(be.hasAcceptableFuel() && be.tank.getFill() > 0) {
            double swingSide = Math.sin(System.currentTimeMillis() / 50D) * 0.005;
            double swingFront = Math.sin(System.currentTimeMillis() / 25D) * 0.005;
            RenderContext.translate((float) swingFront, 0F, (float) swingSide);
        }
        ResourceManager.diesel_generator.renderPart("Engine");
        RenderContext.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(MachineDieselBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 2, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_DIESEL.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -2.5F, 0F);
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(2F, 2F, 2F);
                bindTexture(ResourceManager.DIESEL_GENERATOR_TEX);
                ResourceManager.diesel_generator.renderAll();
            }
        };
    }
}
