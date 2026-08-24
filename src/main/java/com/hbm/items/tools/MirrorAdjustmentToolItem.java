package com.hbm.items.tools;

import com.hbm.blockentity.machine.MachineSolarBoilerBlockEntity;
import com.hbm.blockentity.machine.SolarMirrorBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MirrorAdjustmentToolItem extends SwordItem {

    private static final String TARGET = "solarTarget";
    private static final double MAX_DISTANCE_SQUARED = 10_000D;

    public MirrorAdjustmentToolItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if(player == null) return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if(level.getBlockState(clickedPos).getBlock() == NtmBlocks.MACHINE_SOLAR_BOILER.get()) {
            DummyableBlock boilerBlock = (DummyableBlock)NtmBlocks.MACHINE_SOLAR_BOILER.get();
            BlockPos corePos = boilerBlock.findCore(level, clickedPos);
            if(corePos == null || !(level.getBlockEntity(corePos) instanceof MachineSolarBoilerBlockEntity)) {
                return InteractionResult.FAIL;
            }

            if(!level.isClientSide) {
                CompoundTag tag = TagsUtil.getCustomData(stack);
                tag.putLong(TARGET, corePos.above().asLong());
                TagsUtil.putCustomData(stack, tag);
                player.displayClientMessage(Component.translatable("message.hbm.mirror_tool.linked").withStyle(ChatFormatting.YELLOW), false);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if(level.getBlockState(clickedPos).getBlock() == NtmBlocks.SOLAR_MIRROR.get() && TagsUtil.getCustomData(stack).contains(TARGET)) {
            if(!level.isClientSide) {
                BlockPos target = BlockPos.of(TagsUtil.getCustomData(stack).getLong(TARGET));
                Vec3 mirrorPivot = Vec3.atBottomCenterOf(clickedPos).add(0D, 1D, 0D);
                Vec3 targetCenter = Vec3.atCenterOf(target);
                Vec3 targetDirection = targetCenter.subtract(mirrorPivot);
                boolean withinReach = targetDirection.lengthSqr() <= MAX_DISTANCE_SQUARED;
                boolean withinAngle = targetDirection.y >= 0D;

                if(!withinReach) {
                    player.displayClientMessage(Component.translatable("message.hbm.mirror_tool.reach").withStyle(ChatFormatting.RED), false);
                } else if(!withinAngle) {
                    player.displayClientMessage(Component.translatable("message.hbm.mirror_tool.angle").withStyle(ChatFormatting.RED), false);
                } else {
                    BlockEntity blockEntity = level.getBlockEntity(clickedPos);
                    if(blockEntity instanceof SolarMirrorBlockEntity mirror) mirror.setTarget(target);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("tooltip.hbm.mirror_tool.0").withStyle(ChatFormatting.YELLOW));
        components.add(Component.translatable("tooltip.hbm.mirror_tool.1").withStyle(ChatFormatting.YELLOW));
    }
}
