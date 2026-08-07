package com.hbm.inventory.recipes;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class CentrifugeRecipes extends GenericRecipes<GenericRecipe> {

    public static final CentrifugeRecipes INSTANCE = new CentrifugeRecipes();

    @Override public int inputItemLimit() { return 1; }
    @Override public int inputFluidLimit() { return 0; }
    @Override public int outputItemLimit() { return 4; }
    @Override public int outputFluidLimit() { return 0; }

    @Override public String getFileName() { return "hbmCentrifuge.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {
        this.register(new GenericRecipe("centrifuge.iron_ore").setup(200, 100)
                .outputItems(
                        new ItemStack(NtmItems.POWDER_IRON.get(), 1),
                        new ItemStack(NtmItems.POWDER_IRON.get(), 1),
                        new ItemStack(NtmItems.POWDER_IRON.get(), 1),
                        new ItemStack(Blocks.GRAVEL.asItem(), 1)
                )
                .inputItems(new ComparableStack(Blocks.IRON_ORE.asItem(), 1)));
    }
}
