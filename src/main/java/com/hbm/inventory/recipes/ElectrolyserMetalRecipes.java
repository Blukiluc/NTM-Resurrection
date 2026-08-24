package com.hbm.inventory.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ElectrolyserMetalRecipes extends SerializableRecipe {

    public static final ElectrolyserMetalRecipes INSTANCE = new ElectrolyserMetalRecipes();

    public final LinkedHashMap<AStack, ElectrolysisMetalRecipe> electrolysis = new LinkedHashMap<>();

    private ElectrolyserMetalRecipes() { }

    private void register(AStack input, ElectrolysisMetalRecipe recipe) {
        electrolysis.put(input, recipe);
    }

    public ElectrolysisMetalRecipe getRecipe(ItemStack stack) {
        if(stack == null || stack.isEmpty()) return null;

        ComparableStack comp = new ComparableStack(stack).makeSingular();
        return electrolysis.get(comp);

        // NOTE: the 1.7.10 version also fell back to matching by ore-dictionary name.
        // There is no reserved tag/class for "crystal" items in this port yet, so for
        // now recipes only match by exact item. Revisit once that system exists.
    }

    public Map<AStack, ElectrolysisMetalRecipe> getRecipes() {
        return electrolysis;
    }

    /*
     * SERIALIZABLE RECIPE CONTRACT
     */

    @Override
    public String getFileName() {
        return "hbmElectrolyserMetal.json";
    }

    @Override
    public Object getRecipeObject() {
        return electrolysis;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        AStack input = SerializableRecipe.readAStack(obj.get("input").getAsJsonArray());

        // NOTE: assumes Mats kept a matByName-style lookup and MaterialStack.material.names[0]
        // like in the 1.7.10 port. Please confirm against the actual Mats.java of this repo.
        MaterialStack output1 = null;
        MaterialStack output2 = null;

        if(obj.has("output1")) {
            JsonArray out1 = obj.get("output1").getAsJsonArray();
            output1 = new MaterialStack(Mats.matByName.get(out1.get(0).getAsString()), out1.get(1).getAsInt());
        }

        if(obj.has("output2")) {
            JsonArray out2 = obj.get("output2").getAsJsonArray();
            output2 = new MaterialStack(Mats.matByName.get(out2.get(0).getAsString()), out2.get(1).getAsInt());
        }

        int duration = obj.has("duration") ? obj.get("duration").getAsInt() : 600;

        ItemStack[] byproducts = obj.has("byproducts") ?
                SerializableRecipe.readItemStackArray(obj.get("byproducts").getAsJsonArray()) :
                new ItemStack[0];

        register(input, new ElectrolysisMetalRecipe(output1, output2, duration, byproducts));
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<AStack, ElectrolysisMetalRecipe> entry = (Entry<AStack, ElectrolysisMetalRecipe>) recipe;

        AStack input = entry.getKey();
        ElectrolysisMetalRecipe data = entry.getValue();

        writer.name("input");
        SerializableRecipe.writeAStack(input, writer);

        if(data.output1 != null) {
            writer.name("output1").beginArray();
            writer.setIndent("");
            writer.value(data.output1.material.names[0]).value(data.output1.amount);
            writer.endArray();
            writer.setIndent("  ");
        }

        if(data.output2 != null) {
            writer.name("output2").beginArray();
            writer.setIndent("");
            writer.value(data.output2.material.names[0]).value(data.output2.amount);
            writer.endArray();
            writer.setIndent("  ");
        }

        if(data.byproduct != null && data.byproduct.length > 0) {
            writer.name("byproducts").beginArray();
            for(ItemStack stack : data.byproduct) {
                SerializableRecipe.writeItemStack(stack, writer);
            }
            writer.endArray();
        }

        writer.name("duration").value(data.duration);
    }

    @Override
    public void registerDefaults() {

        // TODO: placeholder only - there is no reserved crystal item/tag system in this port yet.
        // Swap this for the real crystal item(s) once they exist, and confirm the Mats/MaterialStack
        // API (Mats.MAT_IRON, MaterialShapes.INGOT.q(n), etc.) against the actual Mats.java.
//        register(new ComparableStack(NtmItems.CRYSTAL_IRON.get()), new ElectrolysisMetalRecipe(
//                new MaterialStack(Mats.MAT_IRON, MaterialShapes.INGOT.q(6)),
//                new MaterialStack(Mats.MAT_TITANIUM, MaterialShapes.INGOT.q(2)),
//                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_IRON.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_COPPER, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_TITANIUM, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        /* TODO: rest of the 1.7.10 recipe list, kept for reference - re-enable once the crystal
         * item/tag system and the Mats/MaterialStack API are confirmed for this port.

        register(new ComparableStack(NtmItems.CRYSTAL_GOLD.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_GOLD, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_LEAD, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3),
                new ItemStack(NtmItems.INGOT_MERCURY.get(), 2))); // TODO: ingot_mercury doesn't exist yet

        register(new ComparableStack(NtmItems.CRYSTAL_URANIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_URANIUM, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_RADIUM, MaterialShapes.NUGGET.q(4)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_THORIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_THORIUM, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_URANIUM, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_PLUTONIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_PLUTONIUM, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_POLONIUM, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_TITANIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_TITANIUM, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_IRON, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_COPPER.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_COPPER, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_LEAD, MaterialShapes.NUGGET.q(4)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3),
                new ItemStack(NtmItems.SULFUR.get(), 2)));

        register(new ComparableStack(NtmItems.CRYSTAL_TUNGSTEN.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_TUNGSTEN, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_IRON, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_ALUMINIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_ALUMINIUM, MaterialShapes.INGOT.q(2)),
                new MaterialStack(Mats.MAT_IRON, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3))); // TODO: also had a cryolite chunk byproduct in 1.7.10

        register(new ComparableStack(NtmItems.CRYSTAL_BERYLLIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_BERYLLIUM, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_LEAD, MaterialShapes.NUGGET.q(4)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3),
                new ItemStack(NtmItems.POWDER_QUARTZ.get(), 2)));

        register(new ComparableStack(NtmItems.CRYSTAL_LEAD.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_LEAD, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_GOLD, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_SCHRABIDIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_SCHRABIDIUM, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_PLUTONIUM, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_RARE.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_ZIRCONIUM, MaterialShapes.NUGGET.q(6)),
                new MaterialStack(Mats.MAT_BORON, MaterialShapes.NUGGET.q(2)),
                new ItemStack(NtmItems.POWDER_DESH_MIX.get(), 3)));

        register(new ComparableStack(NtmItems.CRYSTAL_TRIXITE.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_PLUTONIUM, MaterialShapes.INGOT.q(3)),
                new MaterialStack(Mats.MAT_COBALT, MaterialShapes.INGOT.q(4)),
                new ItemStack(NtmItems.POWDER_NIOBIUM.get(), 4),
                new ItemStack(NtmItems.POWDER_NITAN_MIX.get(), 2)));

        register(new ComparableStack(NtmItems.CRYSTAL_LITHIUM.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_LITHIUM, MaterialShapes.INGOT.q(6)),
                new MaterialStack(Mats.MAT_BORON, MaterialShapes.INGOT.q(2)),
                new ItemStack(NtmItems.POWDER_QUARTZ.get(), 2),
                new ItemStack(NtmItems.FLUORITE.get(), 2)));

        register(new ComparableStack(NtmItems.CRYSTAL_STARMETAL.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_DURA, MaterialShapes.INGOT.q(4)),
                new MaterialStack(Mats.MAT_COBALT, MaterialShapes.INGOT.q(4)),
                new ItemStack(NtmItems.POWDER_ASTATINE.get(), 3),
                new ItemStack(NtmItems.INGOT_MERCURY.get(), 8))); // TODO: ingot_mercury doesn't exist yet

        register(new ComparableStack(NtmItems.CRYSTAL_COBALT.get()), new ElectrolysisMetalRecipe(
                new MaterialStack(Mats.MAT_COBALT, MaterialShapes.INGOT.q(3)),
                new MaterialStack(Mats.MAT_IRON, MaterialShapes.INGOT.q(4)),
                new ItemStack(NtmItems.POWDER_COPPER.get(), 4),
                new ItemStack(NtmItems.POWDER_LITHIUM_TINY.get(), 3)));

        // Bedrock ore crystal loop - depends on ItemBedrockOreNew / BedrockOreType / BedrockOreGrade /
        // BedrockOreOutput, none of which exist in this port yet.
        //
        // for(BedrockOreType type : BedrockOreType.values()) {
        //     ...
        // }

        */
    }

    @Override
    public void deleteRecipes() {
        electrolysis.clear();
    }

    public static class ElectrolysisMetalRecipe {

        public MaterialStack output1;
        public MaterialStack output2;
        public ItemStack[] byproduct;
        public int duration;

        public ElectrolysisMetalRecipe(MaterialStack output1, MaterialStack output2, ItemStack... byproduct) {
            this(output1, output2, 600, byproduct);
        }

        public ElectrolysisMetalRecipe(MaterialStack output1, MaterialStack output2, int duration, ItemStack... byproduct) {
            this.output1 = output1;
            this.output2 = output2;
            this.byproduct = byproduct;
            this.duration = duration;
        }
    }
}