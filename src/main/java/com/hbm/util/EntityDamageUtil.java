package com.hbm.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

public class EntityDamageUtil {

    // fuck it, every LivingEntity method is private/protected, les do something else
    /** Should mixin change vanilla behavior right now */
    public static boolean changeVanilla = false;
    /// FLAGS ///
    public static boolean ignoreIFrame = true;
    public static boolean allowSpecialCancel = false;
    public static double knockbackMultiplier = 0.0;

    private static void setup(boolean ignoreIFrame, boolean allowSpecialCancel, double knockbackMultiplier) {
        EntityDamageUtil.ignoreIFrame = ignoreIFrame;
        EntityDamageUtil.allowSpecialCancel = allowSpecialCancel;
        EntityDamageUtil.knockbackMultiplier = knockbackMultiplier;

        EntityDamageUtil.changeVanilla = true;
    }

    private static void reset() {
        EntityDamageUtil.ignoreIFrame = false;
        EntityDamageUtil.allowSpecialCancel = false;
        EntityDamageUtil.knockbackMultiplier = 0.0;

        EntityDamageUtil.changeVanilla = false;
    }

    /** Shitty hack, if the first attack fails, it retries with damage + previous damage, allowing damage to penetrate */
    @Deprecated
    public static boolean hurtIgnoreIFrame(Entity victim, DamageSource src, float damage) {

        if(!victim.hurt(src, damage)) {

            if(victim instanceof LivingEntity living) {

                if(living.invulnerableTime > 10.0F && !src.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
                    damage += living.lastHurt;
                }
            }
            return victim.hurt(src, damage);
        } else {
            return true;
        }
    }

    /** New and improved entity damage calc - only use this one */
    public static boolean hurtNT(LivingEntity living, DamageSource source, float amount, boolean ignoreIFrame, boolean allowSpecialCancel, double knockbackMultiplier, float pierceDT, float pierce) {
        if(living instanceof ServerPlayer serverPlayer && source.getEntity() instanceof Player attacker) {
            if(!serverPlayer.canHarmPlayer(attacker)) return false; // handles wack-ass no PVP rule as well as scoreboard friendly fire
        }
        DamageResistanceHandler.setup(pierceDT, pierce);
        setup(ignoreIFrame, allowSpecialCancel, knockbackMultiplier); boolean ret = living.hurt(source, amount); reset();
        DamageResistanceHandler.reset();
        return ret;
    }

    public static void damageArmorNT(LivingEntity entity, float amount) { }

    public static void knockBack(LivingEntity living, double strength, double x, double z, double multiplier) {
        LivingKnockBackEvent event = CommonHooks.onLivingKnockBack(living, (float) strength, x, z);
        if(!event.isCanceled()) {
            double eventStrength = event.getStrength();
            double eventX = event.getRatioX();
            double eventZ = event.getRatioZ();
            eventStrength *= 1.0D - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            if(eventStrength > 0.0D) {
                living.hasImpulse = true;

                Vec3 currentMotion = living.getDeltaMovement();
                while(eventX * eventX + eventZ * eventZ < 1.0E-5D) {
                    eventX = (Math.random() - Math.random()) * 0.01D;
                    eventZ = (Math.random() - Math.random()) * 0.01D;
                }

                double magnitude = eventStrength * multiplier;
                Vec3 knockbackVec = (new Vec3(eventX, 0.0D, eventZ)).normalize().scale(magnitude);
                double motionY = currentMotion.y;
                if(living.onGround()) {
                    motionY = currentMotion.y / 2.0D + magnitude;
                    if(motionY > 0.2D) motionY = 0.2D * multiplier;
                }
                if(motionY > 0.2D) motionY = 0.2D * multiplier;

                living.setDeltaMovement(currentMotion.x / 2.0D - knockbackVec.x, motionY, currentMotion.z / 2.0D - knockbackVec.z);
            }
        }
    }
}
