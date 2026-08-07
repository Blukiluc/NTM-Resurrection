package com.hbm.blocks.machine;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.oil.MachineCatalyticReformerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineCatalyticReformerBlock extends DummyableBlock implements IPersistentInfoProvider {

    public MachineCatalyticReformerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineCatalyticReformerBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineCatalyticReformerBlock> CODEC = simpleCodec(MachineCatalyticReformerBlock::new);
    @Override public MapCodec<MachineCatalyticReformerBlock> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] {2, 0, 1, 1, 2, 2}; }
    @Override public int getOffset() { return 1; }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        return super.checkRequirement(level, pos, dir, offset) &&
                MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), new int[] {3, -3, 1, 0, -1, 2}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), new int[] {6, -3, 1, 1, 2, 0}, pos, dir);
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        MultiblockHandlerXR.fillSpace(level, pos.relative(dir, offset), new int[] {3, -3, 1, 0, -1, 2}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.relative(dir, offset), new int[] {6, -3, 1, 1, 2, 0}, this, dir);

        Direction rot = dir.getCounterClockWise(Direction.Axis.Y);

        this.makeExtra(level, pos.offset(dir.getStepX() * -1 + 1, 0, dir.getStepZ() * -1 + 1));
        this.makeExtra(level, pos.offset(dir.getStepX() * -1 + 1, 0, dir.getStepZ() * -1 - 1));
        this.makeExtra(level, pos.offset(dir.getStepX() * -1 - 1, 0, dir.getStepZ() * -1 + 1));
        this.makeExtra(level, pos.offset(dir.getStepX() * -1 - 1, 0, dir.getStepZ() * -1 - 1));
        this.makeExtra(level, pos.offset(dir.getStepX() * -1 + rot.getStepX() * 2, 0, dir.getStepZ() * -1 + rot.getStepZ() * 2));
        this.makeExtra(level, pos.offset(dir.getStepX() * -1 - rot.getStepX() * 2, 0, dir.getStepZ() * -1 - rot.getStepZ() * 2));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public void appendHoverText(ItemStack stack, CompoundTag persistentTag, List<Component> components, Item.TooltipContext context, TooltipFlag flag) {

        for(int i = 0; i < 4; i++) {
            FluidTank tank = new FluidTank(Fluids.NONE, 0);
            tank.readFromNBT(persistentTag, "" + i);
            components.add(Component.literal(tank.getFill() + "/" + tank.getMaxFill() + "mB ").append(tank.getTankType().getName()).withStyle(ChatFormatting.YELLOW));
        }
    }
}