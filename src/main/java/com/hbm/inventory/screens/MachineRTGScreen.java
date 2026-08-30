package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineRTGBlockEntity;
import com.hbm.inventory.menus.MachineRTGMenu;
import com.hbm.items.machine.RTGPelletItem;
import com.hbm.main.NuclearTechMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class MachineRTGScreen extends InfoScreen<MachineRTGMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/generators/gui_rtg.png");

    private final MachineRTGBlockEntity be;

    public MachineRTGScreen(MachineRTGMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 188;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 146, this.topPos + 9, 16, 51, this.be.power, MachineRTGBlockEntity.MAX_POWER);
        this.drawCustomInfoStat(
                guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 124,
                this.topPos + 9,
                16,
                51,
                mouseX,
                mouseY,
                Component.translatable("desc.gui.rtg.heat", this.be.heat).withStyle(ChatFormatting.YELLOW)
        );

        List<Component> pelletInfo = new ArrayList<>();
        pelletInfo.add(Component.translatable("desc.gui.rtg.pellets"));
        for(RTGPelletItem pellet : RTGPelletItem.getPellets()) {
            pelletInfo.add(Component.translatable(
                    "desc.gui.rtg.pelletPower",
                    Component.translatable(pellet.getDescriptionId()),
                    pellet.getPower()
            ));
        }
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 12, this.topPos + 25, 16, 16, mouseX, mouseY, pelletInfo);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 60 - this.font.width(this.title) / 2, 7, 10925486, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.be.heat > 0) {
            int heat = this.be.heat * 51 / MachineRTGBlockEntity.HEAT_MAX;
            guiGraphics.blit(TEXTURE, this.leftPos + 124, this.topPos + 61 - heat, 176, 10 + 51 - heat, 16, heat);
        }

        if(this.be.power > 0) {
            int power = (int) (this.be.power * 51 / MachineRTGBlockEntity.MAX_POWER);
            guiGraphics.blit(TEXTURE, this.leftPos + 146, this.topPos + 61 - power, 192, 10 + 51 - power, 16, power);
        }

        this.drawInfoPanel(guiGraphics, this.leftPos - 12, this.topPos + 25, 2);
    }
}
