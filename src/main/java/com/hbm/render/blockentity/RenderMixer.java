package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineMixerBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.awt.Color;

public class RenderMixer extends BlockEntityRendererNT<MachineMixerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineMixerBlockEntity> create(Context context) {
        return new RenderMixer();
    }

    @Override
    public void render(MachineMixerBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(ResourceManager.mixer == null) return;

        int mixerLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(2));
        RenderContext.setup(poseStack, mixerLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);

        RenderSystem.disableCull();
        bindTexture(ResourceManager.MIXER_TEX);
        ResourceManager.mixer.renderPart("Main");

        RenderContext.pushPose();
        float rotation = Mth.lerp(partialTicks, be.prevRotation, be.rotation);
        RenderContext.mulPose(Axis.YN.rotationDegrees(rotation));
        ResourceManager.mixer.renderPart("Mixer");
        RenderContext.popPose();

        int totalFill = 0;
        int totalMax = 0;
        for(FluidTank tank : be.tanks) {
            if(tank.getTankType() != Fluids.NONE) {
                totalFill += tank.getFill();
                totalMax += tank.getMaxFill();
            }
        }

        if(totalFill > 0 && totalMax > 0) {
            Color color = new Color(be.tanks[2].getTankType().getColor());

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            bindTexture(ResourceManager.WHITE_TEX);
            RenderContext.setColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.75F);

            RenderContext.pushPose();
            RenderContext.translate(0F, 1F, 0F);
            RenderContext.scale(1F, (float) totalFill / totalMax * 0.99F, 1F);
            RenderContext.translate(0F, -1F, 0F);
            ResourceManager.mixer.renderPart("Fluid");
            RenderContext.popPose();

            RenderContext.setColor(1F, 1F, 1F, 1F);
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
        }

        RenderSystem.enableCull();
        RenderContext.end();
    }

    @Override
    public AABB getRenderBoundingBox(MachineMixerBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_MIXER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -5F, 0F);
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                if(ResourceManager.mixer == null) return;
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                RenderSystem.disableCull();
                bindTexture(ResourceManager.MIXER_TEX);
                ResourceManager.mixer.renderPart("Main");
                ResourceManager.mixer.renderPart("Mixer");
                RenderSystem.enableCull();
            }
        };
    }
}
