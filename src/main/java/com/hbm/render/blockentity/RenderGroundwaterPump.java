package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineGroundwaterPumpBlockEntity;
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

public class RenderGroundwaterPump extends BlockEntityRendererNT<MachineGroundwaterPumpBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineGroundwaterPumpBlockEntity> create(Context context) {
        return new RenderGroundwaterPump();
    }

    @Override
    public void render(MachineGroundwaterPumpBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        float rotation = Mth.lerp(partialTicks, be.lastRotor, be.rotor);
        this.renderCommon(rotation, be.getBlockState().is(NtmBlocks.PUMP_ELECTRIC.get()));
    }

    private void renderCommon(float rotation, boolean electric) {
        RenderSystem.disableCull();
        bindTexture(electric ? ResourceManager.PUMP_ELECTRIC_TEX : ResourceManager.PUMP_STEAM_TEX);
        ResourceManager.pump.renderPart("Base");

        RenderContext.pushPose();
        RenderContext.translate(0F, 2.25F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(rotation - 90F));
        RenderContext.translate(0F, -2.25F, 0F);
        ResourceManager.pump.renderPart("Rotor");
        RenderContext.popPose();

        double sin = Math.sin(Math.toRadians(rotation)) * 0.5D - 0.5D;
        double cos = Math.cos(Math.toRadians(rotation)) * 0.5D;
        double angle = Math.acos(cos / 2D);
        double length = Math.sqrt(1D + cos * cos / 2D);

        RenderContext.pushPose();
        RenderContext.translate(0F, (float)(1D - length + sin + 4.75D), 0F);
        RenderContext.mulPose(Axis.ZN.rotationDegrees((float)Math.toDegrees(angle) - 90F));
        RenderContext.translate(0F, -4.75F, 0F);
        ResourceManager.pump.renderPart("Arms");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, (float)(1D - length + sin), 0F);
        ResourceManager.pump.renderPart("Piston");
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
    public AABB getRenderBoundingBox(MachineGroundwaterPumpBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 5, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.PUMP_STEAM.asItem();
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {NtmBlocks.PUMP_STEAM.asItem(), NtmBlocks.PUMP_ELECTRIC.asItem()};
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -3F, 0F);
                RenderContext.scale(2.5F, 2.5F, 2.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderGroundwaterPump.this.renderCommon((System.currentTimeMillis() % 3_600L) * 0.1F, stack.is(NtmBlocks.PUMP_ELECTRIC.asItem()));
            }
        };
    }
}
