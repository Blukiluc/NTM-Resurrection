package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.oil.MachineCatalyticReformerBlockEntity;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.menus.MachineCatalyticReformerMenu;
import com.hbm.inventory.recipes.CatalyticReformerRecipes;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.Tuple;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineCatalyticReformerScreen extends InfoScreen<MachineCatalyticReformerMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_catalytic_reformer.png");

    private final MachineCatalyticReformerBlockEntity be;

    public MachineCatalyticReformerScreen(MachineCatalyticReformerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 238;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 35, this.topPos + 18, 21, 52);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 107, this.topPos + 18, 16, 52);
        this.be.tanks[2].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 125, this.topPos + 18, 16, 52);
        this.be.tanks[3].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 143, this.topPos + 18, 16, 52);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 17, this.topPos + 18, 16, 52, this.be.power, MachineCatalyticReformerBlockEntity.MAX_POWER);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 4, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int p = (int) (be.power * 52 / be.MAX_POWER);
        guiGraphics.blit(TEXTURE, this.leftPos + 17, this.topPos + 18 + (52 - p), 176, 52 - p, 16, p);

        this.be.tanks[0].renderTank(this.leftPos + 35, this.topPos + 18, 0, 16, 52);
        this.be.tanks[1].renderTank(this.leftPos + 107, this.topPos + 18, 0, 16, 52);
        this.be.tanks[2].renderTank(this.leftPos + 125, this.topPos + 18, 0, 16, 52);
        this.be.tanks[3].renderTank(this.leftPos + 143, this.topPos + 18, 0, 16, 52);
    }
}