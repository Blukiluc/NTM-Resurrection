package com.hbm.blockentity.machine.oil;

import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.recipes.CatalyticCrackingTowerRecipes;
import com.hbm.util.Tuple.Pair;
import com.hbm.util.fauxpointtwelve.DirPos;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCatalyticCrackingTowerBlockEntity extends LoadedBaseBlockEntity implements IFluidStandardTransceiverMK2, IFluidCopiable, ITickable {

    public FluidTank[] tanks;

    public MachineCatalyticCrackingTowerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CATALYTIC_CRACKING_TOWER.get(), pos, state);
        tanks = new FluidTank[5];
        tanks[0] = new FluidTank(Fluids.BITUMEN, 4000);
        tanks[1] = new FluidTank(Fluids.STEAM, 8000);
        tanks[2] = new FluidTank(Fluids.OIL, 4000);
        tanks[3] = new FluidTank(Fluids.PETROLEUM, 4000);
        tanks[4] = new FluidTank(Fluids.SPENTSTEAM, 800);
    }

    @Override
    public void updateEntity() {
        if (this.level == null || this.level.isClientSide) return;

        Level level = this.level;

        level.getProfiler().push("catalyticCracker_setup_tanks");
        setupTanks();
        level.getProfiler().popPush("catalyticCracker_update_connections");
        updateConnections();

        level.getProfiler().popPush("catalyticCracker_do_recipe");
        if (level.getGameTime() % 5 == 0) crack();

        level.getProfiler().popPush("catalyticCracker_send_fluid");
        if (level.getGameTime() % 10 == 0) {
            for (DirPos pos : getConPos()) {
                for (int i = 2; i <= 4; i++) {
                    if (tanks[i].getFill() > 0) this.tryProvide(tanks[i], level, pos);
                }
            }
        }
        level.getProfiler().pop();

        networkPackNT(25);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        for (FluidTank tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        for (FluidTank tank : tanks) tank.deserialize(buf);
    }

    private void updateConnections() {
        for (DirPos pos : getConPos()) {
            this.trySubscribe(tanks[0].getTankType(), level, pos.makeCompat(), pos.getDir());
            this.trySubscribe(tanks[1].getTankType(), level, pos.makeCompat(), pos.getDir());
        }
    }

    private void crack() {
        Pair<FluidStack, FluidStack> quart = CatalyticCrackingTowerRecipes.getCracking(tanks[0].getTankType());

        if (quart == null) return;

        int left = quart.getKey().fill;
        int right = quart.getValue().fill;

        for (int i = 0; i < 2; i++) {
            if (tanks[0].getFill() >= 100 && tanks[1].getFill() >= 200 && hasSpace(left, right)) {
                tanks[0].setFill(tanks[0].getFill() - 100);
                tanks[1].setFill(tanks[1].getFill() - 200);
                tanks[2].setFill(tanks[2].getFill() + left);
                tanks[3].setFill(tanks[3].getFill() + right);
                tanks[4].setFill(tanks[4].getFill() + 2); // LPS has the density of WATER not STEAM (1%!)
            }
        }
    }

    private boolean hasSpace(int left, int right) {
        return tanks[2].getFill() + left <= tanks[2].getMaxFill() && tanks[3].getFill() + right <= tanks[3].getMaxFill() && tanks[4].getFill() + 2 <= tanks[4].getMaxFill();
    }

    private void setupTanks() {
        Pair<FluidStack, FluidStack> quart = CatalyticCrackingTowerRecipes.getCracking(tanks[0].getTankType());

        if (quart != null) {
            tanks[1].setTankType(Fluids.STEAM);
            tanks[2].setTankType(quart.getKey().type);
            tanks[3].setTankType(quart.getValue().type);
            tanks[4].setTankType(Fluids.SPENTSTEAM);
        } else {
            tanks[2].setTankType(Fluids.NONE);
            tanks[3].setTankType(Fluids.NONE);
            tanks[4].setTankType(Fluids.NONE);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for (int i = 0; i < 5; i++) tanks[i].readFromNBT(tag, "tank" + i);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for (int i = 0; i < 5; i++) tanks[i].writeToNBT(tag, "tank" + i);
    }

    protected DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();

        int x = worldPosition.getX();
        int y = worldPosition.getY();
        int z = worldPosition.getZ();

        return new DirPos[]{
                new DirPos(x + dir.getStepX() * 4 + rot.getStepX() * 1, y, z + dir.getStepZ() * 4 + rot.getStepZ() * 1, dir),
                new DirPos(x + dir.getStepX() * 4 - rot.getStepX() * 2, y, z + dir.getStepZ() * 4 - rot.getStepZ() * 2, dir),
                new DirPos(x - dir.getStepX() * 4 + rot.getStepX() * 1, y, z - dir.getStepZ() * 4 + rot.getStepZ() * 1, dir.getOpposite()),
                new DirPos(x - dir.getStepX() * 4 - rot.getStepX() * 2, y, z - dir.getStepZ() * 4 - rot.getStepZ() * 2, dir.getOpposite()),
                new DirPos(x + dir.getStepX() * 2 + rot.getStepX() * 3, y, z + dir.getStepZ() * 2 + rot.getStepZ() * 3, rot),
                new DirPos(x + dir.getStepX() * 2 - rot.getStepX() * 4, y, z + dir.getStepZ() * 2 - rot.getStepZ() * 4, rot),
                new DirPos(x - dir.getStepX() * 2 + rot.getStepX() * 3, y, z - dir.getStepZ() * 2 + rot.getStepZ() * 3, rot.getOpposite()),
                new DirPos(x - dir.getStepX() * 2 - rot.getStepX() * 4, y, z - dir.getStepZ() * 2 - rot.getStepZ() * 4, rot.getOpposite())
        };
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[]{tanks[2], tanks[3], tanks[4]};
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[]{tanks[0], tanks[1]};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return tanks;
    }

    @Override
    public FluidTank getTankToPaste() {
        return tanks[0];
    }
}