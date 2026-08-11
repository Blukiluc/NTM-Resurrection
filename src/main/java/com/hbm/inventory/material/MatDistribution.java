package com.hbm.inventory.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * Defines which items can be smelted in the crucible and what materials/quantities they yield.
 * Extends SerializableRecipe so entries can be overridden via config (hbmCrucibleSmelting.json).
 *
 * Safe to load even without any crucible machine implemented:
 * - materialEntries and materialOreEntries live in Mats and are just HashMaps
 * - Nothing reads them until a crucible BlockEntity calls them
 * - registerDefaults() is intentionally empty until items/materials exist to reference
 */
public class MatDistribution extends SerializableRecipe {

    @Override
    public void registerDefaults() {
        for (NTMMaterial material : Mats.orderedList) {
            for (Entry<MaterialShapes, Supplier<? extends net.minecraft.world.item.Item>> entry : material.generatedItems.entrySet()) {
                int amount = entry.getKey().q(material.convOut) / material.convIn;
                registerEntry(entry.getValue().get(), material.smeltsInto, amount);
            }
        }

        registerEntry(Items.IRON_NUGGET, Mats.MAT_IRON, MaterialShapes.NUGGET.q(1));
        registerEntry(Items.IRON_BLOCK, Mats.MAT_IRON, MaterialShapes.BLOCK.q(1));
        registerEntry(Items.GOLD_NUGGET, Mats.MAT_GOLD, MaterialShapes.NUGGET.q(1));
        registerEntry(Items.GOLD_BLOCK, Mats.MAT_GOLD, MaterialShapes.BLOCK.q(1));
        registerEntry(Items.COPPER_BLOCK, Mats.MAT_COPPER, MaterialShapes.BLOCK.q(1));
        registerEntry(Items.CHARCOAL, Mats.MAT_CARBON, MaterialShapes.NUGGET.q(3));
        registerEntry(Items.COAL, Mats.MAT_CARBON, MaterialShapes.INGOT.q(1) / 2);
        registerEntry(NtmItems.POWDER_COAL.get(), Mats.MAT_CARBON, MaterialShapes.INGOT.q(1) / 2);
        registerEntry(Items.REDSTONE, Mats.MAT_REDSTONE, MaterialShapes.INGOT.q(1));
        registerEntry(Items.REDSTONE_BLOCK, Mats.MAT_REDSTONE, MaterialShapes.BLOCK.q(1));
        registerEntry(NtmItems.POWDER_LIMESTONE.get(), Mats.MAT_FLUX, MaterialShapes.INGOT.q(10));
    }

    public static List<MaterialStack> getSmeltingMaterials(ItemStack stack) {
        if (stack.isEmpty()) return List.of();

        for (Entry<ComparableStack, List<MaterialStack>> entry : Mats.materialEntries.entrySet()) {
            if (entry.getKey().matchesRecipe(stack, true)) {
                List<MaterialStack> result = new ArrayList<>();
                for (MaterialStack material : entry.getValue()) result.add(material.copy());
                return result;
            }
        }

        return List.of();
    }

    // ---------------------------------------------------------------
    // Static helpers for adding entries
    // ---------------------------------------------------------------

    public static void registerEntry(Object key, Object... matDef) {
        ComparableStack comp = null;

        if (key instanceof net.minecraft.world.item.Item i) comp = new ComparableStack(i);
        if (key instanceof ItemStack is) comp = new ComparableStack(is);
        if (key instanceof ComparableStack cs) comp = cs;

        if (comp == null) return;
        if (matDef.length == 0 || matDef.length % 2 != 0) return;

        List<MaterialStack> stacks = new ArrayList<>();
        for (int i = 0; i < matDef.length; i += 2) {
            stacks.add(new MaterialStack((NTMMaterial) matDef[i], (int) matDef[i + 1]));
        }

        if (stacks.isEmpty()) return;
        Mats.materialEntries.put(comp, stacks);
    }

    public static void registerOre(String key, Object... matDef) {
        if (matDef.length == 0 || matDef.length % 2 != 0) return;

        List<MaterialStack> stacks = new ArrayList<>();
        for (int i = 0; i < matDef.length; i += 2) {
            stacks.add(new MaterialStack((NTMMaterial) matDef[i], (int) matDef[i + 1]));
        }

        if (stacks.isEmpty()) return;
        Mats.materialOreEntries.put(key, stacks);
    }

    // ---------------------------------------------------------------
    // SerializableRecipe implementation
    // ---------------------------------------------------------------

    @Override
    public String getFileName() {
        return "hbmCrucibleSmelting.json";
    }

    @Override
    public String getComment() {
        return "Defines items that can be smelted in the crucible. Amounts are in quanta (1 ingot = 72, 1 nugget = 8). " +
                "Material names are their canonical name (first name), case-sensitive. " +
                "Remove the leading underscore from the filename to enable custom entries.";
    }

    @Override
    public Object getRecipeObject() {
        List<Object> entries = new ArrayList<>();
        entries.addAll(Mats.materialEntries.entrySet());
        entries.addAll(Mats.materialOreEntries.entrySet());
        return entries;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;
        AStack input = this.readAStack(obj.get("input").getAsJsonArray());
        List<MaterialStack> materials = new ArrayList<>();
        JsonArray output = obj.get("output").getAsJsonArray();

        for (int i = 0; i < output.size(); i++) {
            JsonArray entry = output.get(i).getAsJsonArray();
            String matName = entry.get(0).getAsString();
            int amount = entry.get(1).getAsInt();
            NTMMaterial mat = Mats.matByName.get(matName);
            if (mat != null) materials.add(new MaterialStack(mat, amount));
        }

        if (input instanceof ComparableStack cs) {
            Mats.materialEntries.put(cs, materials);
        } else {
            // Tag/ore-dict style inputs stored by name
            // Expand as needed when tag-based lookup is implemented
        }
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<?, List<MaterialStack>> entry = (Entry<?, List<MaterialStack>>) recipe;
        List<MaterialStack> materials = entry.getValue();

        AStack toSmelt = null;
        if (entry.getKey() instanceof String s) {
            // tag/ore key - skip for now, needs OreDictStack equivalent
            return;
        } else if (entry.getKey() instanceof ComparableStack cs) {
            toSmelt = cs;
        }

        if (toSmelt == null) return;

        writer.name("input");
        this.writeAStack(toSmelt, writer);
        writer.name("output").beginArray();
        writer.setIndent("");
        for (MaterialStack stack : materials) {
            writer.beginArray();
            writer.value(stack.material.names[0]).value(stack.amount);
            writer.endArray();
        }
        writer.endArray();
        writer.setIndent("  ");
    }

    @Override
    public void deleteRecipes() {
        Mats.materialEntries.clear();
        Mats.materialOreEntries.clear();
    }
}
