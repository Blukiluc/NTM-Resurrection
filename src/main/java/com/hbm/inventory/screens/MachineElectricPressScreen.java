package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineElectricPressBlockEntity;
import com.hbm.inventory.menus.MachineElectricPressMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class MachineElectricPressScreen extends InfoScreen<MachineElectricPressMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_electric_press.png");

    private final MachineElectricPressBlockEntity be;

    public MachineElectricPressScreen(MachineElectricPressMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 34, this.be.power, MachineElectricPressBlockEntity.maxPower);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 89 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int power = (int) (this.be.power * 34 / MachineElectricPressBlockEntity.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 52 - power, 176, 34 - power, 16, power);

        float interpolatedPress = Mth.lerp(partialTicks, (float) this.be.lastPress, (float) this.be.renderPress);
        int press = Mth.clamp((int) (interpolatedPress * 16 / MachineElectricPressBlockEntity.maxPress), 0, 16);
        guiGraphics.blit(TEXTURE, this.leftPos + 18, this.topPos + 33, 192, 0, 18, press);
    }
}
