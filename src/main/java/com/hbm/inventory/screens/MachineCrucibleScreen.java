package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineCrucibleBlockEntity;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.menus.MachineCrucibleMenu;
import com.hbm.inventory.recipes.CrucibleRecipe;
import com.hbm.inventory.recipes.CrucibleRecipes;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MachineCrucibleScreen extends InfoScreen<MachineCrucibleMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_crucible.png");
    private final MachineCrucibleBlockEntity be;

    public MachineCrucibleScreen(MachineCrucibleMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 214;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderMaterialTooltip(graphics, mouseX, mouseY, 16, 17, this.be.wasteStack);
        this.renderMaterialTooltip(graphics, mouseX, mouseY, 61, 17, this.be.recipeStack);
        this.renderGaugeTooltip(graphics, mouseX, mouseY, 125, 81, this.be.progress, MachineCrucibleBlockEntity.PROCESS_TIME);
        this.renderGaugeTooltip(graphics, mouseX, mouseY, 125, 90, this.be.heat, MachineCrucibleBlockEntity.MAX_HEAT);

        if (this.leftPos + 106 <= mouseX && mouseX < this.leftPos + 124 && this.topPos + 80 <= mouseY && mouseY < this.topPos + 98) {
            CrucibleRecipe recipe = this.be.getLoadedRecipe();
            if (recipe != null) graphics.renderComponentTooltip(this.font, recipe.print(), mouseX, mouseY);
            else graphics.renderTooltip(this.font, Component.translatable("container.recipe.set_recipe").withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
        }
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    private void renderGaugeTooltip(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int amount, int capacity) {
        if (mouseX < this.leftPos + x || mouseX >= this.leftPos + x + 34 || mouseY < this.topPos + y || mouseY >= this.topPos + y + 7) return;
        graphics.renderTooltip(this.font, Component.literal(String.format(Locale.US, "%,d / %,dTU", amount, capacity)), mouseX, mouseY);
    }

    private void renderMaterialTooltip(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, List<MaterialStack> materials) {
        if (mouseX < this.leftPos + x || mouseX >= this.leftPos + x + 36 || mouseY < this.topPos + y || mouseY >= this.topPos + y + 81) return;
        List<Component> lines = new ArrayList<>();
        if (materials.isEmpty()) lines.add(Component.translatable("container.machine_crucible.empty").withStyle(ChatFormatting.RED));
        for (MaterialStack material : materials) {
            lines.add(Component.translatable(material.material.getTranslationKey()).append(": " + material.amount + " q").withStyle(ChatFormatting.YELLOW));
        }
        graphics.renderComponentTooltip(this.font, lines, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.checkClick(mouseX, mouseY, 106, 80, 18, 18)) {
            RecipeSelectorScreen.openSelector(CrucibleRecipes.INSTANCE, this.be, this.be.recipe, 0, null, this);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0xFFFFFF, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int progress = this.be.progress * 33 / MachineCrucibleBlockEntity.PROCESS_TIME;
        if (progress > 0) graphics.blit(TEXTURE, this.leftPos + 126, this.topPos + 82, 176, 0, progress, 5);
        int heat = this.be.heat * 33 / MachineCrucibleBlockEntity.MAX_HEAT;
        if (heat > 0) graphics.blit(TEXTURE, this.leftPos + 126, this.topPos + 91, 176, 5, heat, 5);

        CrucibleRecipe recipe = this.be.getLoadedRecipe();
        graphics.renderItem(recipe != null ? recipe.getIcon() : new ItemStack(NtmItems.TEMPLATE_FOLDER.get()), this.leftPos + 107, this.topPos + 81);
        this.renderMaterials(graphics, this.be.wasteStack, MachineCrucibleBlockEntity.WASTE_CAPACITY, 17, 97);
        this.renderMaterials(graphics, this.be.recipeStack, MachineCrucibleBlockEntity.RECIPE_CAPACITY, 62, 97);
    }

    private void renderMaterials(GuiGraphics graphics, List<MaterialStack> materials, int capacity, int x, int bottom) {
        int stored = 0;
        int previousHeight = 0;
        for (MaterialStack material : materials) {
            stored += material.amount;
            int height = Math.min(79, stored * 79 / capacity);
            if (height > previousHeight) {
                graphics.fill(this.leftPos + x, this.topPos + bottom - height, this.leftPos + x + 34, this.topPos + bottom - previousHeight,
                        0xFF000000 | material.material.moltenColor);
                previousHeight = height;
            }
        }
    }
}
