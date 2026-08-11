package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineCrucibleBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderCrucible extends BlockEntityRendererNT<MachineCrucibleBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineCrucibleBlockEntity> create(Context context) {
        return new RenderCrucible();
    }

    @Override
    public void render(MachineCrucibleBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch (facing) {
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }

        bindTexture(ResourceManager.CRUCIBLE_TEX);
        ResourceManager.crucible.renderPart("Main");

        int total = be.getAmount(be.recipeStack, null) + be.getAmount(be.wasteStack, null);
        if (total > 0) {
            int color = !be.recipeStack.isEmpty() ? be.recipeStack.get(0).material.moltenColor : be.wasteStack.get(0).material.moltenColor;
            float red = ((color >> 16) & 255) / 255F;
            float green = ((color >> 8) & 255) / 255F;
            float blue = (color & 255) / 255F;
            RenderContext.pushPose();
            RenderContext.translate(0F, (float) total / (MachineCrucibleBlockEntity.RECIPE_CAPACITY + MachineCrucibleBlockEntity.WASTE_CAPACITY) * 0.875F, 0F);
            RenderContext.setColor(red, green, blue, 1F);
            RenderContext.setLight(LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above()));
            bindTexture(ResourceManager.MOLTEN_METAL_TEX);
            ResourceManager.crucible.renderPart("Lava");
            RenderContext.setColor(1F, 1F, 1F, 1F);
            RenderContext.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(MachineCrucibleBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 2, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CRUCIBLE.asItem();
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
                bindTexture(ResourceManager.CRUCIBLE_TEX);
                ResourceManager.crucible.renderPart("Main");
            }
        };
    }
}
