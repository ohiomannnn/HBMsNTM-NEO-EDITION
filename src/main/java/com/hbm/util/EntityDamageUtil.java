package com.hbm.util;

import com.hbm.config.ModConfigs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EntityDamageUtil {

    /** New and improved entity damage calc - only use this one */
    public static boolean hurtNT(LivingEntity living, DamageSource source, float amount, boolean ignoreIFrame, boolean allowSpecialCancel, double knockbackMultiplier, float pierceDT, float pierce) {
        if (living instanceof ServerPlayer playerMP && source.getEntity() instanceof Player attacker) {
            if (!playerMP.canHarmPlayer(attacker)) return false; // handles wack-ass no PVP rule as well as scoreboard friendly fire
        }
        DamageResistanceHandler.setup(pierceDT, pierce);
        boolean ret = attackEntityFromNTInternal(living, source, amount, ignoreIFrame, allowSpecialCancel, knockbackMultiplier);
        DamageResistanceHandler.reset();
        return ret;
    }

    private static boolean attackEntityFromNTInternal(LivingEntity living, DamageSource source, float amount, boolean ignoreIFrame, boolean allowSpecialCancel, double knockbackMultiplier) {
        boolean superCompatibility = ModConfigs.SERVER.DAMAGE_COMPATIBILITY_MODE.get();
        return true;
        // TODO: add hurt shit
//        return superCompatibility
//                ? attackEntitySuperCompatibility(living, source, amount, ignoreIFrame, allowSpecialCancel, knockbackMultiplier)
//                : attackEntitySEDNAPatch(living, source, amount, ignoreIFrame, allowSpecialCancel, knockbackMultiplier);
    }


    public static void damageArmorNT(LivingEntity entity, float amount) { }
}
