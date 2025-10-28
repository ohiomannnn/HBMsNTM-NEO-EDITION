package com.hbm.explosion.vanillant.interfaces;

import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public interface IFortuneMutator {
    int mutateFortune(ExplosionVNT explosion, Block block, BlockPos pos);
}