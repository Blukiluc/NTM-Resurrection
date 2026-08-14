package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineGroundwaterPumpBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineGroundwaterPumpBlock extends DummyableBlock implements ITooltipProvider, ILookOverlay {

    private final boolean electric;

    public MachineGroundwaterPumpBlock(Properties properties, boolean electric) {
        super(properties);
        this.electric = electric;
    }

    public boolean isElectric() {
        return this.electric;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineGroundwaterPumpBlockEntity(pos, state);
            case EXTRA -> this.electric ? new ProxyComboBlockEntity(pos, state).power().fluid() : new ProxyComboBlockEntity(pos, state).fluid();
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
        return new int[] {3, 0, 1, 1, 1, 1};
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null || !(level.getBlockEntity(corePos) instanceof MachineGroundwaterPumpBlockEntity pump)) return;

        List<Component> text = new ArrayList<>();
        if(this.electric) {
            text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN)
                    .append(Component.literal(BobMathUtil.format(pump.power) + " / " + BobMathUtil.format(pump.getMaxPower()) + " HE").withStyle(ChatFormatting.WHITE)));
        } else {
            text.add(this.tankLine("-> ", ChatFormatting.GREEN, pump.steam));
            text.add(this.tankLine("<- ", ChatFormatting.RED, pump.spentSteam));
        }
        text.add(this.tankLine("<- ", ChatFormatting.RED, pump.water));

        if(corePos.getY() > 70) {
            text.add(Component.literal("! ! ! ALTITUDE ! ! !").withColor(BobMathUtil.getBlink() ? 0xFF0000 : 0xFFFF00));
        }
        if(!pump.onGround) {
            text.add(Component.literal("! ! ! NO VALID GROUND ! ! !").withColor(BobMathUtil.getBlink() ? 0xFF0000 : 0xFFFF00));
        }

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xFFFF00, 0x404000, text);
    }

    private Component tankLine(String arrow, ChatFormatting color, com.hbm.inventory.fluid.tank.FluidTank tank) {
        return Component.literal(arrow).withStyle(color)
                .append(tank.getTankType().getName())
                .append(Component.literal(": " + BobMathUtil.format(tank.getFill()) + " / " + BobMathUtil.format(tank.getMaxFill()) + " mB").withStyle(ChatFormatting.WHITE));
    }

    public static final MapCodec<MachineGroundwaterPumpBlock> CODEC = simpleCodec(properties -> new MachineGroundwaterPumpBlock(properties, false));

    @Override
    protected MapCodec<MachineGroundwaterPumpBlock> codec() {
        return CODEC;
    }
}
