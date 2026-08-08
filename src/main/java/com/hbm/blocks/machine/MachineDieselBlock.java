package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.MachineDieselBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.trait.FT_Combustible.FuelGrade;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineDieselBlock extends DummyableBlock {

    public static final MapCodec<MachineDieselBlock> CODEC = simpleCodec(MachineDieselBlock::new);

    public MachineDieselBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<MachineDieselBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(TYPE) == DummyBlockType.CORE ? new MachineDieselBlockEntity(pos, state) : null;
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
        return new int[] { 0, 0, 0, 0, 0, 0 };
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if(blockEntity instanceof MachineDieselBlockEntity diesel && diesel.hasAcceptableFuel() && diesel.tank.getFill() > 0) {
            Direction direction = state.getValue(FACING);
            Direction side = direction.getClockWise();
            level.addParticle(
                    ParticleTypes.SMOKE,
                    pos.getX() + 0.5 - direction.getStepX() * 0.6 + side.getStepX() * 0.1875,
                    pos.getY() + 0.3125,
                    pos.getZ() + 0.5 - direction.getStepZ() * 0.6 + side.getStepZ() * 0.1875,
                    0,
                    0,
                    0
            );
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Fuel efficiency:").withStyle(ChatFormatting.YELLOW));

        for(FuelGrade grade : FuelGrade.values()) {
            Double efficiency = MachineDieselBlockEntity.FUEL_EFFICIENCY.get(grade);

            if(efficiency != null) {
                int percent = (int) (efficiency * 100);
                components.add(
                        Component.literal("-" + grade.getGrade() + ": ")
                                .withStyle(ChatFormatting.YELLOW)
                                .append(Component.literal(percent + "%").withStyle(ChatFormatting.RED))
                );
            }
        }
    }
}
