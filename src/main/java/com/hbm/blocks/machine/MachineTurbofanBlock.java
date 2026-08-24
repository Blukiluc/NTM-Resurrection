package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineTurbofanBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.trait.FT_Combustible.FuelGrade;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineTurbofanBlock extends DummyableBlock {

    public static final MapCodec<MachineTurbofanBlock> CODEC = simpleCodec(MachineTurbofanBlock::new);

    public MachineTurbofanBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<MachineTurbofanBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineTurbofanBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != com.hbm.blocks.DummyBlockType.CORE) return null;
        return (tickerLevel, pos, tickerState, blockEntity) -> {
            if(blockEntity instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 2, 0, 1, 1, 3, 3 };
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);
        Direction rot = dir.getClockWise();

        this.makeExtra(level, corePos);
        this.makeExtra(level, corePos.relative(rot.getOpposite()));
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 2));
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 2).relative(rot.getOpposite()));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.literal("Fuel efficiency:").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("- " + FuelGrade.AERO.getGrade() + ": ").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal("100%").withStyle(ChatFormatting.RED)));
    }
}