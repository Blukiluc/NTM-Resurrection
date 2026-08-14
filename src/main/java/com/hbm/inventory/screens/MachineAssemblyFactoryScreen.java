package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineAssemblyFactoryBlockEntity;
import com.hbm.inventory.menus.MachineAssemblyFactoryMenu;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
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

public class MachineAssemblyFactoryScreen extends InfoScreen<MachineAssemblyFactoryMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/assembly_factory.png");

    private final MachineAssemblyFactoryBlockEntity be;

    public MachineAssemblyFactoryScreen(MachineAssemblyFactoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 256;
        this.imageHeight = 240;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for(int i = 0; i < 4; i++) {
            be.inputTanks[i].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 105 + (i % 2) * 109, this.topPos + 20 + (i / 2) * 56, 5, 32);
            be.outputTanks[i].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 105 + (i % 2) * 109, this.topPos + 54 + (i / 2) * 56, 5, 16);
        }

        be.water.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 232, this.topPos + 149, 7, 52);
        be.lps.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 241, this.topPos + 149, 7, 52);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 234, this.topPos + 18, 16, 92, be.power, be.maxPower);

        for(int i = 0; i < 4; i++) {
            if(this.leftPos + 6 + (i % 2) * 109 <= mouseX && this.leftPos + 24 + (i % 2) * 109 > mouseX && this.topPos + 53 + (i / 2) * 56 < mouseY && this.topPos + 71 + (i / 2) * 56 >= mouseY) {
                GenericRecipe recipe = be.assemblerModule[i].getRecipe();
                if(recipe != null) guiGraphics.renderComponentTooltip(this.font, recipe.print(), mouseX, mouseY);
                else guiGraphics.renderTooltip(this.font, Component.translatable("container.recipe.set_recipe").withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
            }
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(int i = 0; i < 4; i++) {
            if(this.checkClick(mouseX, mouseY, 6 + (i % 2) * 109, 53 + (i / 2) * 56, 18, 18)) {
                RecipeSelectorScreen.openSelector(AssemblyMachineRecipes.INSTANCE, be, be.assemblerModule[i].recipe, i, BlueprintsItem.grabPool(be.slots.get(4 + i * 14)), this);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 113 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 33, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, 256, 140);
        guiGraphics.blit(TEXTURE, this.leftPos + 25, this.topPos + 140, 25, 140, 231, 100);

        int power = (int) (be.power * 92 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 234, this.topPos + 110 - power, 0, 232 - power, 16, power);

        for(int i = 0; i < 4; i++) {
            if(be.assemblerModule[i].progress > 0) {
                int progress = (int) Math.ceil(37 * be.assemblerModule[i].progress);
                guiGraphics.blit(TEXTURE, this.leftPos + 45 + (i % 2) * 109, this.topPos + 63 + (i / 2) * 56, 0, 240, progress, 6);
            }
        }

        for(int i = 0; i < 4; i++) {
            GenericRecipe recipe = be.assemblerModule[i].getRecipe();

            /// LEFT LED
            if(be.didProcess[i]) {
                guiGraphics.blit(TEXTURE, this.leftPos + 45 + (i % 2) * 109, this.topPos + 55 + (i / 2) * 56, 4, 236, 4, 4);
            } else if(recipe != null) {
                guiGraphics.blit(TEXTURE, this.leftPos + 45 + (i % 2) * 109, this.topPos + 55 + (i / 2) * 56, 0, 236, 4, 4);
            }

            /// RIGHT LED
            if(be.didProcess[i]) {
                guiGraphics.blit(TEXTURE, this.leftPos + 53 + (i % 2) * 109, this.topPos + 55 + (i / 2) * 56, 4, 236, 4, 4);
            } else if(recipe != null && be.power >= recipe.power && be.canCool()) {
                guiGraphics.blit(TEXTURE, this.leftPos + 53 + (i % 2) * 109, this.topPos + 55 + (i / 2) * 56, 0, 236, 4, 4);
            }

            guiGraphics.renderItem(recipe != null ? recipe.getIcon() : new ItemStack(NtmItems.TEMPLATE_FOLDER.get()), this.leftPos + 7 + (i % 2) * 109, this.topPos + 54 + (i / 2) * 56);

            if(recipe != null && recipe.inputItem != null) {
                RenderSystem.setShaderColor(1F, 1F, 1F, 0.5F);
                for(int input = 0; input < recipe.inputItem.length; input++) {
                    Slot slot = this.menu.slots.get(be.assemblerModule[i].inputSlots[input]);
                    if(!slot.hasItem()) guiGraphics.renderItem(recipe.inputItem[input].extractForCyclingDisplay(20), this.leftPos + slot.x, this.topPos + slot.y);
                }
                RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            }
        }

        for(int i = 0; i < 4; i++) {
            be.inputTanks[i].renderTank(this.leftPos + 105 + (i % 2) * 109, this.topPos + 52 + (i / 2) * 56, 1F, 5, 32);
            be.outputTanks[i].renderTank(this.leftPos + 105 + (i % 2) * 109, this.topPos + 70 + (i / 2) * 56, 1F, 5, 16);
        }

        be.water.renderTank(this.leftPos + 232, this.topPos + 201, 1F, 7, 52);
        be.lps.renderTank(this.leftPos + 241, this.topPos + 201, 1F, 7, 52);
    }
}
