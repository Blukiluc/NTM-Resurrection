package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineFurnaceSteelBlockEntity;
import com.hbm.inventory.menus.MachineFurnaceSteelMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Locale;

public class MachineFurnaceSteelScreen extends InfoScreen<MachineFurnaceSteelMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_furnace_steel.png");

    private final MachineFurnaceSteelBlockEntity be;

    public MachineFurnaceSteelScreen(MachineFurnaceSteelMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for(int i = 0; i < 3; i++) {
            this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 53, this.topPos + 17 + 18 * i, 70, 7, mouseX, mouseY,
                    Component.literal(String.format(Locale.US, "%,d / %,d TU", this.be.progress[i], MachineFurnaceSteelBlockEntity.PROCESS_TIME)));
            this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 53, this.topPos + 26 + 18 * i, 70, 7, mouseX, mouseY,
                    Component.literal("Bonus: " + this.be.bonus[i] + "%"));
        }

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 151, this.topPos + 18, 9, 50, mouseX, mouseY,
                Component.literal(String.format(Locale.US, "%,d / %,d TU", this.be.heat, MachineFurnaceSteelBlockEntity.MAX_HEAT)));
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        int heat = this.be.heat * 48 / MachineFurnaceSteelBlockEntity.MAX_HEAT;
        if(heat > 0) guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 67 - heat, 176, 76 - heat, 7, heat, 256, 256);

        for(int i = 0; i < 3; i++) {
            int progress = this.be.progress[i] * 69 / MachineFurnaceSteelBlockEntity.PROCESS_TIME;
            if(progress > 0) guiGraphics.blit(TEXTURE, this.leftPos + 54, this.topPos + 18 + 18 * i, 176, 18, progress, 5, 256, 256);

            int bonus = this.be.bonus[i] * 69 / 100;
            if(bonus > 0) guiGraphics.blit(TEXTURE, this.leftPos + 54, this.topPos + 27 + 18 * i, 176, 23, bonus, 5, 256, 256);

            if(this.be.wasOn) guiGraphics.blit(TEXTURE, this.leftPos + 16, this.topPos + 16 + 18 * i, 176, 0, 18, 18, 256, 256);
        }
    }
}
