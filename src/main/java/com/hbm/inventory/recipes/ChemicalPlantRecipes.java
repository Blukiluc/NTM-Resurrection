package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIconItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ChemicalPlantRecipes extends GenericRecipes<GenericRecipe> {

    public static final ChemicalPlantRecipes INSTANCE = new ChemicalPlantRecipes();

    @Override public int inputItemLimit() { return 3; }
    @Override public int inputFluidLimit() { return 3; }
    @Override public int outputItemLimit() { return 3; }
    @Override public int outputFluidLimit() { return 3; }

    @Override public String getFileName() { return "hbmChemicalPlant.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {
        this.register(new GenericRecipe("chem.boytarget").setup(200, 100)
                .outputItems(new ItemStack(NtmItems.LITTLE_BOY_TARGET.get(), 1))
                .inputItems(new ComparableStack(NtmItems.INGOT_URANIUM.get(), 18)));

        this.register(new GenericRecipe("chem.grass_block").setup(200, 100)
                .outputItems(new ItemStack(Blocks.GRASS_BLOCK, 1))
                .inputItems(
                        new ComparableStack(NtmItems.SINGULARITY.get(), 1),
                        new ComparableStack(NtmItems.SINGULARITY_COUNTER_RESONANT.get(), 1),
                        new ComparableStack(NtmItems.MISSILE_DOOMSDAY_RUSTED.get(), 1)
                )
        );

        this.register(new GenericRecipe("chem.man").setup(200, 100)
                .outputItems(new ItemStack(NtmBlocks.NUKE_FAT_MAN.get(), 1))
                .inputItems(new ComparableStack(NtmItems.PELLET_ANTIMATTER.get(), 1)));

        this.register(new GenericRecipe("chem.testfluidin").setup(40, 100)
                .outputItems(new ItemStack(NtmItems.SPAWN_DUCK.get(), 2))
                .inputItems(new ComparableStack(NtmItems.CAP.get()))
                .inputFluids(new FluidStack(Fluids.WATER, 1_000)));

        this.register(new GenericRecipe("chem.testfluidout").setup(40, 100)
                .setIcon(FluidIconItem.make(Fluids.WATER, 10))
                .inputFluids(new FluidStack(Fluids.WATER, 1_000))
                .outputFluids(new FluidStack(Fluids.LAVA, 500)));

        this.register(new GenericRecipe("chem.test").setup(40, 100)
                .setIcon(FluidIconItem.make(Fluids.LAVA, 10))
                .outputItems(new ItemStack(NtmBlocks.BRICK_CONCRETE.get(), 1))
                .inputItems(new ComparableStack(Items.WATER_BUCKET, 1)));

        this.register(new GenericRecipe("chem.peroxide").setup(50, 100)
                .setIcon(FluidIconItem.make(Fluids.PEROXIDE, 10))
                .inputFluids(new FluidStack(Fluids.WATER, 1_000))
                .outputFluids(new FluidStack(Fluids.PEROXIDE, 1_000)));

        this.register(new GenericRecipe("chem.sulfuricacid").setup(50, 100)
                .setIcon(FluidIconItem.make(Fluids.SULFURIC_ACID, 10))
                .inputFluids(new FluidStack(Fluids.WATER, 1_000))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 1_000))
                .inputItems(new ComparableStack(NtmItems.SULFUR.get()))
                .outputFluids(new FluidStack(Fluids.SULFURIC_ACID, 2_000)));

        this.register(new GenericRecipe("chem.nitricacid").setup(50, 100)
                .setIcon(FluidIconItem.make(Fluids.NITRIC_ACID, 10))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 500))
                .inputItems(new ComparableStack(NtmItems.NITER.get()))
                .outputFluids(new FluidStack(Fluids.NITRIC_ACID, 1_000)));
    }
}
