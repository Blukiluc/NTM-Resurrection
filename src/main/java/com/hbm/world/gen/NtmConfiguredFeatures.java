package com.hbm.world.gen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.main.NuclearTechMod;
import com.hbm.world.gen.feature.NtmFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class NtmConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRASHED_BOMB = registerKey("crashed_bomb");

    // --- Classic overworld ore veins ---
    // Only URANIUM_ORE is active for now, the rest is ready but commented out
    // since the corresponding NtmBlocks fields don't exist yet.
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_URANIUM = registerKey("ore_uranium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_THORIUM = registerKey("ore_thorium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TITANIUM = registerKey("ore_titanium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SULFUR = registerKey("ore_sulfur");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ALUMINIUM = registerKey("ore_aluminium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COPPER = registerKey("ore_copper");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FLUORITE = registerKey("ore_fluorite");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NITER = registerKey("ore_niter");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TUNGSTEN = registerKey("ore_tungsten");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LEAD = registerKey("ore_lead");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_BERYLLIUM = registerKey("ore_beryllium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_RARE = registerKey("ore_rare");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LIGNITE = registerKey("ore_lignite");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ASBESTOS = registerKey("ore_asbestos");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CINNABAR = registerKey("ore_cinnabar");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COBALT = registerKey("ore_cobalt");

    // --- Overworld clusters (bigger, rarer veins) ---
//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_IRON = registerKey("cluster_iron");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_TITANIUM = registerKey("cluster_titanium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_ALUMINIUM = registerKey("cluster_aluminium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_COPPER = registerKey("cluster_copper");

    // --- Special zone-based deposits (coltan/australium) ---
    // Active with placeholder vanilla ore blocks so the special placement logic can be tested
    // before the real NTM blocks (ore_coltan / ore_australium) are ported.
    public static final ResourceKey<ConfiguredFeature<?, ?>> COLTAN_DEPOSIT = registerKey("coltan_deposit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AUSTRALIUM_DEPOSIT = registerKey("australium_deposit");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        register(context, CRASHED_BOMB, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                new RandomizedIntStateProvider(
                                        BlockStateProvider.simple(NtmBlocks.CRASHED_BOMB.get().defaultBlockState()),
                                        NtmBlockStateProperties.META,
                                        BiasedToBottomInt.of(0, 4)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.STONE, Blocks.SAND, Blocks.SANDSTONE),
                        1
                )
        );

        // Vein size (amount) taken directly from the old DungeonToolbox.generateOre() calls in HbmWorldGen.
        register(context, ORE_URANIUM, Feature.ORE, oreConfig(NtmBlocks.ORE_URANIUM.get().defaultBlockState(), 5));

//        register(context, ORE_THORIUM, Feature.ORE, oreConfig(NtmBlocks.ORE_THORIUM.get().defaultBlockState(), 5));
//        register(context, ORE_TITANIUM, Feature.ORE, oreConfig(NtmBlocks.ORE_TITANIUM.get().defaultBlockState(), 6));
//        register(context, ORE_SULFUR, Feature.ORE, oreConfig(NtmBlocks.ORE_SULFUR.get().defaultBlockState(), 8));
//        register(context, ORE_ALUMINIUM, Feature.ORE, oreConfig(NtmBlocks.ORE_ALUMINIUM.get().defaultBlockState(), 6));
//        register(context, ORE_COPPER, Feature.ORE, oreConfig(NtmBlocks.ORE_COPPER.get().defaultBlockState(), 6));
//        register(context, ORE_FLUORITE, Feature.ORE, oreConfig(NtmBlocks.ORE_FLUORITE.get().defaultBlockState(), 4));
//        register(context, ORE_NITER, Feature.ORE, oreConfig(NtmBlocks.ORE_NITER.get().defaultBlockState(), 6));
//        register(context, ORE_TUNGSTEN, Feature.ORE, oreConfig(NtmBlocks.ORE_TUNGSTEN.get().defaultBlockState(), 8));
//        register(context, ORE_LEAD, Feature.ORE, oreConfig(NtmBlocks.ORE_LEAD.get().defaultBlockState(), 9));
//        register(context, ORE_BERYLLIUM, Feature.ORE, oreConfig(NtmBlocks.ORE_BERYLLIUM.get().defaultBlockState(), 4));
//        register(context, ORE_RARE, Feature.ORE, oreConfig(NtmBlocks.ORE_RARE.get().defaultBlockState(), 5));
//        register(context, ORE_LIGNITE, Feature.ORE, oreConfig(NtmBlocks.ORE_LIGNITE.get().defaultBlockState(), 24));
//        register(context, ORE_ASBESTOS, Feature.ORE, oreConfig(NtmBlocks.ORE_ASBESTOS.get().defaultBlockState(), 4));
//        register(context, ORE_CINNABAR, Feature.ORE, oreConfig(NtmBlocks.ORE_CINNABAR.get().defaultBlockState(), 4));
//        register(context, ORE_COBALT, Feature.ORE, oreConfig(NtmBlocks.ORE_COBALT.get().defaultBlockState(), 4));

//        register(context, CLUSTER_IRON, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_IRON.get().defaultBlockState(), 6));
//        register(context, CLUSTER_TITANIUM, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_TITANIUM.get().defaultBlockState(), 6));
//        register(context, CLUSTER_ALUMINIUM, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_ALUMINIUM.get().defaultBlockState(), 6));
//        register(context, CLUSTER_COPPER, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_COPPER.get().defaultBlockState(), 6));

        // Placeholder blocks (vanilla ores) stand in for NtmBlocks.ORE_COLTAN / ORE_AUSTRALIUM,
        // which don't exist yet - this is only meant to validate the special zone-based placement.
        register(context, COLTAN_DEPOSIT, NtmFeatures.COLTAN_DEPOSIT.get(), NoneFeatureConfiguration.NONE);
        register(context, AUSTRALIUM_DEPOSIT, NtmFeatures.AUSTRALIUM_DEPOSIT.get(), NoneFeatureConfiguration.NONE);
    }

    // Same rule tests vanilla's own OreFeatures class builds internally (they aren't exposed as
    // public constants there, only as local variables in its bootstrap() method), so we rebuild
    // them here from the block tags directly.
    private static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

    /**
     * Builds an OreConfiguration targeting both regular stone and deepslate,
     * so ores generate correctly at all depths in 1.21.1 (1.7.10 had no deepslate layer).
     */
    private static OreConfiguration oreConfig(BlockState ore, int veinSize) {
        return new OreConfiguration(
                List.of(
                        OreConfiguration.target(STONE_ORE_REPLACEABLES, ore),
                        OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, ore)
                ),
                veinSize
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, NuclearTechMod.withDefaultNamespace(path));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}