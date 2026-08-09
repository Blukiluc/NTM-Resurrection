package com.hbm.blocks.machine;

import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.turbine.AbstractTurbineBlockEntity;
import com.hbm.blockentity.machine.turbine.MachineSteamEngineBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.ITooltipProvider;
import com.mojang.serialization.MapCodec;
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

public class MachineSteamEngineBlock extends AbstractTurbineBlock implements ITooltipProvider {

    public MachineSteamEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineSteamEngineBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power().fluid();
            default -> null;
        };
    }

    @Override
    public int[] getDimensions() {
        return new int[] {1, 0, 5, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);
        BlockPos core = pos.relative(dir, offset);
        Direction side = dir.getClockWise();
        this.makeExtra(level, core.relative(side).above());
        this.makeExtra(level, core.relative(side).relative(dir).above());
        this.makeExtra(level, core.relative(side).relative(dir.getOpposite()).above());
    }

    @Override
    protected boolean isLeverPosition(BlockPos pos, AbstractTurbineBlockEntity turbine) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }

    public static final MapCodec<MachineSteamEngineBlock> CODEC = simpleCodec(MachineSteamEngineBlock::new);

    @Override
    protected MapCodec<MachineSteamEngineBlock> codec() {
        return CODEC;
    }
}
