package com.hbm.blocks.network;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.network.PylonBaseBlockEntity;
import com.hbm.blockentity.network.PylonBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ITooltipProvider;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class ElectricityPylonBlock extends DummyableBlock implements ITooltipProvider {

    public enum Variant {
        WOOD,
        STEEL,
        MEDIUM_WOOD,
        MEDIUM_WOOD_TRANSFORMER,
        MEDIUM_STEEL,
        MEDIUM_STEEL_TRANSFORMER,
        LARGE,
        SUBSTATION
    }

    private final Variant variant;

    public ElectricityPylonBlock(Properties properties, Variant variant) {
        super(properties);
        this.variant = variant;
    }

    public Variant getVariant() {
        return this.variant;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new PylonBlockEntity(pos, state);
            case EXTRA -> this.variant == Variant.SUBSTATION ? new ProxyComboBlockEntity(pos, state).conductor() : null;
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
        return switch(this.variant) {
            case WOOD, STEEL -> new int[] {4, 0, 0, 0, 0, 0};
            case MEDIUM_WOOD, MEDIUM_WOOD_TRANSFORMER, MEDIUM_STEEL, MEDIUM_STEEL_TRANSFORMER -> new int[] {6, 0, 0, 0, 0, 0};
            case LARGE -> new int[] {13, 0, 1, 1, 1, 1};
            case SUBSTATION -> new int[] {4, 0, 1, 1, 2, 2};
        };
    }

    @Override
    public int getOffset() {
        return this.variant == Variant.SUBSTATION ? 1 : 0;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);
        if(this.variant != Variant.SUBSTATION) return;
        BlockPos core = pos.relative(dir, offset);
        Direction side = dir.getClockWise();
        this.makeExtra(level, core.relative(dir).relative(side, 2));
        this.makeExtra(level, core.relative(dir).relative(side.getOpposite(), 2));
        this.makeExtra(level, core.relative(dir.getOpposite()).relative(side, 2));
        this.makeExtra(level, core.relative(dir.getOpposite()).relative(side.getOpposite(), 2));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos != null && level.getBlockEntity(corePos) instanceof PylonBaseBlockEntity pylon && pylon.applyDye(stack, player)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
        components.add(Component.translatable("tooltip.hbm.pylon.range", this.getWireLength()).withStyle(ChatFormatting.GRAY));
    }

    public int getWireLength() {
        return switch(this.variant) {
            case WOOD, STEEL -> 25;
            case MEDIUM_WOOD, MEDIUM_WOOD_TRANSFORMER, MEDIUM_STEEL, MEDIUM_STEEL_TRANSFORMER -> 45;
            case LARGE -> 100;
            case SUBSTATION -> 20;
        };
    }

    public static final MapCodec<ElectricityPylonBlock> CODEC = simpleCodec(properties -> new ElectricityPylonBlock(properties, Variant.WOOD));

    @Override
    protected MapCodec<ElectricityPylonBlock> codec() {
        return CODEC;
    }
}
