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
        this.register(new GenericRecipe("gas_centrifuge.uf6_high_speed").setup(200, 100)
                .inputFluids(new FluidStack(Fluids.UF6, 1_200))
                .outputItems(
                        new ItemStack(NtmItems.NUGGET_U238.get(), 11),
                        new ItemStack(NtmItems.NUGGET_U235.get()),
                        new ItemStack(NtmItems.FLUORITE.get(), 4)));

        this.register(new GenericRecipe("gas_centrifuge.uf6_low_speed").setup(400, 100)
                .inputFluids(new FluidStack(Fluids.UF6, 1_200))
                .outputItems(
                        new ItemStack(NtmItems.NUGGET_U238.get(), 6),
                        new ItemStack(NtmItems.NUGGET_URANIUM_FUEL.get(), 6),
                        new ItemStack(NtmItems.FLUORITE.get(), 4)));

        this.register(new GenericRecipe("gas_centrifuge.puf6").setup(250, 150)
                .inputFluids(new FluidStack(Fluids.PUF6, 900))
                .outputItems(
                        new ItemStack(NtmItems.NUGGET_PU238.get(), 3),
                        new ItemStack(NtmItems.NUGGET_PU_MIX.get(), 6),
                        new ItemStack(NtmItems.FLUORITE.get(), 3)));

        // Recettes propres au port actuel, conservées.
        this.register(new GenericRecipe("gas_centrifuge.water").setup(300, 200)
                .inputFluids(new FluidStack(Fluids.WATER, 1_000))
                .outputItems(
                        new ItemStack(NtmItems.POWDER_SCHRABIDIUM.get()),
                        new ItemStack(NtmItems.POWDER_TUNGSTEN.get()),
                        new ItemStack(NtmItems.POWDER_STEEL.get()),
                        new ItemStack(Blocks.COBBLESTONE)));

        this.register(new GenericRecipe("gas_centrifuge.peroxide").setup(300, 200)
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 300))
                .outputItems(
                        new ItemStack(NtmItems.STAMP_WIRE_DESH.get()),
                        new ItemStack(NtmItems.BILLET_AC227.get()),
                        new ItemStack(NtmItems.INGOT_URANIUM.get()),
                        new ItemStack(Blocks.GRASS_BLOCK)));
    }

    /*
     * Recette originale désactivée : NUCLEAR_WASTE_TINY n'existe pas encore.
     *
     * gasCent.put(new FluidStack(1000, Fluids.WATZ), new Object[] {
     *     new ItemStack[] {
     *         new ItemStack(ModItems.powder_iron, 1),
     *         new ItemStack(ModItems.powder_lead, 1),
     *         new ItemStack(ModItems.nuclear_waste_tiny, 1),
     *         new ItemStack(ModItems.dust, 2)
     *     },
     *     false,
     *     2
     * });
     *
     * Les étapes pseudo-fluides HEUF6/MEUF6/LEUF6/NUF6, PF6 et MUD sont également
     * conservées dans la source d'origine, mais le registre GenericRecipe actuel
     * ne possède pas de type de sortie pseudo-fluide compatible.
     */
}
