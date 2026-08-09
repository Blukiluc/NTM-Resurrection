package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineStirlingBlockEntity;
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
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineStirlingBlock extends DummyableBlock implements ITooltipProvider {

    public enum Variant {
        STANDARD,
        HEAVY,
        CREATIVE
    }

    private final Variant variant;

    public MachineStirlingBlock(Properties properties, Variant variant) {
        super(properties);
        this.variant = variant;
    }

    public Variant getVariant() {
        return this.variant;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineStirlingBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power();
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
        if(corePos == null || !(level.getBlockEntity(corePos) instanceof MachineStirlingBlockEntity stirling)) {
            return ItemInteractionResult.FAIL;
        }

        Item gear = this.variant == Variant.HEAVY ? NtmItems.GEAR_LARGE_STEEL.get() : NtmItems.GEAR_LARGE.get();
        if(this.variant != Variant.CREATIVE && !stirling.hasGear && stack.is(gear)) {
            if(!level.isClientSide) {
                stirling.hasGear = true;
                stirling.overspeed = 0;
                stirling.setChanged();
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

    public static final MapCodec<MachineStirlingBlock> CODEC = simpleCodec(properties -> new MachineStirlingBlock(properties, Variant.STANDARD));

    @Override
    protected MapCodec<MachineStirlingBlock> codec() {
        return CODEC;
    }
}
