package com.hbm.blocks.fluids;

import com.hbm.fluids.NtmFluids;
import net.minecraft.world.level.block.LiquidBlock;
import com.hbm.blocks.NtmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.common.Tags;



public class SulfuricLiquidBlock extends LiquidBlock {

    public SulfuricLiquidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }


    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        for (Direction dir : Direction.values()) {
            BlockPos targetPos = pos.relative(dir);
            BlockState resultState = getReaction(level, targetPos);
            if (resultState != null) level.setBlock(targetPos, resultState, 3);
        }
    }

    public BlockState getReaction(Level level, BlockPos pos) {
        BlockState b = level.getBlockState(pos);

        if (b.getFluidState().is(FluidTags.WATER)) return Blocks.AIR.defaultBlockState();
        if (b.is(BlockTags.LOGS)) return NtmBlocks.WASTE_LOG.get().defaultBlockState();
        if (b.is(BlockTags.PLANKS)) return NtmBlocks.WASTE_PLANKS.get().defaultBlockState();
        if (b.is(BlockTags.LEAVES)) return Blocks.AIR.defaultBlockState();
        return null;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        if (!level.isClientSide) {
            if (entity.hurt(entity.damageSources().lava(), 4.0F)) {
                entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + entity.random.nextFloat() * 0.4F);
            }
        }
    }


    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockPos blockpos = pos.above();
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
            if (random.nextInt(100) == 0) {
                double d0 = (double) pos.getX() + random.nextDouble();
                double d1 = (double) pos.getY() + 1.0;
                double d2 = (double) pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.DUST_PLUME, d0, d1, d2, 0.0, 0.0, 0.0);
                level.playLocalSound(d0, d1, d2, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }


    }
}
