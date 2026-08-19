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
 * Reproduces the 1.7.10 coltan generation from HbmWorldGen: a single deposit zone, centered on a
 * point that is deterministically derived from the world seed (always the same location for a
 * given seed), with veins becoming denser the closer they are to that center point.
 * <p>
 * NOTE: uses Blocks.DIAMOND_ORE as a placeholder for NtmBlocks.ORE_COLTAN, which isn't ported yet.
 * This is only meant to test whether the special placement logic behaves correctly in-game.
 */
public class ColtanDepositFeature extends Feature<NoneFeatureConfiguration> {

    private static final int DEPOSIT_RANGE = 750;
    private static final int VEIN_SIZE = 4;
    private static final int RING_COUNT = 5;
    private static final int ATTEMPTS_PER_CHUNK = 2;

    public ColtanDepositFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        // Same seed used in the original code (world seed + 5), first two nextGaussian() calls
        // always yield the same result, so this is one fixed point for the whole world - not per chunk.
        RandomSource centerRandom = RandomSource.create(level.getSeed() + 5);
        int centerX = (int) (centerRandom.nextGaussian() * 1500);
        int centerZ = (int) (centerRandom.nextGaussian() * 1500);

        int chunkX = origin.getX() & ~15;
        int chunkZ = origin.getZ() & ~15;

        boolean placedAny = false;

        for (int attempt = 0; attempt < ATTEMPTS_PER_CHUNK; attempt++) {
            for (int ring = 1; ring <= RING_COUNT; ring++) {
                int posX = chunkX + random.nextInt(16);
                int posY = random.nextInt(25) + 15;
                int posZ = chunkZ + random.nextInt(16);

                int range = DEPOSIT_RANGE / ring;

                boolean withinZone = posX <= centerX + range && posX >= centerX - range
                        && posZ <= centerZ + range && posZ >= centerZ - range;

                if (withinZone) {
                    // TODO: swap for NtmBlocks.ORE_COLTAN.get().defaultBlockState() once ported
                    placeVein(level, random, new BlockPos(posX, posY, posZ), Blocks.DIAMOND_ORE.defaultBlockState(), VEIN_SIZE);
                    placedAny = true;
                }
            }
        }

        return placedAny;
    }

    /**
     * Simple random-cluster vein placement (not a faithful reproduction of vanilla's ore blob shape,
     * just enough to validate the special zone logic while the real block is missing).
     */
    private void placeVein(WorldGenLevel level, RandomSource random, BlockPos center, BlockState oreState, int size) {
        for (int i = 0; i < size; i++) {
            BlockPos pos = center.offset(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
            BlockState current = level.getBlockState(pos);
            if (current.is(BlockTags.STONE_ORE_REPLACEABLES) || current.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
                level.setBlock(pos, oreState, 2);
            }
        }
    }
}