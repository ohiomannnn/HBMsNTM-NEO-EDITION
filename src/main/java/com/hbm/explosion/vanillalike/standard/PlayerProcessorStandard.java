package com.hbm.explosion.vanillalike.standard;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.explosion.vanillalike.interfaces.IPlayerProcessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PlayerProcessorStandard implements IPlayerProcessor {

    @Override
    public void processPlayers(ExplosionVNT explosion, Level level, double x, double y, double z, Map<Player, Vec3> players) {
        for (Map.Entry<Player, Vec3> entry : players.entrySet()) {
            if (entry.getKey() instanceof ServerPlayer serverPlayer) {

                Vec3 playerPos = serverPlayer.position();
                Vec3 knockback = playerPos.subtract(x, y, z);

                if (knockback.lengthSqr() < 1.0E-6) continue;

                knockback = knockback.normalize().scale(1.5);

                serverPlayer.push(knockback.x, knockback.y, knockback.z);
                serverPlayer.hurtMarked = true;
            }
        }
    }
}
