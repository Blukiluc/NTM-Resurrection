package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.turbine.MachineIndustrialTurbineBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
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

public class RenderIndustrialTurbine extends BlockEntityRendererNT<MachineIndustrialTurbineBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineIndustrialTurbineBlockEntity> create(Context context) {
        return new RenderIndustrialTurbine();
    }

    @Override
    public void render(MachineIndustrialTurbineBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));

        bindTexture(ResourceManager.INDUSTRIAL_TURBINE_TEX);
        ResourceManager.industrial_turbine.renderPart("Turbine");

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(getGaugeAngle(be.tanks[0].getTankType())));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.industrial_turbine.renderPart("Gauge");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        float rotor = be.lastRotor + (be.rotor - be.lastRotor) * partialTicks;
        RenderContext.mulPose(Axis.ZN.rotationDegrees(rotor));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.industrial_turbine.renderPart("Flywheel");
        RenderContext.popPose();
    }

    private static void rotateForFacing(Direction facing) {
        switch(facing) {
            case SOUTH -> { }
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }
    }

    private static float getGaugeAngle(FluidType type) {
        if(type == Fluids.HOTSTEAM) return 45F;
        if(type == Fluids.SUPERHOTSTEAM) return -45F;
        if(type == Fluids.ULTRAHOTSTEAM) return -135F;
        return 135F;
    }

    @Override
    public AABB getRenderBoundingBox(MachineIndustrialTurbineBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 3, y, z - 3, x + 4, y + 3, z + 4);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_INDUSTRIAL_TURBINE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(1F, 0F, 0F);
                RenderContext.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                RenderContext.translate(0.5F, 0F, 0F);
                bindTexture(ResourceManager.INDUSTRIAL_TURBINE_TEX);
                ResourceManager.industrial_turbine.renderPart("Turbine");

                RenderContext.pushPose();
                RenderContext.translate(0F, 1.5F, 0F);
                RenderContext.mulPose(Axis.ZP.rotationDegrees(135F));
                RenderContext.translate(0F, -1.5F, 0F);
                ResourceManager.industrial_turbine.renderPart("Gauge");
                RenderContext.popPose();

                RenderContext.translate(0F, 1.5F, 0F);
                RenderContext.mulPose(Axis.ZN.rotationDegrees((System.currentTimeMillis() / 5L) % 360L));
                RenderContext.translate(0F, -1.5F, 0F);
                ResourceManager.industrial_turbine.renderPart("Flywheel");
            }
        };
    }
}
