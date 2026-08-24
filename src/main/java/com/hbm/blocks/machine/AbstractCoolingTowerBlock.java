package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.tower.AbstractCoolingTowerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.tank.FluidTank;
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

public abstract class AbstractCoolingTowerBlock extends DummyableBlock implements ILookOverlay {

    protected AbstractCoolingTowerBlock(Properties properties) {
        super(properties);
    }

    protected abstract AbstractCoolingTowerBlockEntity createCoreBlockEntity(BlockPos pos, BlockState state);

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> this.createCoreBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid();
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

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof AbstractCoolingTowerBlockEntity tower)) return;

        List<Component> text = new ArrayList<>();
        FluidTank input = tower.tanks[0];
        FluidTank output = tower.tanks[1];

        text.add(Component.empty()
                .append(Component.literal("-> ").withStyle(ChatFormatting.GREEN))
                .append(input.getTankType().getName())
                .append(Component.literal(": " + input.getFill() + "/" + input.getMaxFill() + " mB")));
        text.add(Component.empty()
                .append(Component.literal("<- ").withStyle(ChatFormatting.RED))
                .append(output.getTankType().getName())
                .append(Component.literal(": " + output.getFill() + "/" + output.getMaxFill() + " mB")));

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
