package com.hbm.util;

import api.hbm.entity.IRadiationImmune;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.handler.HazmatRegistry;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;

public class ContaminationUtil {

    public static HashSet<Class<?>> immuneEntities = new HashSet<>();

    public static boolean isRadImmune(Entity entity) {
        if (entity instanceof LivingEntity living && living.hasEffect(ModEffect.MUTATION)) {
            return true;
        }

        if (immuneEntities.isEmpty()) {
            immuneEntities.add(CreeperNuclear.class);
            immuneEntities.add(MushroomCow.class);
            immuneEntities.add(Zombie.class);
            immuneEntities.add(Skeleton.class);
//            immuneEntities.add(EntityQuackos.class);
            immuneEntities.add(Ocelot.class);
            immuneEntities.add(IRadiationImmune.class);
        }

        Class<?> entityClass = entity.getClass();
        for (Class<?> clazz : immuneEntities) {
            if (clazz.isAssignableFrom(entityClass)) return true;
        }

        return false;
    }

    /// DIGAMMA ///
    public static void applyDigammaData(Entity entity, float dig) {

        if (!(entity instanceof LivingEntity living)) return;
        if (entity instanceof Duck || entity instanceof Ocelot) return;
        if (entity instanceof Player player) {
            if (player.tickCount < 200) return;
            if (player.isCreative() || player.isSpectator()) return;
        }
        if (living.hasEffect(ModEffect.STABILITY)) return;

        HbmLivingAttachments.incrementDigamma(living, dig);
    }

    public static float calculateRadiationMod(LivingEntity entity) {
        if (entity instanceof Player player) {
            float coefficient = 10.0F;
            return (float) Math.pow(coefficient, -HazmatRegistry.getResistance(player));
        }
        return 1;
    }


    public static void printGeigerData(Player player) {
        double playerRad = ((int)(HbmLivingAttachments.getRadiation(player) * 10)) / 10D;
        double chunkRad = ((int)(ChunkRadiationManager.proxy.getRadiation(player.level(), player.blockPosition()) * 10)) / 10D;
        double envRad = ((int)(HbmLivingAttachments.getRadBuf(player) * 10D)) / 10D;
        double res = ((int)(10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D;
        double resKoeff = ((int)(HazmatRegistry.getResistance(player) * 100D)) / 100D;

        String playerRadPrefix = getPrefixFromRadPlayer(playerRad);
        String chunkPrefix = getPrefixFromRad(chunkRad);
        String envPrefix = getPrefixFromRad(envRad);
        String resPrefix = getPrefixFromRadResistance(resKoeff);

        player.displayClientMessage(Component.translatable("geiger.title").withStyle(ChatFormatting.GOLD), false);
        player.displayClientMessage(Component.translatable("geiger.chunkRad", chunkPrefix + (chunkRad + " RAD/s")).withStyle(ChatFormatting.YELLOW), false);
        player.displayClientMessage(Component.translatable("geiger.envRad", envPrefix + (envRad + " RAD/s")).withStyle(ChatFormatting.YELLOW), false);
        player.displayClientMessage(Component.translatable("geiger.playerRad", playerRadPrefix + (playerRad + " RAD")).withStyle(ChatFormatting.YELLOW), false);
        player.displayClientMessage(Component.translatable("geiger.playerRes", resPrefix + res + "% (" + resKoeff + ")").withStyle(ChatFormatting.YELLOW), false);
    }

    public static void printDosimeterData(Player player) {
        double env = ((int)(HbmLivingAttachments.getRadBuf(player) * 10D)) / 10D;
        boolean limit = false;

        if (env > 3.6F) {
            env = 3.6F;
            limit = true;
        }

        String envPrefix = getPrefixFromRad(env);

        player.displayClientMessage(Component.translatable("geiger.title.dosimeter").withStyle(ChatFormatting.GOLD), false);
        player.displayClientMessage(Component.translatable("geiger.envRad", envPrefix + (limit ? ">" : "") + env + " RAD/s").withStyle(ChatFormatting.YELLOW), false);
    }

    public static void printDiagnosticData(Player player) {
        double digamma = ((int)(HbmLivingAttachments.getDigamma(player) * 100)) / 100D;
        double halfLife = ((int)((1D - Math.pow(0.5, digamma)) * 10000)) / 100D; // hl 3 confirmed??

        player.displayClientMessage(Component.translatable("digamma.title").withStyle(ChatFormatting.DARK_PURPLE), false);
        player.displayClientMessage(Component.translatable("digamma.playerDigamma", ChatFormatting.RED + (digamma + " DRX")).withStyle(ChatFormatting.LIGHT_PURPLE), false);
        player.displayClientMessage(Component.translatable("digamma.playerHealth", ChatFormatting.RED + (halfLife + " %")).withStyle(ChatFormatting.LIGHT_PURPLE), false);
        player.displayClientMessage(Component.translatable("digamma.playerRes", ChatFormatting.BLUE + "N/A").withStyle(ChatFormatting.LIGHT_PURPLE), false);
    }

    public static String getPrefixFromRad(double rads) {
        if (rads == 0) return ChatFormatting.GREEN.toString();
        else if (rads < 1) return ChatFormatting.YELLOW.toString();
        else if (rads < 10) return ChatFormatting.GOLD.toString();
        else if (rads < 100) return ChatFormatting.RED.toString();
        else if (rads < 1000) return ChatFormatting.DARK_RED.toString();
        return ChatFormatting.DARK_GRAY.toString();
    }

    public static String getPrefixFromRadPlayer(double rads) {
        if (rads < 200) return ChatFormatting.GREEN.toString();
        else if (rads < 400) return ChatFormatting.YELLOW.toString();
        else if (rads < 600) return ChatFormatting.GOLD.toString();
        else if (rads < 800) return ChatFormatting.RED.toString();
        else if (rads < 1000) return ChatFormatting.DARK_RED.toString();
        return ChatFormatting.DARK_GRAY.toString();
    }
    public static String getPrefixFromRadResistance(double koeff) {
        if (koeff > 0) return ChatFormatting.GREEN.toString();
        return ChatFormatting.WHITE.toString();
    }

    public enum HazardType {
        RADIATION,
        DIGAMMA
    }

    public enum ContaminationType {
        FARADAY,			//preventable by metal armor
        HAZMAT,				//preventable by hazmat
        HAZMAT_HEAVY,		//preventable by heavy hazmat
        DIGAMMA,			//preventable by fau armor or stability
        DIGAMMA_ROBE,		//preventable by robes
        CREATIVE,			//preventable by creative mode, for rad calculation armor piece bonuses still apply
        RAD_BYPASS,			//same as creative but will not apply radiation resistance calculation
        NONE				//not preventable
    }

    public static void contaminate(LivingEntity entity, HazardType hazard, ContaminationType type, float amount) {

        if (hazard == HazardType.RADIATION) {
            HbmLivingAttachments.setRadEnv(entity, HbmLivingAttachments.getRadEnv(entity) + amount);
        }

        if (entity instanceof Player player) {
            if (player.isCreative() || player.isSpectator() && type != ContaminationType.NONE && type != ContaminationType.DIGAMMA_ROBE) return;
            if (player.tickCount < 200) return;
        }

        if (hazard == HazardType.RADIATION && isRadImmune(entity)) return;

        switch (hazard) {
            case RADIATION -> HbmLivingAttachments.incrementRadiation(entity, amount * (type == ContaminationType.RAD_BYPASS ? 1 : calculateRadiationMod(entity)));
            case DIGAMMA -> HbmLivingAttachments.incrementDigamma(entity, amount);
        }
    }
}
