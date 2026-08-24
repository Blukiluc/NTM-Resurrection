package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineSmokestackBlockEntity;
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

public class RenderSmokestack extends BlockEntityRendererNT<MachineSmokestackBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineSmokestackBlockEntity> create(Context context) {
        return new RenderSmokestack();
    }

    @Override
    public void render(MachineSmokestackBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
        RenderSystem.disableCull();
        bindTexture(ResourceManager.CHIMNEY_BRICK_TEX);
        ResourceManager.chimney_brick.renderAll();
        RenderSystem.enableCull();
    }

    @Override
    public AABB getRenderBoundingBox(MachineSmokestackBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 13, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_SMOKESTACK.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -5F, 0F);
                RenderContext.scale(2.25F, 2.25F, 2.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                RenderSystem.disableCull();
                bindTexture(ResourceManager.CHIMNEY_BRICK_TEX);
                ResourceManager.chimney_brick.renderAll();
                RenderSystem.enableCull();
            }
        };
    }
}
