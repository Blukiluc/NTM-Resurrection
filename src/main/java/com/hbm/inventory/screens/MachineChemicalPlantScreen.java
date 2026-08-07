package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineChemicalPlantBlockEntity;
import com.hbm.inventory.menus.MachineChemicalPlantMenu;
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

public class MachineChemicalPlantScreen extends InfoScreen<MachineChemicalPlantMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/chemical_plant.png");

    private final MachineChemicalPlantBlockEntity be;

    public MachineChemicalPlantScreen(MachineChemicalPlantMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 256;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        int[] tankX = {8, 26, 44, 80, 98, 116};
        for(int i = 0; i < be.tanks.length; i++) {
            be.tanks[i].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + tankX[i], this.topPos + 18, 16, 34);
        }

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 61, be.power, be.maxPower);

        if(this.leftPos + 7 <= mouseX && this.leftPos + 7 + 18 > mouseX && this.topPos + 125 < mouseY && this.topPos + 125 + 18 >= mouseY) {
            if(this.be.assemblerModule.recipe != null && ChemicalPlantRecipes.INSTANCE.recipeNameMap.containsKey(this.be.assemblerModule.recipe)) {
                GenericRecipe recipe = ChemicalPlantRecipes.INSTANCE.recipeNameMap.get(this.be.assemblerModule.recipe);
                guiGraphics.renderComponentTooltip(this.font, recipe.print(), mouseX, mouseY);
                //GUIElements.drawHoveringTextRecipe(recipe.print(), mouseX, mouseY, this.fontRendererObj, itemRender, this.width, this.height);
            } else {
                guiGraphics.renderTooltip(this.font, Component.translatable("container.recipe.set_recipe").withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
            }
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(this.checkClick(mouseX, mouseY, 7, 125, 18, 18)) {
            RecipeSelectorScreen.openSelector(ChemicalPlantRecipes.INSTANCE, be, be.assemblerModule.recipe, 0, BlueprintsItem.grabPool(be.slots.get(1)), this);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 70 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int p = (int) (be.power * 61 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 79 - p, 176, 61 - p, 16, p);

        if(be.assemblerModule.progress > 0) {
            int j = (int) Math.ceil(70 * be.assemblerModule.progress);
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 126, 176, 61, j, 16);
        }

        GenericRecipe recipe = ChemicalPlantRecipes.INSTANCE.recipeNameMap.get(be.assemblerModule.recipe);

        /// LEFT LED
        if(be.didProcess) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 195, 0, 3, 6);
        } else if(recipe != null) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 192, 0, 3, 6);
        }

        /// RIGHT LED
        if(be.didProcess) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 195, 0, 3, 6);
        } else if(recipe != null && be.power >= recipe.power) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 192, 0, 3, 6);
        }

        guiGraphics.renderItem(recipe != null ? recipe.getIcon() : new ItemStack(NtmItems.TEMPLATE_FOLDER.get()), this.leftPos + 8, this.topPos + 126);

        if(recipe != null && recipe.inputItem != null) {

            RenderSystem.setShaderColor(1F, 1F, 1F, 0.5F);
            for(int i = 0; i < recipe.inputItem.length; i++) {
                Slot slot = this.menu.slots.get(be.assemblerModule.inputSlots[i]);
                if(!slot.hasItem()) guiGraphics.renderItem(recipe.inputItem[i].extractForCyclingDisplay(20), this.leftPos + slot.x, this.topPos + slot.y);
            }
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        }

        int[] tankX = {8, 26, 44, 80, 98, 116};
        for(int i = 0; i < be.tanks.length; i++) {
            be.tanks[i].renderTank(this.leftPos + tankX[i], this.topPos + 18 + 34, 1F, 16, 34, 0);
        }
    }
}
