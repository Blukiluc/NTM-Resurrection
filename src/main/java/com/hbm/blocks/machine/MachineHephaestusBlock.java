package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineHephaestusBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

public class MachineHephaestusBlock extends DummyableBlock implements ILookOverlay {

    public static final MapCodec<MachineHephaestusBlock> CODEC = simpleCodec(MachineHephaestusBlock::new);

    public MachineHephaestusBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<MachineHephaestusBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(state.getValue(TYPE)) {
            case CORE -> new MachineHephaestusBlockEntity(pos, state);
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
        return new int[] {11, 0, 1, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            this.makeExtra(level, corePos.relative(direction));
            this.makeExtra(level, corePos.above(11).relative(direction));
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(player.isShiftKeyDown() || !(stack.getItem() instanceof IItemFluidIdentifier identifier)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if(level.isClientSide) return ItemInteractionResult.SUCCESS;

        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return ItemInteractionResult.FAIL;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof MachineHephaestusBlockEntity hephaestus)) return ItemInteractionResult.FAIL;

        FluidType type = identifier.getType(level, corePos, stack);
        FT_Heatable trait = type.getTrait(FT_Heatable.class);
        if(trait == null || !trait.hasSteps() || trait.getEfficiency(FT_Heatable.HeatingType.HEATEXCHANGER) <= 0D) {
            return ItemInteractionResult.FAIL;
        }

        hephaestus.input.setTankType(type);
        hephaestus.setChanged();
        player.displayClientMessage(
                Component.literal("Changed type to ")
                        .append(type.getName())
                        .append(Component.literal("!"))
                        .withStyle(ChatFormatting.YELLOW),
                false
        );
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof MachineHephaestusBlockEntity hephaestus)) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.literal(BobMathUtil.format(hephaestus.bufferedHeat) + " TU"));

        FluidTank[] tanks = hephaestus.getAllTanks();
        for(int i = 0; i < tanks.length; i++) {
            FluidTank tank = tanks[i];
            text.add(Component.literal(i == 0 ? "-> " : "<- ")
                    .withStyle(i == 0 ? ChatFormatting.GREEN : ChatFormatting.RED)
                    .append(tank.getTankType().getName())
                    .append(Component.literal(": " + BobMathUtil.format(tank.getFill()) + "/" + BobMathUtil.format(tank.getMaxFill()) + " mB")
                            .withStyle(ChatFormatting.WHITE)));
        }

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
