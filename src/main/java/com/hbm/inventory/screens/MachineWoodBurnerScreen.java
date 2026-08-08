package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineWoodBurnerBlockEntity;
import com.hbm.inventory.menus.MachineWoodBurnerMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class MachineWoodBurnerScreen extends InfoScreen<MachineWoodBurnerMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/generators/gui_wood_burner.png");

    private final MachineWoodBurnerBlockEntity be;

    public MachineWoodBurnerScreen(MachineWoodBurnerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 143, this.topPos + 18, 16, 34, this.be.power, MachineWoodBurnerBlockEntity.MAX_POWER);

        Slot hoveredSlot = this.hoveredSlot;
        if(hoveredSlot != null && hoveredSlot.index == 0 && this.menu.getCarried().isEmpty() && !hoveredSlot.hasItem()) {
            List<Component> tooltip = this.buildBurnTooltip(MachineWoodBurnerBlockEntity.burnModule.getDesc());
            if(!tooltip.isEmpty()) {
                guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
            }
        }

        if(this.be.liquidBurn) {
            this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 80, this.topPos + 18, 16, 52);
        } else {
            this.drawCustomInfoStat(
                    guiGraphics,
                    mouseX,
                    mouseY,
                    this.leftPos + 16,
                    this.topPos + 17,
                    8,
                    54,
                    mouseX,
                    mouseY,
                    Component.literal(this.be.burnTime / 20 + "s")
            );
        }

        this.drawCustomInfoStat(
                guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 53,
                this.topPos + 17,
                16,
                15,
                mouseX,
                mouseY,
                this.be.isOn
                        ? Component.literal("ON").withStyle(ChatFormatting.GREEN)
                        : Component.literal("OFF").withStyle(ChatFormatting.RED)
        );
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 70 - this.font.width(this.title) / 2, 6, 0xffffff, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.be.liquidBurn) {
            guiGraphics.blit(TEXTURE, this.leftPos + 16, this.topPos + 17, 176, 52, 60, 54);
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 17, 176, 106, 36, 54);
        }

        if(this.be.isOn) {
            guiGraphics.blit(TEXTURE, this.leftPos + 53, this.topPos + 17, 196, 0, 16, 15);
        }

        int power = (int) (this.be.power * 34 / Math.max(MachineWoodBurnerBlockEntity.MAX_POWER, 1L));
        guiGraphics.blit(TEXTURE, this.leftPos + 143, this.topPos + 52 - power, 176, 52 - power, 16, power);

        if(this.be.maxBurnTime > 0 && !this.be.liquidBurn) {
            int burn = this.be.burnTime * 52 / this.be.maxBurnTime;
            guiGraphics.blit(TEXTURE, this.leftPos + 17, this.topPos + 70 - burn, 192, 52 - burn, 4, burn);
        }

        if(this.be.liquidBurn) {
            this.be.tank.renderTank(this.leftPos + 80, this.topPos + 70, 0, 16, 52);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isHovered(mouseX, mouseY, 53, 17, 16, 15)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("toggle", false);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
        }

        if(this.isHovered(mouseX, mouseY, 46, 37, 30, 14)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("switch", false);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private List<Component> buildBurnTooltip(List<String> lines) {
        List<Component> tooltip = new ArrayList<>();
        for(String line : lines) {
            tooltip.add(this.colorBurnLine(line));
        }
        return tooltip;
    }

    private Component colorBurnLine(String line) {
        if("Burn time bonuses:".equals(line)) {
            return Component.literal(line).withStyle(ChatFormatting.GOLD);
        }

        if("Burn heat bonuses:".equals(line)) {
            return Component.literal(line).withStyle(ChatFormatting.RED);
        }

        if(line.startsWith("- ")) {
            int separator = line.indexOf(": ");
            if(separator > 0 && separator + 2 < line.length()) {
                String name = line.substring(2, separator);
                String value = line.substring(separator + 2);
                ChatFormatting valueColor = value.startsWith("-") ? ChatFormatting.RED : ChatFormatting.GREEN;

                return Component.literal("- ").withStyle(ChatFormatting.YELLOW)
                        .append(Component.literal(name).withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(": ").withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(value).withStyle(valueColor));
            }
        }

        return Component.literal(line).withStyle(ChatFormatting.GRAY);
    }
}
