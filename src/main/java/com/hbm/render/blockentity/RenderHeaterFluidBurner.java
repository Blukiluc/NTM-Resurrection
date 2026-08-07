package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.heater.HeaterFluidBurnerBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderHeaterFluidBurner extends BlockEntityRendererNT<HeaterFluidBurnerBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<HeaterFluidBurnerBlockEntity> create(Context context) { return new RenderHeaterFluidBurner(); }

    @Override
    public void render(HeaterFluidBurnerBlockEntity be, MultiBufferSource buffer, float partialTicks) {

        RenderContext.translate(0.5F, 0.0F, 0.5F);

        bindTexture(ResourceManager.HEATER_FLUID_BURNER_TEX);
        ResourceManager.heater_fluid_burner.renderAll();
    }

    @Override
    public AABB getRenderBoundingBox(HeaterFluidBurnerBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.HEATER_FLUID_BURNER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.HEATER_FLUID_BURNER_TEX);
                ResourceManager.heater_fluid_burner.renderAll();
            }
        };
    }
}
