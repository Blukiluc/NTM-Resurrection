package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineHephaestusBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderHephaestus extends BlockEntityRendererNT<MachineHephaestusBlockEntity> implements IBEWLRProvider {

    private static final ResourceLocation COBBLESTONE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/block/cobblestone.png");

    @Override
    public BlockEntityRenderer<MachineHephaestusBlockEntity> create(Context context) {
        return new RenderHephaestus();
    }

    @Override
    public void render(MachineHephaestusBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        float movement = Mth.lerp(partialTicks, be.prevRot, be.rot);
        this.renderMachine(movement, be.bufferedHeat > 0);
    }

    private void renderMachine(float movement, boolean isOn) {
        RenderSystem.disableCull();
        bindTexture(ResourceManager.HEPHAESTUS_TEX);
        ResourceManager.hephaestus.renderPart("Main");

        RenderContext.pushPose();
        RenderContext.mulPose(Axis.YP.rotationDegrees(movement));
        for(int i = 0; i < 3; i++) {
            ResourceManager.hephaestus.renderPart("Rotor");
            RenderContext.mulPose(Axis.YP.rotationDegrees(120F));
        }
        RenderContext.popPose();

        if(isOn) {
            bindTexture(ResourceManager.HEPHAESTUS_CORE_TEX);
            RenderContext.setLightning(false);
        } else {
            bindTexture(COBBLESTONE_TEXTURE);
            RenderContext.setColor(0.5F, 0.5F, 0.5F, 1F);
        }

        ResourceManager.hephaestus.renderPart("Core");
        RenderContext.setLightning(true);
        RenderContext.setColor(1F, 1F, 1F, 1F);
        RenderSystem.enableCull();
    }

    @Override
    public AABB getRenderBoundingBox(MachineHephaestusBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 3, y, z - 3, x + 4, y + 12, z + 4);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_HEPHAESTUS.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4.5F, 0F);
                RenderContext.scale(2.25F, 2.25F, 2.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                RenderHephaestus.this.renderMachine((float)((System.currentTimeMillis() / 10L) % 360L), false);
            }
        };
    }
}
