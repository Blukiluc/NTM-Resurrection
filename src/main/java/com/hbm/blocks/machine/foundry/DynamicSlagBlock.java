package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.foundry.SlagBlockEntity;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FoundryScrapItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DynamicSlagBlock extends BaseEntityBlock {

    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 16);

    public DynamicSlagBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
    }

    public static final MapCodec<DynamicSlagBlock> CODEC = simpleCodec(DynamicSlagBlock::new);
    @Override protected MapCodec<DynamicSlagBlock> codec() { return CODEC; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, state.getValue(LAYERS), 16);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(ItemTags.SHOVELS) || !(level.getBlockEntity(pos) instanceof SlagBlockEntity slag) || slag.getAmount() <= 0) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide) {
            ItemStack scrap = FoundryScrapItem.create(NtmItems.SCRAP.get(), slag.removeMaterial(slag.getAmount()));
            if (!player.addItem(scrap)) player.drop(scrap, false);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            level.removeBlock(pos, false);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && !player.isCreative() && level.getBlockEntity(pos) instanceof SlagBlockEntity slag && slag.getAmount() > 0) {
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    FoundryScrapItem.create(NtmItems.SCRAP.get(), slag.removeMaterial(slag.getAmount())));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SlagBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> {
            if (be instanceof ITickable tickable) tickable.updateEntity();
        };
    }
}
