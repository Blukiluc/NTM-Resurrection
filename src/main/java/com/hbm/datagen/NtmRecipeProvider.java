package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats;
import com.hbm.items.NtmItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class NtmRecipeProvider extends RecipeProvider {

    public NtmRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.EGG_BALEFIRE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', NtmItems.EGG_BALEFIRE_SHARD.get())
                .unlockedBy("has_balefire_shard", has(NtmItems.EGG_BALEFIRE_SHARD.get()))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NtmItems.EGG_BALEFIRE_SHARD.get(), 9)
                .requires(NtmItems.EGG_BALEFIRE.get())
                .unlockedBy("has_balefire_egg", has(NtmItems.EGG_BALEFIRE.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_MOSSY.get(), 8)
                .pattern("BBB")
                .pattern("BVB")
                .pattern("BBB")
                .define('B', NtmBlocks.BRICK_CONCRETE.get())
                .define('V', Items.VINE)
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_CRACKED.get(), 6)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .define('B', NtmBlocks.BRICK_CONCRETE.get())
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_BROKEN.get(), 6)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .define('B', NtmBlocks.BRICK_CONCRETE_CRACKED.get())
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE_MOSSY))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE_CRACKED))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE_BROKEN))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);

        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_SLAB.get(), NtmBlocks.BRICK_CONCRETE.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), NtmBlocks.BRICK_CONCRETE_MOSSY.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), NtmBlocks.BRICK_CONCRETE_CRACKED.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), NtmBlocks.BRICK_CONCRETE_BROKEN.get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, NtmBlocks.RED_CABLE_CLASSIC.get())
                .requires(NtmBlocks.RED_CABLE.get())
                .unlockedBy("has_red_cable", has(NtmBlocks.RED_CABLE.get()))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, NtmBlocks.RED_CABLE.get())
                .requires(NtmBlocks.RED_CABLE_CLASSIC.get())
                .unlockedBy("has_red_cable_classic", has(NtmBlocks.RED_CABLE_CLASSIC.get()))
                .save(recipeOutput, "hbm:red_cable_from_classic");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, NtmItems.WRENCH.get())
                .pattern(" S ")
                .pattern(" IS")
                .pattern("I  ")
                .define('S', NtmItems.INGOT_STEEL.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_steel_ingot", has(NtmItems.INGOT_STEEL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, NtmItems.MIRROR_TOOL.get())
                .pattern(" A ")
                .pattern(" IA")
                .pattern("I  ")
                .define('A', NtmItems.INGOT_ALUMINIUM.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_aluminium_ingot", has(NtmItems.INGOT_ALUMINIUM.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.MACHINE_SOLAR_BOILER.get())
                .pattern("SHS")
                .pattern("DHD")
                .pattern("SHS")
                .define('S', NtmItems.INGOT_STEEL.get())
                .define('H', Mats.MAT_STEEL.generatedItems.get(MaterialShapes.SHELL).get())
                .define('D', Items.BLACK_DYE)
                .unlockedBy("has_steel_shell", has(Mats.MAT_STEEL.generatedItems.get(MaterialShapes.SHELL).get()))
                .save(recipeOutput);

//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.SOLAR_MIRROR.get(), 3)
//                .pattern("AAA")
//                .pattern(" B ")
//                .pattern("SSS")
//                .define('A', NtmItems.PLATE_ALUMINIUM.get())
//                .define('B', NtmBlocks.STEEL_BEAM.get())
//                .define('S', NtmItems.INGOT_STEEL.get())
//                .unlockedBy("has_steel_beam", has(NtmBlocks.STEEL_BEAM.get()))
//                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.MACHINE_FURNACE_BRICK.get())
                .pattern("III")
                .pattern("I I")
                .pattern("BBB")
                .define('I', Items.BRICK)
                .define('B', Blocks.STONE)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(recipeOutput);

//        for(int i = 0; i < 15; i += 3) {
//            constructionRecipes.add(new AnvilConstructionRecipe(new AStack[] {new OreDictStack(IRON.plate(), 1), new ComparableStack(ModItems.plate_polymer)}, new AnvilOutput(new ItemStack(ModBlocks.fluid_duct_exhaust, 8, i))).setTier(2).setOverlay(OverlayType.CONSTRUCTION));
//            constructionRecipes.add(new AnvilConstructionRecipe(new ComparableStack(ModBlocks.fluid_duct_exhaust, 8, i), new AnvilOutput[] {new AnvilOutput(new ItemStack(ModItems.plate_iron)), new AnvilOutput(new ItemStack(ModItems.plate_polymer))}).setTier(2));
//        }

//        constructionRecipes.add(new AnvilConstructionRecipe(
//                new AStack[] {
//                        new OreDictStack(STEEL.plate(), 4),
//                        new ComparableStack(Blocks.brick_block, 16),
//                        new ComparableStack(ModBlocks.steel_grate, 2)
//                },
//                new AnvilOutput(new ItemStack(ModBlocks.chimney_brick))).setTier(2));

//        constructionRecipes.add(new AnvilConstructionRecipe(
//                new AStack[] {
//                        new OreDictStack(STEEL.plate(), 16),
//                        new OreDictStack(ANY_CONCRETE.any(), 64),
//                        new ComparableStack(ModBlocks.steel_grate, 4),
//                        new ComparableStack(ModItems.filter_coal, 4)
//                },
//                new AnvilOutput(new ItemStack(ModBlocks.chimney_industrial))).setTier(3));

//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.MACHINE_ELECTRIC_FURNACE.get())
//                .pattern("BBB")
//                .pattern("WFW")
//                .pattern("RRR")
//                .define('B', NtmItems.INGOT_BERYLLIUM.get())
//                .define('W', NtmItems.PLATE_COPPER.get())
//                .define('F', Blocks.FURNACE)
//                .define('R', NtmItems.COIL_TUNGSTEN.get())
//                .unlockedBy("has_tungsten_coil", has(NtmItems.COIL_TUNGSTEN.get()))
//                .save(recipeOutput);

//        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, NtmBlocks.PIPE_ANCHOR.get(), 2)
//                .pattern("P")
//                .pattern("P")
//                .pattern("S")
//                .define('P', NtmItems.PIPE_STEEL.get())
//                .define('S', NtmItems.INGOT_STEEL.get())
//                .unlockedBy("has_steel_pipe", has(NtmItems.PIPE_STEEL.get()))
//                .save(recipeOutput);
    }
}
