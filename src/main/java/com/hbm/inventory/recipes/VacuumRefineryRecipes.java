package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class VacuumRefineryRecipes extends SerializableRecipe {

    public static final VacuumRefineryRecipes INSTANCE = new VacuumRefineryRecipes();

    // Every recipe outputs exactly 4 fluids: heavy oil, reformate, light oil, sour gas.
    public static final int OUTPUT_COUNT = 4;

    public final LinkedHashMap<FluidType, VacuumRefineryRecipe> vacuum = new LinkedHashMap<>();

    private VacuumRefineryRecipes() { }

    private void register(FluidType input, FluidStack... outputs) {
        if(outputs.length != OUTPUT_COUNT) {
            throw new IllegalArgumentException("Vacuum refinery recipes need exactly " + OUTPUT_COUNT + " outputs!");
        }
        vacuum.put(input, new VacuumRefineryRecipe(outputs));
    }

    public VacuumRefineryRecipe getVacuum(FluidType oil) {
        return vacuum.get(oil);
    }

    public Map<FluidType, VacuumRefineryRecipe> getRecipes() {
        return vacuum;
    }

    /*
     * SERIALIZABLE RECIPE CONTRACT
     */

    @Override
    public String getFileName() {
        return "hbmVacuumRefinery.json";
    }

    @Override
    public Object getRecipeObject() {
        return vacuum;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        FluidType input = Fluids.fromName(obj.get("input").getAsString());
        FluidStack[] outputs = new FluidStack[OUTPUT_COUNT];
        for(int i = 0; i < OUTPUT_COUNT; i++) {
            outputs[i] = SerializableRecipe.readFluidStack(obj.get("output" + (i + 1)).getAsJsonArray());
        }

        register(input, outputs);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<FluidType, VacuumRefineryRecipe> entry = (Entry<FluidType, VacuumRefineryRecipe>) recipe;

        FluidType input = entry.getKey();
        FluidStack[] outputs = entry.getValue().outputs;

        writer.name("input").value(input.getUnlocalizedName());

        for(int i = 0; i < outputs.length; i++) {
            writer.name("output" + (i + 1));
            SerializableRecipe.writeFluidStack(outputs[i], writer);
        }
    }

    @Override
    public void registerDefaults() {
        // TODO: the real oil -> (heavy/reformate/light/gas) fractions lived in the
        // pre-port VacuumRefineryRecipes. Send me that file (or just the numbers)
        // and I'll fill this in. Example of the call shape once we have them:
        //
        register(Fluids.OIL,
                new FluidStack(Fluids.HEAVYOIL_VACUUM, 1),
                new FluidStack(Fluids.REFORMATE, 1),
                new FluidStack(Fluids.LIGHTOIL_VACUUM, 1),
                new FluidStack(Fluids.SOURGAS, 1));

        register(Fluids.OIL_DS,
                new FluidStack(Fluids.HEAVYOIL_VACUUM, 1),
                new FluidStack(Fluids.REFORMATE, 1),
                new FluidStack(Fluids.LIGHTOIL_VACUUM, 1),
                new FluidStack(Fluids.SOURGAS, 1));
    }

    @Override
    public void deleteRecipes() {
        vacuum.clear();
    }

    public static class VacuumRefineryRecipe {
        public final FluidStack[] outputs;

        public VacuumRefineryRecipe(FluidStack[] outputs) {
            this.outputs = outputs;
        }
    }
}