package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineChemicalFactoryBlockEntity;
import com.hbm.inventory.menus.MachineChemicalFactoryMenu;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.BlueprintsItem;
import com.hbm.main.NuclearTechMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineChemicalFactoryScreen extends InfoScreen<MachineChemicalFactoryMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/chemical_factory.png");

    private final MachineChemicalFactoryBlockEntity be;

    public MachineChemicalFactoryScreen(MachineChemicalFactoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 248;
        this.imageHeight = 216;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for(int tank = 0; tank < 3; tank++) for(int module = 0; module < 4; module++) {
            be.inputTanks[tank + module * 3].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 60 + tank * 5, this.topPos + 20 + module * 22, 3, 16);
            be.outputTanks[tank + module * 3].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 189 + tank * 5, this.topPos + 20 + module * 22, 3, 16);
        }

        be.water.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 224, this.topPos + 125, 7, 52);
        be.lps.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 233, this.topPos + 125, 7, 52);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 224, this.topPos + 18, 16, 68, be.power, be.maxPower);

        for(int i = 0; i < 4; i++) {
            if(this.leftPos + 74 <= mouseX && this.leftPos + 92 > mouseX && this.topPos + 19 + i * 22 < mouseY && this.topPos + 37 + i * 22 >= mouseY) {
                GenericRecipe recipe = be.chemplantModule[i].getRecipe();
                if(recipe != null) guiGraphics.renderComponentTooltip(this.font, recipe.print(), mouseX, mouseY);
                else guiGraphics.renderTooltip(this.font, Component.translatable("container.recipe.set_recipe").withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
            }
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(int i = 0; i < 4; i++) {
            if(this.checkClick(mouseX, mouseY, 74, 19 + i * 22, 18, 18)) {
                RecipeSelectorScreen.openSelector(ChemicalPlantRecipes.INSTANCE, be, be.chemplantModule[i].recipe, i, BlueprintsItem.grabPool(be.slots.get(4 + i * 7)), this);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 106 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 26, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, 248, 116);
        guiGraphics.blit(TEXTURE, this.leftPos + 18, this.topPos + 116, 18, 116, 230, 100);

        int power = (int) (be.power * 68 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 224, this.topPos + 86 - power, 0, 184 - power, 16, power);

        for(int i = 0; i < 4; i++) {
            if(be.chemplantModule[i].progress > 0) {
                int progress = (int) Math.ceil(22 * be.chemplantModule[i].progress);
                guiGraphics.blit(TEXTURE, this.leftPos + 113, this.topPos + 29 + i * 22, 0, 216, progress, 6);
            }
        }

        for(int i = 0; i < 4; i++) {
            GenericRecipe recipe = be.chemplantModule[i].getRecipe();

            /// LEFT LED
            if(be.didProcess[i]) {
                guiGraphics.blit(TEXTURE, this.leftPos + 113, this.topPos + 21 + i * 22, 4, 222, 4, 4);
            } else if(recipe != null) {
                guiGraphics.blit(TEXTURE, this.leftPos + 113, this.topPos + 21 + i * 22, 0, 222, 4, 4);
            }

            /// RIGHT LED
            if(be.didProcess[i]) {
                guiGraphics.blit(TEXTURE, this.leftPos + 121, this.topPos + 21 + i * 22, 4, 222, 4, 4);
            } else if(recipe != null && be.power >= recipe.power && be.canCool()) {
                guiGraphics.blit(TEXTURE, this.leftPos + 121, this.topPos + 21 + i * 22, 0, 222, 4, 4);
            }

            guiGraphics.renderItem(recipe != null ? recipe.getIcon() : new ItemStack(NtmItems.TEMPLATE_FOLDER.get()), this.leftPos + 75, this.topPos + 20 + i * 22);

            if(recipe != null && recipe.inputItem != null) {
                RenderSystem.setShaderColor(1F, 1F, 1F, 0.5F);
                for(int input = 0; input < recipe.inputItem.length; input++) {
                    Slot slot = this.menu.slots.get(be.chemplantModule[i].inputSlots[input]);
                    if(!slot.hasItem()) guiGraphics.renderItem(recipe.inputItem[input].extractForCyclingDisplay(20), this.leftPos + slot.x, this.topPos + slot.y);
                }
                RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            }
        }

        for(int tank = 0; tank < 3; tank++) for(int module = 0; module < 4; module++) {
            be.inputTanks[tank + module * 3].renderTank(this.leftPos + 60 + tank * 5, this.topPos + 36 + module * 22, 1F, 3, 16);
            be.outputTanks[tank + module * 3].renderTank(this.leftPos + 189 + tank * 5, this.topPos + 36 + module * 22, 1F, 3, 16);
        }

        be.water.renderTank(this.leftPos + 224, this.topPos + 177, 1F, 7, 52);
        be.lps.renderTank(this.leftPos + 233, this.topPos + 177, 1F, 7, 52);
    }
}
