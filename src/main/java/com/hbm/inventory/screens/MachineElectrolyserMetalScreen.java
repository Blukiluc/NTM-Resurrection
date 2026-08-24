package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineElectrolyserBlockEntity;
import com.hbm.inventory.menus.MachineElectrolyserMetalMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class MachineElectrolyserMetalScreen extends InfoScreen<MachineElectrolyserMetalMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_electrolyser_metal.png");

    private final MachineElectrolyserBlockEntity be;

    private static final int BUTTON_X = 8;
    private static final int BUTTON_Y = 82;
    private static final int BUTTON_W = 54;
    private static final int BUTTON_H = 12;

    public MachineElectrolyserMetalScreen(MachineElectrolyserMetalMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 210;
        this.imageHeight = 204;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[3].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 36, this.topPos + 18, 16, 52);

        if(this.be.leftStack != null) {
            // NOTE: assumes NTMMaterial.getName() returns a Component directly, mirroring
            // FluidType.getName() used elsewhere in this port (see FluidTank.renderTankTooltip).
            // Adjust if the real method name/signature differs.
            Component info = this.be.leftStack.material.getName().copy()
                    .append(": " + this.be.leftStack.amount).withStyle(ChatFormatting.YELLOW);
            if(this.leftPos + 58 <= mouseX && this.leftPos + 58 + 34 > mouseX && this.topPos + 18 < mouseY && this.topPos + 18 + 42 >= mouseY) {
                guiGraphics.renderTooltip(this.font, info, (int) mouseX, (int) mouseY);
            }
        }

        if(this.be.rightStack != null) {
            Component info = this.be.rightStack.material.getName().copy()
                    .append(": " + this.be.rightStack.amount).withStyle(ChatFormatting.YELLOW);
            if(this.leftPos + 96 <= mouseX && this.leftPos + 96 + 34 > mouseX && this.topPos + 18 < mouseY && this.topPos + 18 + 42 >= mouseY) {
                guiGraphics.renderTooltip(this.font, info, (int) mouseX, (int) mouseY);
            }
        }

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

        // TODO: molten metal level bars (leftStack/rightStack) - color tint + fill height,
        // needs the confirmed Mats.MaterialStack.material.moltenColor field before wiring up

        int p = (int) (this.be.power * 89 / MachineElectrolyserBlockEntity.MAX_POWER);
        guiGraphics.blit(TEXTURE, this.leftPos + 186, this.topPos + 107 - p, 210, 89 - p, 16, p);

        this.be.tanks[3].renderTank(this.leftPos + 36, this.topPos + 70, 0, 16, 52);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        int x = (int) mouseX;
        int y = (int) mouseY;

        if(this.leftPos + BUTTON_X <= x && this.leftPos + BUTTON_X + BUTTON_W > x
                && this.topPos + BUTTON_Y < y && this.topPos + BUTTON_Y + BUTTON_H >= y) {

            // TODO: play a UI click sound here once the exact SoundEvent constant is confirmed
            // the basic minecraft gui sound i believe

            CompoundTag data = new CompoundTag();
            data.putBoolean("sgf", true);
            PacketDistributor.sendToServer(new CompoundTagControl(data, this.be.getBlockPos()));

            return true;
        }

        return handled;
    }
}