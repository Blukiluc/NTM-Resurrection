package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes.IOutput;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CompressorRecipeCategory implements IRecipeCategory<GenericRecipe> {

    private static final ResourceLocation BACKGROUND_TEXTURE =
            NuclearTechMod.withDefaultNamespace("textures/gui/jei/assembly_machine.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 86;

    private final IDrawable background;
    private final IDrawable icon;

    public CompressorRecipeCategory(IGuiHelper gui) {
        this.background = gui.createDrawable(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = gui.createDrawableItemStack(new ItemStack(
                NtmBlocks.MACHINE_COMPRESSOR.get()));
    }

    @Override public RecipeType<GenericRecipe> getRecipeType() { return NtmJeiRecipeTypes.COMPRESSOR; }
    @Override public Component getTitle() { return Component.translatable("block.hbm.machine_compressor"); }
    @Override public IDrawable getIcon() { return icon; }

    @Override public int getWidth() { return WIDTH; }
    @Override public int getHeight() { return HEIGHT; }

    @Override
    public void draw(GenericRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenericRecipe recipe, IFocusGroup focuses) {

        // input items
        if(recipe.inputItem != null) {
            for(int i = 0; i < recipe.inputItem.length; i++) {
                AStack stack = recipe.inputItem[i];
                int x = 5 + (i % 6) * 18;
                int y = 5 + (i / 6) * 18;

                builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                        .addItemStacks(stack.extractForJEI());
            }
        }

        // input fluids
        if(recipe.inputFluid != null) {
            for(int i = 0; i < recipe.inputFluid.length; i++) {
                FluidStack fluid = recipe.inputFluid[i];
                int slot = (recipe.inputItem == null ? 0 : recipe.inputItem.length) + i;
                int x = 5 + (slot % 6) * 18;
                int y = 5 + (slot / 6) * 18;

                builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                        .addItemStack(FluidIconItem.make(fluid.type, fluid.fill));
            }
        }

        // output items
        if(recipe.outputItem != null) {
            for(int i = 0; i < recipe.outputItem.length; i++) {
                IOutput output = recipe.outputItem[i];
                int x = 120 + (i % 3) * 18;
                int y = 5 + (i / 3) * 18;

                List<ItemStack> possibilities = new ArrayList<>(List.of(output.getAllPossibilities()));

                builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                        .addItemStacks(possibilities);
            }
        }

        // output fluids
        if(recipe.outputFluid != null) {
            for(int i = 0; i < recipe.outputFluid.length; i++) {
                FluidStack fluid = recipe.outputFluid[i];
                int slot = (recipe.outputItem == null ? 0 : recipe.outputItem.length) + i;
                int x = 120 + (slot % 3) * 18;
                int y = 5 + (slot / 3) * 18;

                builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                        .addItemStack(FluidIconItem.make(fluid.type, fluid.fill));
            }
        }
    }
}
