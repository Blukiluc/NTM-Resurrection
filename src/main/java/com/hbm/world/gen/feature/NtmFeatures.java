package com.hbm.world.gen.feature;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for custom worldgen features.
 */
public class NtmFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, NuclearTechMod.MODID);

    public static final DeferredHolder<Feature<?>, ColtanDepositFeature> COLTAN_DEPOSIT =
            FEATURES.register("coltan_deposit", () -> new ColtanDepositFeature(NoneFeatureConfiguration.CODEC));

//    public static final DeferredHolder<Feature<?>, AustraliumDepositFeature> AUSTRALIUM_DEPOSIT =
//            FEATURES.register("australium_deposit", () -> new AustraliumDepositFeature(NoneFeatureConfiguration.CODEC, NtmBlocks.ORE_AUSTRALIUM));
}
