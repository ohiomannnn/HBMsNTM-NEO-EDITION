package com.hbm.explosion.vanillalike.standard;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.explosion.vanillalike.interfaces.ICustomDamageHandler;
import com.hbm.explosion.vanillalike.interfaces.IEntityProcessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityProcessorStandard implements IEntityProcessor {

    private float rangeMod = 2.0F;
    private ICustomDamageHandler damage;

    @Override
    public Map<Player, Vec3> processEntities(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {
        Map<Player, Vec3> affectedPlayers = new HashMap<>();
        float radius = size * this.rangeMod;

        AABB searchBox = new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        List<Entity> entities = level.getEntities(explosion.exploder, searchBox);
        Vec3 explosionCenter = new Vec3(x, y, z);

        for (Entity entity : entities) {

            double distance = entity.position().distanceTo(explosionCenter);
            if (distance >= radius) continue;

            double exposure = Explosion.getSeenPercent(explosionCenter, entity);
            if (exposure > 0) {
                float damage = (float) ((1.0 - (distance / radius)) * exposure);
                float finalDamage = damage * size * 2.0F + damage;
                entity.hurt(level.damageSources().explosion(null, explosion.exploder), finalDamage);

                double knockbackStrength = (1.0 - (distance / radius)) * exposure;
                Vec3 knockback = entity.position().subtract(explosionCenter).normalize().scale(knockbackStrength);

                if (entity instanceof Player player) {
                    if (player.isSpectator() || (player.isCreative() && player.getAbilities().flying)) {
                        continue;
                    }
                    affectedPlayers.put(player, knockback);
                } else {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));
                }
            }
        }
        return affectedPlayers;
    }

    public EntityProcessorStandard withRangeMod(float mod) {
        this.rangeMod = mod;
        return this;
    }

    public EntityProcessorStandard withDamageHandler(ICustomDamageHandler damage) {
        this.damage = damage;
        return this;
    }
}
