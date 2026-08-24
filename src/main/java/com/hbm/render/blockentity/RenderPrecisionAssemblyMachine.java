package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachinePrecisionAssemblyMachineBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderPrecisionAssemblyMachine extends BlockEntityRendererNT<MachinePrecisionAssemblyMachineBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachinePrecisionAssemblyMachineBlockEntity> create(Context context) {
        return new RenderPrecisionAssemblyMachine();
    }

    @Override
    public void render(MachinePrecisionAssemblyMachineBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int machinePackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(2));
        RenderContext.setup(poseStack, machinePackedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
        }

        bindTexture(ResourceManager.PREC_ASS_TEX);
        ResourceManager.assembly_machine.renderPart("Base");
        if(be.frame) ResourceManager.assembly_machine.renderPart("Frame");

        RenderContext.pushPose();

        float spin = be.getRenderRing(partialTicks);
        float[] arm = be.getRenderArmAngles(partialTicks);

        RenderContext.mulPose(Axis.YP.rotationDegrees(spin));
        ResourceManager.assembly_machine.renderPart("Ring");
        ResourceManager.assembly_machine.renderPart("Ring2");

        for(int i = 0; i < 4; i++) {
            renderArm(arm, be.getRenderStriker(i, partialTicks));
            RenderContext.mulPose(Axis.YP.rotationDegrees(-90F));
        }

        RenderContext.popPose();

        GenericRecipe recipe = be.assemblerModule.getRecipe();
        if(recipe != null && NuclearTechMod.proxy.me().distanceToSqr(be.getBlockPos().getBottomCenter().add(0, 1, 0)) < 35 * 35) {
            RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            RenderContext.translate(0F, 1.0625F, 0F);

            ItemStack stack = recipe.getIcon();
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel model = renderer.getModel(stack, null, null, 0);

            if(!model.isGui3d()) {
                RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));
                RenderContext.translate(0F, -0.25F, 0F);
            } else {
                RenderContext.translate(0F, 0.1F, 0F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            RenderContext.scale(1.25F, 1.25F, 1.25F);

            renderer.render(stack, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);
        }

        RenderContext.end();
    }

    private static void renderArm(float[] arm, float striker) {
        RenderContext.pushPose();
        RenderContext.translate(0F, 1.625F, 0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm[0]));
        RenderContext.translate(0F, -1.625F, -0.9375F);
        ResourceManager.assembly_machine.renderPart("ArmLower1");

        RenderContext.translate(0F, 2.375F, 0.9375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm[1]));
        RenderContext.translate(0F, -2.375F, -0.9375F);
        ResourceManager.assembly_machine.renderPart("ArmUpper1");

        RenderContext.translate(0F, 2.375F, 0.4375F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(arm[2]));
        RenderContext.translate(0F, -2.375F, -0.4375F);
        ResourceManager.assembly_machine.renderPart("Head1");
        RenderContext.translate(0F, striker, 0F);
        ResourceManager.assembly_machine.renderPart("Spike1");
        RenderContext.popPose();
    }

    private static void renderMachine() {
        ResourceManager.assembly_machine.renderPart("Base");
        ResourceManager.assembly_machine.renderPart("Frame");
        ResourceManager.assembly_machine.renderPart("Ring");
        ResourceManager.assembly_machine.renderPart("Ring2");

        float[] arm = {45F, -30F, 45F};
        for(int i = 0; i < 4; i++) {
            renderArm(arm, 0F);
            RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        }
    }

    @Override
    public AABB getRenderBoundingBox(MachinePrecisionAssemblyMachineBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 3, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_PREC_ASS.asItem();
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
                bindTexture(ResourceManager.PREC_ASS_TEX);
                renderMachine();
            }
        };
    }
}
