package com.hbm.blocks.machine;

import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.turbine.AbstractTurbineBlockEntity;
import com.hbm.blockentity.machine.turbine.MachineLeviathanTurbineBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.MultiblockHandlerXR;
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
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MachineLeviathanTurbineBlock extends AbstractTurbineBlock {

    private static final int[][] DIMENSIONS = {
            { 3, 0, 0, 3, 2, 2 },
            { 4, -4, 0, 3, 1, 1 },
            { 3, 0, 6, -1, 1, 1 },
            { 2, 0, 10, -7, 1, 1 }
    };

    public static final MapCodec<MachineLeviathanTurbineBlock> CODEC = simpleCodec(MachineLeviathanTurbineBlock::new);

    public MachineLeviathanTurbineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DummyableBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineLeviathanTurbineBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid().power();
            default -> null;
        };
    }

    @Override
    public int[] getDimensions() {
        return DIMENSIONS[0];
    }

    @Override
    public int getOffset() {
        return 3;
    }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        BlockPos corePos = pos.relative(dir, offset);

        for(int[] dimensions : DIMENSIONS) {
            if(!MultiblockHandlerXR.checkSpace(level, corePos, dimensions, pos, dir)) return false;
        }

        return level.getBlockState(corePos.relative(dir, 4).above(2)).canBeReplaced();
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        BlockPos corePos = pos.relative(dir, offset);

        for(int[] dimensions : DIMENSIONS) {
            MultiblockHandlerXR.fillSpace(level, corePos, dimensions, this, dir);
        }

        BlockPos frontConnector = corePos.relative(dir, 4).above(2);
        DummyableBlock.safeRem = true;
        level.setBlock(frontConnector, this.createDummyState(dir), 3);
        DummyableBlock.safeRem = false;

        this.makeExtra(level, frontConnector);
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 10));

        Direction side = dir.getClockWise();
        this.makeExtra(level, corePos.relative(side, 2));
        this.makeExtra(level, corePos.relative(side.getOpposite(), 2));
    }

    @Override
    protected boolean isLeverPosition(BlockPos pos, AbstractTurbineBlockEntity turbine) {
        BlockPos corePos = turbine.getBlockPos();
        Direction facing = turbine.getBlockState().getValue(FACING);
        Direction turn = facing.getCounterClockWise();
        BlockPos first = corePos.relative(facing).relative(turn, 2);
        BlockPos second = corePos.relative(facing, 2).relative(turn, 2);

        return (pos.equals(first) || pos.equals(first.above()) || pos.equals(second) || pos.equals(second.above()));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.RED));
    }
}
