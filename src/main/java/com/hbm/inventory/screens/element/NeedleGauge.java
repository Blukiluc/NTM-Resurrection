package com.hbm.inventory.screens.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

public class NeedleGauge {

    public static void drawSmoothGauge(GuiGraphics guiGraphics, int x, int y, double progress,
                                       double tipLength, double backLength, double backSide,
                                       int color) {
        drawSmoothGauge(guiGraphics, x, y, progress, tipLength, backLength, backSide, color, 0xFF000000);
    }

    public static void drawSmoothGauge(GuiGraphics guiGraphics, int x, int y, double progress,
                                       double tipLength, double backLength, double backSide,
                                       int color, int colorOuter) {

        progress = Mth.clamp(progress, 0, 1);

        float angle = (float) Math.toRadians(-progress * 270 - 45);
        float sin = Mth.sin(angle);
        float cos = Mth.cos(angle);

        float tipX = 0,           tipY = (float) tipLength;
        float leftX = (float) backSide,  leftY = (float) -backLength;
        float rightX = (float) -backSide, rightY = (float) -backLength;

        float rTipX   = tipX * cos + tipY * sin;
        float rTipY   = -tipX * sin + tipY * cos;
        float rLeftX  = leftX * cos + leftY * sin;
        float rLeftY  = -leftX * sin + leftY * cos;
        float rRightX = rightX * cos + rightY * sin;
        float rRightY = -rightX * sin + rightY * cos;

        PoseStack.Pose pose = guiGraphics.pose().last();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        float a1 = (colorOuter >> 24 & 255) / 255F;
        float r1 = (colorOuter >> 16 & 255) / 255F;
        float g1 = (colorOuter >> 8 & 255) / 255F;
        float b1 = (colorOuter & 255) / 255F;

        float a2 = (color >> 24 & 255) / 255F;
        float r2 = (color >> 16 & 255) / 255F;
        float g2 = (color >> 8 & 255) / 255F;
        float b2 = (color & 255) / 255F;

        double mult = 1.5;
        buffer.addVertex(pose, (float) (x + rTipX * mult),   (float) (y + rTipY * mult), 0)
                .setColor(r1, g1, b1, a1);
        buffer.addVertex(pose, (float) (x + rLeftX * mult),  (float) (y + rLeftY * mult), 0)
                .setColor(r1, g1, b1, a1);
        buffer.addVertex(pose, (float) (x + rRightX * mult), (float) (y + rRightY * mult), 0)
                .setColor(r1, g1, b1, a1);

        buffer.addVertex(pose, x + rTipX,   y + rTipY,   0).setColor(r2, g2, b2, a2);
        buffer.addVertex(pose, x + rLeftX,  y + rLeftY,  0).setColor(r2, g2, b2, a2);
        buffer.addVertex(pose, x + rRightX, y + rRightY, 0).setColor(r2, g2, b2, a2);

        MeshData meshData = buffer.buildOrThrow();
        BufferUploader.drawWithShader(meshData);

        RenderSystem.disableBlend();
    }
}