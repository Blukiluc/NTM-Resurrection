package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import com.hbm.util.Tuple.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class OreAcidizerRecipes extends SerializableRecipe {

    public static final OreAcidizerRecipes INSTANCE = new OreAcidizerRecipes();

    public final LinkedHashMap<Pair<ComparableStack, FluidType>, OreAcidizerRecipe> recipes = new LinkedHashMap<>();
    public final LinkedHashMap<ComparableStack, Integer> amounts = new LinkedHashMap<>();

    private OreAcidizerRecipes() { }

    @Override
    public void registerDefaults() {
        int baseTime = 600;
        int utilityTime = 100;
        int mixingTime = 20;
        FluidStack peroxide = new FluidStack(Fluids.PEROXIDE, 500);
        FluidStack sulfuric = new FluidStack(Fluids.SULFURIC_ACID, 500);

        this.registerOre(Blocks.COAL_ORE, NtmItems.CRYSTAL_COAL.get(), baseTime, peroxide);
        this.registerOre(Blocks.DEEPSLATE_COAL_ORE, NtmItems.CRYSTAL_COAL.get(), baseTime, peroxide);
        this.registerOre(Blocks.IRON_ORE, NtmItems.CRYSTAL_IRON.get(), baseTime, peroxide);
        this.registerOre(Blocks.DEEPSLATE_IRON_ORE, NtmItems.CRYSTAL_IRON.get(), baseTime, peroxide);
        this.registerOre(Blocks.GOLD_ORE, NtmItems.CRYSTAL_GOLD.get(), baseTime, peroxide);
        this.registerOre(Blocks.DEEPSLATE_GOLD_ORE, NtmItems.CRYSTAL_GOLD.get(), baseTime, peroxide);
        this.registerOre(Blocks.REDSTONE_ORE, NtmItems.CRYSTAL_REDSTONE.get(), baseTime, peroxide);
        this.registerOre(Blocks.DEEPSLATE_REDSTONE_ORE, NtmItems.CRYSTAL_REDSTONE.get(), baseTime, peroxide);
        this.registerOre(Blocks.LAPIS_ORE, NtmItems.CRYSTAL_LAPIS.get(), baseTime, peroxide);
        this.registerOre(Blocks.DEEPSLATE_LAPIS_ORE, NtmItems.CRYSTAL_LAPIS.get(), baseTime, peroxide);
        this.registerOre(Blocks.DIAMOND_ORE, NtmItems.CRYSTAL_DIAMOND.get(), baseTime, peroxide);
        this.registerOre(Blocks.DEEPSLATE_DIAMOND_ORE, NtmItems.CRYSTAL_DIAMOND.get(), baseTime, peroxide);
        this.registerOre(Blocks.COPPER_ORE, NtmItems.CRYSTAL_COPPER.get(), baseTime, peroxide);
        this.registerOre(Blocks.DEEPSLATE_COPPER_ORE, NtmItems.CRYSTAL_COPPER.get(), baseTime, peroxide);

        this.registerOre(NtmBlocks.ORE_URANIUM.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_URANIUM_SCORCHED.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_NETHER_URANIUM.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_NETHER_URANIUM_SCORCHED.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_GNEISS_URANIUM.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_GNEISS_URANIUM_SCORCHED.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_NETHER_PLUTONIUM.get(), NtmItems.CRYSTAL_PLUTONIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_SCHRABIDIUM.get(), NtmItems.CRYSTAL_SCHRABIDIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_NETHER_SCHRABIDIUM.get(), NtmItems.CRYSTAL_SCHRABIDIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_GNEISS_SCHRABIDIUM.get(), NtmItems.CRYSTAL_SCHRABIDIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_TIKITE.get(), NtmItems.CRYSTAL_TRIXITE.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.GRAVEL_DIAMOND.get(), NtmItems.CRYSTAL_DIAMOND.get(), baseTime, peroxide);

        this.registerRecipe(new ComparableStack(NtmItems.POWDER_CALCIUM.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.POWDER_CEMENT.get(), 8), utilityTime).prod(0.1F), new FluidStack(Fluids.REDMUD, 75));
        this.registerRecipe(new ComparableStack(NtmBlocks.GRAVEL_OBSIDIAN.get()), new OreAcidizerRecipe(new ItemStack(NtmBlocks.BRICK_OBSIDIAN.get()), utilityTime));
        this.registerRecipe(new ComparableStack(net.minecraft.world.item.Items.ROTTEN_FLESH), new OreAcidizerRecipe(new ItemStack(net.minecraft.world.item.Items.LEATHER), utilityTime).prod(0.25F));
        this.registerRecipe(new ComparableStack(NtmItems.COAL_INFERNAL.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.SOLID_FUEL.get()), utilityTime));
        this.registerRecipe(new ComparableStack(net.minecraft.world.item.Items.BONE), new OreAcidizerRecipe(new ItemStack(net.minecraft.world.item.Items.SLIME_BALL, 16), mixingTime), new FluidStack(Fluids.SULFURIC_ACID, 1_000));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_DIAMOND.get()), new OreAcidizerRecipe(new ItemStack(net.minecraft.world.item.Items.DIAMOND), utilityTime));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_EMERALD.get()), new OreAcidizerRecipe(new ItemStack(net.minecraft.world.item.Items.EMERALD), utilityTime));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_LAPIS.get()), new OreAcidizerRecipe(new ItemStack(net.minecraft.world.item.Items.LAPIS_LAZULI), utilityTime));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_SEMTEX_MIX.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.INGOT_SEMTEX.get()), baseTime));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_DESH_READY.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.INGOT_DESH.get()), baseTime));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_METEORITE.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.FRAGMENT_METEORITE.get()), utilityTime));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_SAWDUST.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.CORDITE.get()), mixingTime).prod(0.25F), new FluidStack(Fluids.NITROGLYCERIN, 250));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_IMPURE_OSMIRIDIUM.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.CRYSTAL_OSMIRIDIUM.get()), baseTime), new FluidStack(Fluids.SCHRABIDIC, 1_000));
        this.registerRecipe(new ComparableStack(Blocks.SAND), new OreAcidizerRecipe(new ItemStack(Blocks.CLAY), mixingTime), new FluidStack(Fluids.COLLOID, 1_000));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_QUARTZ.get()), new OreAcidizerRecipe(new ItemStack(NtmItems.BALL_DYNAMITE.get(), 4), mixingTime), new FluidStack(Fluids.NITROGLYCERIN, 250));
    }

    private void registerOre(ItemLike input, ItemLike output, int duration, FluidStack fluid) {
        this.registerRecipe(new ComparableStack(input.asItem()), new OreAcidizerRecipe(new ItemStack(output), duration).prod(0.05F), fluid);
    }

    public OreAcidizerRecipe getOutput(ItemStack stack, FluidType type) {
        if(stack.isEmpty()) return null;

        int meta = MetaHelper.getMeta(stack);
        for(Entry<Pair<ComparableStack, FluidType>, OreAcidizerRecipe> entry : this.recipes.entrySet()) {
            ComparableStack input = entry.getKey().getKey();
            if(entry.getKey().getValue() == type && input.item == stack.getItem() && (input.meta == MetaHelper.WILDCARD_VALUE || input.meta == meta)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public int getAmount(ItemStack stack) {
        if(stack.isEmpty()) return 0;

        int meta = MetaHelper.getMeta(stack);
        for(Entry<ComparableStack, Integer> entry : this.amounts.entrySet()) {
            ComparableStack input = entry.getKey();
            if(input.item == stack.getItem() && (input.meta == MetaHelper.WILDCARD_VALUE || input.meta == meta)) return entry.getValue();
        }

        return 0;
    }

    public void registerRecipe(ComparableStack input, OreAcidizerRecipe recipe) {
        this.registerRecipe(input, recipe, new FluidStack(Fluids.PEROXIDE, 500));
    }

    public void registerRecipe(ComparableStack input, OreAcidizerRecipe recipe, FluidStack fluid) {
        ComparableStack singular = new ComparableStack(input.item, 1, input.meta);
        recipe.acidAmount = fluid.fill;
        this.recipes.put(new Pair<>(singular, fluid.type), recipe);
        this.amounts.put(singular, recipe.itemAmount);
    }

    @Override
    public String getFileName() {
        return "hbmOreAcidizer.json";
    }

    @Override
    public Object getRecipeObject() {
        return this.recipes;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = recipe.getAsJsonObject();

        ItemStack output = SerializableRecipe.readItemStack(obj.get("output").getAsJsonArray());
        AStack input = SerializableRecipe.readAStack(obj.get("input").getAsJsonArray());
        FluidStack fluid = SerializableRecipe.readFluidStack(obj.get("fluid").getAsJsonArray());
        int duration = obj.get("duration").getAsInt();
        float productivity = obj.has("productivity") ? obj.get("productivity").getAsFloat() : 0F;

        if(input instanceof ComparableStack comparable) {
            OreAcidizerRecipe oreAcidizerRecipe = new OreAcidizerRecipe(output, duration).setReq(comparable.stacksize).prod(productivity);
            this.registerRecipe(new ComparableStack(comparable.item, 1, comparable.meta), oreAcidizerRecipe, fluid);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<Pair<ComparableStack, FluidType>, OreAcidizerRecipe> entry = (Entry<Pair<ComparableStack, FluidType>, OreAcidizerRecipe>) recipe;
        ComparableStack keyInput = entry.getKey().getKey();
        OreAcidizerRecipe oreAcidizerRecipe = entry.getValue();
        ComparableStack input = new ComparableStack(keyInput.item, oreAcidizerRecipe.itemAmount, keyInput.meta);

        writer.name("duration").value(oreAcidizerRecipe.duration);
        writer.name("fluid");
        SerializableRecipe.writeFluidStack(new FluidStack(entry.getKey().getValue(), oreAcidizerRecipe.acidAmount), writer);
        writer.name("input");
        SerializableRecipe.writeAStack(input, writer);
        writer.name("output");
        SerializableRecipe.writeItemStack(oreAcidizerRecipe.output, writer);
        writer.name("productivity").value(oreAcidizerRecipe.productivity);
    }

    @Override
    public void deleteRecipes() {
        this.recipes.clear();
        this.amounts.clear();
    }

    @Override
    public String getComment() {
        return "The acidizer also supports stack size requirements for input items, eg. the cadmium recipe requires 10 willow leaves.";
    }

    public static class OreAcidizerRecipe {
        public int acidAmount = 500;
        public int itemAmount = 1;
        public final int duration;
        public float productivity;
        public final ItemStack output;

        public OreAcidizerRecipe(ItemStack output, int duration) {
            this.output = output;
            this.duration = duration;
        }

        public OreAcidizerRecipe setReq(int amount) {
            this.itemAmount = amount;
            return this;
        }

        public OreAcidizerRecipe prod(float productivity) {
            this.productivity = productivity;
            return this;
        }
    }
}
