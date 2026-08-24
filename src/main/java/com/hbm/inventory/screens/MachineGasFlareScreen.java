package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.oil.MachineGasFlareBlockEntity;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.menus.MachineGasFlareMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;

public class MachineGasFlareScreen extends InfoScreen<MachineGasFlareMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/generators/gui_flare_stack.png");

    private final MachineGasFlareBlockEntity be;

    public MachineGasFlareScreen(MachineGasFlareMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 203;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 79, this.topPos + 16, 35, 10, mouseX, mouseY, Arrays.stream(I18nUtil.resolveKeyArray("flare.valve")).<Component>map(Component::literal).toList());
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 79, this.topPos + 50, 35, 14, mouseX, mouseY, Arrays.stream(I18nUtil.resolveKeyArray("flare.ignition")).<Component>map(Component::literal).toList());
        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 35, this.topPos + 17, 16, 52);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 143, this.topPos + 17, 16, 52, this.be.power, MachineGasFlareBlockEntity.MAX_POWER);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int power = (int) (this.be.power * 52L / MachineGasFlareBlockEntity.MAX_POWER);
        guiGraphics.blit(TEXTURE, this.leftPos + 143, this.topPos + 69 - power, 176, 94 - power, 16, power);

        if(this.be.isOn) guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 15, 176, 0, 35, 10);
        if(this.be.doesBurn) guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 49, 176, 10, 35, 14);

        if(this.be.isOn && this.be.doesBurn && this.be.tank.getFill() > 0 && this.be.tank.getTankType().hasTrait(FT_Flammable.class)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 88, this.topPos + 29, 176, 24, 18, 18);
        }

        this.be.tank.renderTank(this.leftPos + 35, this.topPos + 69, 0, 16, 52);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isHovered(mouseX, mouseY, 89, 16, 16, 10)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("valve", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
        } else if(this.isHovered(mouseX, mouseY, 89, 50, 16, 14)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("dial", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
