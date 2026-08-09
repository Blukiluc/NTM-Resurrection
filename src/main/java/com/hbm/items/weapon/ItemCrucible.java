package com.hbm.items.weapon;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ItemCrucible extends Item {

	public static final SoundEvent C_DEPLOY = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hbm", "weapon.cDeploy"));
	public static final SoundEvent C_SWING = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hbm", "weapon.cSwing"));

	private final float attackDamage;
	private final double movementSpeed;

	public ItemCrucible(float damage, double movement, Properties properties) {
		super(properties.durability(3).attributes(createAttributes(damage, movement)));
		this.attackDamage = 5000;
		this.movementSpeed = 5;
	}

	private static ItemAttributeModifiers createAttributes(float damage, double movement) {
		return ItemAttributeModifiers.builder()
				.add(
						Attributes.ATTACK_DAMAGE,
						new AttributeModifier(BASE_ATTACK_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.add(
						Attributes.MOVEMENT_SPEED,
						new AttributeModifier(BASE_ATTACK_SPEED_ID, movement, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
						EquipmentSlotGroup.MAINHAND
				)
				.build();
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (!level.isClientSide() && isSelected && entity instanceof Player player) {
			if (stack.getDamageValue() < stack.getMaxDamage()) {
			}
		}
	}

	public void onEquip(Player player, ItemStack stack) {
		if (!player.level().isClientSide) {
			if (stack.getDamageValue() < stack.getMaxDamage()) {
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), C_DEPLOY, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Player player) {
			if (player.getGameProfile().getName().equals("Tankish")) {
				stack.setDamageValue(0);
			}

			if (stack.getDamageValue() >= stack.getMaxDamage()) {
				return false;
			}

			if (player.level().isClientSide) {
				player.level().playSound(player, player.getX(), player.getY(), player.getZ(), C_SWING, SoundSource.PLAYERS, 0.8F + player.getRandom().nextFloat() * 0.2F, 1.0F);
			}
		}
		return false;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
		boolean active = stack.getDamageValue() < stack.getMaxDamage();

		if (active) {
			attacker.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(),
					SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minecraft", "block.wood.break")),
					SoundSource.PLAYERS, 1.0F, 0.75F + victim.getRandom().nextFloat() * 0.2F);

			if (!attacker.level().isClientSide() && !victim.isAlive() && attacker.level() instanceof ServerLevel serverLevel) {
				int count = Math.min((int) Math.ceil(victim.getMaxHealth() / 3.0D), 250);

				serverLevel.sendParticles(
						new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
						victim.getX(), victim.getY() + victim.getBbHeight() * 0.5, victim.getZ(),
						count * 4, 0.1D, 0.1D, 0.1D, 0.05D
				);
			}

			if (attacker instanceof Player player && (player.getGameProfile().getName().equals("Tankish") || player.getGameProfile().getName().equals("Tankish020"))) {
				return true;
			}

			stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
			return true;
		} else {
			if (!attacker.level().isClientSide() && attacker instanceof Player player) {
				player.displayClientMessage(Component.literal("Not enough energy.").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))), false);
			}
			return false;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		StringBuilder charge = new StringBuilder("Charge [");

		int currentDamage = stack.getDamageValue();
		for (int i = 2; i >= 0; i--) {
			if (currentDamage <= i) {
				charge.append("||||||");
			} else {
				charge.append("   ");
			}
		}
		charge.append("]");

		tooltipComponents.add(Component.literal(charge.toString()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
	}
}