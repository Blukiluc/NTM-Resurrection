package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.FractioningSpacerBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderFractioningSpacer extends BlockEntityRendererNT<FractioningSpacerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<FractioningSpacerBlockEntity> create(Context context) {
        return new RenderFractioningSpacer();
    }

    @Override
    public void render(FractioningSpacerBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0.0F, 0.5F);
        RenderSystem.enableCull();

        bindTexture(ResourceManager.FRACTION_SPACER_TEX);
        ResourceManager.fraction_spacer.renderAll();
    }

    @Override
    public AABB getRenderBoundingBox(FractioningSpacerBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 1, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.FRACTION_SPACER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -0.5F, 0.0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderSystem.enableCull();
                bindTexture(ResourceManager.FRACTION_SPACER_TEX);
                ResourceManager.fraction_spacer.renderAll();
            }
        };
    }
}
