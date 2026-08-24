package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineRotaryFurnaceBlockEntity;
import com.hbm.inventory.menus.MachineRotaryFurnaceMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class MachineRotaryFurnaceScreen extends InfoScreen<MachineRotaryFurnaceMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_rotary_furnace.png");

    private final MachineRotaryFurnaceBlockEntity be;

    public MachineRotaryFurnaceScreen(MachineRotaryFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 36, 52, 16);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 134, this.topPos + 18, 16, 52);
        this.be.tanks[2].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 52);
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 63, this.topPos + 30, 33, 10, mouseX, mouseY,
                Component.literal(this.be.getProgressScaled(100) + "%"));
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        Slot hoveredSlot = this.hoveredSlot;
        if(hoveredSlot != null
                && hoveredSlot.index == MachineRotaryFurnaceBlockEntity.SLOT_FUEL
                && this.menu.getCarried().isEmpty()
                && !hoveredSlot.hasItem()) {
            List<Component> tooltip = new ArrayList<>();
            for(String line : MachineRotaryFurnaceBlockEntity.burnModule.getDesc()) {
                tooltip.add(Component.literal(line));
            }
            if(!tooltip.isEmpty()) guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 61 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        int progress = this.be.getProgressScaled(33);
        if(progress > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 63, this.topPos + 30, 176, 0, progress, 10, 256, 256);
        }

        int burn = this.be.getBurnScaled(14);
        if(burn > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 26, this.topPos + 69 - burn, 176, 24 - burn, 14, burn, 256, 256);
        }

        this.be.tanks[0].renderTank(this.leftPos + 8, this.topPos + 52, 0, 52, 16, 1);
        this.be.tanks[1].renderTank(this.leftPos + 134, this.topPos + 70, 0, 16, 52);
        this.be.tanks[2].renderTank(this.leftPos + 152, this.topPos + 70, 0, 16, 52);
    }
}
