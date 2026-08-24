package com.hbm.world.gen;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class NtmPlacedFeatures {

    public static final ResourceKey<PlacedFeature> CRASHED_BOMB_PLACED = registerKey("crashed_bomb_placed");

    public static final ResourceKey<PlacedFeature> ORE_URANIUM_PLACED = registerKey("ore_uranium_placed");
    public static final ResourceKey<PlacedFeature> ORE_THORIUM_PLACED = registerKey("ore_thorium_placed");
    public static final ResourceKey<PlacedFeature> ORE_TITANIUM_PLACED = registerKey("ore_titanium_placed");
    public static final ResourceKey<PlacedFeature> ORE_SULFUR_PLACED = registerKey("ore_sulfur_placed");
    public static final ResourceKey<PlacedFeature> ORE_ALUMINIUM_PLACED = registerKey("ore_aluminium_placed");
//    public static final ResourceKey<PlacedFeature> ORE_COPPER_PLACED = registerKey("ore_copper_placed");
    public static final ResourceKey<PlacedFeature> ORE_FLUORITE_PLACED = registerKey("ore_fluorite_placed");
//    public static final ResourceKey<PlacedFeature> ORE_NITER_PLACED = registerKey("ore_niter_placed");
    public static final ResourceKey<PlacedFeature> ORE_TUNGSTEN_PLACED = registerKey("ore_tungsten_placed");
    public static final ResourceKey<PlacedFeature> ORE_LEAD_PLACED = registerKey("ore_lead_placed");
    public static final ResourceKey<PlacedFeature> ORE_BERYLLIUM_PLACED = registerKey("ore_beryllium_placed");
    public static final ResourceKey<PlacedFeature> ORE_RARE_PLACED = registerKey("ore_rare_placed");
    public static final ResourceKey<PlacedFeature> ORE_LIGNITE_PLACED = registerKey("ore_lignite_placed");
    public static final ResourceKey<PlacedFeature> ORE_ASBESTOS_PLACED = registerKey("ore_asbestos_placed");
    public static final ResourceKey<PlacedFeature> ORE_CINNABAR_PLACED = registerKey("ore_cinnabar_placed");
    public static final ResourceKey<PlacedFeature> ORE_COBALT_PLACED = registerKey("ore_cobalt_placed");

//    public static final ResourceKey<PlacedFeature> CLUSTER_IRON_PLACED = registerKey("cluster_iron_placed");
//    public static final ResourceKey<PlacedFeature> CLUSTER_TITANIUM_PLACED = registerKey("cluster_titanium_placed");
//    public static final ResourceKey<PlacedFeature> CLUSTER_ALUMINIUM_PLACED = registerKey("cluster_aluminium_placed");
//    public static final ResourceKey<PlacedFeature> CLUSTER_COPPER_PLACED = registerKey("cluster_copper_placed");

    public static final ResourceKey<PlacedFeature> COLTAN_DEPOSIT_PLACED = registerKey("coltan_deposit_placed");
//    public static final ResourceKey<PlacedFeature> AUSTRALIUM_DEPOSIT_PLACED = registerKey("australium_deposit_placed");

    public static final ResourceKey<PlacedFeature> ORE_NETHER_URANIUM_PLACED = registerKey("ore_nether_uranium_placed");
//    public static final ResourceKey<PlacedFeature> ORE_NETHER_TUNGSTEN_PLACED = registerKey("ore_nether_tungsten_placed");
//    public static final ResourceKey<PlacedFeature> ORE_NETHER_SULFUR_PLACED = registerKey("ore_nether_sulfur_placed");
//    public static final ResourceKey<PlacedFeature> ORE_NETHER_PHOSPHORUS_PLACED = registerKey("ore_nether_phosphorus_placed");
//    public static final ResourceKey<PlacedFeature> ORE_NETHER_COAL_PLACED = registerKey("ore_nether_coal_placed");
//    public static final ResourceKey<PlacedFeature> ORE_NETHER_COBALT_PLACED = registerKey("ore_nether_cobalt_placed");
    public static final ResourceKey<PlacedFeature> ORE_NETHER_PLUTONIUM_PLACED = registerKey("ore_nether_plutonium_placed");

    public static final ResourceKey<PlacedFeature> ORE_END_TIKITE_PLACED = registerKey("ore_end_tikite_placed");

    private static final int URANIUM_VEINS_PER_CHUNK = 7;
    private static final int THORIUM_VEINS_PER_CHUNK = 7;
    private static final int TITANIUM_VEINS_PER_CHUNK = 8;
    private static final int SULFUR_VEINS_PER_CHUNK = 5;
    private static final int ALUMINIUM_VEINS_PER_CHUNK = 7;
    private static final int COPPER_VEINS_PER_CHUNK = 12;
    private static final int FLUORITE_VEINS_PER_CHUNK = 6;
    private static final int NITER_VEINS_PER_CHUNK = 6;
    private static final int TUNGSTEN_VEINS_PER_CHUNK = 10;
    private static final int LEAD_VEINS_PER_CHUNK = 6;
    private static final int BERYLLIUM_VEINS_PER_CHUNK = 6;
    private static final int RARE_VEINS_PER_CHUNK = 6;
    private static final int LIGNITE_VEINS_PER_CHUNK = 2;
    private static final int ASBESTOS_VEINS_PER_CHUNK = 2;
    private static final int CINNABAR_VEINS_PER_CHUNK = 1;
    private static final int COBALT_VEINS_PER_CHUNK = 2;

    private static final int IRON_CLUSTER_VEINS_PER_CHUNK = 4;
    private static final int TITANIUM_CLUSTER_VEINS_PER_CHUNK = 2;
    private static final int ALUMINIUM_CLUSTER_VEINS_PER_CHUNK = 3;
    private static final int COPPER_CLUSTER_VEINS_PER_CHUNK = 4;

    private static final int NETHER_URANIUM_VEINS_PER_CHUNK = 8;
    private static final int NETHER_TUNGSTEN_VEINS_PER_CHUNK = 10;
    private static final int NETHER_SULFUR_VEINS_PER_CHUNK = 26;
    private static final int NETHER_PHOSPHORUS_VEINS_PER_CHUNK = 24;
    private static final int NETHER_COAL_VEINS_PER_CHUNK = 8;
    private static final int NETHER_COBALT_VEINS_PER_CHUNK = 2;
    private static final int NETHER_PLUTONIUM_VEINS_PER_CHUNK = 8;

    private static final int END_TIKITE_VEINS_PER_CHUNK = 8;

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, CRASHED_BOMB_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CRASHED_BOMB), List.of(RarityFilter.onAverageOnceEvery(500), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, ORE_URANIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_URANIUM),
                orePlacement(URANIUM_VEINS_PER_CHUNK, 5, 24));

        register(context, ORE_THORIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_THORIUM),
                orePlacement(THORIUM_VEINS_PER_CHUNK, 5, 29));
        register(context, ORE_TITANIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_TITANIUM),
                orePlacement(TITANIUM_VEINS_PER_CHUNK, 5, 34));
        register(context, ORE_SULFUR_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_SULFUR),
                orePlacement(SULFUR_VEINS_PER_CHUNK, 5, 34));
        register(context, ORE_ALUMINIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_ALUMINIUM),
                orePlacement(ALUMINIUM_VEINS_PER_CHUNK, 5, 44));
