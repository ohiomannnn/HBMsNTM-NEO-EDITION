package com.hbm.explosion.vanillalike.interfaces;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Set;

@FunctionalInterface
public interface IBlockAllocator {
    Set<BlockPos> allocate(ExplosionVNT explosion, Level level, double x, double y, double z, float size);
}
