package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.SolarMirrorBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class RenderSolarMirror extends BlockEntityRendererNT<SolarMirrorBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<SolarMirrorBlockEntity> create(Context context) {
        return new RenderSolarMirror();
    }

    @Override
    public void render(SolarMirrorBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.pushPose();
        RenderContext.translate(0.5F, 0F, 0.5F);
        this.renderMachine(be);
        RenderContext.popPose();

        if(be.isOn && be.hasTarget()) this.renderBeam(be, buffer);
    }

    private void renderMachine(SolarMirrorBlockEntity be) {
        RenderSystem.disableCull();
        this.bindTexture(ResourceManager.SOLAR_MIRROR_TEX);
        ResourceManager.solar_mirror.renderPart("Base");

        if(be != null && be.hasTarget() && be.getTarget().getY() > be.getBlockPos().getY()) {
            BlockPos delta = be.getTarget().subtract(be.getBlockPos());
            Vec3 targetDirection = new Vec3(delta.getX(), delta.getY() - 0.5D, delta.getZ()).normalize();
            Vec3 mirrorNormal = targetDirection.add(0D, 1D, 0D).normalize();
            double pitch = Math.toDegrees(Math.acos(Mth.clamp(mirrorNormal.y, -1D, 1D)));
            double yaw = Math.toDegrees(Math.atan2(mirrorNormal.x, mirrorNormal.z));

            RenderContext.translate(0F, 1F, 0F);
            RenderContext.mulPose(Axis.YP.rotationDegrees((float)yaw));
            RenderContext.mulPose(Axis.XP.rotationDegrees((float)pitch));
            RenderContext.translate(0F, -1F, 0F);
        }

        ResourceManager.solar_mirror.renderPart("Mirror");
        RenderSystem.enableCull();
    }

    private void renderItem() {
        RenderSystem.disableCull();
        this.bindTexture(ResourceManager.SOLAR_MIRROR_TEX);
        ResourceManager.solar_mirror.renderPart("Base");
        RenderContext.pushPose();
        RenderContext.translate(0F, 1F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(-45F));
        RenderContext.translate(0F, -1F, 0F);
        ResourceManager.solar_mirror.renderPart("Mirror");
        RenderContext.popPose();
        RenderSystem.enableCull();
    }

    private void renderBeam(SolarMirrorBlockEntity be, MultiBufferSource buffer) {
        Vec3 start = new Vec3(0.5D, 1.0625D, 0.5D);
        BlockPos delta = be.getTarget().subtract(be.getBlockPos());
        Vec3 end = new Vec3(delta.getX() + 0.5D, delta.getY() + 0.5D, delta.getZ() + 0.5D);
        Vec3 direction = end.subtract(start).normalize();
        Vec3 right = direction.cross(new Vec3(0D, 1D, 0D));
        if(right.lengthSqr() < 1.0E-6D) right = new Vec3(1D, 0D, 0D);
        right = right.normalize().scale(0.5D);
        Vec3 up = right.cross(direction).normalize().scale(0.5D);

        Vec3[] startCorners = new Vec3[] {
                start.add(right).add(up),
                start.add(right).subtract(up),
                start.subtract(right).subtract(up),
                start.subtract(right).add(up)
        };
        Vec3[] endCorners = new Vec3[] {
                end.add(right).add(up),
                end.add(right).subtract(up),
                end.subtract(right).subtract(up),
                end.subtract(right).add(up)
        };

        VertexConsumer consumer = buffer.getBuffer(NtmRenderTypes.GLOW);
        Matrix4f matrix = RenderContext.poseStack().last().pose();
        for(int side = 0; side < 4; side++) {
            int next = (side + 1) % 4;
            consumer.addVertex(matrix, (float)startCorners[side].x, (float)startCorners[side].y, (float)startCorners[side].z).setColor(1F, 1F, 1F, 0.05F);
            consumer.addVertex(matrix, (float)startCorners[next].x, (float)startCorners[next].y, (float)startCorners[next].z).setColor(1F, 1F, 1F, 0.05F);
            consumer.addVertex(matrix, (float)endCorners[next].x, (float)endCorners[next].y, (float)endCorners[next].z).setColor(1F, 1F, 1F, 0.005F);
            consumer.addVertex(matrix, (float)endCorners[side].x, (float)endCorners[side].y, (float)endCorners[side].z).setColor(1F, 1F, 1F, 0.005F);
        }
    }

    @Override
    public AABB getRenderBoundingBox(SolarMirrorBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 101, y - 101, z - 101, x + 102, y + 102, z + 102);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.SOLAR_MIRROR.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -3F, 0F);
                RenderContext.scale(8F, 8F, 8F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderSolarMirror.this.renderItem();
            }
        };
    }
}
