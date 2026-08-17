package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ElectrolyserFluidRecipes extends SerializableRecipe {

    public static final ElectrolyserFluidRecipes INSTANCE = new ElectrolyserFluidRecipes();

    public final LinkedHashMap<FluidType, ElectrolysisRecipe> electrolysis = new LinkedHashMap<>();

    private ElectrolyserFluidRecipes() { }

    private void register(FluidType input, int amount, FluidStack output1, FluidStack output2, ItemStack... byproduct) {
        electrolysis.put(input, new ElectrolysisRecipe(amount, output1, output2, byproduct));
    }

    private void register(FluidType input, int amount, FluidStack output1, FluidStack output2, int duration, ItemStack... byproduct) {
        electrolysis.put(input, new ElectrolysisRecipe(amount, output1, output2, duration, byproduct));
    }

    public ElectrolysisRecipe getRecipe(FluidType type) {
        if(type == null) return null;
        return electrolysis.get(type);
    }

    public Map<FluidType, ElectrolysisRecipe> getRecipes() {
        return electrolysis;
    }

    /*
     * SERIALIZABLE RECIPE CONTRACT
     */

    @Override
    public String getFileName() {
        return "hbmElectrolyserFluid.json";
    }

    @Override
    public Object getRecipeObject() {
        return electrolysis;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        FluidType input = Fluids.fromName(obj.get("input").getAsString());
        int amount = obj.get("amount").getAsInt();

        FluidStack output1 = SerializableRecipe.readFluidStack(obj.get("output1").getAsJsonArray());
        FluidStack output2 = SerializableRecipe.readFluidStack(obj.get("output2").getAsJsonArray());

        int duration = obj.has("duration") ? obj.get("duration").getAsInt() : 20;

        ItemStack[] byproducts = obj.has("byproducts") ?
                SerializableRecipe.readItemStackArray(obj.get("byproducts").getAsJsonArray()) :
                new ItemStack[0];

        register(input, amount, output1, output2, duration, byproducts);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<FluidType, ElectrolysisRecipe> entry = (Entry<FluidType, ElectrolysisRecipe>) recipe;

        FluidType input = entry.getKey();
        ElectrolysisRecipe data = entry.getValue();

        writer.name("input").value(input.getUnlocalizedName());
        writer.name("amount").value(data.amount);

        writer.name("output1");
        SerializableRecipe.writeFluidStack(data.output1, writer);

        writer.name("output2");
        SerializableRecipe.writeFluidStack(data.output2, writer);

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
        register(Fluids.WATER, 2_000, new FluidStack(Fluids.HYDROGEN, 200), new FluidStack(Fluids.OXYGEN, 200), 10);
        register(Fluids.HEAVYWATER, 2_000, new FluidStack(Fluids.DEUTERIUM, 200), new FluidStack(Fluids.OXYGEN, 200), 10);

        register(Fluids.VITRIOL, 1_000, new FluidStack(Fluids.SULFURIC_ACID, 500), new FluidStack(Fluids.CHLORINE, 500),
                new ItemStack(NtmItems.POWDER_IRON.get())
                // TODO: missing item, no "ingot_mercury" in this port yet - re-add once it exists
                // , new ItemStack(NtmItems.INGOT_MERCURY.get())
        );

        register(Fluids.SLOP, 1_000, new FluidStack(Fluids.MERCURY, 250), new FluidStack(Fluids.NONE, 0),
                new ItemStack(NtmItems.NITER.get(), 2),
                new ItemStack(NtmItems.POWDER_LIMESTONE.get(), 2),
                new ItemStack(NtmItems.SULFUR.get()));

        register(Fluids.REDMUD, 450, new FluidStack(Fluids.MERCURY, 150), new FluidStack(Fluids.LYE, 50),
                new ItemStack(NtmItems.POWDER_TITANIUM.get(), 3),
                new ItemStack(NtmItems.POWDER_IRON.get(), 3),
                new ItemStack(NtmItems.POWDER_ALUMINIUM.get(), 2));

        register(Fluids.ALUMINA, 200, new FluidStack(Fluids.CARBONDIOXIDE, 100), new FluidStack(Fluids.NONE, 0), 40,
                new ItemStack(NtmItems.POWDER_ALUMINIUM.get(), 7),
                new ItemStack(NtmItems.FLUORITE.get(), 2));

        register(Fluids.POTASSIUM_CHLORIDE, 250, new FluidStack(Fluids.CHLORINE, 125), new FluidStack(Fluids.NONE, 0),
                new ItemStack(NtmItems.DUST.get()));

        register(Fluids.CALCIUM_CHLORIDE, 250, new FluidStack(Fluids.CHLORINE, 125), new FluidStack(Fluids.CALCIUM_SOLUTION, 125));
    }

    @Override
    public void deleteRecipes() {
        electrolysis.clear();
    }

    public static class ElectrolysisRecipe {

        public FluidStack output1;
        public FluidStack output2;
        public int amount;
        public ItemStack[] byproduct;
        public int duration;

        public ElectrolysisRecipe(int amount, FluidStack output1, FluidStack output2, ItemStack... byproduct) {
            this(amount, output1, output2, 20, byproduct);
        }

        public ElectrolysisRecipe(int amount, FluidStack output1, FluidStack output2, int duration, ItemStack... byproduct) {
            this.output1 = output1;
            this.output2 = output2;
            this.amount = amount;
            this.byproduct = byproduct;
            this.duration = duration;
        }
    }
}