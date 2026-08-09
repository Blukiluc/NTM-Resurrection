package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.MachineCondenserBlockEntity;
import com.hbm.blocks.ILookOverlay;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineCondenserBlock extends BaseEntityBlock implements ILookOverlay {

    public MachineCondenserBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MachineCondenserBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (tickerLevel, pos, tickerState, blockEntity) -> {
            if(blockEntity instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        if(!(level.getBlockEntity(pos) instanceof MachineCondenserBlockEntity condenser)) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN)
                .append(condenser.spentSteam.getTankType().getName())
                .append(Component.literal(": " + BobMathUtil.format(condenser.spentSteam.getFill()) + "/" + BobMathUtil.format(condenser.spentSteam.getMaxFill()) + " mB").withStyle(ChatFormatting.WHITE)));
        text.add(Component.literal("<- ").withStyle(ChatFormatting.RED)
                .append(condenser.water.getTankType().getName())
                .append(Component.literal(": " + BobMathUtil.format(condenser.water.getFill()) + "/" + BobMathUtil.format(condenser.water.getMaxFill()) + " mB").withStyle(ChatFormatting.WHITE)));

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xFFFF00, 0x404000, text);
    }

    public static final MapCodec<MachineCondenserBlock> CODEC = simpleCodec(MachineCondenserBlock::new);

    @Override
    protected MapCodec<MachineCondenserBlock> codec() {
        return CODEC;
    }
}
