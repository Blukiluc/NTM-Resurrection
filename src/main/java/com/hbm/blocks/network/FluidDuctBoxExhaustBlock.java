package com.hbm.blocks.network;

import com.hbm.blockentity.network.ExhaustPipeBlockEntity;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class FluidDuctBoxExhaustBlock extends FluidDuctBoxBlock {

    public FluidDuctBoxExhaustBlock(Properties properties, double diameter, double junctionDiameter) {
        super(properties, diameter, junctionDiameter);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExhaustPipeBlockEntity(pos, state);
    }

    @Override
    public int getSubCount() {
        return 1;
    }

    @Override
    public boolean canConnectTo(BlockGetter level, BlockPos pos, Direction dir, FluidType type) {
        BlockPos neighborPos = pos.relative(dir);
        return Library.canConnectFluid(level, neighborPos, dir, Fluids.SMOKE) ||
                Library.canConnectFluid(level, neighborPos, dir, Fluids.SMOKE_LEADED) ||
                Library.canConnectFluid(level, neighborPos, dir, Fluids.SMOKE_POISON);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return ItemInteractionResult.FAIL;
    }

    @Override
    public void changeTypeRecursively(Level level, BlockPos pos, FluidType type, int loopsRemaining) {
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        List<Component> text = new ArrayList<>();

        for(FluidType smoke : ExhaustPipeBlockEntity.SMOKES) {
            text.add(Component.translatable(smoke.getUnlocalizedName()).withColor(smoke.getColor()));
        }

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
