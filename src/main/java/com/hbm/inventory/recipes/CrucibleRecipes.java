package com.hbm.inventory.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.inventory.material.NTMMaterial;
import com.hbm.inventory.recipes.loader.GenericRecipes;

import java.io.IOException;

public class CrucibleRecipes extends GenericRecipes<CrucibleRecipe> {

    public static final CrucibleRecipes INSTANCE = new CrucibleRecipes();

    @Override public int inputItemLimit() { return 0; }
    @Override public int inputFluidLimit() { return 0; }
    @Override public int outputItemLimit() { return 0; }
    @Override public int outputFluidLimit() { return 0; }
    @Override public boolean hasDuration() { return false; }
    @Override public boolean hasPower() { return false; }

    @Override
    public CrucibleRecipe instantiateRecipe(String name) {
        return new CrucibleRecipe(name);
    }

    @Override
    public void registerDefaults() {
        NTMMaterial material = Mats.MAT_TUNGTUNGSTEN;
        this.register(new CrucibleRecipe("crucible.tungtungsten")
                .setup(20, material.makeStack(MaterialShapes.INGOT))
                .inputs(new MaterialStack(material, MaterialShapes.INGOT.q(1)))
                .outputs(new MaterialStack(material, MaterialShapes.INGOT.q(1))));
    }

    @Override
    public String getFileName() {
        return "hbmCrucible.json";
    }

    @Override
    public void readExtraData(JsonElement element, CrucibleRecipe recipe) {
        JsonObject object = element.getAsJsonObject();
        recipe.frequency = Math.max(1, object.get("frequency").getAsInt());
        recipe.input = readMaterials(object.getAsJsonArray("input"));
        recipe.output = readMaterials(object.getAsJsonArray("output"));
    }

    @Override
    public void writeExtraData(CrucibleRecipe recipe, JsonWriter writer) throws IOException {
        writer.name("frequency").value(recipe.frequency);
        writeMaterials(writer, "input", recipe.input);
        writeMaterials(writer, "output", recipe.output);
    }

    private static MaterialStack[] readMaterials(JsonArray array) {
        MaterialStack[] materials = new MaterialStack[array.size()];
        for (int i = 0; i < materials.length; i++) {
            JsonArray entry = array.get(i).getAsJsonArray();
            materials[i] = new MaterialStack(Mats.matByName.get(entry.get(0).getAsString()), entry.get(1).getAsInt());
        }
        return materials;
    }

    private static void writeMaterials(JsonWriter writer, String name, MaterialStack[] materials) throws IOException {
        writer.name(name).beginArray();
        for (MaterialStack material : materials) {
            writer.beginArray();
            writer.setIndent("");
            writer.value(material.material.names[0]);
            writer.value(material.amount);
            writer.endArray();
            writer.setIndent("  ");
        }
        writer.endArray();
    }
}
