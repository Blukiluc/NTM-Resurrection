package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineTurbofanBlockEntity;
import com.hbm.inventory.menus.MachineTurbofanMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineTurbofanScreen extends InfoScreen<MachineTurbofanMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/generators/gui_turbofan.png");

    private final MachineTurbofanBlockEntity be;

    public MachineTurbofanScreen(MachineTurbofanMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 203;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 143, this.topPos + 17, 16, 52, this.be.power, MachineTurbofanBlockEntity.MAX_POWER);
        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 35, this.topPos + 16, 34, 52);

        if(this.be.showBlood) {
            this.be.blood.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 98, this.topPos + 17, 16, 16);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 43 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int power = (int) this.be.getPowerScaled(52);
        guiGraphics.blit(TEXTURE, this.leftPos + 143, this.topPos + 69 - power, 176 + 16, 52 - power, 16, power);

        if(this.be.afterburner > 0) {
            int stage = Math.min(this.be.afterburner, 6);
            guiGraphics.blit(TEXTURE, this.leftPos + 98, this.topPos + 44, 176, (stage - 1) * 16, 16, 16);
        }

        if(this.be.showBlood) {
            this.be.blood.renderTank(this.leftPos + 97, this.topPos + 16, 0, 16, 16);
        }

        this.be.tank.renderTank(this.leftPos + 35, this.topPos + 69, 0, 34, 52);
    }
}