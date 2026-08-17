package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineIntakeBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineIntakeBlock extends DummyableBlock implements ILookOverlay {

    public static final MapCodec<MachineIntakeBlock> CODEC = simpleCodec(MachineIntakeBlock::new);

    public MachineIntakeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<MachineIntakeBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineIntakeBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (currentLevel, pos, currentState, blockEntity) -> {
            if(blockEntity instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    // Up, Down, North, South, West, East offsets from the core block
    @Override
    public int[] getDimensions() {
        return new int[] {0, 0, 1, 0, 1, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof MachineIntakeBlockEntity intake)) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.literal("Power: " + BobMathUtil.format(intake.power) + "HE")
                .withStyle(intake.power < intake.getMaxPower() / 20L ? ChatFormatting.RED : ChatFormatting.GREEN));
        text.add(Component.literal("<- ").withStyle(ChatFormatting.RED)
                .append(intake.compair.getTankType().getName())
                .append(Component.literal(": " + intake.compair.getFill() + "/" + intake.compair.getMaxFill() + "mB")
                        .withStyle(ChatFormatting.WHITE)));

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}