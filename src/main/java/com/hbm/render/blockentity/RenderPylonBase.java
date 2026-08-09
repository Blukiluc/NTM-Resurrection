package com.hbm.render.blockentity;

import com.hbm.blockentity.network.PylonBaseBlockEntity;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public abstract class RenderPylonBase<T extends PylonBaseBlockEntity> extends BlockEntityRendererNT<T> {

    private static final RenderType WIRES = RenderType.create(
            "pylon_wires", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 2_048,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_TEXT_BACKGROUND_SHADER)
                    .setTextureState(RenderType.NO_TEXTURE)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );

    protected void renderLines(T be, MultiBufferSource buffer) {
        if(be.getLevel() == null) return;
        Vec3[] localMounts = be.getMountPositions();
        VertexConsumer consumer = buffer.getBuffer(WIRES);
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int packedLight = RenderContext.light();
        float red = ((be.color >> 16) & 255) / 255F;
        float green = ((be.color >> 8) & 255) / 255F;
        float blue = (be.color & 255) / 255F;

        for(BlockPos remotePos : be.connected) {
            BlockEntity remoteEntity = be.getLevel().getBlockEntity(remotePos);
            if(!(remoteEntity instanceof PylonBaseBlockEntity remote) || remote.getConnectionType() != be.getConnectionType()) continue;
            Vec3[] remoteMounts = remote.getMountPositions();
            int count = Math.min(localMounts.length, remoteMounts.length);
            for(int index = 0; index < count; index++) {
                Vec3 start = localMounts[index];
                Vec3 remoteMount = remoteMounts[index].add(remotePos.getX() - be.getBlockPos().getX(), remotePos.getY() - be.getBlockPos().getY(), remotePos.getZ() - be.getBlockPos().getZ());
                Vec3 end = start.add(remoteMount).scale(0.5D);
                this.renderWireHalf(consumer, matrix, packedLight, start, end, red, green, blue);
            }
        }
    }

    private void renderWireHalf(VertexConsumer consumer, Matrix4f matrix, int packedLight, Vec3 start, Vec3 end, float red, float green, float blue) {
        double sag = Math.min(4D, start.distanceTo(end) * 0.08D);
        int segments = 12;
        double width = 0.025D;
        Vec3 previous = start;

        for(int segment = 1; segment <= segments; segment++) {
            double progress = segment / (double)segments;
            Vec3 current = start.lerp(end, progress).add(0D, -Math.sin(progress * Math.PI * 0.5D) * sag, 0D);
            this.addRibbon(consumer, matrix, packedLight, previous, current, width, red, green, blue, true);
            this.addRibbon(consumer, matrix, packedLight, previous, current, width, red, green, blue, false);
            previous = current;
        }
    }

    private void addRibbon(VertexConsumer consumer, Matrix4f matrix, int packedLight, Vec3 start, Vec3 end, double width, float red, float green, float blue, boolean horizontal) {
        double x = horizontal ? width : 0D;
        double z = horizontal ? 0D : width;
        consumer.addVertex(matrix, (float)(start.x - x), (float)start.y, (float)(start.z - z)).setColor(red, green, blue, 1F).setLight(packedLight);
        consumer.addVertex(matrix, (float)(start.x + x), (float)start.y, (float)(start.z + z)).setColor(red, green, blue, 1F).setLight(packedLight);
        consumer.addVertex(matrix, (float)(end.x + x), (float)end.y, (float)(end.z + z)).setColor(red, green, blue, 1F).setLight(packedLight);
        consumer.addVertex(matrix, (float)(end.x - x), (float)end.y, (float)(end.z - z)).setColor(red, green, blue, 1F).setLight(packedLight);
    }
}
