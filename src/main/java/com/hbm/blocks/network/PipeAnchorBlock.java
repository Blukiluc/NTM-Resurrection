package com.hbm.blocks.network;

import com.hbm.blockentity.network.PipeAnchorBlockEntity;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.tools.WrenchItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import javax.annotation.Nullable;
import java.util.List;

public class PipeAnchorBlock extends FluidDuctBaseBlock implements ITooltipProvider, ILookOverlay {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public PipeAnchorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PipeAnchorBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING).getOpposite();
        double min = 4D / 16D;
        double max = 12D / 16D;
        double minX = direction == Direction.WEST ? 0D : min;
        double maxX = direction == Direction.EAST ? 1D : max;
        double minY = direction == Direction.DOWN ? 0D : min;
        double maxY = direction == Direction.UP ? 1D : max;
        double minZ = direction == Direction.NORTH ? 0D : min;
        double maxZ = direction == Direction.SOUTH ? 1D : max;
        return Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.getItem() instanceof WrenchItem) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }

    @Override
    public void changeTypeRecursively(Level level, BlockPos pos, FluidType type, int loopsRemaining) {
        if(!(level.getBlockEntity(pos) instanceof PipeAnchorBlockEntity pipe) || pipe.getFluidType() == type) return;

        pipe.setFluidType(type);
        if(loopsRemaining <= 0) return;

        Direction direction = pipe.getBlockState().getValue(FACING).getOpposite();
        BlockPos localPos = pos.relative(direction);
        if(level.getBlockState(localPos).getBlock() instanceof IBlockFluidDuct duct) {
            duct.changeTypeRecursively(level, localPos, type, loopsRemaining - 1);
        }

        for(BlockPos remotePos : pipe.getConnected()) {
            if(level.getBlockState(remotePos).getBlock() instanceof IBlockFluidDuct duct) {
                duct.changeTypeRecursively(level, remotePos, type, loopsRemaining - 1);
            }
        }
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        if(!(level.getBlockEntity(pos) instanceof PipeAnchorBlockEntity pipe)) return;

        FluidType type = pipe.getFluidType();
        List<Component> text = List.of(Component.translatable(type.getUnlocalizedName()).withColor(type.getColor()));
        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
