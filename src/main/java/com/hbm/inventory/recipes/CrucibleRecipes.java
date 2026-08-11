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
        this.register(new CrucibleRecipe("crucible.steel")
                .setup(20, Mats.MAT_STEEL.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_IRON, 16), stack(Mats.MAT_CARBON, 24), stack(Mats.MAT_FLUX, 8))
                .outputs(stack(Mats.MAT_STEEL, 16)));
        this.register(new CrucibleRecipe("crucible.hss")
                .setup(9, Mats.MAT_DURA_STEEL.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_STEEL, 40), stack(Mats.MAT_TUNGSTEN, 24), stack(Mats.MAT_COBALT, 8))
                .outputs(stack(Mats.MAT_DURA_STEEL, 72)));
        this.register(new CrucibleRecipe("crucible.redcopper")
                .setup(2, Mats.MAT_REDCOPPER.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_COPPER, 8), stack(Mats.MAT_REDSTONE, 8))
                .outputs(stack(Mats.MAT_REDCOPPER, 16)));
        this.register(new CrucibleRecipe("crucible.ferro")
                .setup(3, Mats.MAT_FERROURANIUM.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_STEEL, 16), stack(Mats.MAT_U238, 8))
                .outputs(stack(Mats.MAT_FERROURANIUM, 24)));
        this.register(new CrucibleRecipe("crucible.tcalloy")
                .setup(9, Mats.MAT_TCALLOY.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_STEEL, 64), stack(Mats.MAT_TECHNETIUM, 8))
                .outputs(stack(Mats.MAT_TCALLOY, 72)));
        this.register(new CrucibleRecipe("crucible.cdalloy")
                .setup(9, Mats.MAT_CDALLOY.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_STEEL, 64), stack(Mats.MAT_CADMIUM, 8))
                .outputs(stack(Mats.MAT_CDALLOY, 72)));
        this.register(new CrucibleRecipe("crucible.bbronze")
                .setup(9, Mats.MAT_BISMUTH_BRONZE.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_COPPER, 64), stack(Mats.MAT_BISMUTH, 8), stack(Mats.MAT_FLUX, 24))
                .outputs(stack(Mats.MAT_BISMUTH_BRONZE, 72), stack(Mats.MAT_SLAG, 24)));
        this.register(new CrucibleRecipe("crucible.abronze")
                .setup(9, Mats.MAT_ARSENIC_BRONZE.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_COPPER, 64), stack(Mats.MAT_ARSENIC, 8), stack(Mats.MAT_FLUX, 24))
                .outputs(stack(Mats.MAT_ARSENIC_BRONZE, 72), stack(Mats.MAT_SLAG, 24)));
        this.register(new CrucibleRecipe("crucible.bscco")
                .setup(3, Mats.MAT_BSCCO.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_BISMUTH, 16), stack(Mats.MAT_STRONTIUM, 16), stack(Mats.MAT_CALCIUM, 16), stack(Mats.MAT_COPPER, 24))
                .outputs(stack(Mats.MAT_BSCCO, 72)));
        this.register(new CrucibleRecipe("crucible.hematite")
                .setup(6, Mats.MAT_IRON.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_HEMATITE, 144), stack(Mats.MAT_FLUX, 16))
                .outputs(stack(Mats.MAT_IRON, 72), stack(Mats.MAT_SLAG, 24)));
        this.register(new CrucibleRecipe("crucible.malachite")
                .setup(6, Mats.MAT_COPPER.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_MALACHITE, 144), stack(Mats.MAT_FLUX, 16))
                .outputs(stack(Mats.MAT_COPPER, 72), stack(Mats.MAT_SLAG, 24)));
        this.register(new CrucibleRecipe("crucible.magtung")
                .setup(3, Mats.MAT_MAGNETIZED_TUNGSTEN.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_TUNGSTEN, 72), stack(Mats.MAT_SCHRABIDIUM, 8))
                .outputs(stack(Mats.MAT_MAGNETIZED_TUNGSTEN, 72)));
        this.register(new CrucibleRecipe("crucible.cmb")
                .setup(3, Mats.MAT_COMBINE_STEEL.makeStack(MaterialShapes.INGOT))
                .inputs(stack(Mats.MAT_MAGNETIZED_TUNGSTEN, 48), stack(Mats.MAT_MUD, 24))
                .outputs(stack(Mats.MAT_COMBINE_STEEL, 72)));
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
            String name = entry.get(0).getAsString();
            NTMMaterial material = Mats.matByName.get(name);
            if (material == null) throw new IllegalArgumentException("Unknown crucible material: " + name);
            materials[i] = new MaterialStack(material, entry.get(1).getAsInt());
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

    private static MaterialStack stack(NTMMaterial material, int amount) {
        return new MaterialStack(material, amount);
    }
}
