package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.turbine.MachineLeviathanTurbineBlockEntity;
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

public class RenderLeviathanTurbine extends BlockEntityRendererNT<MachineLeviathanTurbineBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineLeviathanTurbineBlockEntity> create(Context context) {
        return new RenderLeviathanTurbine();
    }

    @Override
    public void render(MachineLeviathanTurbineBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        rotateForFacing(be.getBlockState().getValue(DummyableBlock.FACING));
        RenderContext.translate(0F, 0F, -3F);

        bindTexture(ResourceManager.LEVIATHAN_TURBINE_TEX);
        ResourceManager.leviathan_turbine.renderPart("Body");

        RenderContext.pushPose();
        RenderContext.translate(0F, 0F, 4.5F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(getLeverAngle(be.tanks[0].getTankType())));
        RenderContext.translate(0F, 0F, -4.5F);
        ResourceManager.leviathan_turbine.renderPart("Lever");
        RenderContext.popPose();

        RenderContext.translate(0F, 2.5F, 0F);
        float rotor = be.lastRotor + (be.rotor - be.lastRotor) * partialTicks;
        RenderContext.mulPose(Axis.ZN.rotationDegrees(rotor));
        RenderContext.translate(0F, -2.5F, 0F);
        ResourceManager.leviathan_turbine.renderPart("Blades");
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

    private static float getLeverAngle(FluidType type) {
        if(type == Fluids.HOTSTEAM) return 5F;
        if(type == Fluids.SUPERHOTSTEAM) return -5F;
        if(type == Fluids.ULTRAHOTSTEAM) return -15F;
        return 15F;
    }

    @Override
    public AABB getRenderBoundingBox(MachineLeviathanTurbineBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 11, y, z - 11, x + 12, y + 5, z + 12);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CHUNGUS.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(2.5F, 2.5F, 2.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.4F, 0.4F, 0.4F);
                bindTexture(ResourceManager.LEVIATHAN_TURBINE_TEX);
                ResourceManager.leviathan_turbine.renderPart("Body");

                RenderContext.pushPose();
                RenderContext.translate(0F, 0F, 4.5F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(15F));
                RenderContext.translate(0F, 0F, -4.5F);
                ResourceManager.leviathan_turbine.renderPart("Lever");
                RenderContext.popPose();

                RenderContext.translate(0F, 2.5F, 0F);
                RenderContext.mulPose(Axis.ZN.rotationDegrees((System.currentTimeMillis() / 5L) % 360L));
                RenderContext.translate(0F, -2.5F, 0F);
                ResourceManager.leviathan_turbine.renderPart("Blades");
            }
        };
    }
}
