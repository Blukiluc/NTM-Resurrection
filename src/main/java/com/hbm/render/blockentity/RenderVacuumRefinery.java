package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.MachineVacuumRefineryBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderVacuumRefinery extends BlockEntityRendererNT<MachineVacuumRefineryBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineVacuumRefineryBlockEntity> create(Context context) {
        return new RenderVacuumRefinery();
    }

    @Override
    public void render(MachineVacuumRefineryBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);

        bindTexture(ResourceManager.VACUUM_REFINERY_TEX);
        ResourceManager.vacuum_refinery.renderAll();
    }

    @Override
    public AABB getRenderBoundingBox(MachineVacuumRefineryBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y, z - 2, x + 3, y + 3, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_VACUUM_REFINERY.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                bindTexture(ResourceManager.VACUUM_REFINERY_TEX);
                ResourceManager.vacuum_refinery.renderAll();
            }
        };
    }
}