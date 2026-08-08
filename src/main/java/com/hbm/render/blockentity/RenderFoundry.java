package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.foundry.FoundryBaseBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryCastingBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryChannelBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryMoldBlockEntity;
import com.hbm.blocks.machine.foundry.FoundryChannelBlock;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class RenderFoundry<T extends FoundryBaseBlockEntity> extends BlockEntityRendererNT<T> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/block/lava_gray.png");

    @Override
    public BlockEntityRenderer<T> create(Context context) {
        return new RenderFoundry<>();
    }

    @Override
    public void render(T be, MultiBufferSource buffer, float partialTicks) {
        if (be.getMaterial() != null && be.getAmount() > 0) {
            float height;
            if (be instanceof FoundryMoldBlockEntity) height = 0.125F + be.getFill() * 0.25F;
            else if (be instanceof FoundryCastingBlockEntity) height = 0.125F + be.getFill() * 0.75F;
            else if (be instanceof FoundryChannelBlockEntity) height = 0.125F + be.getFill() * 0.25F;
            else height = 0.125F + be.getFill() * 0.75F;
            if (be instanceof FoundryChannelBlockEntity channel) {
                this.renderSurface(buffer, 0.375F, 0.375F, 0.625F, 0.625F, height, be.getMaterial().moltenColor);
                if (channel.getBlockState().getValue(FoundryChannelBlock.NORTH)) this.renderSurface(buffer, 0.375F, 0F, 0.625F, 0.375F, height, be.getMaterial().moltenColor);
                if (channel.getBlockState().getValue(FoundryChannelBlock.EAST)) this.renderSurface(buffer, 0.625F, 0.375F, 1F, 0.625F, height, be.getMaterial().moltenColor);
                if (channel.getBlockState().getValue(FoundryChannelBlock.SOUTH)) this.renderSurface(buffer, 0.375F, 0.625F, 0.625F, 1F, height, be.getMaterial().moltenColor);
                if (channel.getBlockState().getValue(FoundryChannelBlock.WEST)) this.renderSurface(buffer, 0F, 0.375F, 0.375F, 0.625F, height, be.getMaterial().moltenColor);
            } else {
                this.renderSurface(buffer, 0.125F, 0.125F, 0.875F, 0.875F, height, be.getMaterial().moltenColor);
            }
        }

        if (be instanceof FoundryCastingBlockEntity casting) {
            ItemStack display = casting.getResult().isEmpty() ? casting.getMold() : casting.getResult();
            if (!display.isEmpty()) {
                boolean mold = casting.getResult().isEmpty();
                RenderContext.pushPose();
                float height = mold ? 0.13F : casting instanceof FoundryMoldBlockEntity ? 0.25F : 0.875F;
                RenderContext.translate(0.5F, height, 0.5F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
                BakedModel model = renderer.getModel(display, be.getLevel(), null, 0);
                renderer.render(display, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);
                RenderContext.popPose();
            }
        }
    }

    private void renderSurface(MultiBufferSource buffer, float minX, float minZ, float maxX, float maxZ, float height, int color) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int packedLight = RenderContext.light();
        int packedOverlay = RenderContext.overlay();
        int argb = 0xFF000000 | color;
        consumer.addVertex(matrix, minX, height, minZ).setColor(argb).setUv(minX, minZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, minX, height, maxZ).setColor(argb).setUv(minX, maxZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, maxX, height, maxZ).setColor(argb).setUv(maxX, maxZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, maxX, height, minZ).setColor(argb).setUv(maxX, minZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
    }

    @Override
    public AABB getRenderBoundingBox(T be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x, y, z, x + 1, y + 1, z + 1);
    }
}
