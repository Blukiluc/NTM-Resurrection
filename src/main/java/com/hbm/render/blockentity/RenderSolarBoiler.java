package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineSolarBoilerBlockEntity;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderSolarBoiler extends BlockEntityRendererNT<MachineSolarBoilerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineSolarBoilerBlockEntity> create(Context context) {
        return new RenderSolarBoiler();
    }

    @Override
    public void render(MachineSolarBoilerBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        this.rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        this.renderMachine();
    }

    private void rotateForFacing(Direction facing) {
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            default -> { }
        }
    }

    private void renderMachine() {
        this.bindTexture(ResourceManager.SOLAR_BOILER_TEX);
        ResourceManager.solar_boiler.renderPart("Base");
    }

    @Override
    public AABB getRenderBoundingBox(MachineSolarBoilerBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 4, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_SOLAR_BOILER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -2.5F, 0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderSolarBoiler.this.renderMachine();
            }
        };
    }
}
