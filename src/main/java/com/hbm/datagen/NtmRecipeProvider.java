package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.items.NtmItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.FOUNDRY_BASIN.get())
                .pattern("B B").pattern("B B").pattern("BSB")
                .define('B', NtmItems.INGOT_FIREBRICK.get()).define('S', Items.STONE_SLAB)
                .unlockedBy("has_firebrick", has(NtmItems.INGOT_FIREBRICK.get())).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.FOUNDRY_MOLD.get())
                .pattern("B B").pattern("BSB")
                .define('B', NtmItems.INGOT_FIREBRICK.get()).define('S', Items.STONE_SLAB)
                .unlockedBy("has_firebrick", has(NtmItems.INGOT_FIREBRICK.get())).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.FOUNDRY_CHANNEL.get(), 4)
                .pattern("B B").pattern(" S ")
                .define('B', NtmItems.INGOT_FIREBRICK.get()).define('S', Items.STONE_SLAB)
                .unlockedBy("has_firebrick", has(NtmItems.INGOT_FIREBRICK.get())).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.FOUNDRY_TANK.get())
                .pattern("B B").pattern("I I").pattern("BSB")
                .define('B', NtmItems.INGOT_FIREBRICK.get()).define('I', NtmItems.INGOT_STEEL.get()).define('S', Items.STONE_SLAB)
                .unlockedBy("has_firebrick", has(NtmItems.INGOT_FIREBRICK.get())).save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NtmBlocks.FOUNDRY_OUTLET.get())
                .requires(NtmBlocks.FOUNDRY_CHANNEL.get()).requires(NtmItems.PLATE_STEEL.get())
                .unlockedBy("has_foundry_channel", has(NtmBlocks.FOUNDRY_CHANNEL.get())).save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NtmBlocks.FOUNDRY_SLAGTAP.get())
                .requires(NtmBlocks.FOUNDRY_CHANNEL.get()).requires(Items.STONE_BRICKS)
                .unlockedBy("has_foundry_channel", has(NtmBlocks.FOUNDRY_CHANNEL.get())).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.MOLD_BLANK.get())
                .pattern(" B ").pattern("BIB").pattern(" B ")
                .define('B', NtmItems.INGOT_FIREBRICK.get()).define('I', Items.IRON_INGOT)
                .unlockedBy("has_firebrick", has(NtmItems.INGOT_FIREBRICK.get())).save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NtmItems.MOLD_INGOT.get())
                .requires(NtmItems.MOLD_BLANK.get()).requires(Items.IRON_INGOT)
                .unlockedBy("has_blank_mold", has(NtmItems.MOLD_BLANK.get())).save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NtmItems.MOLD_BLOCK.get())
                .requires(NtmItems.MOLD_BLANK.get()).requires(Items.IRON_BLOCK)
                .unlockedBy("has_blank_mold", has(NtmItems.MOLD_BLANK.get())).save(recipeOutput);

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
    }
}
