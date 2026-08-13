package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineCombustionEngineBlockEntity;
import com.hbm.inventory.menus.MachineCombustionEngineMenu;
import com.hbm.items.machine.PistonSetItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Locale;

public class MachineCombustionEngineScreen extends InfoScreen<MachineCombustionEngineMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/generators/gui_combustion.png");

    private final MachineCombustionEngineBlockEntity be;
    private int setting;
    private boolean sliderDragging;

    public MachineCombustionEngineScreen(MachineCombustionEngineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.setting = this.be.setting;
        this.imageWidth = 176;
        this.imageHeight = 203;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(this.sliderDragging) this.updateSetting(mouseX);
        if(!this.sliderDragging) this.setting = this.be.setting;

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        if(!this.sliderDragging) {
            this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 143, this.topPos + 17, 16, 52, this.be.power, MachineCombustionEngineBlockEntity.MAX_POWER);
            this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 35, this.topPos + 17, 16, 52);
        }

        if(this.sliderDragging || this.isHovered(mouseX, mouseY, 80, 38, 34, 8)) {
            int tooltipX = Mth.clamp(mouseX, this.leftPos + 80, this.leftPos + 114);
            int tooltipY = Mth.clamp(mouseY, this.topPos + 38, this.topPos + 46);
            guiGraphics.renderTooltip(this.font, Component.literal(String.format(Locale.US, "%.1f mB/t", this.setting * 0.2D)), tooltipX, tooltipY);
        }

        if(this.be.slots.get(2).getItem() instanceof PistonSetItem) {
            long power = this.be.getGenerationPerTick();
            this.drawCustomInfoStat(
                    guiGraphics,
                    mouseX,
                    mouseY,
                    this.leftPos + 79,
                    this.topPos + 50,
                    35,
                    14,
                    mouseX,
                    mouseY,
                    Component.literal(String.format(Locale.US, "%,d HE/t", power)),
                    Component.literal(String.format(Locale.US, "%,d HE/s", power * 20))
            );
        }

        this.drawCustomInfoStat(
                guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 79,
                this.topPos + 13,
                35,
                15,
                mouseX,
                mouseY,
                Component.literal("Ignition")
        );

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.be.slots.get(2).getItem() instanceof PistonSetItem) {
            int piston = com.hbm.inventory.MetaHelper.getMeta(this.be.slots.get(2));
            guiGraphics.blit(TEXTURE, this.leftPos + 80, this.topPos + 51, 176, 52 + piston * 12, 25, 12);
        }

        guiGraphics.blit(TEXTURE, this.leftPos + 79 + this.setting * 32 / MachineCombustionEngineBlockEntity.MAX_SETTING, this.topPos + 38, 192, 15, 4, 8);

        if(this.be.isOn) guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 13, 192, 0, 35, 15);

        if(this.be.power > 0) {
            int power = (int) (this.be.power * 53 / MachineCombustionEngineBlockEntity.MAX_POWER);
            guiGraphics.blit(TEXTURE, this.leftPos + 143, this.topPos + 69 - power, 176, 52 - power, 16, power);
        }

        this.be.tank.renderTank(this.leftPos + 35, this.topPos + 69, 0, 16, 52);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isHovered(mouseX, mouseY, 89, 13, 16, 14)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("turnOn", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
        }

        if(this.isHovered(mouseX, mouseY, 79, 38, 36, 8)) {
            this.click();
            this.sliderDragging = true;
            this.updateSetting(mouseX);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.sliderDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateSetting(double mouseX) {
        int requested = Mth.clamp((int) ((mouseX - this.leftPos - 81) * MachineCombustionEngineBlockEntity.MAX_SETTING / 32D), 0, MachineCombustionEngineBlockEntity.MAX_SETTING);
        if(this.setting == requested) return;

        this.setting = requested;
        this.be.setting = requested;
        CompoundTag tag = new CompoundTag();
        tag.putInt("setting", requested);
        PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
    }
}
