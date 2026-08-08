package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2.ConnectionPriority;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.fluid.trait.FT_Polluting;
import com.hbm.inventory.fluid.trait.FluidTrait.FluidReleaseType;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Gaseous;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Liquid;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Viscous;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;

public class MachineDrainBlockEntity extends LoadedBaseBlockEntity implements IFluidStandardReceiverMK2, IFluidCopiable, ITickable {

    public FluidTank tank;

    public MachineDrainBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_DRAIN.get(), pos, state);
        this.tank = new FluidTank(Fluids.NONE, 2_000);
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        if(this.level.getGameTime() % 20 == 0) {
            for(DirPos pos : this.getConPos()) this.trySubscribe(tank.getTankType(), this.level, pos);
        }

        this.networkPackNT(50);

        if(tank.getFill() <= 0) return;

        FluidType type = tank.getTankType();
        if(type.isAntimatter()) {
            this.level.explode(null, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 10F, true, Level.ExplosionInteraction.BLOCK);
            return;
        }

        int toSpill = Math.max(tank.getFill() / 2, 1);
        tank.setFill(tank.getFill() - toSpill);
        FT_Polluting.pollute(this.level, this.worldPosition, type, FluidReleaseType.SPILL, toSpill);

        this.tryPlaceOilSpill(type, toSpill);
        this.spawnDrainParticle(type);
    }

    private void tryPlaceOilSpill(FluidType type, int toSpill) {
        if(this.level == null || toSpill < 100 || this.level.random.nextInt(20) != 0) return;
        if(!type.hasTrait(FT_Liquid.class) || !type.hasTrait(FT_Viscous.class) || !type.hasTrait(FT_Flammable.class)) return;

        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Vec3 start = Vec3.atCenterOf(this.worldPosition).subtract(dir.getStepX() * 3D, 0D, dir.getStepZ() * 3D);
        Vec3 end = start.add(this.level.random.nextGaussian() * 5D, -25D, this.level.random.nextGaussian() * 5D);
        BlockHitResult hit = this.level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));

        if(hit.getType() != HitResult.Type.BLOCK || hit.getDirection() != Direction.UP) return;

        BlockPos targetPos = hit.getBlockPos().above();
        BlockState targetState = this.level.getBlockState(targetPos);
        BlockState spillState = NtmBlocks.OIL_SPILL.get().defaultBlockState();
        if(targetState.getFluidState().isEmpty() && targetState.canBeReplaced() && spillState.canSurvive(this.level, targetPos)) {
            this.level.setBlock(targetPos, spillState, 3);
        }
    }

    private void spawnDrainParticle(FluidType type) {
        if(!(this.level instanceof ServerLevel serverLevel)) return;

        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        double x = this.worldPosition.getX() + 0.5 - dir.getStepX() * 2.5;
        double y = this.worldPosition.getY() + 0.5;
        double z = this.worldPosition.getZ() + 0.5 - dir.getStepZ() * 2.5;

        CompoundTag data = new CompoundTag();
        if(type.hasTrait(FT_Gaseous.class)) {
            data.putString("type", "tower");
            data.putFloat("lift", 0.5F);
            data.putFloat("base", 0.375F);
            data.putFloat("max", 3F);
            data.putInt("life", 100 + this.level.random.nextInt(50));
        } else {
            data.putString("type", "splash");
        }
        data.putInt("color", type.getColor());

        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 10D, new AuxParticle(data, x, y, z));
    }

    public DirPos[] getConPos() {
        Direction dir0 = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction dir1 = dir0.getCounterClockWise(Direction.Axis.Y);
        Direction dir2 = dir0.getClockWise(Direction.Axis.Y);
        BlockPos pos = this.getBlockPos();

        return new DirPos[] {
                new DirPos(pos.relative(dir0), dir0),
                new DirPos(pos.relative(dir1), dir1),
                new DirPos(pos.relative(dir2), dir2)
        };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tank.readFromNBT(tag, "t");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.tank.writeToNBT(tag, "t");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.tank.deserialize(buf);
    }

    @Override public FluidTank[] getAllTanks() { return new FluidTank[] {tank}; }
    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {tank}; }
    @Override public ConnectionPriority getFluidPriority() { return ConnectionPriority.LOW; }
    @Override public boolean canConnect(FluidType type, Direction dir) { return dir != Direction.UP && dir != Direction.DOWN; }
    @Override public FluidTank getTankToPaste() { return tank; }
}
