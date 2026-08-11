package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.machine.foundry.FoundryCastingBlockEntity;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FoundryMoldItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class FoundryCastingBlock extends FoundryMaterialBlock {

    private final VoxelShape shape;

    protected FoundryCastingBlock(Properties properties, VoxelShape shape) {
        super(properties);
        this.shape = shape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemInteractionResult materialResult = super.useItemOn(stack, state, level, pos, player, hand, hit);
        if (materialResult != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) return materialResult;
        if (!(level.getBlockEntity(pos) instanceof FoundryCastingBlockEntity casting)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (stack.getItem() instanceof FoundryMoldItem && casting.canPlaceItem(0, stack) && casting.getMold().isEmpty()) {
            if (!level.isClientSide) {
                casting.setItem(0, stack.copyWithCount(1));
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if ((stack.is(NtmItems.SCREWDRIVER) || stack.is(NtmItems.SCREWDRIVER_DESH)) && casting.getAmount() == 0 && !casting.getMold().isEmpty()) {
            if (!level.isClientSide) {
                ItemStack mold = casting.removeItemNoUpdate(0);
                if (!player.addItem(mold)) player.drop(mold, false);
                casting.setChanged();
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof FoundryCastingBlockEntity casting) || casting.getResult().isEmpty()) return InteractionResult.PASS;
        if (!level.isClientSide) {
            ItemStack result = casting.removeItemNoUpdate(1);
            if (!player.addItem(result)) player.drop(result, false);
            casting.setChanged();
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof FoundryCastingBlockEntity casting) {
            Containers.dropContents(level, pos, casting);
        }
        super.onRemove(state, level, pos, newState, moving);
    }
}
