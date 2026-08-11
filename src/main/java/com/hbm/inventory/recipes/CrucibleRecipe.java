package com.hbm.inventory.recipes;

import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrucibleRecipe extends GenericRecipe {

    public MaterialStack[] input = new MaterialStack[0];
    public MaterialStack[] output = new MaterialStack[0];
    public int frequency = 1;

    public CrucibleRecipe(String name) {
        super(name);
    }

    public CrucibleRecipe setup(int frequency, ItemStack icon) {
        this.frequency = Math.max(1, frequency);
        this.setIcon(icon);
        this.setNamed();
        return this;
    }

    public CrucibleRecipe inputs(MaterialStack... input) {
        this.input = input;
        return this;
    }

    public CrucibleRecipe outputs(MaterialStack... output) {
        this.output = output;
        return this;
    }

    public int getInputAmount() {
        int amount = 0;
        for (MaterialStack stack : this.input) amount += stack.amount;
        return amount;
    }

    @Override
    public List<Component> print() {
        List<Component> lines = new ArrayList<>();
        lines.add(this.getName().withStyle(ChatFormatting.YELLOW));
        lines.add(Component.translatable("container.recipe.input").withStyle(ChatFormatting.BOLD));
        for (MaterialStack stack : this.input) {
            lines.add(Component.translatable(stack.material.getTranslationKey()).append(": " + stack.amount + " q").withStyle(ChatFormatting.GRAY));
        }
        lines.add(Component.translatable("container.recipe.output").withStyle(ChatFormatting.BOLD));
        for (MaterialStack stack : this.output) {
            lines.add(Component.translatable(stack.material.getTranslationKey()).append(": " + stack.amount + " q").withStyle(ChatFormatting.GRAY));
        }
        return lines;
    }
}
