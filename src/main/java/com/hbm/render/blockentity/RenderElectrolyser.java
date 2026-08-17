package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineElectrolyserBlockEntity;
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

public class RenderElectrolyser extends BlockEntityRendererNT<MachineElectrolyserBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineElectrolyserBlockEntity> create(Context context) {
        return new RenderElectrolyser();
    }

    @Override
    public void render(MachineElectrolyserBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        }

        bindTexture(ResourceManager.ELECTROLYSER_TEX);
        ResourceManager.electrolyser.renderAll();
    }

    @Override
    public AABB getRenderBoundingBox(MachineElectrolyserBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        // todo
        // The electrolyser's multiblock is much larger than the refinery's (dummy poles reach
        // +/-5 blocks out and up to y+5), so this box is intentionally generous. Tighten it once
        // the model is in and you can see its actual extent.
        return new AABB(x - 6, y, z - 6, x + 7, y + 6, z + 7);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_ELECTROLYSER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                RenderContext.translate(0F, 0F, 0F);
                RenderContext.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                bindTexture(ResourceManager.ELECTROLYSER_TEX);
                ResourceManager.electrolyser.renderAll();
            }
        };
    }
}