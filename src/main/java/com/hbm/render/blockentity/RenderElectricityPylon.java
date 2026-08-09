package com.hbm.render.blockentity;

import com.hbm.blockentity.network.PylonBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.network.ElectricityPylonBlock;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderElectricityPylon extends RenderPylonBase<PylonBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<PylonBlockEntity> create(Context context) {
        return new RenderElectricityPylon();
    }

    @Override
    public void render(PylonBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.pushPose();
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        this.renderModel(be.getVariant());
        RenderContext.popPose();
        this.renderLines(be, buffer);
    }

    private void renderModel(ElectricityPylonBlock.Variant variant) {
        switch(variant) {
            case WOOD -> {
                RenderSystem.disableCull();
                bindTexture(ResourceManager.PYLON_TEX);
                ResourceManager.pylon.renderPart("Pylon");
                RenderSystem.enableCull();
            }
            case STEEL -> {
                RenderSystem.disableCull();
                bindTexture(ResourceManager.PYLON_STEEL_TEX);
                ResourceManager.pylon.renderPart("Pylon_steel");
                RenderSystem.enableCull();
            }
            case MEDIUM_WOOD, MEDIUM_WOOD_TRANSFORMER -> {
                bindTexture(ResourceManager.PYLON_MEDIUM_TEX);
                ResourceManager.pylon_medium.renderPart("Pylon");
                if(variant == ElectricityPylonBlock.Variant.MEDIUM_WOOD_TRANSFORMER) ResourceManager.pylon_medium.renderPart("Transformer");
            }
            case MEDIUM_STEEL, MEDIUM_STEEL_TRANSFORMER -> {
                bindTexture(ResourceManager.PYLON_MEDIUM_STEEL_TEX);
                ResourceManager.pylon_medium.renderPart("Pylon");
                if(variant == ElectricityPylonBlock.Variant.MEDIUM_STEEL_TRANSFORMER) ResourceManager.pylon_medium.renderPart("Transformer");
            }
            case LARGE -> {
                RenderSystem.disableCull();
                bindTexture(ResourceManager.PYLON_LARGE_TEX);
                ResourceManager.pylon_large.renderAll();
                RenderSystem.enableCull();
            }
            case SUBSTATION -> {
                bindTexture(ResourceManager.SUBSTATION_TEX);
                ResourceManager.substation.renderAll();
            }
        }
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }
    }

    private ElectricityPylonBlock.Variant getVariant(Item item) {
        if(item == NtmBlocks.RED_PYLON_STEEL.asItem()) return ElectricityPylonBlock.Variant.STEEL;
        if(item == NtmBlocks.RED_PYLON_MEDIUM_WOOD.asItem()) return ElectricityPylonBlock.Variant.MEDIUM_WOOD;
        if(item == NtmBlocks.RED_PYLON_MEDIUM_WOOD_TRANSFORMER.asItem()) return ElectricityPylonBlock.Variant.MEDIUM_WOOD_TRANSFORMER;
        if(item == NtmBlocks.RED_PYLON_MEDIUM_STEEL.asItem()) return ElectricityPylonBlock.Variant.MEDIUM_STEEL;
        if(item == NtmBlocks.RED_PYLON_MEDIUM_STEEL_TRANSFORMER.asItem()) return ElectricityPylonBlock.Variant.MEDIUM_STEEL_TRANSFORMER;
        if(item == NtmBlocks.RED_PYLON_LARGE.asItem()) return ElectricityPylonBlock.Variant.LARGE;
        if(item == NtmBlocks.SUBSTATION.asItem()) return ElectricityPylonBlock.Variant.SUBSTATION;
        return ElectricityPylonBlock.Variant.WOOD;
    }

    @Override
    public AABB getRenderBoundingBox(PylonBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int range = be.getMaxWireLength() + 4;
        return new AABB(x - range, y - 4, z - range, x + range + 1, y + range + 15, z + range + 1);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.RED_PYLON.asItem();
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {
                NtmBlocks.RED_PYLON.asItem(),
                NtmBlocks.RED_PYLON_MEDIUM_WOOD.asItem(),
                NtmBlocks.RED_PYLON_MEDIUM_WOOD_TRANSFORMER.asItem(),
                NtmBlocks.RED_PYLON_MEDIUM_STEEL.asItem(),
                NtmBlocks.RED_PYLON_MEDIUM_STEEL_TRANSFORMER.asItem(),
                NtmBlocks.RED_PYLON_LARGE.asItem(),
                NtmBlocks.RED_PYLON_STEEL.asItem(),
                NtmBlocks.SUBSTATION.asItem()
        };
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                ElectricityPylonBlock.Variant variant = RenderElectricityPylon.this.getVariant(stack.getItem());
                float scale = variant == ElectricityPylonBlock.Variant.LARGE ? 1.4F : variant == ElectricityPylonBlock.Variant.SUBSTATION ? 2F : variant.name().startsWith("MEDIUM") ? 2.5F : 2.9F;
                RenderContext.translate(0F, -5F, 0F);
                RenderContext.scale(scale, scale, scale);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                ElectricityPylonBlock.Variant variant = RenderElectricityPylon.this.getVariant(stack.getItem());
                if(variant.name().startsWith("MEDIUM")) {
                    RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                    RenderContext.scale(0.5F, 0.5F, 0.5F);
                    RenderContext.translate(0.75F, 0F, 0F);
                }
                RenderElectricityPylon.this.renderModel(variant);
            }
        };
    }
}
