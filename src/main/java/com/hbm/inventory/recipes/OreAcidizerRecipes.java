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

        // Minerais originaux désormais disponibles dans le port.
        this.registerOre(NtmBlocks.ORE_URANIUM_H.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_URANIUM_DEEPSLATE.get(), NtmItems.CRYSTAL_URANIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_THORIUM.get(), NtmItems.CRYSTAL_THORIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_THORIUM_DEEPSLATE.get(), NtmItems.CRYSTAL_THORIUM.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_TITANIUM.get(), NtmItems.CRYSTAL_TITANIUM.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_TITANIUM_DEEPSLATE.get(), NtmItems.CRYSTAL_TITANIUM.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_SULFUR.get(), NtmItems.CRYSTAL_SULFUR.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_DEEPSLATE_SULFUR.get(), NtmItems.CRYSTAL_SULFUR.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_DEEPSLATE_NITER.get(), NtmItems.CRYSTAL_NITER.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_TUNGSTEN.get(), NtmItems.CRYSTAL_TUNGSTEN.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_DEEPSLATE_TUNGSTEN.get(), NtmItems.CRYSTAL_TUNGSTEN.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_ALUMINUM.get(), NtmItems.CRYSTAL_ALUMINIUM.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_ALUMINUM_DEEPSLATE.get(), NtmItems.CRYSTAL_ALUMINIUM.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_FLUORITE.get(), NtmItems.CRYSTAL_FLUORITE.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_DEEPSLATE_FLUORITE.get(), NtmItems.CRYSTAL_FLUORITE.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_BERYLLIUM.get(), NtmItems.CRYSTAL_BERYLLIUM.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_BERYLLIUM_DEEPSLATE.get(), NtmItems.CRYSTAL_BERYLLIUM.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_LEAD.get(), NtmItems.CRYSTAL_LEAD.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_LEAD_DEEPSLATE.get(), NtmItems.CRYSTAL_LEAD.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_COBALT.get(), NtmItems.CRYSTAL_COBALT.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_COBALT_DEEPSLATE.get(), NtmItems.CRYSTAL_COBALT.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_RAREGROUND.get(), NtmItems.CRYSTAL_RARE.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_RAREGROUND_DEEPSLATE.get(), NtmItems.CRYSTAL_RARE.get(), baseTime, sulfuric);
        this.registerOre(NtmBlocks.ORE_CINNABAR.get(), NtmItems.CRYSTAL_CINNABAR.get(), baseTime, peroxide);
        this.registerOre(NtmBlocks.ORE_CINNABAR_DEEPSLATE.get(), NtmItems.CRYSTAL_CINNABAR.get(), baseTime, peroxide);

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

        // Transformations originales dont tous les composants existent.
        this.registerRecipe(new ComparableStack(NtmItems.INGOT_SCHRARANIUM.get()),
                new OreAcidizerRecipe(new ItemStack(NtmItems.CRYSTAL_SCHRARANIUM.get()), baseTime).prod(0.05F));
        this.registerRecipe(new ComparableStack(NtmItems.INGOT_SILICON.get()),
                new OreAcidizerRecipe(new ItemStack(net.minecraft.world.item.Items.QUARTZ, 2), utilityTime).prod(0.1F),
                new FluidStack(Fluids.OXYGEN, 250));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_BORAX.get()),
                new OreAcidizerRecipe(new ItemStack(NtmItems.POWDER_BORON_TINY.get(), 3), baseTime).prod(0.25F),
                new FluidStack(Fluids.SULFURIC_ACID, 500));
        this.registerRecipe(new ComparableStack(net.minecraft.world.item.Items.BONE_MEAL),
                new OreAcidizerRecipe(new ItemStack(net.minecraft.world.item.Items.SLIME_BALL, 4), mixingTime),
                new FluidStack(Fluids.SULFURIC_ACID, 250));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_ASH_FULLERENE.get()),
                new OreAcidizerRecipe(new ItemStack(NtmItems.INGOT_CTF.get()), baseTime).prod(0.1F).setReq(4),
                new FluidStack(Fluids.XYLENE, 1_000));
        this.registerRecipe(new ComparableStack(NtmItems.POWDER_CADMIUM.get()),
                new OreAcidizerRecipe(new ItemStack(NtmItems.INGOT_RUBBER.get(), 16), utilityTime),
                new FluidStack(Fluids.FISHOIL, 4_000));
    }

    // Catalogue original complet. Les entrées qui ne sont pas enregistrées ci-dessus
    // restent volontairement commentées tant que leurs objets, blocs ou systèmes manquent.
    // public void registerDefaults() {
    //
    // 		final int baseTime = 600;
    // 		final int utilityTime = 100;
    // 		final int mixingTime = 20;
    // 		FluidStack sulfur = new FluidStack(Fluids.SULFURIC_ACID, 500);
    //
    // 		registerRecipe(new OreDictStack(COAL.ore()),		new CrystallizerRecipe(ModItems.crystal_coal, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(IRON.ore()),		new CrystallizerRecipe(ModItems.crystal_iron, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(GOLD.ore()),		new CrystallizerRecipe(ModItems.crystal_gold, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(REDSTONE.ore()),	new CrystallizerRecipe(ModItems.crystal_redstone, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(LAPIS.ore()),		new CrystallizerRecipe(ModItems.crystal_lapis, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(DIAMOND.ore()),		new CrystallizerRecipe(ModItems.crystal_diamond, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(U.ore()),			new CrystallizerRecipe(ModItems.crystal_uranium, baseTime).prod(0.05F), sulfur);
    // 		for(String ore : OreDictManager.TH232.all(MaterialShapes.ONLY_ORE)) registerRecipe(new OreDictStack(ore),	new CrystallizerRecipe(ModItems.crystal_thorium, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new OreDictStack(PU.ore()),			new CrystallizerRecipe(ModItems.crystal_plutonium, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new OreDictStack(TI.ore()),			new CrystallizerRecipe(ModItems.crystal_titanium, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new OreDictStack(S.ore()),			new CrystallizerRecipe(ModItems.crystal_sulfur, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(KNO.ore()),			new CrystallizerRecipe(ModItems.crystal_niter, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(CU.ore()),			new CrystallizerRecipe(ModItems.crystal_copper, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(W.ore()),			new CrystallizerRecipe(ModItems.crystal_tungsten, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new OreDictStack(AL.ore()),			new CrystallizerRecipe(ModItems.crystal_aluminium, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(F.ore()),			new CrystallizerRecipe(ModItems.crystal_fluorite, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(BE.ore()),			new CrystallizerRecipe(ModItems.crystal_beryllium, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(PB.ore()),			new CrystallizerRecipe(ModItems.crystal_lead, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(SA326.ore()),		new CrystallizerRecipe(ModItems.crystal_schrabidium, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new OreDictStack(LI.ore()),			new CrystallizerRecipe(ModItems.crystal_lithium, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new OreDictStack(CO.ore()),			new CrystallizerRecipe(ModItems.crystal_cobalt, baseTime).prod(0.05F), sulfur);
    //
    // 		registerRecipe(new ComparableStack(ModItems.powder_calcium),	new CrystallizerRecipe(new ItemStack(ModItems.powder_cement, 8), utilityTime).prod(0.1F), new FluidStack(Fluids.REDMUD, 75));
    // 		registerRecipe(new OreDictStack(MALACHITE.ingot()),				new CrystallizerRecipe(ItemScraps.create(new MaterialStack(Mats.MAT_COPPER, MaterialShapes.INGOT.q(1))), 300).prod(0.1F), new FluidStack(Fluids.SULFURIC_ACID, 250));
    //
    // 		registerRecipe(new OreDictStack("oreRareEarth"),	new CrystallizerRecipe(ModItems.crystal_rare, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new OreDictStack("oreCinnabar"),		new CrystallizerRecipe(ModItems.crystal_cinnebar, baseTime).prod(0.05F));
    //
    // 		registerRecipe(new ComparableStack(ModBlocks.ore_nether_fire),	new CrystallizerRecipe(ModItems.crystal_phosphorus, baseTime).prod(0.05F));
    // 		registerRecipe(new ComparableStack(ModBlocks.ore_tikite),		new CrystallizerRecipe(ModItems.crystal_trixite, baseTime).prod(0.05F), sulfur);
    // 		registerRecipe(new ComparableStack(ModBlocks.gravel_diamond),	new CrystallizerRecipe(ModItems.crystal_diamond, baseTime).prod(0.05F));
    // 		registerRecipe(new OreDictStack(SRN.ingot()),					new CrystallizerRecipe(ModItems.crystal_schraranium, baseTime).prod(0.05F));
    //
    // 		registerRecipe(new OreDictStack(KEY_SAND),				new CrystallizerRecipe(ModItems.ingot_fiberglass, utilityTime).prod(0.15F));
    // 		registerRecipe(new OreDictStack(SI.ingot()),			new CrystallizerRecipe(new ItemStack(Items.quartz, 2), utilityTime).prod(0.1F), new FluidStack(Fluids.OXYGEN, 250));
    // 		registerRecipe(new OreDictStack(REDSTONE.block()),		new CrystallizerRecipe(ModItems.ingot_mercury, baseTime).prod(0.25F));
    // 		registerRecipe(new OreDictStack(CINNABAR.crystal()),	new CrystallizerRecipe(new ItemStack(ModItems.ingot_mercury, 3), baseTime).prod(0.25F));
    // 		registerRecipe(new OreDictStack(BORAX.dust()),			new CrystallizerRecipe(new ItemStack(ModItems.powder_boron_tiny, 3), baseTime).prod(0.25F), sulfur);
    // 		registerRecipe(new OreDictStack(COAL.block()),			new CrystallizerRecipe(ModBlocks.block_graphite, baseTime));
    //
    // 		registerRecipe(new ComparableStack(Blocks.cobblestone),			new CrystallizerRecipe(ModBlocks.reinforced_stone, utilityTime));
    // 		registerRecipe(new ComparableStack(ModBlocks.gravel_obsidian),	new CrystallizerRecipe(ModBlocks.brick_obsidian, utilityTime));
    // 		registerRecipe(new ComparableStack(Items.rotten_flesh),			new CrystallizerRecipe(Items.leather, utilityTime).prod(0.25F));
    // 		registerRecipe(new ComparableStack(ModItems.coal_infernal),		new CrystallizerRecipe(ModItems.solid_fuel, utilityTime));
    // 		registerRecipe(new ComparableStack(ModBlocks.stone_gneiss),		new CrystallizerRecipe(ModItems.powder_lithium, utilityTime).prod(0.25F));
    // 		registerRecipe(new ComparableStack(Items.dye, 1, 15),			new CrystallizerRecipe(new ItemStack(Items.slime_ball, 4), mixingTime), new FluidStack(Fluids.SULFURIC_ACID, 250));
    // 		registerRecipe(new ComparableStack(Items.bone),					new CrystallizerRecipe(new ItemStack(Items.slime_ball, 16), mixingTime), new FluidStack(Fluids.SULFURIC_ACID, 1_000));
    // 		registerRecipe(new ComparableStack(DictFrame.fromOne(ModItems.plant_item, EnumPlantType.MUSTARDWILLOW)), new CrystallizerRecipe(new ItemStack(ModItems.powder_cadmium), 100).setReq(10), new FluidStack(Fluids.RADIOSOLVENT, 250));
    // 		registerRecipe(new ComparableStack(ModItems.scrap_oil),			new CrystallizerRecipe(new ItemStack(ModItems.nugget_arsenic), 100).prod(0.3F).setReq(16), new FluidStack(Fluids.RADIOSOLVENT, 100));
    // 		registerRecipe(new ComparableStack(DictFrame.fromOne(ModItems.powder_ash, EnumAshType.FULLERENE)), new CrystallizerRecipe(new ItemStack(ModItems.ingot_cft), baseTime).prod(0.1F).setReq(4), new FluidStack(Fluids.XYLENE, 1_000));
    //
    // 		registerRecipe(new OreDictStack(DIAMOND.dust()), 				new CrystallizerRecipe(Items.diamond, utilityTime));
    // 		registerRecipe(new OreDictStack(EMERALD.dust()), 				new CrystallizerRecipe(Items.emerald, utilityTime));
    // 		registerRecipe(new OreDictStack(LAPIS.dust()),					new CrystallizerRecipe(new ItemStack(Items.dye, 1, 4), utilityTime));
    // 		registerRecipe(new ComparableStack(ModItems.powder_semtex_mix),	new CrystallizerRecipe(ModItems.ingot_semtex, baseTime));
    // 		registerRecipe(new ComparableStack(ModItems.powder_desh_ready),	new CrystallizerRecipe(ModItems.ingot_desh, baseTime));
    // 		registerRecipe(new ComparableStack(ModItems.powder_meteorite),	new CrystallizerRecipe(ModItems.fragment_meteorite, utilityTime));
    // 		registerRecipe(new OreDictStack(CD.dust()),						new CrystallizerRecipe(new ItemStack(ModItems.ingot_rubber, 16), utilityTime), new FluidStack(Fluids.FISHOIL, 4_000));
    // 		registerRecipe(new OreDictStack(LATEX.ingot()),					new CrystallizerRecipe(ModItems.ingot_rubber, mixingTime).prod(0.15F), new FluidStack(Fluids.SOURGAS, 25));
    // 		registerRecipe(new ComparableStack(ModItems.powder_sawdust),	new CrystallizerRecipe(ModItems.cordite, mixingTime).prod(0.25F), new FluidStack(Fluids.NITROGLYCERIN, 250));
    // 		registerRecipe(new ComparableStack(ModBlocks.rebar),			new CrystallizerRecipe(ModBlocks.concrete_rebar, 10), new FluidStack(Fluids.CONCRETE, 1_000));
    //
    // 		registerRecipe(new ComparableStack(ModItems.meteorite_sword_treated),	new CrystallizerRecipe(ModItems.meteorite_sword_etched, baseTime));
    // 		registerRecipe(new ComparableStack(ModItems.powder_impure_osmiridium),	new CrystallizerRecipe(ModItems.crystal_osmiridium, baseTime), new FluidStack(Fluids.SCHRABIDIC, 1_000));
    //
    // 		for(int i = 0; i < ScrapType.values().length; i++) {
    // 			registerRecipe(new ComparableStack(ModItems.scrap_plastic, 1, i), new CrystallizerRecipe(new ItemStack(ModItems.circuit_star_piece, 1, i), baseTime));
    // 		}
    //
    // 		FluidStack nitric = new FluidStack(Fluids.NITRIC_ACID, 500);
    // 		FluidStack organic = new FluidStack(Fluids.SOLVENT, 500);
    // 		FluidStack hiperf = new FluidStack(Fluids.RADIOSOLVENT, 500);
    //
    // 		int oreTime = 200;
    //
    // 		for(EnumBedrockOre ore : EnumBedrockOre.values()) {
    // 			int i = ore.ordinal();
    //
    // 			registerRecipe(new ComparableStack(ModItems.ore_centrifuged, 1, i),			new CrystallizerRecipe(new ItemStack(ModItems.ore_cleaned, 1, i), oreTime));
    // 			registerRecipe(new ComparableStack(ModItems.ore_separated, 1, i),			new CrystallizerRecipe(new ItemStack(ModItems.ore_purified, 1, i), oreTime), sulfur);
    // 			registerRecipe(new ComparableStack(ModItems.ore_separated, 1, i),			new CrystallizerRecipe(new ItemStack(ModItems.ore_nitrated, 1, i), oreTime), nitric);
    // 			registerRecipe(new ComparableStack(ModItems.ore_nitrocrystalline, 1, i),	new CrystallizerRecipe(new ItemStack(ModItems.ore_deepcleaned, 1, i), oreTime), organic);
    // 			registerRecipe(new ComparableStack(ModItems.ore_nitrocrystalline, 1, i),	new CrystallizerRecipe(new ItemStack(ModItems.ore_seared, 1, i), oreTime), hiperf);
    // 		}
    //
    // 		int bedrock = 200;
    // 		int washing = 100;
    // 		for(BedrockOreType type : BedrockOreType.values()) {
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.BASE, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.BASE_WASHED, type), washing), new FluidStack(Fluids.WATER, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.BASE_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.BASE_WASHED, type), washing), new FluidStack(Fluids.WATER, 250));
    //
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SULFURIC, type), bedrock), new FluidStack(Fluids.SULFURIC_ACID, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SULFURIC, type), bedrock), new FluidStack(Fluids.SULFURIC_ACID, 250));
    //
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SOLVENT, type), bedrock), new FluidStack(Fluids.SOLVENT, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SOLVENT, type), bedrock), new FluidStack(Fluids.SOLVENT, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSULFURIC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SOLVENT, type), bedrock), new FluidStack(Fluids.SOLVENT, 250));
    //
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_RAD, type), bedrock), new FluidStack(Fluids.RADIOSOLVENT, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_RAD, type), bedrock), new FluidStack(Fluids.RADIOSOLVENT, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSULFURIC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_RAD, type), bedrock), new FluidStack(Fluids.RADIOSOLVENT, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSOLVENT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_RAD, type), bedrock), new FluidStack(Fluids.RADIOSOLVENT, 250));
    //
    // 			int sulf = 4;
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_BYPRODUCT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_WASHED, type), washing).setReq(sulf), new FluidStack(Fluids.WATER, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_WASHED, type), washing).setReq(sulf), new FluidStack(Fluids.WATER, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_ARC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_WASHED, type), washing).setReq(sulf), new FluidStack(Fluids.WATER, 250));
    //
    // 			int solv = 4;
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_BYPRODUCT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_WASHED, type), washing).setReq(solv), new FluidStack(Fluids.WATER, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_WASHED, type), washing).setReq(solv), new FluidStack(Fluids.WATER, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_ARC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_WASHED, type), washing).setReq(solv), new FluidStack(Fluids.WATER, 250));
    //
    // 			int rad = 4;
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.RAD_BYPRODUCT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.RAD_WASHED, type), washing).setReq(rad), new FluidStack(Fluids.WATER, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.RAD_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.RAD_WASHED, type), washing).setReq(rad), new FluidStack(Fluids.WATER, 250));
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.RAD_ARC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.RAD_WASHED, type), washing).setReq(rad), new FluidStack(Fluids.WATER, 250));
    //
    // 			FluidStack primary = new FluidStack(Fluids.HYDROGEN, 250);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SULFURIC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSULFURIC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SOLVENT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSOLVENT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_RAD, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NORAD, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type), bedrock), primary);
    //
    // 			FluidStack secondary = new FluidStack(Fluids.CHLORINE, 250);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_ROASTED, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SULFURIC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSULFURIC, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SOLVENT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSOLVENT, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_RAD, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NORAD, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type), bedrock), secondary);
    //
    // 			registerRecipe(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type)), new CrystallizerRecipe(ItemBedrockOreNew.make(BedrockOreGrade.BASE, type), bedrock).setReq(64), new FluidStack(Fluids.SLOP, 1000));
    // 		}
    //
    // 		FluidStack[] dyes = new FluidStack[] {new FluidStack(Fluids.WOODOIL, 100), new FluidStack(Fluids.FISHOIL, 100), new FluidStack(Fluids.LIGHTOIL, 100)};
    // 		for(FluidStack dye : dyes) {
    // 			registerRecipe(new OreDictStack(COAL.dust()),	new CrystallizerRecipe(DictFrame.fromOne(ModItems.chemical_dye, EnumChemDye.BLACK, 4), mixingTime).prod(0.15F), dye);
    // 			registerRecipe(new OreDictStack(TI.dust()),		new CrystallizerRecipe(DictFrame.fromOne(ModItems.chemical_dye, EnumChemDye.WHITE, 4), mixingTime).prod(0.15F), dye);
    // 			registerRecipe(new OreDictStack(IRON.dust()),	new CrystallizerRecipe(DictFrame.fromOne(ModItems.chemical_dye, EnumChemDye.RED, 4), mixingTime).prod(0.15F), dye);
    // 			registerRecipe(new OreDictStack(W.dust()),		new CrystallizerRecipe(DictFrame.fromOne(ModItems.chemical_dye, EnumChemDye.YELLOW, 4), mixingTime).prod(0.15F), dye);
    // 			registerRecipe(new OreDictStack(CU.dust()),		new CrystallizerRecipe(DictFrame.fromOne(ModItems.chemical_dye, EnumChemDye.GREEN, 4), mixingTime).prod(0.15F), dye);
    // 			registerRecipe(new OreDictStack(CO.dust()),		new CrystallizerRecipe(DictFrame.fromOne(ModItems.chemical_dye, EnumChemDye.BLUE, 4), mixingTime).prod(0.15F), dye);
    // 		}
    //
    // 		registerRecipe(new ComparableStack(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRUDE)),		new CrystallizerRecipe(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.WAX), 20),	new FluidStack(Fluids.CHLORINE, 250));
    // 		registerRecipe(new ComparableStack(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRACK)),		new CrystallizerRecipe(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.WAX), 20),	new FluidStack(Fluids.CHLORINE, 100));
    // 		registerRecipe(new ComparableStack(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.PARAFFIN)),	new CrystallizerRecipe(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.WAX), 20),	new FluidStack(Fluids.CHLORINE, 100));
    // 		registerRecipe(new ComparableStack(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.WAX)), 		new CrystallizerRecipe(new ItemStack(ModItems.pellet_charged), 200), 				new FluidStack(Fluids.IONGEL, 500));
    // 		registerRecipe(new ComparableStack(DictFrame.fromOne(ModItems.oil_tar, EnumTarType.PARAFFIN)), 	new CrystallizerRecipe(new ItemStack(ModItems.pill_red), 200), 						new FluidStack(Fluids.ESTRADIOL, 250));
    //
    // 		registerRecipe(new OreDictStack(KEY_SAND), new CrystallizerRecipe(Blocks.clay, 20), new FluidStack(Fluids.COLLOID, 1_000));
    // 		registerRecipe(new ComparableStack(ModBlocks.sand_mix, 1, EnumSandType.QUARTZ), new CrystallizerRecipe(new ItemStack(ModItems.ball_dynamite, 16), 20), new FluidStack(Fluids.NITROGLYCERIN, 1_000));
    // 		registerRecipe(new OreDictStack(NETHERQUARTZ.dust()), new CrystallizerRecipe(new ItemStack(ModItems.ball_dynamite, 4), 20), new FluidStack(Fluids.NITROGLYCERIN, 250));
    //
    // 		/// COMPAT CERTUS QUARTZ ///
    // 		List<ItemStack> quartz = OreDictionary.getOres("crystalCertusQuartz");
    // 		if(quartz != null && !quartz.isEmpty()) {
    // 			ItemStack qItem = quartz.get(0).copy();
    // 			qItem.stackSize = 12;
    // 			registerRecipe(new OreDictStack("oreCertusQuartz"), new CrystallizerRecipe(qItem, baseTime));
    // 		}
    //
    // 		/// COMPAT WHITE PHOSPHORUS DUST ///
    // 		List<ItemStack> dustWhitePhosphorus = OreDictionary.getOres(P_WHITE.dust());
    // 		if(dustWhitePhosphorus != null && !dustWhitePhosphorus.isEmpty()) {
    // 			registerRecipe(new OreDictStack(P_WHITE.dust()), new CrystallizerRecipe(new ItemStack(ModItems.ingot_phosphorus), utilityTime), new FluidStack(Fluids.AROMATICS, 50));
    // 		}
    //
    // 		/// COMPAT CINNABAR DUST ///
    // 		List<ItemStack> dustCinnabar = OreDictionary.getOres(CINNABAR.dust());
    // 		if(dustCinnabar != null && !dustCinnabar.isEmpty()) {
    // 			registerRecipe(new OreDictStack(CINNABAR.dust()), new CrystallizerRecipe(new ItemStack(ModItems.cinnebar), utilityTime), new FluidStack(Fluids.PEROXIDE, 50));
    // 		}
    //
    // 		registerRecipe(new ComparableStack(ModBlocks.moon_turf), new CrystallizerRecipe(new ItemStack(ModItems.chunk_ore, 1, EnumChunkType.MOONSTONE.ordinal()), 1200).setReq(16));
    // 	}


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
