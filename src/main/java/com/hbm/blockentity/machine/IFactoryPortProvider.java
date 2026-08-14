package com.hbm.blockentity.machine;

import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.fauxpointtwelve.DirPos;

public interface IFactoryPortProvider {

    FluidTank getWaterTank();

    FluidTank getSpentSteamTank();

    DirPos[] getCoolPos();

    DirPos[] getIOPos();
}
