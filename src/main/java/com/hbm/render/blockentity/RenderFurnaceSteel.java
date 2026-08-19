package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineFurnaceSteelBlockEntity;
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

public class RenderFurnaceSteel extends BlockEntityRendererNT<MachineFurnaceSteelBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineFurnaceSteelBlockEntity> create(Context context) {
        return new RenderFurnaceSteel();
    }

    @Override
    public void render(MachineFurnaceSteelBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0.0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0.0F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90.0F));
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180.0F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270.0F));
        }

        RenderContext.mulPose(Axis.YP.rotationDegrees(-90.0F));
        bindTexture(ResourceManager.FURNACE_STEEL_TEX);
        ResourceManager.furnace_steel.renderAll();
    }

    @Override
    public AABB getRenderBoundingBox(MachineFurnaceSteelBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 3, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_FURNACE_STEEL.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -1.5F, 0.0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.FURNACE_STEEL_TEX);
                ResourceManager.furnace_steel.renderAll();
            }
        };
    }
}
