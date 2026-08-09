package com.hbm.items.tools;

import com.hbm.blockentity.network.PylonBaseBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class CableDrumItem extends Item {

    private static final String START = "wireStart";

    public CableDrumItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if(player == null) return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();
        CompoundTag tag = TagsUtil.getCustomData(stack);
        if(player.isShiftKeyDown()) {
            if(!level.isClientSide) {
                tag.remove(START);
                TagsUtil.putCustomData(stack, tag);
                player.displayClientMessage(Component.translatable("message.hbm.cable_drum.cleared"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        BlockPos selected = this.findPylon(level, context.getClickedPos());
        if(selected == null || !(level.getBlockEntity(selected) instanceof PylonBaseBlockEntity pylon)) return InteractionResult.PASS;

        if(!tag.contains(START)) {
            if(!level.isClientSide) {
                tag.putLong(START, selected.asLong());
                TagsUtil.putCustomData(stack, tag);
                player.displayClientMessage(Component.translatable("message.hbm.cable_drum.start", selected.getX(), selected.getY(), selected.getZ()), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if(!level.isClientSide) {
            BlockPos start = BlockPos.of(tag.getLong(START));
            tag.remove(START);
            TagsUtil.putCustomData(stack, tag);
            BlockEntity startEntity = level.getBlockEntity(start);
            if(!(startEntity instanceof PylonBaseBlockEntity first)) {
                player.displayClientMessage(Component.translatable("message.hbm.cable_drum.missing"), true);
                return InteractionResult.CONSUME;
            }
            if(first.connectTo(pylon)) {
                player.displayClientMessage(Component.translatable("message.hbm.cable_drum.connected"), true);
            } else {
                player.displayClientMessage(Component.translatable("message.hbm.cable_drum.invalid"), true);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private BlockPos findPylon(Level level, BlockPos pos) {
        if(level.getBlockState(pos).getBlock() instanceof DummyableBlock dummy) return dummy.findCore(level, pos);
        return level.getBlockEntity(pos) instanceof PylonBaseBlockEntity ? pos : null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        CompoundTag tag = TagsUtil.getCustomData(stack);
        if(tag.contains(START)) {
            BlockPos pos = BlockPos.of(tag.getLong(START));
            components.add(Component.translatable("tooltip.hbm.cable_drum.selected", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.YELLOW));
        } else {
            components.add(Component.translatable("tooltip.hbm.cable_drum").withStyle(ChatFormatting.GRAY));
        }
    }
}
