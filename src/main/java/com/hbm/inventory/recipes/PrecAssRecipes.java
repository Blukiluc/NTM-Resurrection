package com.hbm.inventory.recipes;

import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;

public class PrecAssRecipes extends GenericRecipes<GenericRecipe> {

    public static final PrecAssRecipes INSTANCE = new PrecAssRecipes();

    @Override public int inputItemLimit() { return 9; }
    @Override public int inputFluidLimit() { return 1; }
    @Override public int outputItemLimit() { return 9; }
    @Override public int outputFluidLimit() { return 1; }

    @Override public String getFileName() { return "hbmPrecisionAssembly.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {
//        this.register(new GenericRecipe("precass.chip").setup(100, 200L)
//                .inputItems(
//                        new ComparableStack(NtmItems.CIRCUIT_SILICON.get(), 1),
//                        new ComparableStack(NtmItems.PLATE_POLYMER.get(), 3),
//                        new OreDictStack(GOLD.wireFine(), 4))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.CIRCUIT_CHIP.get(), 1), 90),
//                        new ChanceOutput(BrokenItem.make(NtmItems.CIRCUIT_CHIP.get()), 10))));
//        this.register(new GenericRecipe("precass.chip_bismoid").setup(200, 1_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.CIRCUIT_SILICON.get(), 4),
//                        new ComparableStack(NtmItems.PLATE_POLYMER.get(), 8),
//                        new OreDictStack(ANY_BISMOID.nugget(), 2),
//                        new OreDictStack(GOLD.wireFine(), 4))
//                .inputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 500))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.CIRCUIT_CHIP_BISMOID.get(), 1), 75),
//                        new ChanceOutput(BrokenItem.make(NtmItems.CIRCUIT_CHIP_BISMOID.get()), 25))));
//        this.register(new GenericRecipe("precass.chip_quantum").setup(300, 20_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.CIRCUIT_SILICON.get(), 8),
//                        new OreDictStack(BSCCO.wireDense(), 2),
//                        new OreDictStack(ANY_HARDPLASTIC.ingot(), 4),
//                        new ComparableStack(NtmItems.PELLET_CHARGED.get(), 4),
//                        new OreDictStack(GOLD.wireFine(), 8))
//                .inputFluids(new FluidStack(Fluids.HELIUM4, 250))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.CIRCUIT_CHIP_QUANTUM.get(), 1), 90),
//                        new ChanceOutput(BrokenItem.make(NtmItems.CIRCUIT_CHIP_QUANTUM.get()), 10))));
//        this.register(new GenericRecipe("precass.atomic_clock").setup(200, 2_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.CIRCUIT_CHIP.get(), 8),
//                        new OreDictStack(ANY_PLASTIC.ingot(), 4),
//                        new OreDictStack(ZR.wireFine(), 8),
//                        new OreDictStack(SR.dust(), 1))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.CIRCUIT_ATOMIC_CLOCK.get(), 1), 50),
//                        new ChanceOutput(BrokenItem.make(NtmItems.CIRCUIT_ATOMIC_CLOCK.get()), 50))));
//        this.register(new GenericRecipe("precass.controller").setup(400, 15_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.CIRCUIT_CHIP.get(), 32),
//                        new ComparableStack(NtmItems.CIRCUIT_CAPACITOR.get(), 32),
//                        new ComparableStack(NtmItems.CIRCUIT_CAPACITOR_TANTALIUM.get(), 16),
//                        new ComparableStack(NtmItems.CIRCUIT_CONTROLLER_CHASSIS.get(), 1),
//                        new ComparableStack(NtmItems.UPGRADE_SPEED_1.get(), 1),
//                        new OreDictStack(PB.wireFine(), 16))
//                .inputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 1_000))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.CIRCUIT_CONTROLLER.get(), 1), 75),
//                        new ChanceOutput(BrokenItem.make(NtmItems.CIRCUIT_CONTROLLER.get()), 25))));
//        this.register(new GenericRecipe("precass.controller_advanced").setup(600, 25_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.CIRCUIT_CHIP_BISMOID.get(), 16),
//                        new ComparableStack(NtmItems.CIRCUIT_CAPACITOR_TANTALIUM.get(), 48),
//                        new ComparableStack(NtmItems.CIRCUIT_ATOMIC_CLOCK.get(), 1),
//                        new ComparableStack(NtmItems.CIRCUIT_CONTROLLER_CHASSIS.get(), 1),
//                        new ComparableStack(NtmItems.UPGRADE_SPEED_3.get(), 1),
//                        new OreDictStack(PB.wireFine(), 24))
//                .inputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 4_000))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.CIRCUIT_CONTROLLER_ADVANCED.get(), 1), 50),
//                        new ChanceOutput(BrokenItem.make(NtmItems.CIRCUIT_CONTROLLER_ADVANCED.get()), 50))));
//        this.register(new GenericRecipe("precass.controller_quantum").setup(600, 250_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.CIRCUIT_CHIP_QUANTUM.get(), 16),
//                        new ComparableStack(NtmItems.CIRCUIT_CHIP_BISMOID.get(), 48),
//                        new ComparableStack(NtmItems.CIRCUIT_ATOMIC_CLOCK.get(), 8),
//                        new ComparableStack(NtmItems.CIRCUIT_CONTROLLER_ADVANCED.get(), 2),
//                        new ComparableStack(NtmItems.UPGRADE_OVERDRIVE_1.get(), 1),
//                        new OreDictStack(PB.wireFine(), 32))
//                .inputFluids(new FluidStack(Fluids.PERFLUOROMETHYL_COLD, 6_000))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.CIRCUIT_CONTROLLER_QUANTUM.get(), 1), 75),
//                        new ChanceOutput(BrokenItem.make(NtmItems.CIRCUIT_CONTROLLER_QUANTUM.get()), 25))));
//        addFirstUpgrade(NtmItems.UPGRADE_SPEED_1.get(), NtmItems.UPGRADE_SPEED_2.get(), "precass.upgrade_speed_ii");
//        addSecondUpgrade(NtmItems.UPGRADE_SPEED_2.get(), NtmItems.UPGRADE_SPEED_3.get(), "precass.upgrade_speed_iii");
//        addFirstUpgrade(NtmItems.UPGRADE_EFFECT_1.get(), NtmItems.UPGRADE_EFFECT_2.get(), "precass.upgrade_effect_ii");
//        addSecondUpgrade(NtmItems.UPGRADE_EFFECT_2.get(), NtmItems.UPGRADE_EFFECT_3.get(), "precass.upgrade_effect_iii");
//        addFirstUpgrade(NtmItems.UPGRADE_POWER_1.get(), NtmItems.UPGRADE_POWER_2.get(), "precass.upgrade_power_ii");
//        addSecondUpgrade(NtmItems.UPGRADE_POWER_2.get(), NtmItems.UPGRADE_POWER_3.get(), "precass.upgrade_power_iii");
//        addFirstUpgrade(NtmItems.UPGRADE_FORTUNE_1.get(), NtmItems.UPGRADE_FORTUNE_2.get(), "precass.upgrade_fortune_ii");
//        addSecondUpgrade(NtmItems.UPGRADE_FORTUNE_2.get(), NtmItems.UPGRADE_FORTUNE_3.get(), "precass.upgrade_fortune_iii");
//        addFirstUpgrade(NtmItems.UPGRADE_AFTERBURN_1.get(), NtmItems.UPGRADE_AFTERBURN_2.get(), "precass.upgrade_ab_ii");
//        addSecondUpgrade(NtmItems.UPGRADE_AFTERBURN_2.get(), NtmItems.UPGRADE_AFTERBURN_3.get(), "precass.upgrade_ab_iii");
//        this.register(new GenericRecipe("precass.upgrade_overdive_i").setup(200, 1_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.UPGRADE_SPEED_3.get(), 1),
//                        new ComparableStack(NtmItems.UPGRADE_EFFECT_3.get(), 1),
//                        new OreDictStack(BIGMT.ingot(), 16),
//                        new OreDictStack(ANY_HARDPLASTIC.ingot(), 16),
//                        new ComparableStack(NtmItems.CIRCUIT_ADVANCED.get(), 16))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.UPGRADE_OVERDRIVE_1.get(), 1), 50),
//                        new ChanceOutput(BrokenItem.make(NtmItems.UPGRADE_OVERDRIVE_1.get()), 50))));
//        this.register(new GenericRecipe("precass.upgrade_overdive_ii").setup(600, 5_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.UPGRADE_OVERDRIVE_1.get(), 1),
//                        new ComparableStack(NtmItems.UPGRADE_SPEED_3.get(), 1),
//                        new ComparableStack(NtmItems.UPGRADE_EFFECT_3.get(), 1),
//                        new OreDictStack(BIGMT.ingot(), 16),
//                        new ComparableStack(NtmItems.INGOT_CFT.get(), 8),
//                        new ComparableStack(NtmItems.CIRCUIT_CAPACITOR_BOARD.get(), 16))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.UPGRADE_OVERDRIVE_2.get(), 1), 50),
//                        new ChanceOutput(BrokenItem.make(NtmItems.UPGRADE_OVERDRIVE_2.get()), 50))));
//        this.register(new GenericRecipe("precass.upgrade_overdive_iii").setup(1_200, 100_000L)
//                .inputItems(
//                        new ComparableStack(NtmItems.UPGRADE_OVERDRIVE_2.get(), 1),
//                        new ComparableStack(NtmItems.UPGRADE_SPEED_3.get(), 1),
//                        new ComparableStack(NtmItems.UPGRADE_EFFECT_3.get(), 1),
//                        new OreDictStack(ANY_BISMOIDBRONZE.ingot(), 16),
//                        new ComparableStack(NtmItems.INGOT_CFT.get(), 16),
//                        new ComparableStack(NtmItems.CIRCUIT_BISMOID.get(), 16))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.UPGRADE_OVERDRIVE_3.get(), 1), 25),
//                        new ChanceOutput(BrokenItem.make(NtmItems.UPGRADE_OVERDRIVE_3.get()), 75))));
//        this.register(new GenericRecipe("precass.blueprints").setup(6_000, 20_000L)
//                .inputItems(
//                        new ComparableStack(Items.PAPER, 16),
//                        new OreDictStack(KEY_BLUE, 16),
//                        new ComparableStack(Items.PUFFERFISH, 4))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.BLUEPRINT_FOLDER.get(), 1), 10),
//                        new ChanceOutput(new ItemStack(Items.PAPER, 16), 90))));
//        this.register(new GenericRecipe("precass.beigeprints").setup(6_000, 50_000L)
//                .inputItems(
//                        new ComparableStack(Items.PAPER, 24),
//                        new OreDictStack(CINNABAR.gem(), 24),
//                        new ComparableStack(Items.PUFFERFISH, 8))
//                .outputItems(new ChanceOutputMulti(
//                        new ChanceOutput(new ItemStack(NtmItems.BLUEPRINT_FOLDER_DISCOVER.get(), 1), 5),
//                        new ChanceOutput(new ItemStack(Items.PAPER, 24), 95))));
    }
}
