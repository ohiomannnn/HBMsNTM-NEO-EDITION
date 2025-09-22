package com.hbm.explosion.vanillalike.interfaces;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import net.minecraft.world.level.Level;

public interface IExplosionSFX {
    void doEffect(ExplosionVNT explosion, Level level, double x, double y, double z, float size);
}
