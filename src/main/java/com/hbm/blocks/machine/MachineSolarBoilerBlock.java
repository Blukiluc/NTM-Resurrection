package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineSolarBoilerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.BobMathUtil;
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

public class MachineSolarBoilerBlock extends DummyableBlock implements ILookOverlay {

    public static final MapCodec<MachineSolarBoilerBlock> CODEC = simpleCodec(MachineSolarBoilerBlock::new);

    public MachineSolarBoilerBlock(Properties properties) {
        super(properties);
        this.bounding.add(new AABB(-1.6875D, 0D, -1.6875D, 1.6875D, 3D, 1.6875D));
    }

    @Override
    protected MapCodec<MachineSolarBoilerBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineSolarBoilerBlockEntity(pos, state);
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
    public int[] getDimensions() {
        return new int[] {2, 0, 1, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);
        BlockPos corePos = pos.relative(dir, offset);
        this.makeExtra(level, corePos.above(2));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof MachineSolarBoilerBlockEntity boiler)) return;

        List<Component> text = new ArrayList<>();
        FluidTank[] tanks = boiler.getAllTanks();
        for(int i = 0; i < tanks.length; i++) {
            FluidTank tank = tanks[i];
            text.add(Component.literal(i == 0 ? "-> " : "<- ")
                    .withStyle(i == 0 ? ChatFormatting.GREEN : ChatFormatting.RED)
                    .append(tank.getTankType().getName())
                    .append(Component.literal(": " + BobMathUtil.format(tank.getFill()) + "/" + BobMathUtil.format(tank.getMaxFill()) + " mB")
                            .withStyle(ChatFormatting.WHITE)));
        }

        if(boiler.display < 1) {
            text.add(Component.translatable("overlay.hbm.machine_solar_boiler.too_cold")
                    .withColor(BobMathUtil.getBlink() ? 0xFF0000 : 0xFFFF00));
        }

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xFFFF00, 0x404000, text);
    }
}
