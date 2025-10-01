package com.hbm.explosion.vanillant.interfaces;

import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public interface IEntityProcessor {
    Map<Player, Vec3> processEntities(ExplosionVNT explosion, Level level, double x, double y, double z, float size);
}
