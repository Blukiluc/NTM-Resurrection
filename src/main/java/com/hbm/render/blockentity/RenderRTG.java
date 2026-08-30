package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineRTGBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.lib.Library;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderRTG extends BlockEntityRendererNT<MachineRTGBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineRTGBlockEntity> create(Context context) {
        return new RenderRTG();
    }

    @Override
    public void render(MachineRTGBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
        RenderSystem.disableCull();

        bindTexture(ResourceManager.RTG_TEX);
        ResourceManager.rtg.renderPart("Gen");

        this.renderConnector(be, Direction.EAST, 0F);
        this.renderConnector(be, Direction.WEST, 180F);
        this.renderConnector(be, Direction.NORTH, 90F);
        this.renderConnector(be, Direction.SOUTH, -90F);

        RenderSystem.enableCull();
    }

    private void renderConnector(MachineRTGBlockEntity be, Direction direction, float rotation) {
        if(be.getLevel() == null || !Library.canConnect(be.getLevel(), be.getBlockPos().relative(direction), direction)) return;

        RenderContext.pushPose();
        RenderContext.mulPose(Axis.YP.rotationDegrees(rotation));
        ResourceManager.rtg.renderPart("Connector");
        RenderContext.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(MachineRTGBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 1, y, z - 1, x + 2, y + 2, z + 2);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_RTG.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(8F, 8F, 8F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                RenderSystem.disableCull();
                bindTexture(ResourceManager.RTG_TEX);
                ResourceManager.rtg.renderPart("Gen");
                RenderSystem.enableCull();
            }
        };
    }
}
