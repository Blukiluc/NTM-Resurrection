package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.oil.MachineFractioningTowerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineFractioningTowerBlock extends DummyableBlock implements ILookOverlay {

    public static final MapCodec<MachineFractioningTowerBlock> CODEC = simpleCodec(MachineFractioningTowerBlock::new);

    public MachineFractioningTowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineFractioningTowerBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> {
            if(be instanceof ITickable tickable) {
                tickable.updateEntity();
            }
        };
    }

    @Override
    public MapCodec<MachineFractioningTowerBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide && !player.isShiftKeyDown()) {
            if(stack.getItem() instanceof IItemFluidIdentifier identifier) {
                BlockPos corePos = this.findCore(level, pos);
                if(corePos == null) return ItemInteractionResult.FAIL;

                BlockEntity blockEntity = level.getBlockEntity(corePos);
                if(!(blockEntity instanceof MachineFractioningTowerBlockEntity fractionTower)) return ItemInteractionResult.FAIL;

                if(level.getBlockEntity(corePos.below(3)) instanceof MachineFractioningTowerBlockEntity) {
                    player.displayClientMessage(Component.translatable("chat.fraction_tower.bottom_only").withStyle(ChatFormatting.RED), false);
                } else {
                    FluidType type = identifier.getType(level, corePos, stack);
                    fractionTower.tanks[0].setTankType(type);
                    fractionTower.setChanged();
                    player.displayClientMessage(Component.translatable("chat.fraction_tower.changed", Component.translatable(type.getUnlocalizedName())), false);
                }

                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.FAIL;
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 2, 0, 1, 1, 1, 1 };
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);
        this.makeExtra(level, corePos.east());
        this.makeExtra(level, corePos.west());
        this.makeExtra(level, corePos.south());
        this.makeExtra(level, corePos.north());
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof MachineFractioningTowerBlockEntity fractionTower)) return;

        List<Component> text = new ArrayList<>();

        for(int i = 0; i < fractionTower.tanks.length; i++) {
            FluidTank tank = fractionTower.tanks[i];
            ChatFormatting color = i == 0 ? ChatFormatting.GREEN : ChatFormatting.RED;
            String arrow = i == 0 ? "-> " : "<- ";

            text.add(Component.empty()
                    .append(Component.literal(arrow).withStyle(color))
                    .append(Component.translatable(tank.getTankType().getUnlocalizedName()))
                    .append(Component.literal(": " + tank.getFill() + "/" + tank.getMaxFill() + " mB")));
        }

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
