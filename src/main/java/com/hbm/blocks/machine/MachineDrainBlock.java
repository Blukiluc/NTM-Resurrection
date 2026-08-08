package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.MachineDrainBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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

public class MachineDrainBlock extends DummyableBlock implements ILookOverlay {

    public MachineDrainBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(state.getValue(TYPE) == DummyBlockType.CORE) return new MachineDrainBlockEntity(pos, state);
        return null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineDrainBlock> CODEC = simpleCodec(MachineDrainBlock::new);
    @Override public MapCodec<MachineDrainBlock> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] {0, 0, 2, 0, 0, 0}; }
    @Override public int getOffset() { return 0; }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide && !player.isShiftKeyDown()) {
            if(stack.getItem() instanceof IItemFluidIdentifier identifier) {
                BlockPos corePos = this.findCore(level, pos);
                if(corePos == null) return ItemInteractionResult.FAIL;

                BlockEntity blockEntity = level.getBlockEntity(corePos);
                if(!(blockEntity instanceof MachineDrainBlockEntity drain)) return ItemInteractionResult.FAIL;

                FluidType type = identifier.getType(level, corePos, stack);
                drain.tank.setTankType(type);
                drain.setChanged();
                player.displayClientMessage(
                        Component.literal("Changed type to ")
                                .append(type.getName())
                                .append(Component.literal("!"))
                                .withStyle(ChatFormatting.YELLOW),
                        false
                );

                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.FAIL;
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof MachineDrainBlockEntity drain)) return;

        FluidTank tank = drain.tank;
        List<Component> text = new ArrayList<>();
        text.add(Component.empty()
                .append(Component.literal("-> ").withStyle(ChatFormatting.GREEN))
                .append(tank.getTankType().getName())
                .append(Component.literal(": " + tank.getFill() + "/" + tank.getMaxFill() + "mB")));
        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
