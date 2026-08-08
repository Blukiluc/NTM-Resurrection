package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.MachineFractioningTowerBlockEntity;
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

public class RenderFractioningTower extends BlockEntityRendererNT<MachineFractioningTowerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineFractioningTowerBlockEntity> create(Context context) {
        return new RenderFractioningTower();
    }

    @Override
    public void render(MachineFractioningTowerBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0.0F, 0.5F);
        RenderSystem.disableCull();

        bindTexture(ResourceManager.FRACTION_TOWER_TEX);
        ResourceManager.fraction_tower.renderAll();

        RenderSystem.enableCull();
    }

    @Override
    public AABB getRenderBoundingBox(MachineFractioningTowerBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 3, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_FRACTION_TOWER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -1.5F, 0.0F);
                RenderContext.scale(2.0F, 2.0F, 2.0F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderSystem.disableCull();
                bindTexture(ResourceManager.FRACTION_TOWER_TEX);
                ResourceManager.fraction_tower.renderAll();
                RenderSystem.enableCull();
            }
        };
    }
}
