package com.hbm.blockentity;

import net.minecraft.core.BlockPos;

public interface IProxyDelegateProvider {

    Object getDelegateForPosition(BlockPos pos);
}
