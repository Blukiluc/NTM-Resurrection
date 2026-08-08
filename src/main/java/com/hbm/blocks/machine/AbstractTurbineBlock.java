package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.turbine.AbstractTurbineBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.BobMathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTurbineBlock extends DummyableBlock implements ILookOverlay {

    protected AbstractTurbineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (currentLevel, pos, currentState, blockEntity) -> {
            if(blockEntity instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    protected abstract boolean isLeverPosition(BlockPos pos, AbstractTurbineBlockEntity turbine);

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(player.isShiftKeyDown()) return InteractionResult.PASS;

        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return InteractionResult.PASS;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof AbstractTurbineBlockEntity turbine) || !this.isLeverPosition(pos, turbine)) {
            return InteractionResult.PASS;
        }

        if(level.isClientSide) return InteractionResult.SUCCESS;

        if(turbine.operational) {
            player.displayClientMessage(Component.translatable("message.hbm.turbine.operational").withStyle(ChatFormatting.RED), false);
        } else {
            level.playSound(null, pos, NtmSoundEvents.TURBINE_LEVER.get(), SoundSource.BLOCKS, 1.5F, 1F);
            turbine.onLeverPull();
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof AbstractTurbineBlockEntity turbine)) return;

        FluidTank input = turbine.tanks[0];
        FluidTank output = turbine.tanks[1];
        List<Component> text = new ArrayList<>();

        text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN)
                .append(input.getTankType().getName())
                .append(Component.literal(": " + BobMathUtil.format(input.getFill()) + "/" + BobMathUtil.format(input.getMaxFill()) + " mB").withStyle(ChatFormatting.GRAY)));
        text.add(Component.literal("<- ").withStyle(ChatFormatting.RED)
                .append(output.getTankType().getName())
                .append(Component.literal(": " + BobMathUtil.format(output.getFill()) + "/" + BobMathUtil.format(output.getMaxFill()) + " mB").withStyle(ChatFormatting.GRAY)));

        MutableComponent power = Component.literal("<- ").withStyle(ChatFormatting.RED)
                .append(Component.literal(BobMathUtil.getShortNumber(turbine.output) + " HE").withStyle(ChatFormatting.WHITE));
        this.appendPowerInfo(power, turbine);
        text.add(power);

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }

    protected void appendPowerInfo(MutableComponent power, AbstractTurbineBlockEntity turbine) { }
}
