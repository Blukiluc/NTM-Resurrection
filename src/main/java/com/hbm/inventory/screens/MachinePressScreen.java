package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.inventory.menus.MachinePressMenu;
import com.hbm.inventory.screens.element.NeedleGauge;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachinePressScreen extends InfoScreen<MachinePressMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/press.png");

    private static final int FIRE_SRC_X = 0, FIRE_SRC_Y = 202, FIRE_W = 14, FIRE_H = 14;
    private static final int FIRE_X = 27, FIRE_Y = 36;

    private static final int HAMMER_SRC_X = 14, HAMMER_SRC_Y = 202, HAMMER_W = 18, HAMMER_H = 16;
    private static final int HAMMER_X = 79, HAMMER_Y = 35;

    private final MachinePressBlockEntity be;

    public MachinePressScreen(MachinePressMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 202;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        double speedRatio = MachinePressBlockEntity.maxSpeed > 0
                ? (double) be.speed / (double) MachinePressBlockEntity.maxSpeed
                : 0;

        NeedleGauge.drawSmoothGauge(
                guiGraphics,
                this.leftPos + 34, this.topPos + 25,
                speedRatio,
                5, 2, 1,
                0xFF7F0000
        );

        if(be.burnTime > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + FIRE_X, this.topPos + FIRE_Y, FIRE_SRC_X, FIRE_SRC_Y, FIRE_W, FIRE_H);
        }

        double interpolatedPress = be.lastPress + (be.renderPress - be.lastPress) * partialTicks;
        if(interpolatedPress > 0.001D) {
            int filledHeight = Math.round((float) (interpolatedPress * HAMMER_H / MachinePressBlockEntity.maxPress));
            filledHeight = Math.min(filledHeight, HAMMER_H);

            if(filledHeight > 0) {
                guiGraphics.blit(
                        TEXTURE,
                        this.leftPos + HAMMER_X,
                        this.topPos + HAMMER_Y,
                        HAMMER_SRC_X,
                        HAMMER_SRC_Y,
                        HAMMER_W,
                        filledHeight
                );
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        int speedPercent = MachinePressBlockEntity.maxSpeed > 0
                ? (be.speed * 100 / MachinePressBlockEntity.maxSpeed)
                : 0;

        this.drawCustomInfoStat(
                guiGraphics, mouseX, mouseY,
                this.leftPos + 25, this.topPos + 16, 18, 18,
                mouseX, mouseY,
                Component.literal(speedPercent + "%")
        );

        int operationsLeft = be.burnTime / 200;

        this.drawCustomInfoStat(
                guiGraphics, mouseX, mouseY,
                this.leftPos + 25, this.topPos + 34, 18, 19,
                mouseX, mouseY,
                Component.literal(operationsLeft + " operations left")
        );

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int panelCenterX = imageWidth / 2;
        guiGraphics.drawString(this.font, this.title, panelCenterX - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}