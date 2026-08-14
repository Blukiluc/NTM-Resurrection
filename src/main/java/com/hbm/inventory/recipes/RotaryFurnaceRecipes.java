package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.IOException;

public class RotaryFurnaceRecipes extends GenericRecipes<RotaryFurnaceRecipe> {

    public static final RotaryFurnaceRecipes INSTANCE = new RotaryFurnaceRecipes();

    @Override public int inputItemLimit() { return 3; }
    @Override public int inputFluidLimit() { return 1; }
    @Override public int outputItemLimit() { return 1; }
    @Override public int outputFluidLimit() { return 0; }
    @Override public boolean hasPower() { return false; }
    @Override public String getFileName() { return "hbmRotaryFurnace.json"; }
    @Override public RotaryFurnaceRecipe instantiateRecipe(String name) { return new RotaryFurnaceRecipe(name); }

    @Override
    public void registerDefaults() {
        if(!this.recipeOrderedList.isEmpty()) return;

        this.register(new RotaryFurnaceRecipe("rotary.steelFromCoal").setDuration(100).setSteam(100)
                .inputItems(new ComparableStack(Items.IRON_INGOT), new ComparableStack(Items.COAL))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get())));
        this.register(new RotaryFurnaceRecipe("rotary.steelFromCoalCoke").setDuration(100).setSteam(100)
                .inputItems(new ComparableStack(Items.IRON_INGOT), new ComparableStack(NtmItems.COKE_COAL.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get())));
        this.register(new RotaryFurnaceRecipe("rotary.steelFromLigniteCoke").setDuration(100).setSteam(100)
                .inputItems(new ComparableStack(Items.IRON_INGOT), new ComparableStack(NtmItems.COKE_LIGNITE.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get())));
        this.register(new RotaryFurnaceRecipe("rotary.steelFromPetroleumCoke").setDuration(100).setSteam(100)
                .inputItems(new ComparableStack(Items.IRON_INGOT), new ComparableStack(NtmItems.COKE_PETROLEUM.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get())));

