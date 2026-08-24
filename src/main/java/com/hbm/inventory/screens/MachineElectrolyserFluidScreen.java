package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineElectrolyserBlockEntity;
import com.hbm.inventory.menus.MachineElectrolyserFluidMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class MachineElectrolyserFluidScreen extends InfoScreen<MachineElectrolyserFluidMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_electrolyser_fluid.png");

    private final MachineElectrolyserBlockEntity be;

    private static final int BUTTON_X = 8;
    private static final int BUTTON_Y = 82;
    private static final int BUTTON_W = 54;
    private static final int BUTTON_H = 12;

    public MachineElectrolyserFluidScreen(MachineElectrolyserFluidMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 210;
        this.imageHeight = 204;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 42, this.topPos + 17, 16, 52);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 96, this.topPos + 17, 16, 52);
        this.be.tanks[2].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 116, this.topPos + 17, 16, 52);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 186, this.topPos + 18, 16, 89, this.be.power, MachineElectrolyserBlockEntity.MAX_POWER);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2 - 16, 7, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int p = (int) (this.be.power * 89 / MachineElectrolyserBlockEntity.MAX_POWER);
        guiGraphics.blit(TEXTURE, this.leftPos + 186, this.topPos + 107 - p, 210, 89 - p, 16, p);

        this.be.tanks[0].renderTank(this.leftPos + 42, this.topPos + 70, 0, 16, 52);
        this.be.tanks[1].renderTank(this.leftPos + 96, this.topPos + 70, 0, 16, 52);
        this.be.tanks[2].renderTank(this.leftPos + 116, this.topPos + 70, 0, 16, 52);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        int x = (int) mouseX;
        int y = (int) mouseY;

        if(this.leftPos + BUTTON_X <= x && this.leftPos + BUTTON_X + BUTTON_W > x
                && this.topPos + BUTTON_Y < y && this.topPos + BUTTON_Y + BUTTON_H >= y) {

            // TODO: play a UI click sound here once the exact SoundEvent constant is confirmed

            CompoundTag data = new CompoundTag();
            data.putBoolean("sgm", true);
            PacketDistributor.sendToServer(new CompoundTagControl(data, this.be.getBlockPos()));

            return true;
        }

        return handled;
    }
}