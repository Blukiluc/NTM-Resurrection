package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.foundry.FoundryBaseBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryCastingBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryChannelBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryMoldBlockEntity;
import com.hbm.blocks.machine.foundry.FoundryChannelBlock;
import com.hbm.items.machine.FoundryMoldItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class RenderFoundry<T extends FoundryBaseBlockEntity> extends BlockEntityRendererNT<T> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/block/lava_gray.png");
    private static final RenderType CASTING_CONTENTS = RenderType.create(
            "foundry_casting_contents", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                    .setTransparencyState(RenderType.NO_TRANSPARENCY)
                    .setCullState(RenderType.NO_CULL)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
                    .createCompositeState(false));
    private static final RenderType FLUID = RenderType.create(
            "foundry_fluid", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, false, false))
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setCullState(RenderType.NO_CULL)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setWriteMaskState(RenderType.COLOR_WRITE)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .setOutputState(RenderType.TRANSLUCENT_TARGET)
                    .createCompositeState(false));

    @Override
    public BlockEntityRenderer<T> create(Context context) {
        return new RenderFoundry<>();
    }

    @Override
    public void render(T be, MultiBufferSource buffer, float partialTicks) {
        // The mold and the cast already exist below the metal. Only the metal fades,
        // so both are uncovered together instead of being spawned or faded in later.
        if (be instanceof FoundryCastingBlockEntity casting) {
            if (!casting.getMold().isEmpty()) this.renderItem(casting.getMold(), be, buffer, 0.13F);

            ItemStack cast = casting.getResult();
            boolean preview = cast.isEmpty();
            if (preview && casting.getMaterial() != null
                    && casting.getCapacity() > 0 && casting.getAmount() >= casting.getCapacity()
                    && casting.getMold().getItem() instanceof FoundryMoldItem mold) {
                cast = mold.getOutput(casting.getMaterial());
            }
            if (!cast.isEmpty()) this.renderItem(cast, be, buffer,
                    casting instanceof FoundryMoldBlockEntity ? 0.25F : preview ? 0.85F : 0.875F);
        }

        if (be.getMaterial() != null && be.getAmount() > 0) {
            float height;
            float alpha = 1F;
            height = getFluidSurfaceHeight(be);
            if (be instanceof FoundryCastingBlockEntity casting
                    && casting.getCapacity() > 0 && casting.getAmount() >= casting.getCapacity()) {
                float progress = Math.min(1F, (casting.getCooloff() + partialTicks) / FoundryCastingBlockEntity.COOLOFF_TIME);
                float remaining = 1F - progress;
                alpha = 0.12F + 0.88F * (float) Math.pow(remaining, 0.65D);
            }
            if (be instanceof FoundryChannelBlockEntity channel) {
                renderSurface(buffer, 0.375F, 0.375F, 0.625F, 0.625F, height, be.getMaterial().moltenColor, alpha);
                if (channel.getBlockState().getValue(FoundryChannelBlock.NORTH)) renderSurface(buffer, 0.375F, 0F, 0.625F, 0.375F, height, be.getMaterial().moltenColor, alpha);
                if (channel.getBlockState().getValue(FoundryChannelBlock.EAST)) renderSurface(buffer, 0.625F, 0.375F, 1F, 0.625F, height, be.getMaterial().moltenColor, alpha);
                if (channel.getBlockState().getValue(FoundryChannelBlock.SOUTH)) renderSurface(buffer, 0.375F, 0.625F, 0.625F, 1F, height, be.getMaterial().moltenColor, alpha);
                if (channel.getBlockState().getValue(FoundryChannelBlock.WEST)) renderSurface(buffer, 0F, 0.375F, 0.375F, 0.625F, height, be.getMaterial().moltenColor, alpha);
            } else {
                renderSurface(buffer, 0.125F, 0.125F, 0.875F, 0.875F, height, be.getMaterial().moltenColor, alpha);
            }
        }

    }

    private void renderItem(ItemStack stack, T be, MultiBufferSource buffer, float height) {
        RenderContext.pushPose();
        RenderContext.translate(0.5F, height, 0.5F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(90F));
        RenderContext.scale(0.75F, 0.75F, 0.75F);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getModel(stack, be.getLevel(), null, 0);
        model.getTransforms().getTransform(ItemDisplayContext.FIXED).apply(false, RenderContext.poseStack());
        RenderContext.translate(-0.5F, -0.5F, -0.5F);
        renderer.renderModelLists(model, stack, RenderContext.light(), RenderContext.overlay(),
                RenderContext.poseStack(), buffer.getBuffer(CASTING_CONTENTS));
        RenderContext.popPose();
    }

    static void renderSurface(MultiBufferSource buffer, float minX, float minZ, float maxX, float maxZ,
                              float height, int color, float alpha) {
        VertexConsumer consumer = buffer.getBuffer(FLUID);
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int packedLight = RenderContext.light();
        int packedOverlay = RenderContext.overlay();
        int argb = (Math.max(0, Math.min(255, (int) (alpha * 255F))) << 24) | (color & 0xFFFFFF);
        consumer.addVertex(matrix, minX, height, minZ).setColor(argb).setUv(minX, minZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, minX, height, maxZ).setColor(argb).setUv(minX, maxZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, maxX, height, maxZ).setColor(argb).setUv(maxX, maxZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, maxX, height, minZ).setColor(argb).setUv(maxX, minZ).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
    }

    static void renderSlopedSurface(MultiBufferSource buffer,
                                    float innerX, float innerZ, float innerY,
                                    float outerX, float outerZ, float outerY,
                                    float halfWidth, Direction direction, int color) {
        float perpendicularX = -direction.getStepZ() * halfWidth;
        float perpendicularZ = direction.getStepX() * halfWidth;
        int argb = 0xFF000000 | (color & 0xFFFFFF);
        VertexConsumer consumer = buffer.getBuffer(FLUID);
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int light = RenderContext.light();
        int overlay = RenderContext.overlay();

        addFluidVertex(consumer, matrix, innerX + perpendicularX, innerY, innerZ + perpendicularZ,
                argb, light, overlay);
        addFluidVertex(consumer, matrix, outerX + perpendicularX, outerY, outerZ + perpendicularZ,
                argb, light, overlay);
        addFluidVertex(consumer, matrix, outerX - perpendicularX, outerY, outerZ - perpendicularZ,
                argb, light, overlay);
        addFluidVertex(consumer, matrix, innerX - perpendicularX, innerY, innerZ - perpendicularZ,
                argb, light, overlay);
    }

    private static void addFluidVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z,
                                       int argb, int light, int overlay) {
        consumer.addVertex(matrix, x, y, z).setColor(argb).setUv(x, z)
                .setOverlay(overlay).setLight(light).setNormal(0F, 1F, 0F);
    }

    static void renderFallingStream(MultiBufferSource buffer, float topY, float bottomY, int color) {
        renderFallingStream(buffer, 0.375F, 0.375F, 0.625F, 0.625F, topY, bottomY, color);
    }

    static void renderFallingStream(MultiBufferSource buffer,
                                    float minX, float minZ, float maxX, float maxZ,
                                    float topY, float bottomY, int color) {
        if (bottomY >= topY) return;

        int argb = 0xFF000000 | (color & 0xFFFFFF);
        VertexConsumer consumer = buffer.getBuffer(FLUID);
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int light = RenderContext.light();
        int overlay = RenderContext.overlay();

        // Use one texture repeat per block, exactly like the channel surfaces. Keeping
        // coordinates in world-sized units prevents the falling texture from stretching.
        renderVerticalQuad(consumer, matrix, minX, bottomY, minZ, maxX, topY, minZ,
                minX, -bottomY, maxX, -topY, argb, light, overlay, 0F, 0F, -1F);
        renderVerticalQuad(consumer, matrix, maxX, bottomY, maxZ, minX, topY, maxZ,
                minX, -bottomY, maxX, -topY, argb, light, overlay, 0F, 0F, 1F);
        renderVerticalQuad(consumer, matrix, minX, bottomY, maxZ, minX, topY, minZ,
                minZ, -bottomY, maxZ, -topY, argb, light, overlay, -1F, 0F, 0F);
        renderVerticalQuad(consumer, matrix, maxX, bottomY, minZ, maxX, topY, maxZ,
                minZ, -bottomY, maxZ, -topY, argb, light, overlay, 1F, 0F, 0F);
        renderHorizontalQuad(consumer, matrix, minX, minZ, maxX, maxZ, topY, argb, light, overlay);
    }

    static void renderStoppedFaucetFace(MultiBufferSource buffer, int color, Direction facing, float topY) {
        float min = 0.375F;
        float max = 0.625F;
        float bottomY = 0.125F;
        if (topY <= bottomY) return;
        int argb = 0xFF000000 | (color & 0xFFFFFF);
        VertexConsumer consumer = buffer.getBuffer(FLUID);
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int light = RenderContext.light();
        int overlay = RenderContext.overlay();

        switch (facing) {
            case NORTH -> renderVerticalQuad(consumer, matrix, min, bottomY, 0.625F, max, topY, 0.625F,
                    min, -bottomY, max, -topY, argb, light, overlay, 0F, 0F, -1F);
            case SOUTH -> renderVerticalQuad(consumer, matrix, max, bottomY, 0.375F, min, topY, 0.375F,
                    min, -bottomY, max, -topY, argb, light, overlay, 0F, 0F, 1F);
            case EAST -> renderVerticalQuad(consumer, matrix, 0.375F, bottomY, min, 0.375F, topY, max,
                    min, -bottomY, max, -topY, argb, light, overlay, 1F, 0F, 0F);
            case WEST -> renderVerticalQuad(consumer, matrix, 0.625F, bottomY, max, 0.625F, topY, min,
                    min, -bottomY, max, -topY, argb, light, overlay, -1F, 0F, 0F);
            default -> { }
        }
    }

    static float getFluidSurfaceHeight(FoundryBaseBlockEntity foundry) {
        if (foundry instanceof FoundryMoldBlockEntity || foundry instanceof FoundryChannelBlockEntity) {
            return 0.125F + foundry.getFill() * 0.25F;
        }
        return 0.125F + foundry.getFill() * 0.75F;
    }

    private static void renderHorizontalQuad(VertexConsumer consumer, Matrix4f matrix,
                                             float minX, float minZ, float maxX, float maxZ, float height,
                                             int argb, int light, int overlay) {
        consumer.addVertex(matrix, minX, height, minZ).setColor(argb).setUv(minX, minZ).setOverlay(overlay).setLight(light).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, minX, height, maxZ).setColor(argb).setUv(minX, maxZ).setOverlay(overlay).setLight(light).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, maxX, height, maxZ).setColor(argb).setUv(maxX, maxZ).setOverlay(overlay).setLight(light).setNormal(0F, 1F, 0F);
        consumer.addVertex(matrix, maxX, height, minZ).setColor(argb).setUv(maxX, minZ).setOverlay(overlay).setLight(light).setNormal(0F, 1F, 0F);
    }

    private static void renderVerticalQuad(VertexConsumer consumer, Matrix4f matrix,
                                           float bottomX1, float bottomY, float bottomZ1,
                                           float bottomX2, float topY, float bottomZ2,
                                           float minU, float maxV, float maxU, float minV,
                                           int argb, int light, int overlay, float normalX, float normalY, float normalZ) {
        consumer.addVertex(matrix, bottomX1, bottomY, bottomZ1).setColor(argb).setUv(minU, maxV).setOverlay(overlay).setLight(light).setNormal(normalX, normalY, normalZ);
        consumer.addVertex(matrix, bottomX2, bottomY, bottomZ2).setColor(argb).setUv(maxU, maxV).setOverlay(overlay).setLight(light).setNormal(normalX, normalY, normalZ);
        consumer.addVertex(matrix, bottomX2, topY, bottomZ2).setColor(argb).setUv(maxU, minV).setOverlay(overlay).setLight(light).setNormal(normalX, normalY, normalZ);
        consumer.addVertex(matrix, bottomX1, topY, bottomZ1).setColor(argb).setUv(minU, minV).setOverlay(overlay).setLight(light).setNormal(normalX, normalY, normalZ);
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
