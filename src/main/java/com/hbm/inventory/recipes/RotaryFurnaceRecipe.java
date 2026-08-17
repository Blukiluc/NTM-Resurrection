package com.hbm.inventory.recipes;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import net.minecraft.world.item.ItemStack;

public class RotaryFurnaceRecipe extends GenericRecipe {

    public int steam;

    public RotaryFurnaceRecipe(String name) {
        super(name);
    }

    @Override
    public RotaryFurnaceRecipe setDuration(int duration) {
        super.setDuration(duration);
        return this;
    }

    public RotaryFurnaceRecipe setSteam(int steam) {
        this.steam = steam;
        return this;
    }

    @Override
    public RotaryFurnaceRecipe inputItems(AStack... input) {
        super.inputItems(input);
        return this;
    }

    @Override
    public RotaryFurnaceRecipe inputFluids(FluidStack... input) {
        super.inputFluids(input);
        return this;
    }

    @Override
    public RotaryFurnaceRecipe outputItems(ItemStack... output) {
        super.outputItems(output);
        return this;
    }
}
