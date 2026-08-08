package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineCrucibleBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FoundryScrapItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class MachineCrucibleBlock extends DummyableBlock {

    public MachineCrucibleBlock(Properties properties) {
        super(properties);
        this.bounding.add(new AABB(-1.5D, 0D, -1.5D, 1.5D, 0.5D, 1.5D));
        this.bounding.add(new AABB(-1.25D, 0.5D, -1.25D, 1.25D, 1.5D, -1D));
        this.bounding.add(new AABB(-1.25D, 0.5D, -1.25D, -1D, 1.5D, 1.25D));
        this.bounding.add(new AABB(-1.25D, 0.5D, 1D, 1.25D, 1.5D, 1.25D));
        this.bounding.add(new AABB(1D, 0.5D, -1.25D, 1.25D, 1.5D, 1.25D));
    }

    public static final MapCodec<MachineCrucibleBlock> CODEC = simpleCodec(MachineCrucibleBlock::new);
    @Override protected MapCodec<MachineCrucibleBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch (state.getValue(TYPE)) {
            case CORE -> new MachineCrucibleBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(ItemTags.SHOVELS)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockPos corePos = this.findCore(level, pos);
        if (corePos == null || !(level.getBlockEntity(corePos) instanceof MachineCrucibleBlockEntity crucible)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!level.isClientSide) {
            for (com.hbm.inventory.material.Mats.MaterialStack material : crucible.drainAllMaterials()) {
                ItemStack scrap = FoundryScrapItem.create(NtmItems.SCRAP.get(), material);
                if (!player.addItem(scrap)) player.drop(scrap, false);
            }
            stack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && state.getValue(TYPE) == DummyBlockType.CORE
                && level.getBlockEntity(pos) instanceof MachineCrucibleBlockEntity crucible && !level.isClientSide) {
            for (com.hbm.inventory.material.Mats.MaterialStack material : crucible.drainAllMaterials()) {
                net.minecraft.world.Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        FoundryScrapItem.create(NtmItems.SCRAP.get(), material));
            }
        }
        super.onRemove(state, level, pos, newState, moving);
    }

    @Override public int[] getDimensions() { return new int[] {1, 0, 1, 1, 1, 1}; }
    @Override public int getOffset() { return 1; }
}
