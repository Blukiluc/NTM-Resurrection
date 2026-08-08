package com.hbm.blockentity.machine.oil;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.recipes.FractioningRecipes;
import com.hbm.util.Tuple.Pair;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class MachineFractioningTowerBlockEntity extends LoadedBaseBlockEntity implements IFluidStandardTransceiverMK2, IFluidCopiable, ITickable {

    public final FluidTank[] tanks;

    private AABB bb;

    public MachineFractioningTowerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FRACTION_TOWER.get(), pos, state);

        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.HEAVYOIL, 4_000),
                new FluidTank(Fluids.BITUMEN, 4_000),
                new FluidTank(Fluids.SMEAR, 4_000)
        };
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        BlockEntity stack = this.level.getBlockEntity(this.worldPosition.above(3));

        if(stack instanceof MachineFractioningTowerBlockEntity fractionTower) {
            //make types equal
            for(int i = 0; i < 3; i++) {
                fractionTower.tanks[i].setTankType(this.tanks[i].getTankType());
            }

            //calculate transfer
            int oil = Math.min(this.tanks[0].getFill(), fractionTower.tanks[0].getMaxFill() - fractionTower.tanks[0].getFill());
            int left = Math.min(fractionTower.tanks[1].getFill(), this.tanks[1].getMaxFill() - this.tanks[1].getFill());
            int right = Math.min(fractionTower.tanks[2].getFill(), this.tanks[2].getMaxFill() - this.tanks[2].getFill());

            //move oil up, pull fractions down
            this.tanks[0].setFill(this.tanks[0].getFill() - oil);
            this.tanks[1].setFill(this.tanks[1].getFill() + left);
            this.tanks[2].setFill(this.tanks[2].getFill() + right);
            fractionTower.tanks[0].setFill(fractionTower.tanks[0].getFill() + oil);
            fractionTower.tanks[1].setFill(fractionTower.tanks[1].getFill() - left);
            fractionTower.tanks[2].setFill(fractionTower.tanks[2].getFill() - right);

            if(oil > 0 || left > 0 || right > 0) {
                this.setChanged();
                fractionTower.setChanged();
            }
        }

        this.setupTanks();
        this.updateConnections();

        if(this.level.getGameTime() % 10 == 0) {
            this.fractionate();
        }

        this.sendFluid();
        this.networkPackNT(50);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        for(FluidTank tank : this.tanks) {
            tank.serialize(buf);
        }
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        for(FluidTank tank : this.tanks) {
            tank.deserialize(buf);
        }
    }

    private void updateConnections() {
        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(this.tanks[0].getTankType(), this.level, pos.makeCompat(), pos.getDir());
        }
    }

    private void sendFluid() {
        for(DirPos pos : this.getConPos()) {
            this.tryProvide(this.tanks[1], this.level, pos);
            this.tryProvide(this.tanks[2], this.level, pos);
        }
    }

    private DirPos[] getConPos() {
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();

        return new DirPos[] {
                new DirPos(x + 2, y, z, Direction.EAST),
                new DirPos(x - 2, y, z, Direction.WEST),
                new DirPos(x, y, z + 2, Direction.SOUTH),
                new DirPos(x, y, z - 2, Direction.NORTH)
        };
    }

    private void setupTanks() {
        Pair<FluidStack, FluidStack> fractions = FractioningRecipes.INSTANCE.getFractions(this.tanks[0].getTankType());

        if(fractions != null) {
            this.tanks[1].setTankType(fractions.getKey().type);
            this.tanks[2].setTankType(fractions.getValue().type);
        } else {
            this.tanks[0].setTankType(Fluids.NONE);
            this.tanks[1].setTankType(Fluids.NONE);
            this.tanks[2].setTankType(Fluids.NONE);
        }
    }

    private void fractionate() {
        Pair<FluidStack, FluidStack> fractions = FractioningRecipes.INSTANCE.getFractions(this.tanks[0].getTankType());

        if(fractions == null) return;

        int left = fractions.getKey().fill;
        int right = fractions.getValue().fill;

        if(this.tanks[0].getFill() >= 100 && this.hasSpace(left, right)) {
            this.tanks[0].setFill(this.tanks[0].getFill() - 100);
            this.tanks[1].setFill(this.tanks[1].getFill() + left);
            this.tanks[2].setFill(this.tanks[2].getFill() + right);
            this.setChanged();
        }
    }

    private boolean hasSpace(int left, int right) {
        return this.tanks[1].getFill() + left <= this.tanks[1].getMaxFill()
                && this.tanks[2].getFill() + right <= this.tanks[2].getMaxFill();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].readFromNBT(tag, "tank" + i);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].writeToNBT(tag, "tank" + i);
        }
    }

    public AABB getRenderBoundingBox() {
        if(this.bb == null) {
            BlockPos pos = this.getBlockPos();
            this.bb = new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 3, pos.getZ() + 2);
        }
        return this.bb;
    }

    public double getMaxRenderDistanceSquared() {
        return 65_536.0D;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.tanks[1], this.tanks[2] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }
}
