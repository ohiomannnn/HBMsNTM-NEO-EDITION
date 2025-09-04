package com.hbm.explosion.vanillalike.interfaces;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

@FunctionalInterface
public interface IPlayerProcessor {
    void processPlayers(ExplosionVNT explosion, Level level, double x, double y, double z, Map<Player, Vec3> players);
}
