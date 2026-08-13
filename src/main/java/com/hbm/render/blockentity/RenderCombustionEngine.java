package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineCombustionEngineBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.Fluids.CD_Canister;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderCombustionEngine extends BlockEntityRendererNT<MachineCombustionEngineBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineCombustionEngineBlockEntity> create(Context context) {
        return new RenderCombustionEngine();
    }

    @Override
    public void render(MachineCombustionEngineBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }
        RenderContext.translate(-0.5F, 0F, 3F);

        bindTexture(ResourceManager.COMBUSTION_ENGINE_TEX);
        ResourceManager.combustionEngine.renderPart("Engine");

        CD_Canister canister = be.tank.getTankType().getContainer(CD_Canister.class);
        if(canister != null) {
            int color = canister.color;
            RenderContext.setColor(
                    ((color >> 16) & 0xFF) / 255F,
                    ((color >> 8) & 0xFF) / 255F,
                    (color & 0xFF) / 255F,
                    1F
            );
        }
        ResourceManager.combustionEngine.renderPart("Canister");
        RenderContext.setColor(1F, 1F, 1F, 1F);

        RenderContext.translate(1F, 0F, -2.6875F);
        float doorAngle = be.prevDoorAngle + (be.doorAngle - be.prevDoorAngle) * partialTicks;
        RenderContext.mulPose(Axis.YN.rotationDegrees(doorAngle));
        RenderContext.translate(-1F, 0F, 2.6875F);
        ResourceManager.combustionEngine.renderPart("Hatch");
    }

    @Override
    public AABB getRenderBoundingBox(MachineCombustionEngineBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 3, y, z - 3, x + 4, y + 3, z + 4);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_COMBUSTION_ENGINE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1F, 0F);
                RenderContext.scale(2.75F, 2.75F, 2.75F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.translate(0F, 0F, 2.75F);
                bindTexture(ResourceManager.COMBUSTION_ENGINE_TEX);
                ResourceManager.combustionEngine.renderAll();
            }
        };
    }
}
