package com.hbm.util;

import api.hbm.block.ICrucibleAcceptor;
import com.hbm.blockentity.machine.foundry.FoundryCastingBlockEntity;
import com.hbm.inventory.material.MaterialShapes;
import com.hbm.inventory.material.Mats.MaterialStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class CrucibleUtil {

    private CrucibleUtil() {
    }

    public static MaterialStack pourFullStack(Level level, double x, double y, double z, int range, List<MaterialStack> materials, int maximum) {
        for (int distance = 0; distance <= range; distance++) {
            BlockPos targetPos = BlockPos.containing(x, y - distance, z);
            BlockEntity blockEntity = level.getBlockEntity(targetPos);
            if (blockEntity instanceof ICrucibleAcceptor acceptor) {
                Vec3 hit = Vec3.atCenterOf(targetPos).add(0, 0.5, 0);
                int transferLimit = blockEntity instanceof FoundryCastingBlockEntity
                        ? Math.min(maximum, MaterialShapes.NUGGET.q(1)) : maximum;
                for (MaterialStack stored : materials) {
                    if (stored.amount <= 0) continue;
                    int offered = Math.min(transferLimit, stored.amount);
                    MaterialStack incoming = new MaterialStack(stored.material, offered);
                    if (!acceptor.canAcceptPartialPour(level, hit, Direction.UP, incoming)) continue;
                    MaterialStack left = acceptor.pour(level, hit, Direction.UP, incoming);
                    int accepted = offered - (left == null ? 0 : left.amount);
                    if (accepted > 0) {
                        stored.amount -= accepted;
                        return new MaterialStack(stored.material, accepted);
                    }
                }
                return null;
            }
            var state = level.getBlockState(targetPos);
            if (!state.getFluidState().isEmpty() || !state.getCollisionShape(level, targetPos).isEmpty()) return null;
        }
        return null;
    }
}
