package com.hbm.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.function.Supplier;

/**
 * Reproduces the 1.7.10 australium generation from HbmWorldGen: unlike coltan, this deposit sits
 * at a fixed set of world coordinates (X and Z both between -450 and -350), independent of seed.
 */
public class AustraliumDepositFeature extends Feature<NoneFeatureConfiguration> {

    private static final int VEIN_SIZE = 50;
    private static final int MIN_COORD = -450;
    private static final int MAX_COORD = -350;
    private final Supplier<? extends Block> ore;

    public AustraliumDepositFeature(Codec<NoneFeatureConfiguration> codec, Supplier<? extends Block> ore) {
        super(codec);
        this.ore = ore;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int chunkX = origin.getX() & ~15;
        int chunkZ = origin.getZ() & ~15;

        boolean placedAny = false;

        for (int i = 0; i < random.nextInt(4); i++) {
            int posX = chunkX + random.nextInt(16);
            int posY = random.nextInt(15) + 15;
            int posZ = chunkZ + random.nextInt(16);

            if (posX <= MAX_COORD && posX >= MIN_COORD && posZ <= MAX_COORD && posZ >= MIN_COORD) {
                placedAny |= Feature.ORE.place(
                        new OreConfiguration(
                                new BlockMatchTest(Blocks.STONE),
                                ore.get().defaultBlockState(),
                                VEIN_SIZE
                        ),
                        level,
                        context.chunkGenerator(),
                        random,
                        new BlockPos(posX, posY, posZ)
                );
            }
        }

        return placedAny;
    }
}
