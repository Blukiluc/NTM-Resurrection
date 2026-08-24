package com.hbm.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class ItemRendererCrucible extends BlockEntityWithoutLevelRenderer {

	public static final ItemRendererCrucible INSTANCE = new ItemRendererCrucible();

	private static final ResourceLocation MODEL_CRUCIBLE = ResourceLocation.fromNamespaceAndPath("hbm", "weapons/crucible_sword.obj");
	private static final ResourceLocation TEXTURE_HILT = ResourceLocation.fromNamespaceAndPath("hbm", "textures/models/weapon/crucible_hilt.png");
	private static final ResourceLocation TEXTURE_GUARD = ResourceLocation.fromNamespaceAndPath("hbm", "textures/models/weapon/crucible_guard.png");
	private static final ResourceLocation TEXTURE_BLADE = ResourceLocation.fromNamespaceAndPath("hbm", "textures/models/weapon/crucible_blade.png");

	public ItemRendererCrucible() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();

		boolean isOn = stack.getDamageValue() < stack.getMaxDamage();

		switch (context) {
			case FIRST_PERSON_RIGHT_HAND:
			case FIRST_PERSON_LEFT_HAND:
				poseStack.translate(1.5, -0.3, 0);
				poseStack.scale(0.3f, 0.3f, 0.3f);
				poseStack.mulPose(Axis.ZP.rotationDegrees(45));
				poseStack.mulPose(Axis.YP.rotationDegrees(90));
				break;

			case GUI:
				poseStack.translate(0.5, 0.5, 0);
				poseStack.mulPose(Axis.ZP.rotationDegrees(-135));
				poseStack.mulPose(Axis.YP.rotationDegrees(90));
				poseStack.scale(0.15f, 0.15f, 0.15f);
				break;

			default:
				poseStack.translate(0.5, 0.5, 0.5);
				poseStack.scale(0.2f, 0.2f, 0.2f);
				break;
		}
		BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(MODEL_CRUCIBLE));

		if (model != null) {
			VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_HILT));


			Minecraft.getInstance().getItemRenderer().renderModelLists(
					model,
					stack,
					packedLight,
					packedOverlay,
					poseStack,
					vertexConsumer
			);
		}


		poseStack.popPose();





		VertexConsumer hiltConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_HILT));
		VertexConsumer guardConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_GUARD));

		float rotGuard = isOn ? 0f : 90f;

		poseStack.pushPose();
		poseStack.translate(0, 3, 0.5);
		poseStack.mulPose(Axis.XP.rotationDegrees(-rotGuard));
		poseStack.translate(0, -3, -0.5);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(0, 3, -0.5);
		poseStack.mulPose(Axis.XP.rotationDegrees(rotGuard));
		poseStack.translate(0, -3, 0.5);
		poseStack.popPose();

		if (isOn) {
			poseStack.pushPose();
			poseStack.translate(0.005, 0, 0);
			int glowLight = LightTexture.FULL_BRIGHT;
			VertexConsumer bladeConsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_BLADE));
			poseStack.popPose();
		}

		poseStack.popPose();
	}
}