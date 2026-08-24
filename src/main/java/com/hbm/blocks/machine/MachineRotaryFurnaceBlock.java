package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineRotaryFurnaceBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineRotaryFurnaceBlock extends DummyableBlock implements ILookOverlay {

    public MachineRotaryFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineRotaryFurnaceBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineRotaryFurnaceBlock> CODEC = simpleCodec(MachineRotaryFurnaceBlock::new);
    @Override public MapCodec<MachineRotaryFurnaceBlock> codec() { return CODEC; }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override public int[] getDimensions() { return new int[] {4, 0, 1, 1, 2, 2}; }
    @Override public int getOffset() { return 1; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos core = pos.relative(dir, offset);
        Direction side = dir.getCounterClockWise();

        for(int i = -2; i <= 2; i++) {
            this.makeExtra(level, core.relative(dir.getOpposite()).relative(side, i));
        }

        this.makeExtra(level, core.relative(dir).relative(side, 2));
        this.makeExtra(level, core.relative(side).above(4));
        this.makeExtra(level, core.relative(dir).relative(side));
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null || !(level.getBlockEntity(corePos) instanceof MachineRotaryFurnaceBlockEntity furnace)) return;

        Direction dir = furnace.getBlockState().getValue(FACING);
        Direction side = dir.getCounterClockWise();
        List<Component> text = new ArrayList<>();

        if(pos.equals(corePos.relative(dir, -1).relative(side, -1))
                || pos.equals(corePos.relative(dir, -1).relative(side, -2))) {
            text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN)
                    .append(furnace.tanks[1].getTankType().getName()));
            text.add(Component.literal("<- ").withStyle(ChatFormatting.RED)
                    .append(furnace.tanks[2].getTankType().getName()));
        }

        if(pos.equals(corePos.relative(dir).relative(side, 2))
                || pos.equals(corePos.relative(dir, -1).relative(side, 2))) {
            text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN)
                    .append(furnace.tanks[0].getTankType().getName()));
        }

        if(pos.equals(corePos.relative(dir).relative(side))) {
            text.add(Component.literal("-> ").withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal("Fuel")));
        }

        if(!text.isEmpty()) {
            ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
        }
    }
}
