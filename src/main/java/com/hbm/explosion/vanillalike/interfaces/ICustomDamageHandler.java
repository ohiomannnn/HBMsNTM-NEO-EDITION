package com.hbm.explosion.vanillalike.interfaces;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import net.minecraft.world.entity.Entity;

public interface ICustomDamageHandler {
    void handleAttack(ExplosionVNT explosion, Entity entity, double distanceScaled);
}