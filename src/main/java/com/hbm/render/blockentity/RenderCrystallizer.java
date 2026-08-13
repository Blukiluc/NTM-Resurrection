package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineCrystallizerBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class RenderCrystallizer extends BlockEntityRendererNT<MachineCrystallizerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineCrystallizerBlockEntity> create(Context context) {
        return new RenderCrystallizer();
    }

    @Override
    public void render(MachineCrystallizerBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(ResourceManager.crystallizer == null) return;

        int crystallizerLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(3));
        RenderContext.setup(poseStack, crystallizerLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }

        RenderSystem.enableCull();
        bindTexture(ResourceManager.CRYSTALLIZER_TEX);
        ResourceManager.crystallizer.renderPart("Body");

        RenderContext.pushPose();
        RenderContext.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, be.prevAngle, be.angle)));
        ResourceManager.crystallizer.renderPart("Spinner");
        RenderContext.popPose();

        if(be.tank.getFill() > 0 && be.tank.getMaxFill() > 0 && be.tank.getTankType() != Fluids.NONE) {
            FluidType type = be.tank.getTankType();
            int tint = type.renderWithTint ? type.getTint() : 0xFFFFFF;
            float fillRatio = Mth.clamp((float) be.tank.getFill() / be.tank.getMaxFill(), 0F, 1F);

            RenderSystem.enableBlend();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            bindTexture(type.getTexture());
            RenderSystem.setTextureMatrix(new Matrix4f().translate(
                    -(be.getLevel().getGameTime() + partialTicks) / 200F,
                    0F,
                    0F
            ));
            RenderContext.setColor(
                    ((tint >> 16) & 0xFF) / 255F,
                    ((tint >> 8) & 0xFF) / 255F,
                    (tint & 0xFF) / 255F,
                    1F
            );

            RenderContext.pushPose();
            RenderContext.translate(0F, 2.375F, 0F);
            RenderContext.scale(1F, fillRatio, 1F);
            RenderContext.translate(0F, -2.375F, 0F);
            ResourceManager.crystallizer.renderPart("Fluid");
            RenderContext.popPose();

            RenderSystem.resetTextureMatrix();
            RenderContext.setColor(1F, 1F, 1F, 1F);
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
        }

        RenderContext.end();
    }

    @Override
    public AABB getRenderBoundingBox(MachineCrystallizerBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 10, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CRYSTALLIZER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderNonInv(ItemStack stack, MultiBufferSource buffer, boolean rightHand) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
            }

            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(2F, 2F, 2F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                if(ResourceManager.crystallizer == null) return;
                bindTexture(ResourceManager.CRYSTALLIZER_TEX);
                ResourceManager.crystallizer.renderPart("Body");
                ResourceManager.crystallizer.renderPart("Spinner");
            }
        };
    }
}
