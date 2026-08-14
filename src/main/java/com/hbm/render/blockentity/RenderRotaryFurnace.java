package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineRotaryFurnaceBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
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

public class RenderRotaryFurnace extends BlockEntityRendererNT<MachineRotaryFurnaceBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineRotaryFurnaceBlockEntity> create(Context context) {
        return new RenderRotaryFurnace();
    }

    @Override
    public void render(MachineRotaryFurnaceBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0.0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90.0F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270.0F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180.0F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0.0F));
        }

        bindTexture(ResourceManager.ROTARY_FURNACE_TEX);
        ResourceManager.rotary_furnace.renderPart("Furnace");

        float anim = Mth.lerp(partialTicks, be.lastAnim, be.anim);
        RenderContext.pushPose();
        RenderContext.translate(0.0F, (float) BobMathUtil.sps(anim * 0.09375D) * 0.5F - 0.5F, 0.0F);
        ResourceManager.rotary_furnace.renderPart("Piston");
        RenderContext.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(MachineRotaryFurnaceBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 5, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_ROTARY_FURNACE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -2.0F, 0.0F);
                RenderContext.scale(3.5F, 3.5F, 3.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.625F, 0.625F, 0.625F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90.0F));
                bindTexture(ResourceManager.ROTARY_FURNACE_TEX);
                ResourceManager.rotary_furnace.renderAll();
            }
        };
    }
}
