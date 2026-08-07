package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Recipes for the Shredder. Maps an input ComparableStack to an output ItemStack.
 * Falls back to a wildcard (any-metadata/any-NBT) match if an exact match isn't found,
 * and finally to scrap if no recipe exists at all.
 */
public class ShredderRecipes extends SerializableRecipe {

    public static HashMap<AStack, ItemStack> recipes = new HashMap<>();

    @Override
    public void registerDefaults() {
        // Basic examples - flesh these out with real NTM items as needed
        makeRecipe(new ComparableStack(net.minecraft.world.level.block.Blocks.IRON_BLOCK.asItem()), new ItemStack(NtmItems.POWDER_IRON.get(), 9));
        makeRecipe(new ComparableStack(net.minecraft.world.item.Items.IRON_INGOT), new ItemStack(NtmItems.POWDER_IRON.get(), 1));
    }

    public static void makeRecipe(AStack in, Item out) {
        recipes.put(in, new ItemStack(out));
    }

    public static void makeRecipe(AStack in, ItemStack out) {
        recipes.put(in, out);
    }

    /**
     * Returns the shredder's output for a given input stack.
     * Tries an exact match first (item + meta), then falls back to a meta-wildcard match,
     * and finally defaults to scrap if nothing is found.
     */
    public static ItemStack getShredderResult(ItemStack stack) {

        if (stack == null || stack.isEmpty()) return new ItemStack(NtmItems.SCRAP.get());

        ComparableStack comp = (ComparableStack) new ComparableStack(stack).copy(1);
        ItemStack result = recipes.get(comp);

        if (result == null) {
            // retry while ignoring metadata/durability-derived "meta" value
            comp.meta = -1; // wildcard sentinel, see matchesRecipe below
            for (Entry<AStack, ItemStack> entry : recipes.entrySet()) {
                if (entry.getKey() instanceof ComparableStack ck && ck.item == comp.item) {
                    result = entry.getValue();
                    break;
                }
            }
        }

        return result == null ? new ItemStack(NtmItems.SCRAP.get()) : result.copy();
    }

    @Override
    public String getFileName() {
        return "hbmShredder.json";
    }

    @Override
    public Object getRecipeObject() {
        return recipes;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        AStack input = SerializableRecipe.readAStack(obj.get("input").getAsJsonArray());
        ItemStack output = SerializableRecipe.readItemStack(obj.get("output").getAsJsonArray());

        makeRecipe(input, output);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<AStack, ItemStack> entry = (Entry<AStack, ItemStack>) recipe;

        writer.name("input");
        SerializableRecipe.writeAStack(entry.getKey(), writer);
        writer.name("output");
        SerializableRecipe.writeItemStack(entry.getValue(), writer);
    }

    @Override
    public void deleteRecipes() {
        recipes.clear();
    }
}
