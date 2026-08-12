package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineFELBlockEntity;
import com.hbm.inventory.menus.MachineFELMenu;
import com.hbm.items.machine.ItemFELCrystal.EnumWavelengths;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class MachineFELScreen extends InfoScreen<MachineFELMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/fel.png");
    private static final int TITLE_LEFT = 177;
    private static final int TITLE_TOP = 0;
    private static final int TITLE_WIDTH = 26;
    private static final int TITLE_HEIGHT = 21;

    private final MachineFELBlockEntity be;

    public MachineFELScreen(MachineFELMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 203;
        this.imageHeight = 169;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 182, this.topPos + 27, 16, 113, this.be.power, MachineFELBlockEntity.MAX_POWER);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.checkClick(mouseX, mouseY, 142, 41, 29, 17)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("toggle", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int titleX = 1 + TITLE_LEFT + (TITLE_WIDTH - this.font.width(this.title)) / 2;
        int titleY = 1 + TITLE_TOP + (TITLE_HEIGHT - this.font.lineHeight) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, titleY, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 98, 4210752, false);

        if(this.be.isOn) {
            Component status = this.be.missingValidSilex
                    ? Component.translatable("gui.hbm.fel.error")
                    : Component.translatable("gui.hbm.fel.live");
            int color = this.be.missingValidSilex ? 0xFF0000 : 0x00FF00;
            guiGraphics.drawString(this.font, status, 157 - this.font.width(status) / 2, 9, color, false);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.be.isOn) {
            guiGraphics.blit(TEXTURE, this.leftPos + 142, this.topPos + 41, 203, 0, 29, 17);
        }

        int power = (int) (this.be.power * 114 / MachineFELBlockEntity.MAX_POWER);
        guiGraphics.blit(TEXTURE, this.leftPos + 182, this.topPos + 140 - power, 203, 130 - power, 16, power);

        if(this.be.isBeamActive() && this.be.mode != EnumWavelengths.NULL && this.be.getLevel() != null) {
            int color = 0xFF000000 | this.be.mode.getGuiColor(this.be.getLevel().getGameTime());
            guiGraphics.fill(this.leftPos + 113, this.topPos + 31, this.leftPos + 135, this.topPos + 32, color);
            guiGraphics.fill(0, this.topPos + 31, this.leftPos + 4, this.topPos + 32, color);
        }
    }
}
