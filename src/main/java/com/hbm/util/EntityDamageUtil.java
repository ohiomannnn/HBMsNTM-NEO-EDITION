package com.hbm.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EntityDamageUtil {

    /** New and improved entity damage calc - only use this one */
    public static boolean hurtNT(LivingEntity living, DamageSource source, float amount, boolean ignoreIFrame, boolean allowSpecialCancel, double knockbackMultiplier, float pierceDT, float pierce) {
        if (living instanceof ServerPlayer serverPlayer && source.getEntity() instanceof Player attacker) {
            if (!serverPlayer.canHarmPlayer(attacker)) return false; // handles wack-ass no PVP rule as well as scoreboard friendly fire
        }
        DamageResistanceHandler.setup(pierceDT, pierce);
        boolean ret = attackEntityFromNTInternal(living, source, amount, ignoreIFrame, allowSpecialCancel, knockbackMultiplier);
        DamageResistanceHandler.reset();
        return ret;
    }

    private static boolean attackEntityFromNTInternal(LivingEntity living, DamageSource source, float amount, boolean ignoreIFrame, boolean allowSpecialCancel, double knockbackMultiplier) {
        if (allowSpecialCancel) return true;
        Entity entity = source.getEntity();
        if (entity != null) {
            double deltaX = entity.getX() - living.getX();
            double deltaZ;

            for (deltaZ = entity.getZ() - living.getZ(); deltaX * deltaX + deltaZ * deltaZ < 1.0E-4D; deltaZ = (Math.random() - Math.random()) * 0.01D) {
                deltaX = (Math.random() - Math.random()) * 0.01D;
            }
            knockBack(living, entity, amount, deltaX, deltaZ, knockbackMultiplier);
        }
        return living.hurt(source, amount);
    }

    public static void knockBack(LivingEntity living, Entity attacker, float damage, double motionX, double motionZ, double multiplier) {
        double resistance = living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        if (living.getRandom().nextDouble() >= resistance) {
            living.hasImpulse = true;

            double horizontal = Math.sqrt(motionX * motionX + motionZ * motionZ);

            double magnitude = 0.4D * multiplier;

            Vec3 current = living.getDeltaMovement();
            Vec3 modified = new Vec3(
                    current.x * 0.5D - motionX / horizontal * magnitude,
                    current.y * 0.5D + magnitude,
                    current.z * 0.5D - motionZ / horizontal * magnitude
            );

            if (modified.y > 0.2D * multiplier) {
                modified = new Vec3(modified.x, 0.2D * multiplier, modified.z);
            }

            living.setDeltaMovement(modified);
        }
    }


    public static void damageArmorNT(LivingEntity entity, float amount) { }
}
