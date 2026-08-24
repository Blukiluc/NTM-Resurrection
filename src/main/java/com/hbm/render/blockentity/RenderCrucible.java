package com.hbm.render.blockentity;

import api.hbm.block.ICrucibleAcceptor;
import com.hbm.blockentity.machine.MachineCrucibleBlockEntity;
import com.hbm.blockentity.machine.foundry.FoundryBaseBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.material.Mats.MaterialStack;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RenderCrucible extends BlockEntityRendererNT<MachineCrucibleBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineCrucibleBlockEntity> create(Context context) {
        return new RenderCrucible();
    }

    @Override
    public void render(MachineCrucibleBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        this.renderPour(be, buffer, facing.getCounterClockWise(Direction.Axis.Y), this.firstMaterial(be.getRecipeOutput()));
        this.renderPour(be, buffer, facing.getClockWise(), this.firstMaterial(be.wasteStack));

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch (facing) {
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }

        bindTexture(ResourceManager.CRUCIBLE_TEX);
        ResourceManager.crucible.renderPart("Main");

        int total = be.getAmount(be.recipeStack, null) + be.getAmount(be.wasteStack, null);
        if (total > 0) {
            int color = !be.recipeStack.isEmpty() ? be.recipeStack.get(0).material.moltenColor : be.wasteStack.get(0).material.moltenColor;
            float red = ((color >> 16) & 255) / 255F;
            float green = ((color >> 8) & 255) / 255F;
            float blue = (color & 255) / 255F;
            RenderContext.pushPose();
            RenderContext.translate(0F, (float) total / (MachineCrucibleBlockEntity.RECIPE_CAPACITY + MachineCrucibleBlockEntity.WASTE_CAPACITY) * 0.875F, 0F);
            RenderContext.setColor(red, green, blue, 1F);
            RenderContext.setLight(LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above()));
            bindTexture(ResourceManager.MOLTEN_METAL_TEX);
            ResourceManager.crucible.renderPart("Lava");
            RenderContext.setColor(1F, 1F, 1F, 1F);
            RenderContext.popPose();
        }
    }

    private MaterialStack firstMaterial(List<MaterialStack> materials) {
        for (MaterialStack material : materials) {
            if (material.amount > 0) return new MaterialStack(material.material, 1);
        }
        return null;
    }

    private void renderPour(MachineCrucibleBlockEntity crucible, MultiBufferSource buffer,
                            Direction direction, MaterialStack sample) {
        if (sample == null || crucible.getLevel() == null) return;
        PourTarget target = this.findPourTarget(crucible, direction, sample);
        if (target == null) return;

        float innerX = 0.5F + direction.getStepX() * 1.25F;
        float innerZ = 0.5F + direction.getStepZ() * 1.25F;
        float outerX = 0.5F + direction.getStepX() * 1.5F;
        float outerZ = 0.5F + direction.getStepZ() * 1.5F;
        float pourX = 0.5F + direction.getStepX() * 1.875F;
        float pourZ = 0.5F + direction.getStepZ() * 1.875F;
        float halfWidth = 0.125F;
        float lipHeight = 0.374F;
        int color = sample.material.moltenColor;

        // The first section is the Crucible's own sloped gutter. Rendering it makes
        // the metal visibly leave the basin instead of appearing only in the receiver.
        RenderFoundry.renderSlopedSurface(buffer, innerX, innerZ, 0.624F,
                outerX, outerZ, lipHeight, halfWidth, direction, color);

        // Continue to the center line of the first foundry channel so there is no
        // visual gap between the machine outlet and the connected channel arm.
        if (direction.getAxis() == Direction.Axis.X) {
            RenderFoundry.renderSurface(buffer, Math.min(outerX, pourX), pourZ - halfWidth,
                    Math.max(outerX, pourX), pourZ + halfWidth, lipHeight, color, 1F);
        } else {
            RenderFoundry.renderSurface(buffer, pourX - halfWidth, Math.min(outerZ, pourZ),
                    pourX + halfWidth, Math.max(outerZ, pourZ), lipHeight, color, 1F);
        }

        RenderFoundry.renderFallingStream(buffer,
                pourX - halfWidth, pourZ - halfWidth, pourX + halfWidth, pourZ + halfWidth,
                lipHeight - 0.001F, target.bottomY, color);
    }

    private PourTarget findPourTarget(MachineCrucibleBlockEntity crucible, Direction direction, MaterialStack sample) {
        double x = crucible.getBlockPos().getX() + 0.5D + direction.getStepX() * 1.875D;
        double y = crucible.getBlockPos().getY() + 0.25D;
        double z = crucible.getBlockPos().getZ() + 0.5D + direction.getStepZ() * 1.875D;

        for (int distance = 0; distance <= 6; distance++) {
            BlockPos targetPos = BlockPos.containing(x, y - distance, z);
            BlockEntity blockEntity = crucible.getLevel().getBlockEntity(targetPos);
            if (blockEntity instanceof ICrucibleAcceptor acceptor) {
                Vec3 hit = Vec3.atCenterOf(targetPos).add(0D, 0.5D, 0D);
                if (!acceptor.canAcceptPartialPour(crucible.getLevel(), hit, Direction.UP, sample)) return null;
                float surfaceHeight = blockEntity instanceof FoundryBaseBlockEntity foundry
                        ? RenderFoundry.getFluidSurfaceHeight(foundry) : 0.875F;
                return new PourTarget(targetPos.getY() - crucible.getBlockPos().getY() + surfaceHeight + 0.02F);
            }

            BlockState state = crucible.getLevel().getBlockState(targetPos);
            if (!state.getFluidState().isEmpty()
                    || !state.getCollisionShape(crucible.getLevel(), targetPos).isEmpty()) return null;
        }
        return null;
    }

    private record PourTarget(float bottomY) { }

    @Override
    public AABB getRenderBoundingBox(MachineCrucibleBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new AABB(x - 2, y - 6, z - 2, x + 3, y + 2, z + 3);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CRUCIBLE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.CRUCIBLE_TEX);
                ResourceManager.crucible.renderPart("Main");
            }
        };
    }
}
