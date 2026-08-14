package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.MachineGasFlareBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderGasFlare extends BlockEntityRendererNT<MachineGasFlareBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineGasFlareBlockEntity> create(Context context) {
        return new RenderGasFlare();
    }

    @Override
    public void render(MachineGasFlareBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));

        if(be.tilted) {
            RenderContext.translate(0F, -0.25F, 0F);
            RenderContext.mulPose(Axis.ZP.rotationDegrees(10F));
            RenderContext.mulPose(Axis.YP.rotationDegrees(5F));
        }

        RenderSystem.disableCull();
        bindTexture(ResourceManager.GAS_FLARE_TEX);
        ResourceManager.gas_flare.renderAll();
        RenderSystem.enableCull();
    }

    @Override
    public AABB getRenderBoundingBox(MachineGasFlareBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 13, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_FLARE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(2.25F, 2.25F, 2.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                RenderSystem.disableCull();
                bindTexture(ResourceManager.GAS_FLARE_TEX);
                ResourceManager.gas_flare.renderAll();
                RenderSystem.enableCull();
            }
        };
    }
}
