package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineCentrifugeBlockEntity;
import com.hbm.inventory.menus.MachineCentrifugeMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineCentrifugeScreen extends InfoScreen<MachineCentrifugeMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/centrifuge.png");

    private final MachineCentrifugeBlockEntity be;

    public MachineCentrifugeScreen(MachineCentrifugeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 9, this.topPos + 12, 16, 35, be.power, be.maxPower);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int p = (int) (be.power * 35 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 9, this.topPos + 13 + (35 - p), 176, 35 - p, 16, p);

        if(be.progress > 0) {
            float totalProgress = (float) be.progress / be.processingSpeed; // 0.0 à 1.0 sur tout le craft

            int[] barX = {65, 85, 105, 125};
            int barY = 14;
            int barW = 12;
            int barH = 36;

            for(int i = 0; i < 4; i++) {
                float barStart = i * 0.25F;
                float barEnd = (i + 1) * 0.25F;

                float localProgress = (totalProgress - barStart) / 0.25F;
                localProgress = Math.min(1.0F, Math.max(0.0F, localProgress));

                if(totalProgress <= barStart) continue;

                int h = (int)(barH * localProgress);
                if(h > 0) {
                    int screenY = this.topPos + barY + (barH - h);
                    int srcY = 35 + (barH - h);

                    guiGraphics.blit(TEXTURE, this.leftPos + barX[i], screenY, 176, srcY, barW, h);
                }
            }
        }
    }
}
