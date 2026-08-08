package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineFurnaceIronBlockEntity;
import com.hbm.inventory.menus.MachineFurnaceIronMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class MachineFurnaceIronScreen extends InfoScreen<MachineFurnaceIronMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_furnace_iron.png");

    private final MachineFurnaceIronBlockEntity be;

    public MachineFurnaceIronScreen(MachineFurnaceIronMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 52, this.topPos + 35, 71, 7, mouseX, mouseY,
                Component.literal(this.be.progress * 100 / Math.max(this.be.processingTime, 1) + "%"));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 52, this.topPos + 44, 71, 7, mouseX, mouseY,
                Component.literal(this.be.burnTime / 20 + "s"));
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        Slot hoveredSlot = this.hoveredSlot;
        if(hoveredSlot != null
                && (hoveredSlot.index == MachineFurnaceIronBlockEntity.SLOT_FUEL_1 || hoveredSlot.index == MachineFurnaceIronBlockEntity.SLOT_FUEL_2)
                && this.menu.getCarried().isEmpty()
                && !hoveredSlot.hasItem()) {
            List<Component> tooltip = new ArrayList<>();
            for(String line : MachineFurnaceIronBlockEntity.burnModule.getTimeDesc()) {
                tooltip.add(Component.literal(line));
            }
            if(!tooltip.isEmpty()) guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        int progress = this.be.progress * 70 / Math.max(this.be.processingTime, 1);
        if(progress > 0) guiGraphics.blit(TEXTURE, this.leftPos + 53, this.topPos + 36, 176, 18, progress, 5, 256, 256);

        int burn = this.be.burnTime * 70 / Math.max(this.be.maxBurnTime, 1);
        if(burn > 0) guiGraphics.blit(TEXTURE, this.leftPos + 53, this.topPos + 45, 176, 23, burn, 5, 256, 256);

        if(this.be.canSmelt()) guiGraphics.blit(TEXTURE, this.leftPos + 70, this.topPos + 16, 176, 0, 18, 18, 256, 256);
    }
}
