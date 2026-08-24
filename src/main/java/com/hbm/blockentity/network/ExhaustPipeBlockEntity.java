package com.hbm.blockentity.network;

import api.hbm.fluidmk2.FluidNode;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.uninos.UniNodespace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ExhaustPipeBlockEntity extends PipeBaseBlockEntity {

    public static final FluidType[] SMOKES = new FluidType[] {Fluids.SMOKE, Fluids.SMOKE_LEADED, Fluids.SMOKE_POISON};

    private final FluidNode[] nodes = new FluidNode[SMOKES.length];

    public ExhaustPipeBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.EXHAUST_PIPE.get(), pos, state);
        this.type = Fluids.SMOKE;
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            for(int i = 0; i < SMOKES.length; i++) {
                FluidType smoke = SMOKES[i];

                if(this.nodes[i] == null || this.nodes[i].expired) {
                    this.nodes[i] = (FluidNode) UniNodespace.getNode(this.level, this.worldPosition, smoke.getNetworkProvider());

                    if(this.nodes[i] == null || this.nodes[i].expired) {
                        this.nodes[i] = this.createNode(smoke);
                        UniNodespace.createNode(this.level, this.nodes[i]);
                    }
                }
            }
        }

        this.networkPackNT(150);
    }

    @Override
    public FluidType getFluidType() {
        return Fluids.SMOKE;
    }

    @Override
    public void setFluidType(FluidType type) {
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && isSmoke(type);
    }

    @Override
    public void setRemoved() {
        if(this.level != null && !this.level.isClientSide) {
            for(int i = 0; i < SMOKES.length; i++) {
                if(this.nodes[i] != null) {
                    UniNodespace.destroyNode(this.level, this.worldPosition, SMOKES[i].getNetworkProvider());
                }
            }
        }

        super.setRemoved();
    }

    @Override
    public int[] getFluidIDToCopy() {
        return new int[] {Fluids.SMOKE.getID(), Fluids.SMOKE_LEADED.getID(), Fluids.SMOKE_POISON.getID()};
    }

    public static boolean isSmoke(FluidType type) {
        return type == Fluids.SMOKE || type == Fluids.SMOKE_LEADED || type == Fluids.SMOKE_POISON;
    }
}