//        register(context, ORE_COPPER_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_COPPER),
//                orePlacement(COPPER_VEINS_PER_CHUNK, 5, 49));
        register(context, ORE_FLUORITE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_FLUORITE),
                orePlacement(FLUORITE_VEINS_PER_CHUNK, 5, 49));
//        register(context, ORE_NITER_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NITER),
//                orePlacement(NITER_VEINS_PER_CHUNK, 5, 34));
        register(context, ORE_TUNGSTEN_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_TUNGSTEN),
                orePlacement(TUNGSTEN_VEINS_PER_CHUNK, 5, 34));
        register(context, ORE_LEAD_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_LEAD),
                orePlacement(LEAD_VEINS_PER_CHUNK, 5, 34));
        register(context, ORE_BERYLLIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_BERYLLIUM),
                orePlacement(BERYLLIUM_VEINS_PER_CHUNK, 5, 34));
        register(context, ORE_RARE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_RARE),
                orePlacement(RARE_VEINS_PER_CHUNK, 5, 24));
        register(context, ORE_LIGNITE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_LIGNITE),
                orePlacement(LIGNITE_VEINS_PER_CHUNK, 35, 59));
        register(context, ORE_ASBESTOS_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_ASBESTOS),
                orePlacement(ASBESTOS_VEINS_PER_CHUNK, 16, 31));
        register(context, ORE_CINNABAR_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_CINNABAR),
                orePlacement(CINNABAR_VEINS_PER_CHUNK, 8, 23));
        register(context, ORE_COBALT_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_COBALT),
                orePlacement(COBALT_VEINS_PER_CHUNK, 4, 11));

