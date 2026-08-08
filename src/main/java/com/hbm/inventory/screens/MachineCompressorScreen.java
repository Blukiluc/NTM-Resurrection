package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineCompressorBaseBlockEntity;
import com.hbm.inventory.menus.MachineCompressorMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class MachineCompressorScreen extends InfoScreen<MachineCompressorMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_compressor.png");

    private final MachineCompressorBaseBlockEntity be;

    public MachineCompressorScreen(MachineCompressorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 204;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 17, this.topPos + 18, 16, 52);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 107, this.topPos + 18, 16, 52);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 52, this.be.power, MachineCompressorBaseBlockEntity.MAX_POWER);

        for(int pressure = 0; pressure < 5; pressure++) {
            this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 43 + pressure * 11, this.topPos + 46, 8, 14, mouseX, mouseY,
                    Component.literal(pressure + " PU -> " + (pressure + 1) + " PU"));
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(int pressure = 0; pressure < 5; pressure++) {
            if(this.checkClick(mouseX, mouseY, 43 + pressure * 11, 46, 8, 14)) {
                this.click();
                CompoundTag tag = new CompoundTag();
                tag.putInt("compression", pressure);
                PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 70 - this.font.width(this.title) / 2, 6, 0xC7C1A3, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.be.power >= this.be.powerRequirement) {
            guiGraphics.blit(TEXTURE, this.leftPos + 156, this.topPos + 4, 176, 52, 9, 12);
        }

        guiGraphics.blit(TEXTURE, this.leftPos + 43 + this.be.tanks[0].getPressure() * 11, this.topPos + 46, 193, 18, 8, 14);

        if(this.be.processTime > 0) {
            int progress = this.be.progress * 55 / this.be.processTime;
            guiGraphics.blit(TEXTURE, this.leftPos + 42, this.topPos + 26, 192, 0, progress, 17);
        }

        int power = (int) (this.be.power * 52 / MachineCompressorBaseBlockEntity.MAX_POWER);
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 70 - power, 176, 52 - power, 16, power);

        this.be.tanks[0].renderTank(this.leftPos + 17, this.topPos + 70, 0, 16, 52);
        this.be.tanks[1].renderTank(this.leftPos + 107, this.topPos + 70, 0, 16, 52);
    }
}
