package com.hbm.explosion.vanillant.interfaces;

import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.entity.Entity;

public interface ICustomDamageHandler {
    void handleAttack(ExplosionVNT explosion, Entity entity, double distanceScaled);
}