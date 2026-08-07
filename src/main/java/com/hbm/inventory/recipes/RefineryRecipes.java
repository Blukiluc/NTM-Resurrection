package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import com.hbm.util.Tuple;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RefineryRecipes extends SerializableRecipe {

    public static final RefineryRecipes INSTANCE = new RefineryRecipes();

    public final int oil_frac_heavy = 50;
    public final int oil_frac_naph = 25;
    public final int oil_frac_light = 15;
    public final int oil_frac_petro = 10;

    public final int crack_frac_naph = 40;
    public final int crack_frac_light = 30;
    public final int crack_frac_aroma = 15;
    public final int crack_frac_unsat = 15;

    public final int oilds_frac_heavy = 30;
    public final int oilds_frac_naph = 35;
    public final int oilds_frac_light = 20;
    public final int oilds_frac_unsat = 15;

    public final int crackds_frac_naph = 35;
    public final int crackds_frac_light = 35;
    public final int crackds_frac_aroma = 15;
    public final int crackds_frac_unsat = 15;

    public final LinkedHashMap<FluidType, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> refinery = new LinkedHashMap<>();

    private RefineryRecipes() { }

    private void register(FluidType input, FluidStack output1, FluidStack output2, FluidStack output3, FluidStack output4, ItemStack byproduct) {
        refinery.put(input, new Tuple.Quintet<>(output1, output2, output3, output4, byproduct));
    }

    public Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> getRefinery(FluidType oil) {
        return refinery.get(oil);
    }

    public Map<FluidType, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> getRecipes() {
        return refinery;
    }

    /*
     * SERIALIZABLE RECIPE CONTRACT
     */

    @Override
    public String getFileName() {
        return "hbmRefinery.json";
    }

    @Override
    public Object getRecipeObject() {
        return refinery;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        FluidType input = Fluids.fromName(obj.get("input").getAsString());
        FluidStack output1 = SerializableRecipe.readFluidStack(obj.get("output1").getAsJsonArray());
        FluidStack output2 = SerializableRecipe.readFluidStack(obj.get("output2").getAsJsonArray());
        FluidStack output3 = SerializableRecipe.readFluidStack(obj.get("output3").getAsJsonArray());
        FluidStack output4 = SerializableRecipe.readFluidStack(obj.get("output4").getAsJsonArray());
        ItemStack byproduct = SerializableRecipe.readItemStack(obj.get("byproduct").getAsJsonArray());

        register(input, output1, output2, output3, output4, byproduct);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<FluidType, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> entry =
                (Entry<FluidType, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>>) recipe;

        FluidType input = entry.getKey();
        Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> outputs = entry.getValue();

        writer.name("input").value(input.getUnlocalizedName());

        writer.name("output1");
        SerializableRecipe.writeFluidStack(outputs.getV(), writer);

        writer.name("output2");
        SerializableRecipe.writeFluidStack(outputs.getW(), writer);

        writer.name("output3");
        SerializableRecipe.writeFluidStack(outputs.getX(), writer);

        writer.name("output4");
        SerializableRecipe.writeFluidStack(outputs.getY(), writer);

        writer.name("byproduct");
        SerializableRecipe.writeItemStack(outputs.getZ(), writer);
    }

    @Override
    public void registerDefaults() {
        register(
                Fluids.HOTOIL,
                new FluidStack(Fluids.HEAVYOIL, oil_frac_heavy),
                new FluidStack(Fluids.NAPHTHA, oil_frac_naph),
                new FluidStack(Fluids.LIGHTOIL, oil_frac_light),
                new FluidStack(Fluids.PETROLEUM, oil_frac_petro),
                new ItemStack(NtmItems.SULFUR.get())
        );

        register(
                Fluids.HOTCRACKOIL,
                new FluidStack(Fluids.NAPHTHA_CRACK, crack_frac_naph),
                new FluidStack(Fluids.LIGHTOIL_CRACK, crack_frac_light),
                new FluidStack(Fluids.AROMATICS, crack_frac_aroma),
                new FluidStack(Fluids.UNSATURATEDS, crack_frac_unsat),
                new ItemStack(NtmItems.TAR_CRACK_OIL.get())
        );

        register(
                Fluids.HOTOIL_DS,
                new FluidStack(Fluids.HEAVYOIL, oilds_frac_heavy),
                new FluidStack(Fluids.NAPHTHA_DS, oilds_frac_naph),
                new FluidStack(Fluids.LIGHTOIL_DS, oilds_frac_light),
                new FluidStack(Fluids.UNSATURATEDS, oilds_frac_unsat),
                new ItemStack(NtmItems.WAX_PARAFFIN.get())
        );

        register(
                Fluids.HOTCRACKOIL_DS,
                new FluidStack(Fluids.NAPHTHA_DS, crackds_frac_naph),
                new FluidStack(Fluids.LIGHTOIL_DS, crackds_frac_light),
                new FluidStack(Fluids.AROMATICS, crackds_frac_aroma),
                new FluidStack(Fluids.UNSATURATEDS, crackds_frac_unsat),
                new ItemStack(NtmItems.WAX_PARAFFIN.get())
        );
    }

    @Override
    public void deleteRecipes() {
        refinery.clear();
    }
}