package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineMixerBlockEntity;
import com.hbm.inventory.menus.MachineMixerMenu;
import com.hbm.inventory.recipes.MixerRecipes;
import com.hbm.inventory.recipes.MixerRecipes.MixerRecipe;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class MachineMixerScreen extends InfoScreen<MachineMixerMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_mixer.png");

    private final MachineMixerBlockEntity be;

    public MachineMixerScreen(MachineMixerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 204;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 12, this.topPos + 18, 16, 52, this.be.getPower(), this.be.getMaxPower());

        List<Component> upgradeText = List.of(
                Component.translatable("desc.gui.upgrade"),
                Component.translatable("desc.gui.upgrade.speed"),
                Component.translatable("desc.gui.upgrade.power"),
                Component.translatable("desc.gui.upgrade.overdrive")
        );
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 55, 8, 8, mouseX, mouseY, upgradeText);

        MixerRecipe[] recipes = MixerRecipes.getOutput(this.be.tanks[2].getTankType());
        if(recipes != null && recipes.length > 1) {
            List<Component> label = new ArrayList<>();
            label.add(Component.literal("Current recipe (" + (this.be.recipeIndex + 1) + "/" + recipes.length + "):").withStyle(ChatFormatting.YELLOW));
            MixerRecipe recipe = recipes[Math.floorMod(this.be.recipeIndex, recipes.length)];
            if(recipe.input1 != null) label.add(Component.literal("-").append(recipe.input1.type.getName()));
            if(recipe.input2 != null) label.add(Component.literal("-").append(recipe.input2.type.getName()));
            if(recipe.solidInput != null) label.add(Component.literal("-").append(recipe.solidInput.extractForCyclingDisplay(20).getHoverName()));
            label.add(Component.literal("Click to change!").withStyle(ChatFormatting.RED));
            this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 71, this.topPos + 17, 12, 12, mouseX, mouseY, label);
        }

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 52, this.topPos + 18, 7, 52);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 61, this.topPos + 18, 7, 52);
        this.be.tanks[2].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 126, this.topPos + 18, 16, 52);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.checkClick(mouseX, mouseY, 71, 17, 12, 12)) {
            MixerRecipe[] recipes = MixerRecipes.getOutput(this.be.tanks[2].getTankType());
            if(recipes != null && recipes.length > 1) {
                this.click();
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("toggle", true);
                PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int titleX = this.imageWidth / 2 + 20 - this.font.width(this.title) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int power = (int) (this.be.getPower() * 52 / Math.max(this.be.getMaxPower(), 1L));
        guiGraphics.blit(TEXTURE, this.leftPos + 12, this.topPos + 70 - power, 176, 52 - power, 16, power);

        if(this.be.processTime > 0 && this.be.progress > 0) {
            int progress = this.be.progress * 52 / this.be.processTime;
            guiGraphics.blit(TEXTURE, this.leftPos + 71, this.topPos + 31, 192, 0, progress, 44);
        }

        this.be.tanks[0].renderTank(this.leftPos + 52, this.topPos + 70, 0, 7, 52);
        this.be.tanks[1].renderTank(this.leftPos + 61, this.topPos + 70, 0, 7, 52);
        this.be.tanks[2].renderTank(this.leftPos + 126, this.topPos + 70, 0, 16, 52);

        this.drawInfoPanel(guiGraphics, this.leftPos + 152, this.topPos + 55, 8);
    }
}
