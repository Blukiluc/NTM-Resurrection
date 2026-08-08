package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.tower.AbstractCoolingTowerBlockEntity;
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

public class RenderCoolingTower<T extends AbstractCoolingTowerBlockEntity> extends BlockEntityRendererNT<T> implements IBEWLRProvider {

    private final boolean large;

    public RenderCoolingTower(boolean large) {
        this.large = large;
    }

    @Override
    public BlockEntityRenderer<T> create(Context context) {
        return new RenderCoolingTower<>(this.large);
    }

    @Override
    public void render(T be, MultiBufferSource buffer, float partialTick) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderSystem.disableCull();
        this.renderModel();
        RenderSystem.enableCull();
    }

    private void renderModel() {
        if(this.large) {
            bindTexture(ResourceManager.TOWER_LARGE_TEX);
            ResourceManager.tower_large.renderAll();
        } else {
            bindTexture(ResourceManager.TOWER_SMALL_TEX);
            ResourceManager.tower_small.renderAll();
        }
    }

    @Override
    public AABB getRenderBoundingBox(T be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if(this.large) return new AABB(x - 4, y, z - 4, x + 5, y + 13, z + 5);
        return new AABB(x - 2, y, z - 2, x + 3, y + 20, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return this.large ? NtmBlocks.MACHINE_TOWER_LARGE.asItem() : NtmBlocks.MACHINE_TOWER_SMALL.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, large ? -3F : -4F, 0F);
                float scale = large ? 3.8F : 3F;
                RenderContext.scale(scale, scale, scale);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.25F, 0.25F, 0.25F);
                RenderSystem.disableCull();
                renderModel();
                RenderSystem.enableCull();
            }
        };
    }
}
