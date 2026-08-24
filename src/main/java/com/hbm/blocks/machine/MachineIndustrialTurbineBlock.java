package com.hbm.blocks.machine;

import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.turbine.AbstractTurbineBlockEntity;
import com.hbm.blockentity.machine.turbine.MachineIndustrialTurbineBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MachineIndustrialTurbineBlock extends AbstractTurbineBlock {

    public static final MapCodec<MachineIndustrialTurbineBlock> CODEC = simpleCodec(MachineIndustrialTurbineBlock::new);

    public MachineIndustrialTurbineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DummyableBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineIndustrialTurbineBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid().power();
            default -> null;
        };
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 2, 0, 3, 3, 1, 1 };
    }

    @Override
    public int getOffset() {
        return 3;
    }

    @Override
    protected void fillSpace(net.minecraft.world.level.Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);
        Direction side = dir.getClockWise();

        this.makeExtra(level, corePos.relative(dir, 3).relative(side));
        this.makeExtra(level, corePos.relative(dir, 3).relative(side.getOpposite()));
        this.makeExtra(level, corePos.relative(dir.getOpposite()).relative(side));
        this.makeExtra(level, corePos.relative(dir.getOpposite()).relative(side.getOpposite()));
        this.makeExtra(level, corePos.relative(dir, 3).above(2));
        this.makeExtra(level, corePos.relative(dir.getOpposite()).above(2));
        this.makeExtra(level, corePos.relative(dir.getOpposite(), 3).above());
    }

    @Override
    protected boolean isLeverPosition(BlockPos pos, AbstractTurbineBlockEntity turbine) {
        Direction facing = turbine.getBlockState().getValue(FACING);
        return pos.equals(turbine.getBlockPos().relative(facing, 3).above());
    }

    @Override
    protected void appendPowerInfo(MutableComponent power, AbstractTurbineBlockEntity turbine) {
        if(turbine instanceof MachineIndustrialTurbineBlockEntity industrial) {
            power.append(Component.literal(" (" + Math.round(industrial.spin * 100D) + "%)").withStyle(ChatFormatting.WHITE));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.YELLOW));
    }
}
