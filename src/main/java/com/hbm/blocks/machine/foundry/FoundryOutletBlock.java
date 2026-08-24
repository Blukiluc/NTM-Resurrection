package com.hbm.blocks.machine.foundry;

import com.hbm.blockentity.machine.foundry.FoundryOutletBlockEntity;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FoundryScrapItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FoundryOutletBlock extends BaseEntityBlock implements ILookOverlay {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty CLOSED = BooleanProperty.create("closed");

    public FoundryOutletBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CLOSED, false));
    }

    public static final MapCodec<FoundryOutletBlock> CODEC = simpleCodec(FoundryOutletBlock::new);
    @Override protected MapCodec<? extends FoundryOutletBlock> codec() { return CODEC; }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING, CLOSED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(CLOSED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof FoundryOutletBlockEntity outlet) outlet.syncClosedState();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoundryOutletBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> net.minecraft.world.level.block.Block.box(5, 0, 10, 11, 8, 16);
            case SOUTH -> net.minecraft.world.level.block.Block.box(5, 0, 0, 11, 8, 6);
            case EAST -> net.minecraft.world.level.block.Block.box(0, 0, 5, 6, 8, 11);
            case WEST -> net.minecraft.world.level.block.Block.box(10, 0, 5, 16, 8, 11);
            default -> net.minecraft.world.level.block.Block.box(5, 0, 10, 11, 8, 16);
        };
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof FoundryOutletBlockEntity outlet)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        MaterialStack material = FoundryScrapItem.getMaterial(stack);
        if (material != null) {
            if (!level.isClientSide) outlet.setFilter(material.material);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (stack.is(NtmItems.SCREWDRIVER)) {
            if (!level.isClientSide) outlet.clearFilter();
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (stack.is(NtmItems.SCREWDRIVER_DESH)) {
            if (!level.isClientSide) outlet.toggleFilter();
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof FoundryOutletBlockEntity outlet) {
            if (!level.isClientSide) {
                outlet.toggleRedstone();
                player.displayClientMessage(Component.translatable(outlet.isClosed() ? "chat.foundry_outlet.closed" : "chat.foundry_outlet.open"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof FoundryOutletBlockEntity outlet)) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.translatable(outlet.isClosed() ? "chat.foundry_outlet.closed" : "chat.foundry_outlet.open")
                .withStyle(outlet.isClosed() ? ChatFormatting.RED : ChatFormatting.GREEN));
        if (outlet.getFilter() != null) {
            text.add(Component.translatable("foundry.filter", Component.translatable(outlet.getFilter().getTranslationKey())).withStyle(ChatFormatting.YELLOW));
        }
        if (outlet.isInvertFilter()) text.add(Component.translatable("foundry.invert_filter").withStyle(ChatFormatting.YELLOW));
        if (outlet.isInvertRedstone()) text.add(Component.translatable("foundry.inverted").withStyle(ChatFormatting.DARK_RED));

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xFF4000, 0x401000, text);
    }
}
