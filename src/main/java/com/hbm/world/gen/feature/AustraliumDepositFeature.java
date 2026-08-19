package com.hbm.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Reproduces the 1.7.10 australium generation from HbmWorldGen: unlike coltan, this deposit sits
 * at a fixed set of world coordinates (X and Z both between -450 and -350), independent of seed.
 * <p>
 * NOTE: uses Blocks.EMERALD_ORE as a placeholder for NtmBlocks.ORE_AUSTRALIUM, which isn't ported
 * yet. This is only meant to test whether the special placement logic behaves correctly in-game.
 */
public class AustraliumDepositFeature extends Feature<NoneFeatureConfiguration> {

    private static final int VEIN_SIZE = 50;
    private static final int MIN_COORD = -450;
    private static final int MAX_COORD = -350;

    public AustraliumDepositFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int chunkX = origin.getX() & ~15;
        int chunkZ = origin.getZ() & ~15;

        boolean placedAny = false;

        // Original loop bound was "rand.nextInt(4)", i.e. 0 to 3 attempts
        int attempts = random.nextInt(4);

        for (int i = 0; i < attempts; i++) {
            int posX = chunkX + random.nextInt(16);
            int posY = random.nextInt(15) + 15;
            int posZ = chunkZ + random.nextInt(16);

            if (posX <= MAX_COORD && posX >= MIN_COORD && posZ <= MAX_COORD && posZ >= MIN_COORD) {
                // TODO: swap for NtmBlocks.ORE_AUSTRALIUM.get().defaultBlockState() once ported
                placeVein(level, random, new BlockPos(posX, posY, posZ), Blocks.EMERALD_ORE.defaultBlockState(), VEIN_SIZE);
                placedAny = true;
            }
        }

        return placedAny;
    }

    private void placeVein(WorldGenLevel level, RandomSource random, BlockPos center, BlockState oreState, int size) {
        for (int i = 0; i < size; i++) {
            BlockPos pos = center.offset(random.nextInt(5) - 2, random.nextInt(5) - 2, random.nextInt(5) - 2);
            BlockState current = level.getBlockState(pos);
            if (current.is(BlockTags.STONE_ORE_REPLACEABLES) || current.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
                level.setBlock(pos, oreState, 2);
            }
        }
    }
}