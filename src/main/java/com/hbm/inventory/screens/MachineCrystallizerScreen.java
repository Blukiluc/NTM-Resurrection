package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineCrystallizerBlockEntity;
import com.hbm.inventory.menus.MachineCrystallizerMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class MachineCrystallizerScreen extends InfoScreen<MachineCrystallizerMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_crystallizer_alt.png");

    private final MachineCrystallizerBlockEntity be;

    public MachineCrystallizerScreen(MachineCrystallizerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 204;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 52, this.be.getPower(), this.be.getMaxPower());
        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 35, this.topPos + 18, 16, 52);

        List<Component> upgradeText = List.of(
                Component.translatable("desc.gui.upgrade"),
                Component.translatable("desc.gui.upgrade.speed"),
                Component.translatable("desc.gui.upgrade.effectiveness"),
                Component.translatable("desc.gui.upgrade.overdrive")
        );
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 117, this.topPos + 22, 8, 8, mouseX, mouseY, upgradeText);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 70 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 110, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int power = (int) (this.be.getPower() * 52 / Math.max(this.be.getMaxPower(), 1L));
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 70 - power, 176, 64 - power, 16, power);

        if(this.be.duration > 0 && this.be.progress > 0) {
            int progress = this.be.progress * 28 / this.be.duration;
            guiGraphics.blit(TEXTURE, this.leftPos + 80, this.topPos + 47, 176, 0, progress, 12);
        }

        this.be.tank.renderTank(this.leftPos + 35, this.topPos + 70, 0, 16, 52);
        this.drawInfoPanel(guiGraphics, this.leftPos + 117, this.topPos + 22, 8);
    }
}
