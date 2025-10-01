package com.hbm.explosion.vanillant.interfaces;

import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.level.block.Block;

public interface IFortuneMutator {

    int mutateFortune(ExplosionVNT explosion, Block block, int x, int y, int z);
}