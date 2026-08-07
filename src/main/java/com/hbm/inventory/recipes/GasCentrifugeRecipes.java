package com.hbm.inventory.recipes;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class GasCentrifugeRecipes extends GenericRecipes<GenericRecipe> {

    public static final GasCentrifugeRecipes INSTANCE = new GasCentrifugeRecipes();

    @Override public int inputItemLimit() { return 0; }
    @Override public int inputFluidLimit() { return 1; }
    @Override public int outputItemLimit() { return 4; }
    @Override public int outputFluidLimit() { return 1; }

    @Override public String getFileName() { return "hbmGasCentrifuge.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {
//        this.register(new GenericRecipe("gas_centrifuge.uf6").setup(200, 100)
//                .outputItems(
//                        new ItemStack(NtmItems.POWDER_GOLD.get(), 1),
//                        new ItemStack(NtmItems.POWDER_URANIUM.get(), 2),
//                        new ItemStack(NtmItems.POWDER_IRON.get(), 1),
//                        new ItemStack(Blocks.GRAVEL.asItem(), 1)
//                )
//                .inputFluids(new FluidStack(Fluids.UF6, 1000)));
//
//        this.register(new GenericRecipe("gas_centrifuge.puf6").setup(250, 150)
//                .outputItems(
//                        new ItemStack(NtmItems.POWDER_PLUTONIUM.get(), 1),
//                        new ItemStack(NtmItems.POWDER_LEAD.get(), 1),
//                        new ItemStack(NtmItems.POWDER_IRON.get(), 1),
//                        new ItemStack(Blocks.SAND.asItem(), 1)
//                )
//                .inputFluids(new FluidStack(Fluids.PUF6, 1000)));

        this.register(new GenericRecipe("gas_centrifuge.water").setup(300, 200)
                .outputItems(
                        new ItemStack(NtmItems.POWDER_SCHRABIDIUM.get(), 1),
                        new ItemStack(NtmItems.POWDER_TUNGSTEN.get(), 1),
                        new ItemStack(NtmItems.POWDER_STEEL.get(), 1),
                        new ItemStack(Blocks.COBBLESTONE.asItem(), 1)
                )
                .inputFluids(new FluidStack(Fluids.WATER, 1000)));

        this.register(new GenericRecipe("gas_centrifuge.peroxide").setup(300, 200)
                .outputItems(
                        new ItemStack(NtmItems.STAMP_WIRE_DESH.get(), 1),
                        new ItemStack(NtmItems.BILLET_AC227.get(), 1),
                        new ItemStack(NtmItems.INGOT_URANIUM.get(), 1),
                        new ItemStack(Blocks.GRASS_BLOCK.asItem(), 1)
                )
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 300)));
    }
}