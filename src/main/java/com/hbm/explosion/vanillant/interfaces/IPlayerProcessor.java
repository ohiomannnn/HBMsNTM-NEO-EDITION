package com.hbm.explosion.vanillant.interfaces;

import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public interface IPlayerProcessor {
    void processPlayers(ExplosionVNT explosion, Level level, double x, double y, double z, HashMap<Player, Vec3> affectedPlayers);
}
