package com.hbm.util;

import api.hbm.block.ICrucibleAcceptor;
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
                for (MaterialStack stored : materials) {
                    if (stored.amount <= 0) continue;
                    int offered = Math.min(maximum, stored.amount);
                    MaterialStack left = acceptor.pour(level, hit, Direction.UP, new MaterialStack(stored.material, offered));
                    int accepted = offered - (left == null ? 0 : left.amount);
                    if (accepted > 0) {
                        stored.amount -= accepted;
                        return new MaterialStack(stored.material, accepted);
                    }
                }
                return null;
            }
            if (!level.getBlockState(targetPos).isAir()) return null;
        }
        return null;
    }
}