//        this.register(new RotaryFurnaceRecipe("rotary.steelFromFragmentsCoal").setDuration(200).setSteam(25)
//                .inputItems(new ComparableStack(NtmItems.FRAGMENT_IRON.get(), 9), new ComparableStack(Items.COAL))
//                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 2)));
//        this.register(new RotaryFurnaceRecipe("rotary.steelFromFragmentsCoke").setDuration(200).setSteam(25)
//                .inputItems(new ComparableStack(NtmItems.FRAGMENT_IRON.get(), 9), new ComparableStack(NtmItems.COKE_COAL.get()))
//                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 3)));
//        this.register(new RotaryFurnaceRecipe("rotary.steelFromFragmentsFlux").setDuration(400).setSteam(25)
//                .inputItems(new ComparableStack(NtmItems.FRAGMENT_IRON.get(), 9), new ComparableStack(NtmItems.COKE_COAL.get()), new ComparableStack(NtmItems.POWDER_FLUX.get()))
//                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 4)));

        this.register(new RotaryFurnaceRecipe("rotary.desh").setDuration(100).setSteam(200)
                .inputItems(new ComparableStack(NtmItems.POWDER_DESH_READY.get()))
                .inputFluids(new FluidStack(Fluids.LIGHTOIL, 100))
                .outputItems(new ItemStack(NtmItems.INGOT_DESH.get())));

        this.register(new RotaryFurnaceRecipe("rotary.gunmetal").setDuration(200).setSteam(100)
                .inputItems(new ComparableStack(Items.COPPER_INGOT, 3), new ComparableStack(NtmItems.INGOT_ALUMINIUM.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_GUNMETAL.get(), 4)));
        this.register(new RotaryFurnaceRecipe("rotary.weaponSteel").setDuration(200).setSteam(400)
                .inputItems(new ComparableStack(NtmItems.INGOT_STEEL.get()), new ComparableStack(NtmItems.POWDER_FLUX.get(), 2))
                .inputFluids(new FluidStack(Fluids.GAS_COKER, 100))
                .outputItems(new ItemStack(NtmItems.INGOT_WEAPONSTEEL.get())));
        this.register(new RotaryFurnaceRecipe("rotary.saturnite").setDuration(200).setSteam(400)
                .inputItems(new ComparableStack(NtmItems.POWDER_DURA_STEEL.get(), 4), new ComparableStack(NtmItems.POWDER_COPPER.get()))
                .inputFluids(new FluidStack(Fluids.REFORMGAS, 250))
                .outputItems(new ItemStack(NtmItems.INGOT_SATURNITE.get(), 2)));
        this.register(new RotaryFurnaceRecipe("rotary.saturniteWithBorax").setDuration(200).setSteam(300)
                .inputItems(new ComparableStack(NtmItems.POWDER_DURA_STEEL.get(), 4), new ComparableStack(NtmItems.POWDER_COPPER.get()), new ComparableStack(NtmItems.POWDER_BORAX.get()))
                .inputFluids(new FluidStack(Fluids.REFORMGAS, 250))
                .outputItems(new ItemStack(NtmItems.INGOT_SATURNITE.get(), 4)));
        this.register(new RotaryFurnaceRecipe("rotary.aluminium").setDuration(100).setSteam(400)
                .inputFluids(new FluidStack(Fluids.SODIUM_ALUMINATE, 150))
                .outputItems(new ItemStack(NtmItems.INGOT_ALUMINIUM.get(), 2)));
        this.register(new RotaryFurnaceRecipe("rotary.aluminiumWithFlux").setDuration(40).setSteam(200)
                .inputItems(new ComparableStack(NtmItems.POWDER_FLUX.get(), 2))
                .inputFluids(new FluidStack(Fluids.SODIUM_ALUMINATE, 150))
                .outputItems(new ItemStack(NtmItems.INGOT_ALUMINIUM.get(), 3)));
    }

    public RotaryFurnaceRecipe getRecipe(ItemStack first, ItemStack second, ItemStack third, FluidType fluid) {
        this.ensureDefaults();
        ItemStack[] inputs = new ItemStack[] {first, second, third};

        for(RotaryFurnaceRecipe recipe : this.recipeOrderedList) {
            if(!matchesInputs(recipe, inputs, false)) continue;
            if(recipe.inputFluid != null && recipe.inputFluid.length > 0 && recipe.inputFluid[0].type != fluid) continue;
            return recipe;
        }

        return null;
    }

    public boolean isItemValid(ItemStack stack) {
        this.ensureDefaults();
        if(stack.isEmpty()) return false;

        for(RotaryFurnaceRecipe recipe : this.recipeOrderedList) {
            if(recipe.inputItem == null) continue;
            for(AStack input : recipe.inputItem) {
                if(input.matchesRecipe(stack, true)) return true;
            }
        }

        return false;
    }

    public static boolean matchesInputs(RotaryFurnaceRecipe recipe, ItemStack[] inputs, boolean ignoreSize) {
        AStack[] ingredients = recipe.inputItem == null ? new AStack[0] : recipe.inputItem;
        boolean[] matched = new boolean[ingredients.length];

        for(ItemStack input : inputs) {
            if(input.isEmpty()) continue;

            boolean found = false;
            for(int i = 0; i < ingredients.length; i++) {
                if(!matched[i] && ingredients[i].matchesRecipe(input, ignoreSize)) {
                    matched[i] = true;
                    found = true;
                    break;
                }
            }

            if(!found) return false;
        }

        for(boolean value : matched) {
            if(!value) return false;
        }

        return true;
    }

    private void ensureDefaults() {
        if(this.recipeOrderedList.isEmpty()) this.registerDefaults();
    }

    @Override
    public void readExtraData(JsonElement element, RotaryFurnaceRecipe recipe) {
        JsonObject object = element.getAsJsonObject();
        recipe.steam = object.get("steam").getAsInt();
    }

    @Override
    public void writeExtraData(RotaryFurnaceRecipe recipe, JsonWriter writer) throws IOException {
        writer.name("steam").value(recipe.steam);
    }
}
