package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.MachineFrackingTowerBlockEntity;
import com.hbm.blockentity.machine.oil.MachineOilDerrickBlockEntity;
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

public class RenderOilDerrick extends BlockEntityRendererNT<MachineOilDerrickBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineOilDerrickBlockEntity> create(Context context) {
        return new RenderOilDerrick();
    }

    @Override
    public void render(MachineOilDerrickBlockEntity be, MultiBufferSource buffer, float partialTicks) {

        RenderContext.translate(0.5F, 0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }

        bindTexture(ResourceManager.OIL_DERRICK_TEX);
        ResourceManager.oil_derrick.renderAll();
    }

    @Override
    public AABB getRenderBoundingBox(MachineOilDerrickBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_OIL_DERRICK.asItem();
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
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                bindTexture(ResourceManager.OIL_DERRICK_TEX);
                ResourceManager.oil_derrick.renderAll();
            }
        };
    }
}