//        register(context, CLUSTER_IRON_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_IRON),
//                orePlacement(IRON_CLUSTER_VEINS_PER_CHUNK, 15, 59));
//        register(context, CLUSTER_TITANIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_TITANIUM),
//                orePlacement(TITANIUM_CLUSTER_VEINS_PER_CHUNK, 15, 44));
//        register(context, CLUSTER_ALUMINIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_ALUMINIUM),
//                orePlacement(ALUMINIUM_CLUSTER_VEINS_PER_CHUNK, 15, 49));
//        register(context, CLUSTER_COPPER_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_COPPER),
//                orePlacement(COPPER_CLUSTER_VEINS_PER_CHUNK, 15, 34));

        register(context, COLTAN_DEPOSIT_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.COLTAN_DEPOSIT),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
//        register(context, AUSTRALIUM_DEPOSIT_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.AUSTRALIUM_DEPOSIT),
//                List.of(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, ORE_NETHER_URANIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NETHER_URANIUM),
                orePlacement(NETHER_URANIUM_VEINS_PER_CHUNK, 0, 126));
//        register(context, ORE_NETHER_TUNGSTEN_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NETHER_TUNGSTEN),
//                orePlacement(NETHER_TUNGSTEN_VEINS_PER_CHUNK, 0, 126));
//        register(context, ORE_NETHER_SULFUR_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NETHER_SULFUR),
//                orePlacement(NETHER_SULFUR_VEINS_PER_CHUNK, 0, 126));
//        register(context, ORE_NETHER_PHOSPHORUS_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NETHER_PHOSPHORUS),
//                orePlacement(NETHER_PHOSPHORUS_VEINS_PER_CHUNK, 0, 126));
//        register(context, ORE_NETHER_COAL_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NETHER_COAL),
//                orePlacement(NETHER_COAL_VEINS_PER_CHUNK, 16, 111));
//        register(context, ORE_NETHER_COBALT_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NETHER_COBALT),
//                orePlacement(NETHER_COBALT_VEINS_PER_CHUNK, 100, 125));
        register(context, ORE_NETHER_PLUTONIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NETHER_PLUTONIUM),
                orePlacement(NETHER_PLUTONIUM_VEINS_PER_CHUNK, 0, 126));

        register(context, ORE_END_TIKITE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_END_TIKITE),
                orePlacement(END_TIKITE_VEINS_PER_CHUNK, 0, 126));
    }

    private static List<PlacementModifier> orePlacement(int count, int minY, int maxY) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)),
                BiomeFilter.biome()
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, NuclearTechMod.withDefaultNamespace(name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
