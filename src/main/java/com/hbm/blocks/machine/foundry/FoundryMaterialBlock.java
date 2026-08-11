package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.foundry.FoundryBaseBlockEntity;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FoundryScrapItem;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class FoundryMaterialBlock extends BaseEntityBlock {

    protected FoundryMaterialBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> {
            if (be instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(ItemTags.SHOVELS)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof FoundryBaseBlockEntity foundry) || foundry.getAmount() <= 0) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!level.isClientSide) {
            ItemStack scrap = FoundryScrapItem.create(NtmItems.SCRAP.get(), foundry.removeMaterial(foundry.getAmount()));
            if (!player.addItem(scrap)) player.drop(scrap, false);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FoundryBaseBlockEntity foundry && foundry.getAmount() > 0 && !level.isClientSide) {
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        FoundryScrapItem.create(NtmItems.SCRAP.get(), foundry.removeMaterial(foundry.getAmount())));
            }
        }
        super.onRemove(state, level, pos, newState, moving);
    }
}
