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
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class NtmConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRASHED_BOMB = registerKey("crashed_bomb");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_URANIUM = registerKey("ore_uranium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_THORIUM = registerKey("ore_thorium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TITANIUM = registerKey("ore_titanium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SULFUR = registerKey("ore_sulfur");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ALUMINIUM = registerKey("ore_aluminium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COPPER = registerKey("ore_copper");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FLUORITE = registerKey("ore_fluorite");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NITER = registerKey("ore_niter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TUNGSTEN = registerKey("ore_tungsten");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LEAD = registerKey("ore_lead");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_BERYLLIUM = registerKey("ore_beryllium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_RARE = registerKey("ore_rare");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LIGNITE = registerKey("ore_lignite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ASBESTOS = registerKey("ore_asbestos");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CINNABAR = registerKey("ore_cinnabar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COBALT = registerKey("ore_cobalt");

//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_IRON = registerKey("cluster_iron");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_TITANIUM = registerKey("cluster_titanium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_ALUMINIUM = registerKey("cluster_aluminium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> CLUSTER_COPPER = registerKey("cluster_copper");

    public static final ResourceKey<ConfiguredFeature<?, ?>> COLTAN_DEPOSIT = registerKey("coltan_deposit");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> AUSTRALIUM_DEPOSIT = registerKey("australium_deposit");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_URANIUM = registerKey("ore_nether_uranium");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_TUNGSTEN = registerKey("ore_nether_tungsten");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_SULFUR = registerKey("ore_nether_sulfur");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_PHOSPHORUS = registerKey("ore_nether_phosphorus");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_COAL = registerKey("ore_nether_coal");
//    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_COBALT = registerKey("ore_nether_cobalt");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_PLUTONIUM = registerKey("ore_nether_plutonium");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_END_TIKITE = registerKey("ore_end_tikite");

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

        register(context, ORE_URANIUM, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_URANIUM.get().defaultBlockState(), NtmBlocks.ORE_URANIUM_DEEPSLATE.get().defaultBlockState(), 5));
        register(context, ORE_THORIUM, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_THORIUM.get().defaultBlockState(), NtmBlocks.ORE_THORIUM_DEEPSLATE.get().defaultBlockState(), 5));
        register(context, ORE_TITANIUM, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_TITANIUM.get().defaultBlockState(), NtmBlocks.ORE_TITANIUM_DEEPSLATE.get().defaultBlockState(), 6));
        register(context, ORE_SULFUR, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_SULFUR.get().defaultBlockState(), NtmBlocks.ORE_DEEPSLATE_SULFUR.get().defaultBlockState(), 8));
        register(context, ORE_ALUMINIUM, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_ALUMINUM.get().defaultBlockState(), NtmBlocks.ORE_ALUMINUM_DEEPSLATE.get().defaultBlockState(), 6));
//        register(context, ORE_COPPER, Feature.ORE, oreConfig(NtmBlocks.ORE_COPPER.get().defaultBlockState(), 6));
        register(context, ORE_FLUORITE, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_FLUORITE.get().defaultBlockState(), NtmBlocks.ORE_DEEPSLATE_FLUORITE.get().defaultBlockState(), 4));
//        register(context, ORE_NITER, Feature.ORE, oreConfig(NtmBlocks.ORE_NITER.get().defaultBlockState(), 6));
        register(context, ORE_TUNGSTEN, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_TUNGSTEN.get().defaultBlockState(), NtmBlocks.ORE_DEEPSLATE_TUNGSTEN.get().defaultBlockState(), 8));
        register(context, ORE_LEAD, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_LEAD.get().defaultBlockState(), NtmBlocks.ORE_LEAD_DEEPSLATE.get().defaultBlockState(), 9));
        register(context, ORE_BERYLLIUM, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_BERYLLIUM.get().defaultBlockState(), NtmBlocks.ORE_BERYLLIUM_DEEPSLATE.get().defaultBlockState(), 4));
        register(context, ORE_RARE, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_RAREGROUND.get().defaultBlockState(), NtmBlocks.ORE_RAREGROUND_DEEPSLATE.get().defaultBlockState(), 5));
        register(context, ORE_LIGNITE, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_LIGNITE.get().defaultBlockState(), NtmBlocks.ORE_DEEPSLATE_BROWNCOAL.get().defaultBlockState(), 24));
        register(context, ORE_ASBESTOS, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_ASBESTOS.get().defaultBlockState(), NtmBlocks.ORE_DEEPSLATE_ASBESTOS.get().defaultBlockState(), 4));
        register(context, ORE_CINNABAR, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_CINNABAR.get().defaultBlockState(), NtmBlocks.ORE_CINNABAR_DEEPSLATE.get().defaultBlockState(), 4));
        register(context, ORE_COBALT, Feature.ORE, overworldOreConfig(NtmBlocks.ORE_COBALT.get().defaultBlockState(), NtmBlocks.ORE_COBALT_DEEPSLATE.get().defaultBlockState(), 4));

