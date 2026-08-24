package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineElectricFurnaceBlockEntity;
import com.hbm.blocks.machine.MachineElectricFurnaceBlock;
import com.hbm.inventory.menus.MachineElectricFurnaceMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class MachineElectricFurnaceScreen extends InfoScreen<MachineElectricFurnaceMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_electric_furnace.png");

    private final MachineElectricFurnaceBlockEntity be;

    public MachineElectricFurnaceScreen(MachineElectricFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 34, this.be.power, MachineElectricFurnaceBlockEntity.MAX_POWER);

        List<Component> upgradeText = List.of(
                Component.translatable("desc.gui.upgrade"),
                Component.translatable("desc.gui.upgrade.speed"),
                Component.translatable("desc.gui.upgrade.power")
        );
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 115, this.topPos + 19, 8, 8, mouseX, mouseY, upgradeText);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 70 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int power = (int) (this.be.power * 34 / MachineElectricFurnaceBlockEntity.MAX_POWER);
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 52 - power, 176, 64 - power, 16, power);

        if(this.be.getBlockState().hasProperty(MachineElectricFurnaceBlock.LIT)
                && this.be.getBlockState().getValue(MachineElectricFurnaceBlock.LIT)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 45, this.topPos + 20, 192, 12, 18, 16);
            guiGraphics.blit(TEXTURE, this.leftPos + 46, this.topPos + 47, 192, 28, 18, 16);
        }

        if(this.be.progress > 0) {
            int progress = this.be.progress * 28 / Math.max(this.be.maxProgress, 1);
            guiGraphics.blit(TEXTURE, this.leftPos + 43, this.topPos + 36, 176, 0, progress, 12);
        }

        this.drawInfoPanel(guiGraphics, this.leftPos + 115, this.topPos + 19, 8);
    }
}
