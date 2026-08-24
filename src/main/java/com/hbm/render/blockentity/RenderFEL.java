package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineFELBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.BeamPronter.BeamType;
import com.hbm.render.util.BeamPronter.WaveType;
import com.hbm.render.util.RenderContext;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class RenderFEL extends BlockEntityRendererNT<MachineFELBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineFELBlockEntity> create(Context context) { return new RenderFEL(); }

    @Override
    public void render(MachineFELBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int tPackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(2));
        RenderContext.setup(poseStack, tPackedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);

        RenderContext.pushPose();

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        }

        bindTexture(ResourceManager.FEL_TEX);
        ResourceManager.fel.renderAll();
        RenderContext.popPose();

        int length = be.distance - 3;
        if(be.isBeamActive() && length > 0) {
            int color = be.mode.getBeamColor(be.getLevel().getGameTime());
            RenderContext.translate(facing.getStepX() * 1.5F, 1.5F, facing.getStepZ() * 1.5F);
            Vec3NT beam = new Vec3NT(facing.getStepX() * (length + 1D), 0D, facing.getStepZ() * (length + 1D));
            BeamPronter.prontBeam(beam, WaveType.SPIRAL, BeamType.SOLID, color, color, 0, 1, 0F, 2, 0.0625F);
            BeamPronter.prontBeam(beam, WaveType.RANDOM, BeamType.SOLID, color, color,
                    (int) (be.getLevel().getGameTime() % 1_000L / 2L), length / 2 + 1, 0.0625F, 2, 0.0625F);
        }

        RenderContext.end();
    }

    @Override
    public AABB getRenderBoundingBox(MachineFELBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 24, y, z - 24, x + 25, y + 3, z + 25);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_FEL.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, 0F, 0F);
                RenderContext.scale(3F, 3F, 3F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                bindTexture(ResourceManager.FEL_TEX);
                ResourceManager.fel.renderAll();
            }
        };
    }
}
