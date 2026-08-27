package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.BatteryPackItem.BatteryPackType;
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
        this.register(new GenericRecipe("chem.hydrogen").setupNamed(20, 400)
                .inputItems(new ComparableStack(Items.COAL))
                .inputFluids(new FluidStack(Fluids.WATER, 8_000))
                .outputFluids(new FluidStack(Fluids.HYDROGEN, 500)));

        this.register(new GenericRecipe("chem.hydrogencoke").setupNamed(20, 400)
                .inputItems(new ComparableStack(NtmItems.COKE_COAL.get()))
                .inputFluids(new FluidStack(Fluids.WATER, 8_000))
                .outputFluids(new FluidStack(Fluids.HYDROGEN, 500)));

        this.register(new GenericRecipe("chem.oxygen").setupNamed(20, 400)
                .inputFluids(new FluidStack(Fluids.AIR, 8_000))
                .outputFluids(new FluidStack(Fluids.OXYGEN, 500)));

        this.register(new GenericRecipe("chem.xenon").setupNamed(300, 1_000)
                .inputFluids(new FluidStack(Fluids.AIR, 16_000))
                .outputFluids(new FluidStack(Fluids.XENON, 50)));

        this.register(new GenericRecipe("chem.xenonoxy").setupNamed(20, 1_000)
                .inputFluids(new FluidStack(Fluids.AIR, 8_000), new FluidStack(Fluids.OXYGEN, 250))
                .outputFluids(new FluidStack(Fluids.XENON, 50))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + ".xenonoxy"));

        this.register(new GenericRecipe("chem.co2").setup(60, 100)
                .inputFluids(new FluidStack(Fluids.GAS, 1_000))
                .outputFluids(new FluidStack(Fluids.CARBONDIOXIDE, 1_000)));

        this.register(new GenericRecipe("chem.perfluoromethyl").setup(20, 100)
                .inputItems(new ComparableStack(NtmItems.FLUORITE.get()))
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 1_000), new FluidStack(Fluids.UNSATURATEDS, 500))
                .outputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 1_000)));

        this.register(new GenericRecipe("chem.cccentrifuge").setup(200, 100)
                .inputFluids(new FluidStack(Fluids.CHLOROCALCITE_CLEANED, 500), new FluidStack(Fluids.SULFURIC_ACID, 8_000))
                .outputFluids(new FluidStack(Fluids.POTASSIUM_CHLORIDE, 250), new FluidStack(Fluids.CALCIUM_CHLORIDE, 250)));

        this.register(new GenericRecipe("chem.ethanol").setupNamed(50, 100)
                .inputItems(new ComparableStack(Items.SUGAR, 10))
                .outputFluids(new FluidStack(Fluids.ETHANOL, 1_000)));

        this.register(new GenericRecipe("chem.biogas").setupNamed(60, 100)
                .inputItems(new ComparableStack(NtmItems.BIOMASS.get(), 16))
                .inputFluids(new FluidStack(Fluids.AIR, 4_000))
                .outputFluids(new FluidStack(Fluids.BIOGAS, 2_000)));

        this.register(new GenericRecipe("chem.biofuel").setupNamed(60, 100)
                .inputFluids(new FluidStack(Fluids.BIOGAS, 1_500), new FluidStack(Fluids.ETHANOL, 250))
                .outputFluids(new FluidStack(Fluids.BIOFUEL, 1_000)));

        this.register(new GenericRecipe("chem.reoil").setupNamed(40, 100)
                .inputFluids(new FluidStack(Fluids.SMEAR, 1_000))
                .outputFluids(new FluidStack(Fluids.RECLAIMED, 800)));

        this.register(new GenericRecipe("chem.gasoline").setupNamed(40, 100)
                .inputFluids(new FluidStack(Fluids.NAPHTHA, 1_000))
                .outputFluids(new FluidStack(Fluids.GASOLINE, 800)));

        this.register(new GenericRecipe("chem.coallube").setupNamed(40, 100)
                .inputFluids(new FluidStack(Fluids.COALCREOSOTE, 1_000))
                .outputFluids(new FluidStack(Fluids.LUBRICANT, 1_000))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + ".lube"));

        this.register(new GenericRecipe("chem.heavylube").setupNamed(40, 100)
                .inputFluids(new FluidStack(Fluids.HEAVYOIL, 2_000))
                .outputFluids(new FluidStack(Fluids.LUBRICANT, 1_000))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + ".lube"));

        this.register(new GenericRecipe("chem.tarsand").setupNamed(200, 100)
                .setIcon(NtmBlocks.ORE_OIL_SAND.get())
                .inputItems(new ComparableStack(NtmBlocks.ORE_OIL_SAND.get(), 16), new ComparableStack(NtmItems.TAR_OIL.get()))
                .outputItems(new ItemStack(Blocks.SAND, 16))
                .outputFluids(new FluidStack(Fluids.BITUMEN, 1_000)));

        this.register(new GenericRecipe("chem.cobble").setup(20, 100)
                .inputFluids(new FluidStack(Fluids.WATER, 1_000), new FluidStack(Fluids.LAVA, 25))
                .outputItems(new ItemStack(Blocks.COBBLESTONE)));

        this.register(new GenericRecipe("chem.stone").setup(60, 500)
                .inputFluids(new FluidStack(Fluids.WATER, 1_000), new FluidStack(Fluids.LAVA, 25), new FluidStack(Fluids.AIR, 4_000))
                .outputItems(new ItemStack(Blocks.STONE))
                .setPools(GenericRecipes.POOL_PREFIX_DISCOVER + ".stone"));

        this.register(new GenericRecipe("chem.obsidian").setup(60, 500)
                .inputFluids(new FluidStack(Fluids.WATER, 1_000), new FluidStack(Fluids.LAVA, 500), new FluidStack(Fluids.AIR, 4_000))
                .outputItems(new ItemStack(Blocks.OBSIDIAN))
                .setPools(GenericRecipes.POOL_PREFIX_DISCOVER + ".stone"));

        this.register(new GenericRecipe("chem.aggregate").setupNamed(320, 500)
                .inputItems(new ComparableStack(Blocks.COBBLESTONE, 16))
                .outputItems(new ItemStack(Blocks.GRAVEL, 8), new ItemStack(Blocks.SAND, 8))
                .setPools(GenericRecipes.POOL_PREFIX_DISCOVER + ".stone"));

        this.register(new GenericRecipe("chem.liquidconk").setup(100, 100)
                .inputItems(new ComparableStack(NtmItems.POWDER_CEMENT.get()), new ComparableStack(Blocks.GRAVEL, 8), new ComparableStack(Blocks.SAND, 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2_000))
                .outputFluids(new FluidStack(Fluids.CONCRETE, 16_000)));

        this.register(new GenericRecipe("chem.asphalt").setup(100, 100)
                .inputItems(new ComparableStack(Blocks.GRAVEL, 2), new ComparableStack(Blocks.SAND, 6))
                .inputFluids(new FluidStack(Fluids.BITUMEN, 1_000))
                .outputItems(new ItemStack(NtmBlocks.ASPHALT.get(), 16)));

        this.register(new GenericRecipe("chem.batterylead").setup(100, 100)
                .inputItems(new ComparableStack(NtmItems.PLATE_STEEL.get(), 4), new ComparableStack(NtmItems.INGOT_LEAD.get(), 4))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 8_000))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), BatteryPackType.BATTERY_LEAD)));

        this.register(new GenericRecipe("chem.batterylithium").setup(100, 1_000)
                .inputItems(new ComparableStack(NtmItems.POWDER_LITHIUM.get(), 12), new ComparableStack(NtmItems.POWDER_COBALT.get(), 8), new ComparableStack(NtmItems.INGOT_POLYMER.get(), 4))
                .inputFluids(new FluidStack(Fluids.OXYGEN, 2_000))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), BatteryPackType.BATTERY_LITHIUM)));

        this.register(new GenericRecipe("chem.batterysodium").setup(100, 10_000)
                .inputItems(new ComparableStack(NtmItems.POWDER_SODIUM.get(), 24), new ComparableStack(NtmItems.POWDER_IRON.get(), 24), new ComparableStack(NtmItems.INGOT_PC.get(), 12))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), BatteryPackType.BATTERY_SODIUM)));

        this.register(new GenericRecipe("chem.desh").setup(100, 100)
                .inputItems(new ComparableStack(NtmItems.POWDER_DESH_MIX.get()))
                .inputFluids(new FluidStack(Fluids.LIGHTOIL, 200), new FluidStack(Fluids.MERCURY, 200))
                .outputItems(new ItemStack(NtmItems.INGOT_DESH.get())));

        this.register(new GenericRecipe("chem.deshcracked").setupNamed(100, 100)
                .inputItems(new ComparableStack(NtmItems.POWDER_DESH_MIX.get()))
                .inputFluids(new FluidStack(Fluids.LIGHTOIL_CRACK, 500, 1), new FluidStack(Fluids.MERCURY, 100))
                .outputItems(new ItemStack(NtmItems.INGOT_DESH.get())));

        this.register(new GenericRecipe("chem.polymer").setup(100, 100)
                .inputItems(new ComparableStack(NtmItems.POWDER_COAL.get(), 2), new ComparableStack(NtmItems.FLUORITE.get()))
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 1_000))
                .outputItems(new ItemStack(NtmItems.INGOT_POLYMER.get(), 4)));

        this.register(new GenericRecipe("chem.bakelite").setup(100, 100)
                .inputFluids(new FluidStack(Fluids.AROMATICS, 500), new FluidStack(Fluids.PETROLEUM, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_BAKELITE.get())));

        this.register(new GenericRecipe("chem.rubber").setup(100, 200)
                .inputItems(new ComparableStack(NtmItems.SULFUR.get()))
                .inputFluids(new FluidStack(Fluids.UNSATURATEDS, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_RUBBER.get(), 2)));

        this.register(new GenericRecipe("chem.hardplastic").setup(100, 1_000)
                .inputFluids(new FluidStack(Fluids.XYLENE, 500), new FluidStack(Fluids.PHOSGENE, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_PC.get())));

        this.register(new GenericRecipe("chem.pvc").setup(100, 1_000)
                .inputItems(new ComparableStack(NtmItems.POWDER_CADMIUM.get()))
                .inputFluids(new FluidStack(Fluids.UNSATURATEDS, 250), new FluidStack(Fluids.CHLORINE, 250))
                .outputItems(new ItemStack(NtmItems.INGOT_PVC.get(), 2)));

        this.register(new GenericRecipe("chem.kevlar").setup(60, 300)
                .inputFluids(new FluidStack(Fluids.AROMATICS, 200), new FluidStack(Fluids.NITRIC_ACID, 100), new FluidStack(Fluids.CHLORINE, 100))
                .outputItems(new ItemStack(NtmItems.PLATE_KEVLAR.get(), 4)));

        this.register(new GenericRecipe("chem.epearl").setup(100, 300)
                .inputItems(new ComparableStack(NtmItems.POWDER_DIAMOND.get()))
                .inputFluids(new FluidStack(Fluids.XPJUICE, 500))
                .outputFluids(new FluidStack(Fluids.ENDERJUICE, 100)));

        this.register(new GenericRecipe("chem.biosolidfuel").setupNamed(40, 100)
                .inputItems(new ComparableStack(NtmItems.BIOMASS_COMPRESSED.get(), 4))
                .outputItems(new ItemStack(NtmItems.SOLID_FUEL.get()))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + ".biosolidfuel"));

        this.register(new GenericRecipe("chem.biooilsolidfuel").setupNamed(40, 100)
                .inputItems(new ComparableStack(NtmItems.BIOMASS_COMPRESSED.get(), 2))
                .inputFluids(new FluidStack(Fluids.HEATINGOIL, 100))
                .outputItems(new ItemStack(NtmItems.SOLID_FUEL.get()))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + ".biosolidfuel"));

        this.register(new GenericRecipe("chem.peroxide").setup(50, 100)
                .setIcon(FluidIconItem.make(Fluids.PEROXIDE, 10))
                .inputFluids(new FluidStack(Fluids.WATER, 1_000))
                .outputFluids(new FluidStack(Fluids.PEROXIDE, 1_000)));

        this.register(new GenericRecipe("chem.sulfuricacid").setup(50, 100)
                .setIcon(FluidIconItem.make(Fluids.SULFURIC_ACID, 10))
                .inputItems(new ComparableStack(NtmItems.SULFUR.get()))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 1_000), new FluidStack(Fluids.WATER, 1_000))
                .outputFluids(new FluidStack(Fluids.SULFURIC_ACID, 2_000)));

        this.register(new GenericRecipe("chem.nitricacid").setup(50, 100)
                .setIcon(FluidIconItem.make(Fluids.NITRIC_ACID, 10))
                .inputItems(new ComparableStack(NtmItems.NITER.get()))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 500))
                .outputFluids(new FluidStack(Fluids.NITRIC_ACID, 1_000)));

        this.register(new GenericRecipe("chem.birkeland").setupNamed(200, 5_000)
                .inputFluids(new FluidStack(Fluids.AIR, 8_000), new FluidStack(Fluids.WATER, 2_000))
                .outputFluids(new FluidStack(Fluids.NITRIC_ACID, 1_000))
                .setPools(GenericRecipes.POOL_PREFIX_ALT + ".birkeland"));

        this.register(new GenericRecipe("chem.schrabidate").setup(150, 5_000)
                .inputItems(new ComparableStack(NtmItems.POWDER_IRON.get()))
                .inputFluids(new FluidStack(Fluids.SCHRABIDIC, 250))
                .outputItems(new ItemStack(NtmItems.POWDER_SCHRABIDATE.get())));

        this.register(new GenericRecipe("chem.coltancleaning").setup(60, 100)
                .inputItems(new ComparableStack(NtmItems.POWDER_COLTAN_ORE.get(), 2), new ComparableStack(NtmItems.POWDER_COAL.get()))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 250), new FluidStack(Fluids.HYDROGEN, 500))
                .outputItems(new ItemStack(NtmItems.POWDER_COLTAN.get()), new ItemStack(NtmItems.POWDER_NIOBIUM.get()), new ItemStack(NtmItems.DUST.get()))
                .outputFluids(new FluidStack(Fluids.WATER, 500)));

        this.register(new GenericRecipe("chem.coltanpain").setup(120, 100)
                .inputItems(new ComparableStack(NtmItems.POWDER_COLTAN.get()), new ComparableStack(NtmItems.FLUORITE.get()))
                .inputFluids(new FluidStack(Fluids.GAS, 1_000), new FluidStack(Fluids.OXYGEN, 500))
                .outputFluids(new FluidStack(Fluids.PAIN, 1_000)));

        this.register(new GenericRecipe("chem.coltancrystal").setup(80, 100)
                .inputFluids(new FluidStack(Fluids.PAIN, 1_000), new FluidStack(Fluids.PEROXIDE, 500))
                .outputItems(new ItemStack(NtmItems.GEM_TANTALIUM.get()), new ItemStack(NtmItems.DUST.get(), 3))
                .outputFluids(new FluidStack(Fluids.WATER, 250)));

        this.register(new GenericRecipe("chem.cordite").setup(40, 100)
                .inputItems(new ComparableStack(NtmItems.NITER.get(), 2), new ComparableStack(NtmItems.POWDER_SAWDUST.get(), 2))
                .inputFluids(new FluidStack(Fluids.GAS, 200))
                .outputItems(new ItemStack(NtmItems.CORDITE.get(), 4)));

        this.register(new GenericRecipe("chem.rocketfuel").setup(200, 100)
                .inputItems(new ComparableStack(NtmItems.SOLID_FUEL.get(), 2))
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 200), new FluidStack(Fluids.NITRIC_ACID, 100))
                .outputItems(new ItemStack(NtmItems.ROCKET_FUEL.get(), 4)));

        this.register(new GenericRecipe("chem.dynamite").setup(50, 100)
                .inputItems(new ComparableStack(Items.SUGAR), new ComparableStack(NtmItems.NITER.get()), new ComparableStack(Blocks.SAND))
                .outputItems(new ItemStack(NtmItems.BALL_DYNAMITE.get(), 2)));

        this.register(new GenericRecipe("chem.tnt").setup(100, 1_000)
                .inputItems(new ComparableStack(NtmItems.NITER.get()))
                .inputFluids(new FluidStack(Fluids.AROMATICS, 500))
                .outputItems(new ItemStack(NtmItems.BALL_TNT.get(), 4)));

        this.register(new GenericRecipe("chem.tatb").setup(50, 5_000)
                .inputItems(new ComparableStack(NtmItems.BALL_TNT.get()))
                .inputFluids(new FluidStack(Fluids.SOURGAS, 200, 1), new FluidStack(Fluids.NITRIC_ACID, 10))
                .outputItems(new ItemStack(NtmItems.BALL_TATB.get())));

        this.register(new GenericRecipe("chem.c4").setup(100, 1_000)
                .inputItems(new ComparableStack(NtmItems.NITER.get()))
                .inputFluids(new FluidStack(Fluids.UNSATURATEDS, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_C4.get(), 4)));

        this.register(new GenericRecipe("chem.yellowcake").setup(250, 500)
                .inputItems(new ComparableStack(NtmItems.BILLET_URANIUM.get(), 2), new ComparableStack(NtmItems.SULFUR.get(), 2))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 500))
                .outputItems(new ItemStack(NtmItems.POWDER_YELLOWCAKE.get())));

        this.register(new GenericRecipe("chem.uf6").setup(100, 500)
                .setIcon(FluidIconItem.make(Fluids.UF6, 10))
                .inputItems(new ComparableStack(NtmItems.POWDER_YELLOWCAKE.get()), new ComparableStack(NtmItems.FLUORITE.get(), 4))
                .inputFluids(new FluidStack(Fluids.WATER, 1_000))
                .outputItems(new ItemStack(NtmItems.SULFUR.get(), 2))
                .outputFluids(new FluidStack(Fluids.UF6, 1_200)));

        this.register(new GenericRecipe("chem.puf6").setup(200, 500)
                .inputItems(new ComparableStack(NtmItems.POWDER_PLUTONIUM.get()), new ComparableStack(NtmItems.FLUORITE.get(), 3))
                .inputFluids(new FluidStack(Fluids.WATER, 1_000))
                .outputFluids(new FluidStack(Fluids.PUF6, 900)));

        this.register(new GenericRecipe("chem.sas3").setup(200, 5_000)
                .inputItems(new ComparableStack(NtmItems.POWDER_SCHRABIDIUM.get()), new ComparableStack(NtmItems.SULFUR.get(), 2))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 2_000))
                .outputFluids(new FluidStack(Fluids.SAS3, 1_000)));

        this.register(new GenericRecipe("chem.balefire").setup(100, 10_000)
                .setIcon(FluidIconItem.make(Fluids.BALEFIRE, 10))
                .inputItems(new ComparableStack(NtmItems.EGG_BALEFIRE_SHARD.get()))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 6_000))
                .outputItems(new ItemStack(NtmItems.POWDER_BALEFIRE.get()))
                .outputFluids(new FluidStack(Fluids.BALEFIRE, 8_000)));

        this.register(new GenericRecipe("chem.dhc").setup(400, 500)
                .setIcon(FluidIconItem.make(Fluids.DHC, 10))
                .inputFluids(new FluidStack(Fluids.DEUTERIUM, 500), new FluidStack(Fluids.REFORMGAS, 250), new FluidStack(Fluids.SYNGAS, 250))
                .outputFluids(new FluidStack(Fluids.DHC, 500)));

        this.register(new GenericRecipe("chem.osmiridiumdeath").setup(240, 1_000)
                .inputItems(new ComparableStack(NtmItems.POWDER_PALEOGENITE.get()), new ComparableStack(NtmItems.FLUORITE.get(), 8), new ComparableStack(NtmItems.NUGGET_BISMUTH.get(), 4))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 1_000, 5))
                .outputFluids(new FluidStack(Fluids.DEATH, 1_000)));

        // Recettes propres au port actuel, conservées.
        this.register(new GenericRecipe("chem.boytarget").setup(200, 100)
                .outputItems(new ItemStack(NtmItems.LITTLE_BOY_TARGET.get()))
                .inputItems(new ComparableStack(NtmItems.INGOT_URANIUM.get(), 18)));
        this.register(new GenericRecipe("chem.grass_block").setup(200, 100)
                .outputItems(new ItemStack(Blocks.GRASS_BLOCK))
                .inputItems(new ComparableStack(NtmItems.SINGULARITY.get()), new ComparableStack(NtmItems.SINGULARITY_COUNTER_RESONANT.get()), new ComparableStack(NtmItems.MISSILE_DOOMSDAY_RUSTED.get())));
        this.register(new GenericRecipe("chem.man").setup(200, 100)
                .outputItems(new ItemStack(NtmBlocks.NUKE_FAT_MAN.get()))
                .inputItems(new ComparableStack(NtmItems.PELLET_ANTIMATTER.get())));
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
                .outputItems(new ItemStack(NtmBlocks.BRICK_CONCRETE.get()))
                .inputItems(new ComparableStack(Items.WATER_BUCKET)));
    }

    /*
     * Recettes originales conservées mais désactivées :
     * au moins un objet ou bloc requis n'existe pas encore dans ce port.
     * this.register(new GenericRecipe("chem.helium3").setupNamed(25, 2_000).setIcon(ModItems.gas_full, Fluids.HELIUM3.getID())
     * 				.inputItems(new ComparableStack(ModBlocks.moon_turf, 1))
     * 				.outputFluids(new FluidStack(Fluids.HELIUM3, 500)));
     *
     * this.register(new GenericRecipe("chem.tel").setup(40, 100)
     * 				.inputItems(new OreDictStack(ANY_TAR.any()), new OreDictStack(PB.dust()))
     * 				.inputFluids(new FluidStack(Fluids.PETROLEUM, 100), new FluidStack(Fluids.STEAM, 1000))
     * 				.outputItems(DictFrame.fromOne(ModItems.fuel_additive, EnumFuelAdditive.ANTIKNOCK)));
     *
     * this.register(new GenericRecipe("chem.deicer").setup(40, 100)
     * 				.inputFluids(new FluidStack(Fluids.GAS, 100), new FluidStack(Fluids.HYDROGEN, 50))
     * 				.outputItems(DictFrame.fromOne(ModItems.fuel_additive, EnumFuelAdditive.DEICER)));
     *
     * this.register(new GenericRecipe("chem.concrete").setup(100, 100)
     * 				.inputItems(new ComparableStack(ModItems.powder_cement, 1), new ComparableStack(Blocks.gravel, 8), new OreDictStack(KEY_SAND, 8))
     * 				.inputFluids(new FluidStack(Fluids.WATER, 2_000))
     * 				.outputItems(new ItemStack(ModBlocks.concrete_smooth, 16)));
     *
     * this.register(new GenericRecipe("chem.concreteasbestos").setup(100, 100)
     * 				.inputItems(new ComparableStack(ModItems.powder_cement, 4), new OreDictStack(ASBESTOS.ingot(), (GeneralConfig.enableLBSM && GeneralConfig.enableLBSMSimpleChemsitry) ? 1 : 4), new OreDictStack(KEY_SAND, 8))
     * 				.inputFluids(new FluidStack(Fluids.WATER, 2_000))
     * 				.outputItems(new ItemStack(ModBlocks.concrete_asbestos, 16)));
     *
     * this.register(new GenericRecipe("chem.ducrete").setup(150, 100)
     * 				.inputItems(new ComparableStack(ModItems.powder_cement, 4), new OreDictStack(FERRO.ingot()), new OreDictStack(KEY_SAND, 8))
     * 				.inputFluids(new FluidStack(Fluids.WATER, 2_000))
     * 				.outputItems(new ItemStack(ModBlocks.ducrete_smooth, 8)));
     *
     * this.register(new GenericRecipe("chem.batteryschrabidium").setup(100, 25_000)
     * 				.inputItems(new OreDictStack(SA326.dust(), 24),
     * 						new OreDictStack(ANY_BISMOIDBRONZE.plateCast(), 8))
     * 				.inputFluids(new FluidStack(Fluids.HELIUM4, 8_000))
     * 				.outputItems(new ItemStack(ModItems.battery_pack, 1, EnumBatteryPack.BATTERY_SCHRABIDIUM.ordinal())));
     *
     * this.register(new GenericRecipe("chem.batteryquantum").setup(100, 100_000)
     * 				.inputItems(new OreDictStack(BSCCO.wireDense(), 24),
     * 						new ComparableStack(ModItems.pellet_charged, 32),
     * 						new ComparableStack(ModItems.ingot_cft, 16))
     * 				.inputFluids(new FluidStack(Fluids.PERFLUOROMETHYL_COLD, 8_000))
     * 				.outputItems(new ItemStack(ModItems.battery_pack, 1, EnumBatteryPack.BATTERY_QUANTUM.ordinal()))
     * 				.outputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 8_000)));
     *
     * this.register(new GenericRecipe("chem.meth").setup(60, 300)
     * 				.inputItems(new ComparableStack(Items.wheat), new ComparableStack(Items.dye, 2, 3))
     * 				.inputFluids(new FluidStack(Fluids.LUBRICANT, 400), new FluidStack(Fluids.PEROXIDE, 500))
     * 				.outputItems(new ItemStack(ModItems.chocolate, 4)));
     *
     * this.register(new GenericRecipe("chem.meatprocessing").setupNamed(200, 200).setIcon(ModItems.glyphid_meat)
     * 				.inputItems(new OreDictStack(KEY_GLYPHID_MEAT, 3))
     * 				.inputFluids(new FluidStack(Fluids.WATER, 1_000))
     * 				.outputItems(new ItemStack(ModItems.sulfur, 4), new ItemStack(ModItems.niter, 3))
     * 				.outputFluids(new FluidStack(Fluids.SALIENT, 250)));
     *
     * this.register(new GenericRecipe("chem.rustysteel").setup(40, 100)
     * 				.inputItems(new ComparableStack(ModBlocks.deco_steel, 8))
     * 				.inputFluids(new FluidStack(Fluids.WATER, 1000))
     * 				.outputItems(new ItemStack(ModBlocks.deco_rusty_steel, 8)));
     *
     * this.register(new GenericRecipe("chem.oilelectrodes").setupNamed(600, 100)
     * 				.inputFluids(new FluidStack(Fluids.HEATINGOIL, 4_000))
     * 				.outputItems(new ItemStack(ModItems.arc_electrode, 1)).setPools(GenericRecipes.POOL_PREFIX_ALT + ".electrodes"));
     *
     * this.register(new GenericRecipe("chem.lubeelectrodes").setupNamed(600, 100)
     * 				.inputFluids(new FluidStack(Fluids.LUBRICANT, 8_000))
     * 				.outputItems(new ItemStack(ModItems.arc_electrode, 1)).setPools(GenericRecipes.POOL_PREFIX_ALT + ".electrodes"));
     *
     * this.register(new GenericRecipe("chem.schrabidic").setup(60, 5_000)
     * 				.inputItems(new ComparableStack(ModItems.pellet_charged))
     * 				.inputFluids(new FluidStack(Fluids.SAS3, 2000), new FluidStack(Fluids.PEROXIDE, 2000))
     * 				.outputFluids(new FluidStack(Fluids.SCHRABIDIC, 2000)));
     *
     * this.register(new GenericRecipe("chem.napalm").setup(40, 100)
     * 				.inputItems(new ComparableStack(ModItems.canister_empty))
     * 				.inputFluids(new FluidStack(Fluids.GASOLINE, 100), new FluidStack(Fluids.AROMATICS, 50)) // aromatics to emulate polystyrene
     * 				.outputItems(new ItemStack(ModItems.canister_napalm, 1)));
     *
     * this.register(new GenericRecipe("chem.laminate").setup(20, 100)
     * 				.inputFluids(new FluidStack(Fluids.XYLENE, 50), new FluidStack(Fluids.PHOSGENE, 50))
     * 				.inputItems(new OreDictStack(KEY_ANYGLASS), new OreDictStack(STEEL.bolt(), 4))
     * 				.outputItems(new ItemStack(ModBlocks.reinforced_laminate)));
     *
     * this.register(new GenericRecipe("chem.polarized").setup(100, 500)
     * 				.inputFluids(new FluidStack(Fluids.PETROLEUM, 1_000))
     * 				.inputItems(new OreDictStack(KEY_ANYPANE))
     * 				.outputItems(DictFrame.fromOne(ModItems.part_generic, EnumPartType.GLASS_POLARIZED, 16)));
     */
}
