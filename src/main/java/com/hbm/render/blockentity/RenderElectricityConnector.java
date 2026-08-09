package com.hbm.render.blockentity;

import com.hbm.blockentity.network.PylonConnectorBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.network.ElectricityConnectorBlock;
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

public class RenderElectricityConnector extends RenderPylonBase<PylonConnectorBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<PylonConnectorBlockEntity> create(Context context) {
        return new RenderElectricityConnector();
    }

    @Override
    public void render(PylonConnectorBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.pushPose();
        RenderContext.translate(0.5F, 0.5F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(ElectricityConnectorBlock.FACING));
        RenderContext.translate(0F, -0.5F, 0F);
        boolean heavy = be.getBlockState().is(NtmBlocks.RED_CONNECTOR_SUPER.get());
        bindTexture(heavy ? ResourceManager.CONNECTOR_SUPER_TEX : ResourceManager.CONNECTOR_TEX);
        if(heavy) ResourceManager.connector_super.renderAll();
        else ResourceManager.connector.renderAll();
        RenderContext.popPose();
        this.renderLines(be, buffer);
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case DOWN -> RenderContext.mulPose(Axis.XP.rotationDegrees(180F));
            case NORTH -> {
                RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.ZP.rotationDegrees(180F));
            }
            case SOUTH -> RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
            case WEST -> {
                RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.ZP.rotationDegrees(90F));
            }
            case EAST -> {
                RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.ZP.rotationDegrees(270F));
            }
            default -> { }
        }
    }

    @Override
    public AABB getRenderBoundingBox(PylonConnectorBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int range = be.getMaxWireLength() + 1;
        return new AABB(x - range, y - range, z - range, x + range + 1, y + range + 1, z + range + 1);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.RED_CONNECTOR.asItem();
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {NtmBlocks.RED_CONNECTOR.asItem(), NtmBlocks.RED_CONNECTOR_SUPER.asItem()};
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, stack.is(NtmBlocks.RED_CONNECTOR_SUPER.asItem()) ? -5F : -3.5F, 0F);
                RenderContext.scale(7F, 7F, 7F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(2F, 2F, 2F);
                boolean heavy = stack.is(NtmBlocks.RED_CONNECTOR_SUPER.asItem());
                bindTexture(heavy ? ResourceManager.CONNECTOR_SUPER_TEX : ResourceManager.CONNECTOR_TEX);
                if(heavy) ResourceManager.connector_super.renderAll();
                else ResourceManager.connector.renderAll();
            }
        };
    }
}
