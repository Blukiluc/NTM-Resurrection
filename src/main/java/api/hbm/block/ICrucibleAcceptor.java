package api.hbm.block;

import com.hbm.inventory.material.Mats.MaterialStack;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface ICrucibleAcceptor {

    boolean canAcceptPartialPour(Level level, Vec3 hit, Direction side, MaterialStack stack);

    MaterialStack pour(Level level, Vec3 hit, Direction side, MaterialStack stack);

    boolean canAcceptPartialFlow(Level level, Direction side, MaterialStack stack);

    MaterialStack flow(Level level, Direction side, MaterialStack stack);
}
