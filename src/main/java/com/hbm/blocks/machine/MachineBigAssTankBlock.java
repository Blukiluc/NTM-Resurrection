package com.hbm.blocks.machine;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.storage.MachineBigAssTankBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.InventoryUtil;
import com.hbm.util.TagsUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineBigAssTankBlock extends DummyableBlock {

    private static final int[][] DIMENSIONS = {
            {5, 0, 4, 4, 4, 4},
            {4, 0, 5, -4, 2, 2},
            {4, 0, -4, 5, 2, 2},
            {4, 0, 2, 2, 5, -4},
            {4, 0, 2, 2, -4, 5},
            {3, 0, 6, -5, 0, 0},
            {3, 0, -5, 6, 0, 0}
    };

    public MachineBigAssTankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineBigAssTankBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineBigAssTankBlock> CODEC = simpleCodec(MachineBigAssTankBlock::new);
    @Override public MapCodec<MachineBigAssTankBlock> codec() { return CODEC; }

    @Override public int[] getDimensions() { return DIMENSIONS[0]; }
    @Override public int getOffset() { return 6; }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        BlockPos corePos = pos.relative(dir, offset);
        for(int[] dimensions : DIMENSIONS) {
            if(!MultiblockHandlerXR.checkSpace(level, corePos, dimensions, pos, dir)) return false;
        }
        return true;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        BlockPos corePos = pos.relative(dir, offset);
        for(int[] dimensions : DIMENSIONS) {
            MultiblockHandlerXR.fillSpace(level, corePos, dimensions, this, dir);
        }

        this.makeExtra(level, corePos.relative(dir, 6));
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 6));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        CompoundTag persistent = TagsUtil.getCustomData(stack).getCompound(IPersistentNBT.NBT_PERSISTENT_KEY);
        FluidTank tank = new FluidTank(Fluids.NONE, 0);
        if(persistent.contains("Tank")) {
            tank.readFromNBT(persistent, "Tank");
            components.add(Component.translatable("fluid.info.mb.name", tank.getFill(), tank.getMaxFill(), tank.getTankType().getName()).withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide) return InteractionResult.SUCCESS;

        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return InteractionResult.FAIL;

        if(!player.isShiftKeyDown()) {
            BlockEntity blockEntity = level.getBlockEntity(corePos);
            if(blockEntity instanceof MenuProvider be) player.openMenu(new SimpleMenuProvider(be, be.getDisplayName()), pos);
            return InteractionResult.CONSUME;
        }

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(blockEntity instanceof MachineBigAssTankBlockEntity be) {
            for(ItemStack stack : InventoryUtil.getItemsFromBothHands(player)) {
                if(stack.getItem() instanceof IItemFluidIdentifier identifier) {
                    FluidType type = identifier.getType(level, corePos, stack);
                    be.tank.setTankType(type);
                    be.setChanged();
                    player.displayClientMessage(
                            Component.literal("Changed type to ")
                                    .append(type.getName())
                                    .withStyle(ChatFormatting.YELLOW),
                            false
                    );
                }
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return 0;
        if(level.getBlockEntity(corePos) instanceof MachineBigAssTankBlockEntity be) {
            return be.getComparatorPower();
        }
        return 0;
    }
}
