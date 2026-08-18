package com.hbm.world.gen;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NtmBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_CRASHED_BOMB = registerKey("add_crashed_bomb");

    public static final ResourceKey<BiomeModifier> ADD_ORE_URANIUM = registerKey("add_ore_uranium");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_THORIUM = registerKey("add_ore_thorium");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_TITANIUM = registerKey("add_ore_titanium");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_SULFUR = registerKey("add_ore_sulfur");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_ALUMINIUM = registerKey("add_ore_aluminium");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_COPPER = registerKey("add_ore_copper");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_FLUORITE = registerKey("add_ore_fluorite");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_NITER = registerKey("add_ore_niter");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_TUNGSTEN = registerKey("add_ore_tungsten");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_LEAD = registerKey("add_ore_lead");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_BERYLLIUM = registerKey("add_ore_beryllium");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_RARE = registerKey("add_ore_rare");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_LIGNITE = registerKey("add_ore_lignite");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_ASBESTOS = registerKey("add_ore_asbestos");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_CINNABAR = registerKey("add_ore_cinnabar");
//    public static final ResourceKey<BiomeModifier> ADD_ORE_COBALT = registerKey("add_ore_cobalt");

//    public static final ResourceKey<BiomeModifier> ADD_CLUSTER_IRON = registerKey("add_cluster_iron");
//    public static final ResourceKey<BiomeModifier> ADD_CLUSTER_TITANIUM = registerKey("add_cluster_titanium");
//    public static final ResourceKey<BiomeModifier> ADD_CLUSTER_ALUMINIUM = registerKey("add_cluster_aluminium");
//    public static final ResourceKey<BiomeModifier> ADD_CLUSTER_COPPER = registerKey("add_cluster_copper");

    public static final ResourceKey<BiomeModifier> ADD_COLTAN_DEPOSIT = registerKey("add_coltan_deposit");
    public static final ResourceKey<BiomeModifier> ADD_AUSTRALIUM_DEPOSIT = registerKey("add_australium_deposit");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(ADD_CRASHED_BOMB, new BiomeModifiers.AddFeaturesBiomeModifier(HolderSet.direct(biomes.getOrThrow(Biomes.FOREST)), HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.CRASHED_BOMB_PLACED)), GenerationStep.Decoration.SURFACE_STRUCTURES));

        // 1.7.10 generated these on every overworld chunk (dimension 0), regardless of biome,
        // so we hook them to the whole overworld via the vanilla is_overworld biome tag.
        HolderSet<Biome> overworldBiomes = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);

        context.register(ADD_ORE_URANIUM, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_URANIUM_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));

//        context.register(ADD_ORE_THORIUM, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_THORIUM_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_TITANIUM, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_TITANIUM_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_SULFUR, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_SULFUR_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_ALUMINIUM, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_ALUMINIUM_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_COPPER, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_COPPER_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_FLUORITE, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_FLUORITE_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_NITER, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_NITER_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_TUNGSTEN, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_TUNGSTEN_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_LEAD, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_LEAD_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_BERYLLIUM, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_BERYLLIUM_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_RARE, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_RARE_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_LIGNITE, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_LIGNITE_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_ASBESTOS, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_ASBESTOS_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_CINNABAR, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_CINNABAR_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_ORE_COBALT, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.ORE_COBALT_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));

//        context.register(ADD_CLUSTER_IRON, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.CLUSTER_IRON_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_CLUSTER_TITANIUM, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.CLUSTER_TITANIUM_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_CLUSTER_ALUMINIUM, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.CLUSTER_ALUMINIUM_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
//        context.register(ADD_CLUSTER_COPPER, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.CLUSTER_COPPER_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_COLTAN_DEPOSIT, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.COLTAN_DEPOSIT_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_AUSTRALIUM_DEPOSIT, new BiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.AUSTRALIUM_DEPOSIT_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NuclearTechMod.withDefaultNamespace(name));
    }
}