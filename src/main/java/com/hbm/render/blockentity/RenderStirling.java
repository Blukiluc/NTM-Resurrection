package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineStirlingBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderStirling extends BlockEntityRendererNT<MachineStirlingBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineStirlingBlockEntity> create(Context context) {
        return new RenderStirling();
    }

    @Override
    public void render(MachineStirlingBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        float rotation = Mth.lerp(partialTicks, be.lastSpin, be.spin);
        this.renderCommon(rotation, be.hasGear, this.getTexture(be.getBlockState().getBlock().asItem()));
    }

    private ResourceLocation getTexture(Item item) {
        if(item == NtmBlocks.MACHINE_STIRLING_STEEL.asItem()) return ResourceManager.STIRLING_STEEL_TEX;
        if(item == NtmBlocks.MACHINE_STIRLING_CREATIVE.asItem()) return ResourceManager.STIRLING_CREATIVE_TEX;
        return ResourceManager.STIRLING_TEX;
    }

    private void renderCommon(float rotation, boolean hasGear, ResourceLocation texture) {
        bindTexture(texture);
        ResourceManager.stirling.renderPart("Base");

        if(hasGear) {
            RenderContext.pushPose();
            RenderContext.translate(0F, 1.375F, 0F);
            RenderContext.mulPose(Axis.ZN.rotationDegrees(rotation));
            RenderContext.translate(0F, -1.375F, 0F);
            ResourceManager.stirling.renderPart("Cog");
            RenderContext.popPose();
        }

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.375F, 0.25F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(rotation * 2F + 3F));
        RenderContext.translate(0F, -1.375F, -0.25F);
        ResourceManager.stirling.renderPart("CogSmall");
        RenderContext.popPose();

        RenderContext.translate((float)Math.sin(rotation * Math.PI / 90D) * 0.25F + 0.125F, 0F, 0F);
        ResourceManager.stirling.renderPart("Piston");
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }
    }

    @Override
    public AABB getRenderBoundingBox(MachineStirlingBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_STIRLING.asItem();
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {NtmBlocks.MACHINE_STIRLING.asItem(), NtmBlocks.MACHINE_STIRLING_STEEL.asItem(), NtmBlocks.MACHINE_STIRLING_CREATIVE.asItem()};
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
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderStirling.this.renderCommon((System.currentTimeMillis() % 3_600L) * 0.1F, true, RenderStirling.this.getTexture(stack.getItem()));
            }
        };
    }
}
