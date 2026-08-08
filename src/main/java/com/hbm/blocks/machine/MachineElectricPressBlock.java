package com.hbm.blocks.machine;

import api.hbm.block.IToolable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineElectricPressBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class MachineElectricPressBlock extends DummyableBlock implements IToolable {

    public static final MapCodec<MachineElectricPressBlock> CODEC = simpleCodec(MachineElectricPressBlock::new);

    public MachineElectricPressBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<MachineElectricPressBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineElectricPressBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 2, 0, 0, 0, 0, 0 };
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);
        this.makeExtra(level, pos.relative(dir, offset).above());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if(stack.has(DataComponents.CUSTOM_NAME)) {
            BlockPos corePos = this.findCore(level, pos);
            if(corePos != null && level.getBlockEntity(corePos) instanceof MachineElectricPressBlockEntity press) {
                press.setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    // Un-multiblickable with a hand drill for schenanigans
    @Override
    public boolean onScrew(Level level, Player player, BlockPos pos, Direction direction, ToolType tool) {
        if(tool != ToolType.HAND_DRILL) return false;

        BlockState state = level.getBlockState(pos);
        if(state.getBlock() != this || state.getValue(TYPE) == DummyBlockType.CORE) return false;
        if(level.isClientSide) return true;

        safeRem = true;
        try {
            level.removeBlock(pos, false);
        } finally {
            safeRem = false;
        }
        return true;
    }
}
