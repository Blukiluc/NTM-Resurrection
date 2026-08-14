package com.hbm.inventory.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.ItemFELCrystal.EnumWavelengths;
import com.hbm.util.WeightedRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SILEXRecipes extends SerializableRecipe {

    public static final SILEXRecipes INSTANCE = new SILEXRecipes();

    public final LinkedHashMap<ComparableStack, SILEXRecipe> recipes = new LinkedHashMap<>();

    private SILEXRecipes() { }

    @Override
    public void registerDefaults() {
        SILEXRecipe uranium = new SILEXRecipe(900, 100, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.NUGGET_U235.get()), 1)
                .addOut(new ItemStack(NtmItems.NUGGET_U238.get()), 11);
        this.register(new ItemStack(NtmItems.INGOT_URANIUM.get()), uranium);
        this.register(new ItemStack(NtmItems.POWDER_URANIUM.get()), uranium);
        this.register(fluidInput(Fluids.UF6), uranium);

        this.register(new ItemStack(NtmItems.INGOT_PU_MIX.get()), new SILEXRecipe(900, 100, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.NUGGET_PU239.get()), 6)
                .addOut(new ItemStack(NtmItems.NUGGET_PU240.get()), 3));

        this.register(new ItemStack(NtmItems.INGOT_AM_MIX.get()), new SILEXRecipe(900, 100, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.NUGGET_AM241.get()), 3)
                .addOut(new ItemStack(NtmItems.NUGGET_AM242.get()), 6));

        SILEXRecipe plutonium = new SILEXRecipe(900, 100, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.NUGGET_PU238.get()), 3)
                .addOut(new ItemStack(NtmItems.NUGGET_PU239.get()), 4)
                .addOut(new ItemStack(NtmItems.NUGGET_PU240.get()), 2);
        this.register(new ItemStack(NtmItems.INGOT_PLUTONIUM.get()), plutonium);
        this.register(new ItemStack(NtmItems.POWDER_PLUTONIUM.get()), plutonium);
        this.register(fluidInput(Fluids.PUF6), plutonium);

        this.register(new ItemStack(NtmItems.INGOT_SCHRARANIUM.get()), new SILEXRecipe(900, 100, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.NUGGET_SCHRABIDIUM.get()), 4)
                .addOut(new ItemStack(NtmItems.NUGGET_URANIUM.get()), 3)
                .addOut(new ItemStack(NtmItems.NUGGET_NEPTUNIUM.get()), 2));

        SILEXRecipe australium = new SILEXRecipe(900, 100, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.NUGGET_AUSTRALIUM_LESSER.get()), 5)
                .addOut(new ItemStack(NtmItems.NUGGET_AUSTRALIUM_GREATER.get()), 1);
        this.register(new ItemStack(NtmItems.INGOT_AUSTRALIUM.get()), australium);
        this.register(new ItemStack(NtmItems.POWDER_AUSTRALIUM.get()), australium);

        this.register(new ItemStack(NtmItems.CRYSTAL_SCHRARANIUM.get()), new SILEXRecipe(900, 100, EnumWavelengths.UV)
                .addOut(new ItemStack(NtmItems.NUGGET_SCHRABIDIUM.get()), 5)
                .addOut(new ItemStack(NtmItems.NUGGET_URANIUM.get()), 2)
                .addOut(new ItemStack(NtmItems.NUGGET_NEPTUNIUM.get()), 2));

        this.register(new ItemStack(NtmBlocks.ORE_TIKITE.get()), new SILEXRecipe(900, 100, EnumWavelengths.UV)
                .addOut(new ItemStack(NtmItems.POWDER_PLUTONIUM.get()), 2)
                .addOut(new ItemStack(NtmItems.POWDER_COBALT.get()), 3)
                .addOut(new ItemStack(NtmItems.POWDER_NIOBIUM.get()), 3)
                .addOut(new ItemStack(NtmItems.POWDER_NITAN_MIX.get()), 2));

        this.register(new ItemStack(NtmItems.CRYSTAL_TRIXITE.get()), new SILEXRecipe(1_200, 100, EnumWavelengths.UV)
                .addOut(new ItemStack(NtmItems.POWDER_PLUTONIUM.get()), 2)
                .addOut(new ItemStack(NtmItems.POWDER_COBALT.get()), 3)
                .addOut(new ItemStack(NtmItems.POWDER_NIOBIUM.get()), 3)
                .addOut(new ItemStack(NtmItems.POWDER_NITAN_MIX.get()), 1)
                .addOut(new ItemStack(NtmItems.POWDER_SPARK_MIX.get()), 1));

        SILEXRecipe lapis = new SILEXRecipe(100, 100, EnumWavelengths.IR)
                .addOut(new ItemStack(NtmItems.SULFUR.get()), 4)
                .addOut(new ItemStack(NtmItems.POWDER_ALUMINIUM.get()), 3)
                .addOut(new ItemStack(NtmItems.POWDER_COBALT.get()), 3);
        this.register(new ItemStack(Items.LAPIS_LAZULI), lapis);
        this.register(new ItemStack(NtmItems.POWDER_LAPIS.get()), lapis);

        this.register(fluidInput(Fluids.DEATH), new SILEXRecipe(1_000, 1_000, EnumWavelengths.GAMMA)
                .addOut(new ItemStack(NtmItems.POWDER_IMPURE_OSMIRIDIUM.get()), 1));

        this.register(fluidInput(Fluids.VITRIOL), new SILEXRecipe(1_000, 300, EnumWavelengths.IR)
                .addOut(new ItemStack(NtmItems.POWDER_BROMINE.get()), 5)
                .addOut(new ItemStack(NtmItems.POWDER_IODINE.get()), 5)
                .addOut(new ItemStack(NtmItems.POWDER_IRON.get()), 5)
                .addOut(new ItemStack(NtmItems.SULFUR.get()), 15));

        this.register(fluidInput(Fluids.REDMUD), new SILEXRecipe(300, 50, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.POWDER_ALUMINIUM.get()), 10)
                .addOut(new ItemStack(NtmItems.POWDER_NEODYMIUM_TINY.get(), 3), 5)
                .addOut(new ItemStack(NtmItems.POWDER_BORON_TINY.get(), 3), 5)
                .addOut(new ItemStack(NtmItems.NUGGET_ZIRCONIUM.get()), 5)
                .addOut(new ItemStack(NtmItems.POWDER_IRON.get()), 20)
                .addOut(new ItemStack(NtmItems.POWDER_TITANIUM.get()), 15)
                .addOut(new ItemStack(NtmItems.POWDER_SODIUM.get()), 10));

        this.register(new ItemStack(Items.GRAVEL), new SILEXRecipe(1_000, 250, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(Items.FLINT), 80)
                .addOut(new ItemStack(NtmItems.POWDER_BORON.get()), 5)
                .addOut(new ItemStack(NtmItems.POWDER_LITHIUM.get()), 10)
                .addOut(new ItemStack(NtmItems.FLUORITE.get()), 5));

        this.register(fluidInput(Fluids.FULLERENE), new SILEXRecipe(1_000, 1_000, EnumWavelengths.VISIBLE)
                .addOut(new ItemStack(NtmItems.POWDER_ASH_FULLERENE.get()), 1));
    }

    public void register(ItemStack input, SILEXRecipe recipe) {
        this.recipes.put(new ComparableStack(input.copyWithCount(1)), recipe);
    }

    public SILEXRecipe getOutput(ItemStack stack) {
        if(stack.isEmpty()) return null;
        return this.recipes.get(new ComparableStack(stack.copyWithCount(1)));
    }

    public boolean hasFluidInput(FluidType type) {
        return this.getOutput(fluidInput(type)) != null;
    }

    public static ItemStack fluidInput(FluidType type) {
        return MetaHelper.newStack(NtmItems.FLUID_ICON.get(), 1, type.getID());
    }

    @Override
    public String getFileName() {
        return "hbmSILEX.json";
    }

    @Override
    public Object getRecipeObject() {
        return this.recipes;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject object = recipe.getAsJsonObject();
        AStack input = SerializableRecipe.readAStack(object.getAsJsonArray("input"));
        if(!(input instanceof ComparableStack comparable)) return;

        int produced = object.get("fluidProduced").getAsInt();
        int consumed = object.get("fluidConsumed").getAsInt();
        int wavelength = Mth.clamp(object.get("laserStrength").getAsInt(), 0, EnumWavelengths.values().length - 1);
        SILEXRecipe silexRecipe = new SILEXRecipe(produced, consumed, EnumWavelengths.values()[wavelength]);

        JsonArray outputs = object.getAsJsonArray("outputs");
        for(JsonElement element : outputs) {
            JsonObject output = element.getAsJsonObject();
            ItemStack stack = SerializableRecipe.readItemStack(output.getAsJsonArray("stack"));
            if(!stack.isEmpty()) silexRecipe.addOut(stack, output.get("weight").getAsInt());
        }

        this.recipes.put(new ComparableStack(comparable.item, 1, comparable.meta), silexRecipe);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        if(!(recipe instanceof Map.Entry<?, ?> entry)) return;
        if(!(entry.getKey() instanceof ComparableStack input)) return;
        if(!(entry.getValue() instanceof SILEXRecipe silexRecipe)) return;

        writer.name("input");
        SerializableRecipe.writeAStack(input, writer);
        writer.name("fluidProduced").value(silexRecipe.fluidProduced);
        writer.name("fluidConsumed").value(silexRecipe.fluidConsumed);
        writer.name("laserStrength").value(silexRecipe.laserStrength.ordinal());
        writer.name("outputs").beginArray();
        for(WeightedOutput output : silexRecipe.outputs) {
            writer.beginObject();
            writer.name("stack");
            SerializableRecipe.writeItemStack(output.stack, writer);
            writer.name("weight").value(output.itemWeight);
            writer.endObject();
        }
        writer.endArray();
    }

    @Override
    public void deleteRecipes() {
        this.recipes.clear();
    }

    public static class SILEXRecipe {
        public final int fluidProduced;
        public final int fluidConsumed;
        public final EnumWavelengths laserStrength;
        public final List<WeightedOutput> outputs = new ArrayList<>();

        public SILEXRecipe(int fluidProduced, int fluidConsumed, EnumWavelengths laserStrength) {
            this.fluidProduced = fluidProduced;
            this.fluidConsumed = fluidConsumed;
            this.laserStrength = laserStrength;
        }

        public SILEXRecipe addOut(ItemStack stack, int weight) {
            this.outputs.add(new WeightedOutput(stack, weight));
            return this;
        }
    }

    public static class WeightedOutput extends WeightedRandom.Item {
        public final ItemStack stack;

        public WeightedOutput(ItemStack stack, int weight) {
            super(weight);
            this.stack = stack;
        }
    }
}
