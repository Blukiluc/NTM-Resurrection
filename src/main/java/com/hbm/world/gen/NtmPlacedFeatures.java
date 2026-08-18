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

    // --- Classic overworld ore veins ---
    public static final ResourceKey<PlacedFeature> ORE_URANIUM_PLACED = registerKey("ore_uranium_placed");
//    public static final ResourceKey<PlacedFeature> ORE_THORIUM_PLACED = registerKey("ore_thorium_placed");
//    public static final ResourceKey<PlacedFeature> ORE_TITANIUM_PLACED = registerKey("ore_titanium_placed");
//    public static final ResourceKey<PlacedFeature> ORE_SULFUR_PLACED = registerKey("ore_sulfur_placed");
//    public static final ResourceKey<PlacedFeature> ORE_ALUMINIUM_PLACED = registerKey("ore_aluminium_placed");
//    public static final ResourceKey<PlacedFeature> ORE_COPPER_PLACED = registerKey("ore_copper_placed");
//    public static final ResourceKey<PlacedFeature> ORE_FLUORITE_PLACED = registerKey("ore_fluorite_placed");
//    public static final ResourceKey<PlacedFeature> ORE_NITER_PLACED = registerKey("ore_niter_placed");
//    public static final ResourceKey<PlacedFeature> ORE_TUNGSTEN_PLACED = registerKey("ore_tungsten_placed");
//    public static final ResourceKey<PlacedFeature> ORE_LEAD_PLACED = registerKey("ore_lead_placed");
//    public static final ResourceKey<PlacedFeature> ORE_BERYLLIUM_PLACED = registerKey("ore_beryllium_placed");
//    public static final ResourceKey<PlacedFeature> ORE_RARE_PLACED = registerKey("ore_rare_placed");
//    public static final ResourceKey<PlacedFeature> ORE_LIGNITE_PLACED = registerKey("ore_lignite_placed");
//    public static final ResourceKey<PlacedFeature> ORE_ASBESTOS_PLACED = registerKey("ore_asbestos_placed");
//    public static final ResourceKey<PlacedFeature> ORE_CINNABAR_PLACED = registerKey("ore_cinnabar_placed");
//    public static final ResourceKey<PlacedFeature> ORE_COBALT_PLACED = registerKey("ore_cobalt_placed");

    // --- Overworld clusters ---
//    public static final ResourceKey<PlacedFeature> CLUSTER_IRON_PLACED = registerKey("cluster_iron_placed");
//    public static final ResourceKey<PlacedFeature> CLUSTER_TITANIUM_PLACED = registerKey("cluster_titanium_placed");
//    public static final ResourceKey<PlacedFeature> CLUSTER_ALUMINIUM_PLACED = registerKey("cluster_aluminium_placed");
//    public static final ResourceKey<PlacedFeature> CLUSTER_COPPER_PLACED = registerKey("cluster_copper_placed");

    // --- Special zone-based deposits ---
    public static final ResourceKey<PlacedFeature> COLTAN_DEPOSIT_PLACED = registerKey("coltan_deposit_placed");
    public static final ResourceKey<PlacedFeature> AUSTRALIUM_DEPOSIT_PLACED = registerKey("australium_deposit_placed");

    // TODO: these vein-per-chunk counts should come from WorldConfig (uraniumSpawn, thoriumSpawn, ...)
    // once the config system is ported over (see open question about hardcoded vs. config-driven values).
    // Using a placeholder of 20 veins/chunk for uranium for now so it can be tested in-game.
    private static final int URANIUM_VEINS_PER_CHUNK = 20;

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, CRASHED_BOMB_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CRASHED_BOMB), List.of(RarityFilter.onAverageOnceEvery(500), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        // Height range copied from HbmWorldGen: y = minHeight + random(variance) -> [minHeight, minHeight + variance - 1]
        register(context, ORE_URANIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_URANIUM),
                orePlacement(URANIUM_VEINS_PER_CHUNK, 5, 24));

//        register(context, ORE_THORIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_THORIUM),
//                orePlacement(WorldConfig.thoriumSpawn, 5, 29));
//        register(context, ORE_TITANIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_TITANIUM),
//                orePlacement(WorldConfig.titaniumSpawn, 5, 34));
//        register(context, ORE_SULFUR_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_SULFUR),
//                orePlacement(WorldConfig.sulfurSpawn, 5, 34));
//        register(context, ORE_ALUMINIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_ALUMINIUM),
//                orePlacement(WorldConfig.aluminiumSpawn, 5, 44));
//        register(context, ORE_COPPER_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_COPPER),
//                orePlacement(WorldConfig.copperSpawn, 5, 49));
//        register(context, ORE_FLUORITE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_FLUORITE),
//                orePlacement(WorldConfig.fluoriteSpawn, 5, 49));
//        register(context, ORE_NITER_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_NITER),
//                orePlacement(WorldConfig.niterSpawn, 5, 34));
//        register(context, ORE_TUNGSTEN_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_TUNGSTEN),
//                orePlacement(WorldConfig.tungstenSpawn, 5, 34));
//        register(context, ORE_LEAD_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_LEAD),
//                orePlacement(WorldConfig.leadSpawn, 5, 34));
//        register(context, ORE_BERYLLIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_BERYLLIUM),
//                orePlacement(WorldConfig.berylliumSpawn, 5, 34));
//        register(context, ORE_RARE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_RARE),
//                orePlacement(WorldConfig.rareSpawn, 5, 24));
//        register(context, ORE_LIGNITE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_LIGNITE),
//                orePlacement(WorldConfig.ligniteSpawn, 35, 59));
//        register(context, ORE_ASBESTOS_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_ASBESTOS),
//                orePlacement(WorldConfig.asbestosSpawn, 16, 31));
//        register(context, ORE_CINNABAR_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_CINNABAR),
//                orePlacement(WorldConfig.cinnebarSpawn, 8, 23));
//        register(context, ORE_COBALT_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.ORE_COBALT),
//                orePlacement(WorldConfig.cobaltSpawn, 4, 11));

//        register(context, CLUSTER_IRON_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_IRON),
//                orePlacement(WorldConfig.ironClusterSpawn, 15, 59));
//        register(context, CLUSTER_TITANIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_TITANIUM),
//                orePlacement(WorldConfig.titaniumClusterSpawn, 15, 44));
//        register(context, CLUSTER_ALUMINIUM_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_ALUMINIUM),
//                orePlacement(WorldConfig.aluminiumClusterSpawn, 15, 49));
//        register(context, CLUSTER_COPPER_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CLUSTER_COPPER),
//                orePlacement(WorldConfig.copperClusterSpawn, 15, 34));

        // These custom features handle their own internal looping/placement logic (fixed deposit zones),
        // so we only need to trigger them once per chunk - the height range here is not actually used.
        register(context, COLTAN_DEPOSIT_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.COLTAN_DEPOSIT),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
        register(context, AUSTRALIUM_DEPOSIT_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.AUSTRALIUM_DEPOSIT),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
    }

    /**
     * count = number of vein attempts per chunk (was a plain for-loop in HbmWorldGen, not a probability)
     * minY/maxY = inclusive height range, matching "minHeight + random(variance)" from the old code
     */
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