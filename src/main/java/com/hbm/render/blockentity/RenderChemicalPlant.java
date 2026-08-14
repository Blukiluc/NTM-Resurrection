package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineChemicalPlantBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class RenderChemicalPlant extends BlockEntityRendererNT<MachineChemicalPlantBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineChemicalPlantBlockEntity> create(Context context) { return new RenderChemicalPlant(); }

    @Override
    public void render(MachineChemicalPlantBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int tPackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(2));
        RenderContext.setup(poseStack, tPackedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
        }

        bindTexture(ResourceManager.CHEMICAL_PLANT_TEX);
        ResourceManager.chemical_plant.renderPart("Base");
        if(be.frame) ResourceManager.chemical_plant.renderPart("Frame");

        RenderContext.pushPose();

        float spin = BobMathUtil.interp(be.prevSpin, be.spin, partialTicks);
        float slide = BobMathUtil.interp(be.prevSlide, be.slide, partialTicks);

        RenderContext.pushPose(); {
            RenderContext.translate(0.5F, 0.5F, 0.5F);
            RenderContext.mulPose(Axis.YP.rotationDegrees(spin));
            RenderContext.translate(-0.5F, -0.5F, -0.5F);
            ResourceManager.chemical_plant.renderPart("Spinner");
        } RenderContext.popPose();

        RenderContext.pushPose(); {
            RenderContext.translate(slide, 0F, 0F);
            ResourceManager.chemical_plant.renderPart("Slider");
        } RenderContext.popPose();

        RenderContext.popPose();

        FluidTank displayTank = hasFluid(be.tanks[3]) ? be.tanks[3] : be.tanks[0];
        if(hasFluid(displayTank)) {
            renderFluid(displayTank, spin);
        }

        RenderContext.end();
    }

    private static boolean hasFluid(FluidTank tank) {
        return tank.getFill() > 0 && tank.getTankType() != Fluids.NONE;
    }

    private void renderFluid(FluidTank tank, float spin) {
        FluidType type = tank.getTankType();
        int color = type.getColor();
        float rotationProgress = spin / 360F;

        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        bindTexture(ResourceManager.CHEMICAL_PLANT_FLUID_TEX);
        RenderSystem.setTextureMatrix(new Matrix4f().translate(
                -rotationProgress,
                (float) BobMathUtil.sps(rotationProgress * Math.PI * 2D) * 0.1F - 0.25F,
                0F
        ));
        RenderContext.setColor(
                ((color >> 16) & 0xFF) / 255F,
                ((color >> 8) & 0xFF) / 255F,
                (color & 0xFF) / 255F,
                0.5F
        );
        ResourceManager.chemical_plant.renderPart("Fluid");
        RenderSystem.resetTextureMatrix();
        RenderContext.setColor(1F, 1F, 1F, 1F);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    @Override
    public AABB getRenderBoundingBox(MachineChemicalPlantBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CHEMICAL_PLANT.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -2.75F, 0F);
                RenderContext.scale(4.5F, 4.5F, 4.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                bindTexture(ResourceManager.CHEMICAL_PLANT_TEX);
                ResourceManager.chemical_plant.renderAll();
            }
        };
    }
}