//        register(context, CLUSTER_IRON, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_IRON.get().defaultBlockState(), 6));
//        register(context, CLUSTER_TITANIUM, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_TITANIUM.get().defaultBlockState(), 6));
//        register(context, CLUSTER_ALUMINIUM, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_ALUMINIUM.get().defaultBlockState(), 6));
//        register(context, CLUSTER_COPPER, Feature.ORE, oreConfig(NtmBlocks.CLUSTER_COPPER.get().defaultBlockState(), 6));

        register(context, COLTAN_DEPOSIT, NtmFeatures.COLTAN_DEPOSIT.get(), NoneFeatureConfiguration.NONE);
//        register(context, AUSTRALIUM_DEPOSIT, NtmFeatures.AUSTRALIUM_DEPOSIT.get(), NoneFeatureConfiguration.NONE);

        register(context, ORE_NETHER_URANIUM, Feature.ORE, singleTargetOreConfig(NETHERRACK, NtmBlocks.ORE_NETHER_URANIUM.get().defaultBlockState(), 6));
//        register(context, ORE_NETHER_TUNGSTEN, Feature.ORE, singleTargetOreConfig(NETHERRACK, NtmBlocks.ORE_NETHER_TUNGSTEN.get().defaultBlockState(), 10));
//        register(context, ORE_NETHER_SULFUR, Feature.ORE, singleTargetOreConfig(NETHERRACK, NtmBlocks.ORE_NETHER_SULFUR.get().defaultBlockState(), 12));
//        register(context, ORE_NETHER_PHOSPHORUS, Feature.ORE, singleTargetOreConfig(NETHERRACK, NtmBlocks.ORE_NETHER_FIRE.get().defaultBlockState(), 6));
//        register(context, ORE_NETHER_COAL, Feature.ORE, singleTargetOreConfig(NETHERRACK, NtmBlocks.ORE_NETHER_COAL.get().defaultBlockState(), 32));
//        register(context, ORE_NETHER_COBALT, Feature.ORE, singleTargetOreConfig(NETHERRACK, NtmBlocks.ORE_NETHER_COBALT.get().defaultBlockState(), 6));
        register(context, ORE_NETHER_PLUTONIUM, Feature.ORE, singleTargetOreConfig(NETHERRACK, NtmBlocks.ORE_NETHER_PLUTONIUM.get().defaultBlockState(), 4));

        register(context, ORE_END_TIKITE, Feature.ORE, singleTargetOreConfig(END_STONE, NtmBlocks.ORE_TIKITE.get().defaultBlockState(), 6));
    }

    private static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    private static final RuleTest NETHERRACK = new BlockMatchTest(Blocks.NETHERRACK);
    private static final RuleTest END_STONE = new BlockMatchTest(Blocks.END_STONE);

    private static OreConfiguration overworldOreConfig(BlockState stoneOre, BlockState deepslateOre, int veinSize) {
        return new OreConfiguration(
                List.of(
                        OreConfiguration.target(STONE_ORE_REPLACEABLES, stoneOre),
                        OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, deepslateOre)
                ),
                veinSize
        );
    }

    private static OreConfiguration singleTargetOreConfig(RuleTest target, BlockState ore, int veinSize) {
        return new OreConfiguration(target, ore, veinSize);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, NuclearTechMod.withDefaultNamespace(path));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
