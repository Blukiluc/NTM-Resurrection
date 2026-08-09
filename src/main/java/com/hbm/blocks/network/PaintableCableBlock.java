package com.hbm.blocks.network;

import api.hbm.block.IToolable;
import com.hbm.blockentity.network.PaintableCableBlockEntity;
import com.hbm.blocks.ITooltipProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class PaintableCableBlock extends CableBlock implements IToolable, ITooltipProvider {

    public PaintableCableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PaintableCableBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.getItem() instanceof BlockItem blockItem && level.getBlockEntity(pos) instanceof PaintableCableBlockEntity cable) {
            BlockState paint = blockItem.getBlock().defaultBlockState();

            if(cable.getPaintedState() == null && paint.getBlock() != this && paint.getBlock() != Blocks.GRASS_BLOCK && paint.canOcclude() && paint.getRenderShape() == RenderShape.MODEL && paint.isCollisionShapeFullBlock(level, pos)) {
                cable.setPaintedState(paint);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public boolean onScrew(Level level, Player player, BlockPos pos, Direction direction, ToolType tool) {
        if(!(level.getBlockEntity(pos) instanceof PaintableCableBlockEntity cable)) return false;

        if(tool == ToolType.SCREWDRIVER && cable.getPaintedState() != null) {
            cable.setPaintedState(null);
            return true;
        }

        if(tool == ToolType.DEFUSER) {
            cable.setPortsVisible(!cable.arePortsVisible());
            return true;
        }

        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }
}
