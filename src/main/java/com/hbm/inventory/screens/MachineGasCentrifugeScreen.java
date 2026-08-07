package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineGasCentrifugeBlockEntity;
import com.hbm.inventory.menus.MachineGasCentrifugeMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineGasCentrifugeScreen extends InfoScreen<MachineGasCentrifugeMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gas_centrifuge.png");

    private final MachineGasCentrifugeBlockEntity be;

    public MachineGasCentrifugeScreen(MachineGasCentrifugeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 206;
        this.imageHeight = 204;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        be.inputTank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 16, this.topPos + 16, 22, 52);
        be.outputTank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 138, this.topPos + 16, 22, 52);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 182, this.topPos + 17, 16, 52, be.power, be.maxPower);

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

        int p = (int) (be.power * 52 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 182, this.topPos + 17 + (52 - p), 206, 52 - p, 16, p);

        if(be.progress > 0) {
            int progress = (int) ((float) be.progress * 36 / be.processingSpeed);
            guiGraphics.blit(TEXTURE, this.leftPos + 70, this.topPos + 35, 206, 52, progress, 13);
        }

        be.inputTank.renderTank(this.leftPos + 16, this.topPos + 68, 1F, 6, 52, 2);
        be.inputTank.renderTank(this.leftPos + 32, this.topPos + 68, 1F, 6, 52, 2);
        be.outputTank.renderTank(this.leftPos + 138, this.topPos + 68, 1F, 6, 52, 2);
        be.outputTank.renderTank(this.leftPos + 154, this.topPos + 68, 1F, 6, 52, 2);
    }
}
