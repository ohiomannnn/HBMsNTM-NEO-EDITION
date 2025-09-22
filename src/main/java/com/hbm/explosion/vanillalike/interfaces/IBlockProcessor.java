package com.hbm.explosion.vanillalike.interfaces;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public interface IBlockProcessor {
    void processBlocks(ExplosionVNT explosion, Level level, double x, double y, double z, HashSet<BlockPos> blocks);
}
