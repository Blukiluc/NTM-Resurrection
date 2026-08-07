package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.ItemStamp;
import com.hbm.items.machine.ItemStamp.StampType;
import com.hbm.util.Tuple.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

public class PressRecipes extends SerializableRecipe {

    public static HashMap<Pair<AStack, StampType>, ItemStack> recipes = new HashMap<>();

    @Override
    public void registerDefaults() {
        makeRecipe(StampType.FLAT, new ComparableStack(Blocks.JUNGLE_LOG.asItem(), 1, 4), NtmItems.NOTHING.get());
        makeRecipe(StampType.PLATE, new ComparableStack(Items.IRON_INGOT.asItem(), 1, 1), NtmItems.PLATE_IRON.get());
        makeRecipe(StampType.WIRE, new ComparableStack(Items.GOLD_INGOT.asItem(), 1, 1), new ItemStack(Items.GOLD_NUGGET, 9));
        makeRecipe(StampType.CIRCUIT, new ComparableStack(Items.REDSTONE.asItem(), 1, 1), NtmItems.CINNABAR.get());
    }

    public static ItemStack getOutput(ItemStack ingredient, ItemStack stamp) {
        if(ingredient.isEmpty() || stamp.isEmpty()) return ItemStack.EMPTY;
        if(!(stamp.getItem() instanceof ItemStamp itemStamp)) return ItemStack.EMPTY;

        StampType type = itemStamp.getStampType();

        for(Entry<Pair<AStack, StampType>, ItemStack> recipe : recipes.entrySet()) {
            if(recipe.getKey().getValue() == type && recipe.getKey().getKey().matchesRecipe(ingredient, true)) {
                return recipe.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    public static void makeRecipe(StampType type, AStack in, Item out) {
        recipes.put(new Pair<>(in, type),  new ItemStack(out));
    }
    public static void makeRecipe(StampType type, AStack in, ItemStack out) {
        recipes.put(new Pair<>(in, type),  out);
    }

    @Override
    public String getFileName() {
        return "hbmPress.json";
    }

    @Override
    public Object getRecipeObject() {
        return recipes;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        AStack input = SerializableRecipe.readAStack(obj.get("input").getAsJsonArray());
        StampType stamp = StampType.valueOf(obj.get("stamp").getAsString().toUpperCase());
        ItemStack output = SerializableRecipe.readItemStack(obj.get("output").getAsJsonArray());

        makeRecipe(stamp, input, output);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<Pair<AStack, StampType>, ItemStack> entry = (Entry<Pair<AStack, StampType>, ItemStack>) recipe;

        writer.name("input");
        SerializableRecipe.writeAStack(entry.getKey().getKey(), writer);
        writer.name("stamp").value(entry.getKey().getValue().name().toLowerCase(Locale.US));
        writer.name("output");
        SerializableRecipe.writeItemStack(entry.getValue(), writer);
    }

    @Override
    public void deleteRecipes() {
        recipes.clear();
    }
}