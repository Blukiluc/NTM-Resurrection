package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.turbine.MachineSteamEngineBlockEntity;
import com.hbm.blocks.DummyableBlock;
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
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderSteamEngine extends BlockEntityRendererNT<MachineSteamEngineBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineSteamEngineBlockEntity> create(Context context) {
        return new RenderSteamEngine();
    }

    @Override
    public void render(MachineSteamEngineBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        RenderContext.translate(2F, 0F, 0F);
        this.renderCommon(Mth.lerp(partialTicks, be.lastRotor, be.rotor));
    }

    private void renderCommon(float rotation) {
        RenderSystem.disableCull();
        bindTexture(ResourceManager.STEAM_ENGINE_TEX);
        ResourceManager.steam_engine.renderPart("Base");

        RenderContext.pushPose();
        RenderContext.translate(2F, 1.375F, 0F);
        RenderContext.mulPose(Axis.ZN.rotationDegrees(rotation));
        RenderContext.translate(-2F, -1.375F, 0F);
        ResourceManager.steam_engine.renderPart("Flywheel");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.375F, -0.5F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(rotation * 2F));
        RenderContext.translate(0F, -1.375F, 0.5F);
        ResourceManager.steam_engine.renderPart("Shaft");
        RenderContext.popPose();

        double sin = Math.sin(Math.toRadians(rotation)) * 0.25D - 0.25D;
        double cos = Math.cos(Math.toRadians(rotation)) * 0.25D;
        double angle = Math.acos(cos / 1.875D);

        RenderContext.pushPose();
        RenderContext.translate((float)(sin + 2.25D), (float)(cos + 1.375D), 0F);
        RenderContext.mulPose(Axis.ZN.rotationDegrees((float)Math.toDegrees(angle) - 90F));
        RenderContext.translate(-2.25F, -1.375F, 0F);
        ResourceManager.steam_engine.renderPart("Transmission");
        RenderContext.popPose();

        RenderContext.pushPose();
        double length = Math.sqrt(3.515625D - cos * cos / 2D);
        RenderContext.translate((float)(1.875D - length + sin), 0F, 0F);
        ResourceManager.steam_engine.renderPart("Piston");
        RenderContext.popPose();
        RenderSystem.enableCull();
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }
    }

    @Override
    public AABB getRenderBoundingBox(MachineSteamEngineBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 6, x + 6, y + 3, z + 6);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_STEAM_ENGINE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YN.rotationDegrees(90F));
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(2F, 2F, 2F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderSteamEngine.this.renderCommon((System.currentTimeMillis() % 3_600L) * 0.1F);
            }
        };
    }
}
