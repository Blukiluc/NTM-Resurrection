package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineSawmillBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.items.NtmItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineSawmillBlock extends DummyableBlock implements ITooltipProvider {

    public MachineSawmillBlock(Properties properties) {
        super(properties);
        this.bounding.add(new AABB(-1.5D, 0D, -1.5D, 1.5D, 1D, 1.5D));
        this.bounding.add(new AABB(-1.25D, 1D, -0.5D, -0.625D, 1.875D, 0.5D));
        this.bounding.add(new AABB(-0.625D, 1D, -1D, 1.375D, 2D, 1D));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineSawmillBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (tickerLevel, pos, tickerState, blockEntity) -> {
            if(blockEntity instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    @Override
    public int[] getDimensions() {
        return new int[] {1, 0, 1, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);
        BlockPos core = pos.relative(dir, offset);
        for(Direction horizontal : Direction.Plane.HORIZONTAL) {
            this.makeExtra(level, core.relative(horizontal));
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null || !(level.getBlockEntity(corePos) instanceof MachineSawmillBlockEntity sawmill)) {
            return ItemInteractionResult.FAIL;
        }

        if(!sawmill.hasBlade && stack.is(NtmItems.SAWBLADE.get())) {
            if(!level.isClientSide) {
                sawmill.hasBlade = true;
                sawmill.setChanged();
                if(!player.hasInfiniteMaterials()) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if(stack.isEmpty()) {
            if(!level.isClientSide) {
                sawmill.giveOutputs(player);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if(sawmill.canPlaceItem(0, stack) && sawmill.getItem(0).isEmpty()) {
            if(!level.isClientSide) {
                sawmill.setItem(0, stack.copyWithCount(1));
                if(!player.hasInfiniteMaterials()) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }

    public static final MapCodec<MachineSawmillBlock> CODEC = simpleCodec(MachineSawmillBlock::new);

    @Override
    protected MapCodec<MachineSawmillBlock> codec() {
        return CODEC;
    }
}
