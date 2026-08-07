package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.util.Tuple;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CatalyticReformerRecipes extends SerializableRecipe {

    public static final CatalyticReformerRecipes INSTANCE = new CatalyticReformerRecipes();

    public final int oil_frac_naph = 50;
    public final int oil_frac_petro = 30;
    public final int oil_frac_hydro = 20;

    public final int crack_frac_naph = 45;
    public final int crack_frac_petro = 35;
    public final int crack_frac_hydro = 20;

    public final int oilds_frac_naph = 45;
    public final int oilds_frac_petro = 35;
    public final int oilds_frac_hydro = 20;

    public final int crackds_frac_naph = 40;
    public final int crackds_frac_petro = 40;
    public final int crackds_frac_hydro = 20;

    public final LinkedHashMap<FluidType, Tuple.Triplet<FluidStack, FluidStack, FluidStack>> catalytic_reformer = new LinkedHashMap<>();

    private CatalyticReformerRecipes() { }

    private void register(FluidType input, FluidStack output1, FluidStack output2, FluidStack output3) {
        catalytic_reformer.put(input, new Tuple.Triplet<>(output1, output2, output3));
    }

    public Tuple.Triplet<FluidStack, FluidStack, FluidStack> getOutput(FluidType oil) {
        return catalytic_reformer.get(oil);
    }

    public Map<FluidType, Tuple.Triplet<FluidStack, FluidStack, FluidStack>> getRecipes() {
        return catalytic_reformer;
    }

    /*
     * SERIALIZABLE RECIPE CONTRACT
     */

    @Override
    public String getFileName() {
        return "hbmCatalyticReformer.json";
    }

    @Override
    public Object getRecipeObject() {
        return catalytic_reformer;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        FluidType input = Fluids.fromName(obj.get("input").getAsString());
        FluidStack output1 = SerializableRecipe.readFluidStack(obj.get("output1").getAsJsonArray());
        FluidStack output2 = SerializableRecipe.readFluidStack(obj.get("output2").getAsJsonArray());
        FluidStack output3 = SerializableRecipe.readFluidStack(obj.get("output3").getAsJsonArray());

        register(input, output1, output2, output3);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<FluidType, Tuple.Triplet<FluidStack, FluidStack, FluidStack>> entry =
                (Entry<FluidType, Tuple.Triplet<FluidStack, FluidStack, FluidStack>>) recipe;

        FluidType input = entry.getKey();
        Tuple.Triplet<FluidStack, FluidStack, FluidStack> outputs = entry.getValue();

        writer.name("input").value(input.getUnlocalizedName());

        writer.name("output1");
        SerializableRecipe.writeFluidStack(outputs.getX(), writer);

        writer.name("output2");
        SerializableRecipe.writeFluidStack(outputs.getY(), writer);

        writer.name("output3");
        SerializableRecipe.writeFluidStack(outputs.getZ(), writer);
    }

    @Override
    public void registerDefaults() {
        // todo recipes
        register(
                Fluids.HEATINGOIL,
                new FluidStack(Fluids.NAPHTHA, oil_frac_naph),
                new FluidStack(Fluids.PETROLEUM, oil_frac_petro),
                new FluidStack(Fluids.HYDROGEN, oil_frac_hydro)
        );
    }

    @Override
    public void deleteRecipes() {
        catalytic_reformer.clear();
    }
}