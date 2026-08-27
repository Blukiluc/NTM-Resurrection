package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class CentrifugeRecipes extends GenericRecipes<GenericRecipe> {
    public static final CentrifugeRecipes INSTANCE = new CentrifugeRecipes();

    @Override public int inputItemLimit() { return 1; }
    @Override public int inputFluidLimit() { return 0; }
    @Override public int outputItemLimit() { return 4; }
    @Override public int outputFluidLimit() { return 0; }
    @Override public String getFileName() { return "hbmCentrifuge.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {
        add("rare_chunk", new ComparableStack(NtmItems.CHUNK_RARE.get()),
                stack(NtmItems.POWDER_COBALT_TINY.get(), 2),
                stack(NtmItems.POWDER_BORON_TINY.get(), 2),
                stack(NtmItems.POWDER_NIOBIUM_TINY.get(), 2),
                stack(NtmItems.NUGGET_ZIRCONIUM.get(), 3));

        add("coal_ore", new ComparableStack(Blocks.COAL_ORE),
                stack(NtmItems.POWDER_COAL.get(), 2),
                stack(NtmItems.POWDER_COAL.get(), 2),
                stack(NtmItems.POWDER_COAL.get(), 2),
                stack(Blocks.GRAVEL, 1));

        add("lignite_ore", new ComparableStack(NtmBlocks.ORE_LIGNITE.get()),
                stack(NtmItems.POWDER_LIGNITE.get(), 2),
                stack(NtmItems.POWDER_LIGNITE.get(), 2),
                stack(NtmItems.POWDER_LIGNITE.get(), 2),
                stack(Blocks.GRAVEL, 1));

        add("iron_ore", new ComparableStack(Blocks.IRON_ORE),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("gold_ore", new ComparableStack(Blocks.GOLD_ORE),
                stack(NtmItems.POWDER_GOLD.get(), 1),
                stack(NtmItems.POWDER_GOLD.get(), 1),
                stack(NtmItems.POWDER_GOLD.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("diamond_ore", new ComparableStack(Blocks.DIAMOND_ORE),
                stack(NtmItems.POWDER_DIAMOND.get(), 1),
                stack(NtmItems.POWDER_DIAMOND.get(), 1),
                stack(NtmItems.POWDER_DIAMOND.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("emerald_ore", new ComparableStack(Blocks.EMERALD_ORE),
                stack(NtmItems.POWDER_EMERALD.get(), 1),
                stack(NtmItems.POWDER_EMERALD.get(), 1),
                stack(NtmItems.POWDER_EMERALD.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("titanium_ore", new ComparableStack(NtmBlocks.ORE_TITANIUM.get()),
                stack(NtmItems.POWDER_TITANIUM.get(), 1),
                stack(NtmItems.POWDER_TITANIUM.get(), 1),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("nether_quartz_ore", new ComparableStack(Blocks.NETHER_QUARTZ_ORE),
                stack(NtmItems.POWDER_QUARTZ.get(), 1),
                stack(NtmItems.POWDER_QUARTZ.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1),
                stack(Blocks.NETHERRACK, 1));

        add("tungsten_ore", new ComparableStack(NtmBlocks.ORE_TUNGSTEN.get()),
                stack(NtmItems.POWDER_TUNGSTEN.get(), 1),
                stack(NtmItems.POWDER_TUNGSTEN.get(), 1),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("copper_ore", new ComparableStack(Blocks.COPPER_ORE),
                stack(NtmItems.POWDER_COPPER.get(), 1),
                stack(NtmItems.POWDER_COPPER.get(), 1),
                stack(NtmItems.POWDER_GOLD.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("aluminium_ore", new ComparableStack(NtmBlocks.ORE_ALUMINUM.get()),
                stack(NtmItems.CHUNK_CRYOLITE.get(), 2),
                stack(NtmItems.POWDER_TITANIUM.get(), 1),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("lead_ore", new ComparableStack(NtmBlocks.ORE_LEAD.get()),
                stack(NtmItems.POWDER_LEAD.get(), 1),
                stack(NtmItems.POWDER_LEAD.get(), 1),
                stack(NtmItems.POWDER_GOLD.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("schrabidium_ore", new ComparableStack(NtmBlocks.ORE_SCHRABIDIUM.get()),
                stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1),
                stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1),
                stack(NtmItems.NUGGET_SOLINIUM.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("rare_earth_ore", new ComparableStack(NtmBlocks.ORE_RAREGROUND.get()),
                stack(NtmItems.POWDER_DESH_MIX.get(), 1),
                stack(NtmItems.NUGGET_ZIRCONIUM.get(), 1),
                stack(NtmItems.NUGGET_ZIRCONIUM.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("plutonium_ore", new ComparableStack(NtmBlocks.ORE_NETHER_PLUTONIUM.get()),
                stack(NtmItems.POWDER_PLUTONIUM.get(), 1),
                stack(NtmItems.POWDER_PLUTONIUM.get(), 1),
                stack(NtmItems.NUGGET_PO210.get(), 3),
                stack(Blocks.GRAVEL, 1));

        add("uranium_ore", new ComparableStack(NtmBlocks.ORE_URANIUM.get()),
                stack(NtmItems.POWDER_URANIUM.get(), 1),
                stack(NtmItems.POWDER_URANIUM.get(), 1),
                stack(NtmItems.NUGGET_RA226.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("thorium_ore", new ComparableStack(NtmBlocks.ORE_THORIUM.get()),
                stack(NtmItems.POWDER_THORIUM.get(), 1),
                stack(NtmItems.POWDER_THORIUM.get(), 1),
                stack(NtmItems.POWDER_URANIUM.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("beryllium_ore", new ComparableStack(NtmBlocks.ORE_BERYLLIUM.get()),
                stack(NtmItems.POWDER_BERYLLIUM.get(), 1),
                stack(NtmItems.POWDER_BERYLLIUM.get(), 1),
                stack(NtmItems.POWDER_EMERALD.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("fluorite_ore", new ComparableStack(NtmBlocks.ORE_FLUORITE.get()),
                stack(NtmItems.FLUORITE.get(), 3),
                stack(NtmItems.FLUORITE.get(), 3),
                stack(NtmItems.GEM_SODALITE.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("tikite_ore", new ComparableStack(NtmBlocks.ORE_TIKITE.get()),
                stack(NtmItems.POWDER_PLUTONIUM.get(), 1),
                stack(NtmItems.POWDER_COBALT.get(), 2),
                stack(NtmItems.POWDER_NIOBIUM.get(), 2),
                stack(Blocks.END_STONE, 1));

        add("lapis_ore", new ComparableStack(Blocks.LAPIS_ORE),
                stack(NtmItems.POWDER_LAPIS.get(), 6),
                stack(NtmItems.POWDER_COBALT_TINY.get(), 1),
                stack(NtmItems.GEM_SODALITE.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("cobalt_ore", new ComparableStack(NtmBlocks.ORE_COBALT.get()),
                stack(NtmItems.POWDER_COBALT.get(), 2),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(NtmItems.POWDER_COPPER.get(), 1),
                stack(Blocks.GRAVEL, 1));

        add("tektite_powder", new ComparableStack(NtmItems.POWDER_TEKTITE.get()),
                stack(NtmItems.POWDER_METEORITE_TINY.get(), 1),
                stack(NtmItems.POWDER_PALEOGENITE_TINY.get(), 1),
                stack(NtmItems.POWDER_METEORITE_TINY.get(), 1),
                stack(NtmItems.DUST.get(), 6));

        add("coal_ash", new ComparableStack(NtmItems.POWDER_ASH_COAL.get()),
                stack(NtmItems.POWDER_COAL_TINY.get(), 2),
                stack(NtmItems.POWDER_BORON_TINY.get(), 1),
                stack(NtmItems.DUST_TINY.get(), 6));

        add("crystal_coal", new ComparableStack(NtmItems.CRYSTAL_COAL.get()),
                stack(NtmItems.POWDER_COAL.get(), 3),
                stack(NtmItems.POWDER_COAL.get(), 3),
                stack(NtmItems.POWDER_COAL.get(), 3),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_iron", new ComparableStack(NtmItems.CRYSTAL_IRON.get()),
                stack(NtmItems.POWDER_IRON.get(), 2),
                stack(NtmItems.POWDER_IRON.get(), 2),
                stack(NtmItems.POWDER_TITANIUM.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_lapis", new ComparableStack(NtmItems.CRYSTAL_LAPIS.get()),
                stack(NtmItems.POWDER_LAPIS.get(), 4),
                stack(NtmItems.POWDER_LAPIS.get(), 4),
                stack(NtmItems.POWDER_COBALT.get(), 1),
                stack(NtmItems.GEM_SODALITE.get(), 2));

        add("crystal_diamond", new ComparableStack(NtmItems.CRYSTAL_DIAMOND.get()),
                stack(NtmItems.POWDER_DIAMOND.get(), 1),
                stack(NtmItems.POWDER_DIAMOND.get(), 1),
                stack(NtmItems.POWDER_DIAMOND.get(), 1),
                stack(NtmItems.POWDER_DIAMOND.get(), 1));

        add("crystal_uranium", new ComparableStack(NtmItems.CRYSTAL_URANIUM.get()),
                stack(NtmItems.POWDER_URANIUM.get(), 2),
                stack(NtmItems.POWDER_URANIUM.get(), 2),
                stack(NtmItems.NUGGET_RA226.get(), 2),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_thorium", new ComparableStack(NtmItems.CRYSTAL_THORIUM.get()),
                stack(NtmItems.POWDER_THORIUM.get(), 2),
                stack(NtmItems.POWDER_THORIUM.get(), 2),
                stack(NtmItems.POWDER_URANIUM.get(), 1),
                stack(NtmItems.NUGGET_RA226.get(), 1));

        add("crystal_plutonium", new ComparableStack(NtmItems.CRYSTAL_PLUTONIUM.get()),
                stack(NtmItems.POWDER_PLUTONIUM.get(), 2),
                stack(NtmItems.POWDER_PLUTONIUM.get(), 2),
                stack(NtmItems.POWDER_PO210.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_titanium", new ComparableStack(NtmItems.CRYSTAL_TITANIUM.get()),
                stack(NtmItems.POWDER_TITANIUM.get(), 2),
                stack(NtmItems.POWDER_TITANIUM.get(), 2),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_niter", new ComparableStack(NtmItems.CRYSTAL_NITER.get()),
                stack(NtmItems.NITER.get(), 3),
                stack(NtmItems.NITER.get(), 3),
                stack(NtmItems.NITER.get(), 3),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_copper", new ComparableStack(NtmItems.CRYSTAL_COPPER.get()),
                stack(NtmItems.POWDER_COPPER.get(), 2),
                stack(NtmItems.POWDER_COPPER.get(), 2),
                stack(NtmItems.SULFUR.get(), 1),
                stack(NtmItems.POWDER_COBALT_TINY.get(), 1));

        add("crystal_tungsten", new ComparableStack(NtmItems.CRYSTAL_TUNGSTEN.get()),
                stack(NtmItems.POWDER_TUNGSTEN.get(), 2),
                stack(NtmItems.POWDER_TUNGSTEN.get(), 2),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_aluminium", new ComparableStack(NtmItems.CRYSTAL_ALUMINIUM.get()),
                stack(NtmItems.CHUNK_CRYOLITE.get(), 3),
                stack(NtmItems.POWDER_TITANIUM.get(), 1),
                stack(NtmItems.POWDER_IRON.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_fluorite", new ComparableStack(NtmItems.CRYSTAL_FLUORITE.get()),
                stack(NtmItems.FLUORITE.get(), 4),
                stack(NtmItems.FLUORITE.get(), 4),
                stack(NtmItems.GEM_SODALITE.get(), 2),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_beryllium", new ComparableStack(NtmItems.CRYSTAL_BERYLLIUM.get()),
                stack(NtmItems.POWDER_BERYLLIUM.get(), 2),
                stack(NtmItems.POWDER_BERYLLIUM.get(), 2),
                stack(NtmItems.POWDER_QUARTZ.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_lead", new ComparableStack(NtmItems.CRYSTAL_LEAD.get()),
                stack(NtmItems.POWDER_LEAD.get(), 2),
                stack(NtmItems.POWDER_LEAD.get(), 2),
                stack(NtmItems.POWDER_GOLD.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_schraranium", new ComparableStack(NtmItems.CRYSTAL_SCHRARANIUM.get()),
                stack(NtmItems.NUGGET_SCHRABIDIUM.get(), 2),
                stack(NtmItems.NUGGET_SCHRABIDIUM.get(), 2),
                stack(NtmItems.NUGGET_URANIUM.get(), 2),
                stack(NtmItems.NUGGET_NEPTUNIUM.get(), 2));

        add("crystal_schrabidium", new ComparableStack(NtmItems.CRYSTAL_SCHRABIDIUM.get()),
                stack(NtmItems.POWDER_SCHRABIDIUM.get(), 2),
                stack(NtmItems.POWDER_SCHRABIDIUM.get(), 2),
                stack(NtmItems.POWDER_PLUTONIUM.get(), 1),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));

        add("crystal_rare", new ComparableStack(NtmItems.CRYSTAL_RARE.get()),
                stack(NtmItems.POWDER_DESH_MIX.get(), 1),
                stack(NtmItems.POWDER_DESH_MIX.get(), 1),
                stack(NtmItems.NUGGET_ZIRCONIUM.get(), 2),
                stack(NtmItems.NUGGET_ZIRCONIUM.get(), 2));

        add("crystal_trixite", new ComparableStack(NtmItems.CRYSTAL_TRIXITE.get()),
                stack(NtmItems.POWDER_PLUTONIUM.get(), 2),
                stack(NtmItems.POWDER_COBALT.get(), 3),
                stack(NtmItems.POWDER_NIOBIUM.get(), 2),
                stack(NtmItems.POWDER_NITAN_MIX.get(), 1));

        add("crystal_lithium", new ComparableStack(NtmItems.CRYSTAL_LITHIUM.get()),
                stack(NtmItems.POWDER_LITHIUM.get(), 2),
                stack(NtmItems.POWDER_LITHIUM.get(), 2),
                stack(NtmItems.POWDER_QUARTZ.get(), 1),
                stack(NtmItems.FLUORITE.get(), 1));

        add("crystal_cobalt", new ComparableStack(NtmItems.CRYSTAL_COBALT.get()),
                stack(NtmItems.POWDER_COBALT.get(), 2),
                stack(NtmItems.POWDER_IRON.get(), 3),
                stack(NtmItems.POWDER_COPPER.get(), 3),
                stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
    }

    private void add(String id, ComparableStack input, ItemStack... outputs) {
        this.register(new GenericRecipe("centrifuge." + id).setup(200, 100)
                .outputItems(outputs)
                .inputItems(input));
    }

    private static ItemStack stack(ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    /*
     * Recettes originales conservées mais désactivées :
     * leurs objets, blocs, variantes de minerai ou dépendances externes ne sont pas encore disponibles.
     * recipes.put(new OreDictStack(REDSTONE.ore()), new ItemStack[] {
     * 				new ItemStack(Items.redstone, 3),
     * 				new ItemStack(Items.redstone, 3),
     * 				lbs ? new ItemStack(ModItems.ingot_mercury, 3) : new ItemStack(ModItems.ingot_mercury, 1),
     * 				new ItemStack(Blocks.gravel, 1) });
     *
     * recipes.put(new ComparableStack(ModBlocks.block_euphemium_cluster), new ItemStack[] {
     * 				new ItemStack(ModItems.nugget_euphemium, 7),
     * 				new ItemStack(ModItems.powder_schrabidium, 4),
     * 				new ItemStack(ModItems.ingot_starmetal, 2),
     * 				new ItemStack(ModItems.nugget_solinium, 2) });
     *
     * recipes.put(new ComparableStack(ModBlocks.ore_nether_fire), new ItemStack[] {
     * 				new ItemStack(Items.blaze_powder, 2),
     * 				new ItemStack(ModItems.powder_fire, 2),
     * 				new ItemStack(ModItems.ingot_phosphorus),
     * 				new ItemStack(Blocks.netherrack) });
     *
     * recipes.put(new ComparableStack(ModBlocks.block_slag), new ItemStack[] {
     * 				new ItemStack(Blocks.gravel, 1),
     * 				new ItemStack(ModItems.powder_fire, 1),
     * 				new ItemStack(ModItems.powder_calcium),
     * 				new ItemStack(ModItems.dust) });
     *
     * recipes.put(new ComparableStack(ModItems.ore_bedrock, 1, i), new ItemStack[] {
     * 					new ItemStack(ModItems.ore_centrifuged, 1, i),
     * 					new ItemStack(ModItems.ore_centrifuged, 1, i),
     * 					new ItemStack(ModItems.ore_centrifuged, 1, i),
     * 					new ItemStack(ModItems.ore_centrifuged, 1, i) });
     *
     * recipes.put(new ComparableStack(ModItems.ore_cleaned, 1, i), new ItemStack[] {
     * 					new ItemStack(ModItems.ore_separated, 1, i),
     * 					new ItemStack(ModItems.ore_separated, 1, i),
     * 					new ItemStack(ModItems.ore_separated, 1, i),
     * 					new ItemStack(ModItems.ore_separated, 1, i) });
     *
     * recipes.put(new ComparableStack(ModItems.ore_purified, 1, i), new ItemStack[] {
     * 					new ItemStack(ModItems.ore_enriched, 1, i),
     * 					new ItemStack(ModItems.ore_enriched, 1, i),
     * 					new ItemStack(ModItems.ore_enriched, 1, i),
     * 					new ItemStack(ModItems.ore_enriched, 1, i) });
     *
     * recipes.put(new ComparableStack(ModItems.ore_nitrated, 1, i), new ItemStack[] {
     * 					new ItemStack(ModItems.ore_nitrocrystalline, 1, i),
     * 					new ItemStack(ModItems.ore_nitrocrystalline, 1, i),
     * 					ItemStackUtil.carefulCopy(by1),
     * 					ItemStackUtil.carefulCopy(by1) });
     *
     * recipes.put(new ComparableStack(ModItems.ore_deepcleaned, 1, i), new ItemStack[] {
     * 					new ItemStack(ModItems.ore_enriched, 1, i),
     * 					new ItemStack(ModItems.ore_enriched, 1, i),
     * 					ItemStackUtil.carefulCopy(by2),
     * 					ItemStackUtil.carefulCopy(by2) });
     *
     * recipes.put(new ComparableStack(ModItems.ore_seared, 1, i), new ItemStack[] {
     * 					new ItemStack(ModItems.ore_enriched, 1, i),
     * 					new ItemStack(ModItems.ore_enriched, 1, i),
     * 					ItemStackUtil.carefulCopy(by3),
     * 					ItemStackUtil.carefulCopy(by3) });
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.BASE, type)), new ItemStack[] {ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type), new ItemStack(Blocks.gravel)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.BASE_ROASTED, type)), new ItemStack[] {ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type), new ItemStack(Blocks.gravel)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.BASE_WASHED, type)), new ItemStack[] {ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type), ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type), new ItemStack(Blocks.gravel)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SULFURIC, type)), new ItemStack[] {ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSULFURIC, type, 2), ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_BYPRODUCT, type, 2)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SOLVENT, type)), new ItemStack[] {ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSOLVENT, type, 2), ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_BYPRODUCT, type, 2), ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_BYPRODUCT, type, 2)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_RAD, type)), new ItemStack[] {ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NORAD, type, 2), ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_BYPRODUCT, type, 2), ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_BYPRODUCT, type, 2), ItemBedrockOreNew.make(BedrockOreGrade.RAD_BYPRODUCT, type, 2)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary2, 1)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_ROASTED, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary2, 1)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSULFURIC, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary2, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NOSOLVENT, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary2, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_NORAD, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary2, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_FIRST, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary2, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type, 1)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.PRIMARY_SECOND, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.primary1, 1), ItemBedrockOreNew.extract(type.primary2, 1), ItemBedrockOreNew.extract(type.primary2, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type, 1)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SULFURIC_WASHED, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.byproductAcid1, 1), ItemBedrockOreNew.extract(type.byproductAcid2, 1), ItemBedrockOreNew.extract(type.byproductAcid3, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.SOLVENT_WASHED, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.byproductSolvent1, 1), ItemBedrockOreNew.extract(type.byproductSolvent2, 1), ItemBedrockOreNew.extract(type.byproductSolvent3, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type)});
     *
     * recipes.put(new ComparableStack(ItemBedrockOreNew.make(BedrockOreGrade.RAD_WASHED, type)), new ItemStack[] {ItemBedrockOreNew.extract(type.byproductRad1, 1), ItemBedrockOreNew.extract(type.byproductRad2, 1), ItemBedrockOreNew.extract(type.byproductRad3, 1), ItemBedrockOreNew.make(BedrockOreGrade.CRUMBS, type)});
     *
     * recipes.put(new OreDictStack("oreCertusQuartz"), new ItemStack[] {
     * 					qItem.copy(),
     * 					qItem.copy(),
     * 					qItem.copy(),
     * 					qItem.copy() });
     *
     * recipes.put(new ComparableStack(Items.blaze_rod), new ItemStack[] {new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.blaze_powder, 1), new ItemStack(ModItems.powder_fire, 1), new ItemStack(ModItems.powder_fire, 1) });
     *
     * recipes.put(new ComparableStack(ModItems.crystal_gold), new ItemStack[] { new ItemStack(ModItems.powder_gold, 2), new ItemStack(ModItems.powder_gold, 2), new ItemStack(ModItems.ingot_mercury, 1), new ItemStack(ModItems.powder_lithium_tiny, 1) });
     *
     * recipes.put(new ComparableStack(ModItems.crystal_redstone), new ItemStack[] { new ItemStack(Items.redstone, 3), new ItemStack(Items.redstone, 3), new ItemStack(Items.redstone, 3), new ItemStack(ModItems.ingot_mercury, 3) });
     *
     * recipes.put(new ComparableStack(ModItems.crystal_sulfur), new ItemStack[] { new ItemStack(ModItems.sulfur, 4), new ItemStack(ModItems.sulfur, 4), new ItemStack(ModItems.powder_iron, 1), new ItemStack(ModItems.ingot_mercury, 1) });
     *
     * recipes.put(new ComparableStack(ModItems.crystal_phosphorus), new ItemStack[] { new ItemStack(ModItems.powder_fire, 3), new ItemStack(ModItems.powder_fire, 3), new ItemStack(ModItems.ingot_phosphorus, 2), new ItemStack(Items.blaze_powder, 2) });
     *
     * recipes.put(new ComparableStack(ModItems.crystal_starmetal), new ItemStack[] { new ItemStack(ModItems.powder_dura_steel, 3), new ItemStack(ModItems.powder_cobalt, 3), new ItemStack(ModItems.powder_astatine, 2), new ItemStack(ModItems.ingot_mercury, 5) });
     */
}
