package com.hbm.items.tools;

import com.hbm.blockentity.network.PipelineBaseBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.NuclearTechModClient;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

public class WrenchItem extends SwordItem {

    private static final String START = "pipeStart";

    public WrenchItem(Tier tier, Properties properties) {
        super(tier, properties);
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
                player.displayClientMessage(Component.translatable("message.hbm.wrench.cleared"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        BlockPos selected = this.findPipeline(level, context.getClickedPos());
        if(selected == null || !(level.getBlockEntity(selected) instanceof PipelineBaseBlockEntity pipeline)) return InteractionResult.PASS;

        if(!tag.contains(START)) {
            if(!level.isClientSide) {
                tag.putLong(START, selected.asLong());
                TagsUtil.putCustomData(stack, tag);
                player.displayClientMessage(Component.translatable("message.hbm.wrench.start"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if(!level.isClientSide) {
            BlockPos start = BlockPos.of(tag.getLong(START));
            tag.remove(START);
            TagsUtil.putCustomData(stack, tag);
            BlockEntity startEntity = level.getBlockEntity(start);
            if(!(startEntity instanceof PipelineBaseBlockEntity first)) {
                player.displayClientMessage(Component.translatable("message.hbm.wrench.missing"), true);
                return InteractionResult.CONSUME;
            }

            PipelineBaseBlockEntity.ConnectionResult result = first.connectTo(pipeline);
            player.displayClientMessage(Component.translatable(this.getMessage(result)), true);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private BlockPos findPipeline(Level level, BlockPos pos) {
        if(level.getBlockState(pos).getBlock() instanceof DummyableBlock dummy) return dummy.findCore(level, pos);
        return level.getBlockEntity(pos) instanceof PipelineBaseBlockEntity ? pos : null;
    }

    private String getMessage(PipelineBaseBlockEntity.ConnectionResult result) {
        return switch(result) {
            case CONNECTED -> "message.hbm.wrench.connected";
            case INCOMPATIBLE -> "message.hbm.wrench.incompatible";
            case SAME_ANCHOR -> "message.hbm.wrench.same";
            case TOO_FAR -> "message.hbm.wrench.too_far";
            case FLUID_MISMATCH -> "message.hbm.wrench.fluid_mismatch";
            case ALREADY_CONNECTED -> "message.hbm.wrench.already_connected";
        };
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Vec3 motion = attacker.getLookAngle().scale(0.5D);
        target.push(motion.x, motion.y, motion.z);
        target.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 3.0F, 0.75F);
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(!level.isClientSide) return;

        CompoundTag tag = TagsUtil.getCustomData(stack);
        if(!tag.contains(START)) return;

        BlockPos start = BlockPos.of(tag.getLong(START));
        int distance = (int)entity.position().distanceTo(Vec3.atLowerCornerOf(start));
        NuclearTechMod.proxy.displayTooltip(
                stack.getHoverName().copy().append(Component.literal(": " + distance + "m")),
                100,
                NuclearTechModClient.ID_WRENCH
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        CompoundTag tag = TagsUtil.getCustomData(stack);
        if(tag.contains(START)) {
            BlockPos pos = BlockPos.of(tag.getLong(START));
            components.add(Component.translatable("tooltip.hbm.wrench.selected", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.YELLOW));
        } else {
            components.add(Component.translatable("tooltip.hbm.wrench").withStyle(ChatFormatting.GRAY));
        }
    }
}
