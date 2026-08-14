package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineFurnaceBrickBlockEntity;
import com.hbm.inventory.menus.MachineFurnaceBrickMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineFurnaceBrickScreen extends InfoScreen<MachineFurnaceBrickMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_furnace_brick.png");

    private final MachineFurnaceBrickBlockEntity be;

    public MachineFurnaceBrickScreen(MachineFurnaceBrickMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0xFFFFFF, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        if(this.be.burnTime > 0) {
            int burn = this.be.burnTime * 13 / Math.max(this.be.maxBurnTime, 1);
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 54 + 12 - burn, 176, 12 - burn, 14, burn + 1, 256, 256);

            int progress = this.be.progress * 24 / MachineFurnaceBrickBlockEntity.BASE_TIME;
            guiGraphics.blit(TEXTURE, this.leftPos + 85, this.topPos + 34, 176, 14, progress + 1, 16, 256, 256);
        }
    }
}
