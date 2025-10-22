package com.hbm.util;

import api.hbm.entity.IRadiationImmune;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.extprop.LivingProperties;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashSet;

public class  ContaminationUtil {

    public static HashSet<Class<?>> immuneEntities = new HashSet<>();

    public static boolean isRadImmune(Entity entity) {
        if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(ModEffect.MUTATION)) {
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

        if (!(entity instanceof LivingEntity e))
            return;

        if (entity instanceof Duck || entity instanceof Ocelot)
            return;

        if (entity instanceof ServerPlayer player && player.isCreative())
            return;

        if (entity instanceof ServerPlayer player && player.tickCount < 200)
            return;

        LivingProperties.incrementDigamma(e, dig);
//        if(entity.isPotionActive(HbmPotion.stability.id))
//            return;
//
//        if(!(entity instanceof EntityPlayer && ArmorUtil.checkForDigamma((EntityPlayer) e)))
//            HbmLivingProps.incrementDigamma(e, f);
    }

    public static float calculateRadiationMod(LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            float coefficient = 10.0F;
            //return (float) Math.pow(coefficient, -HazmatRegistry.getResistance(player));
        }
        return 1;
    }


    public static void printGeigerData(Player player) {
        Level level = player.level();

        float eRad = (float) ((LivingProperties.getRadiation(player) * 10) / 10D);
        double rads = Math.floor(ChunkRadiationManager.proxy.getRadiation(level,
                player.blockPosition().getX(),
                player.blockPosition().getY(),
                player.blockPosition().getZ()) * 10) / 10D;
        double env = Math.floor(LivingProperties.getRadBuf(player) * 10D) / 10D;

        double res = Math.floor((10000D - ContaminationUtil.calculateRadiationMod(player) * 10000D)) / 100D;
//        double resCoefficient = Math.floor(HazmatRegistry.getResistance(player) * 100D) / 100D;

        String chunkPrefix = getPrefixFromRad(rads);
        String envPrefix = getPrefixFromRad(env);
        String radPrefix = "";

        if (eRad < 200) radPrefix += ChatFormatting.GREEN;
        else if (eRad < 400) radPrefix += ChatFormatting.YELLOW;
        else if (eRad < 600) radPrefix += ChatFormatting.GOLD;
        else if (eRad < 800) radPrefix += ChatFormatting.RED;
        else if (eRad < 1000) radPrefix += ChatFormatting.DARK_RED;
        else radPrefix += ChatFormatting.DARK_GRAY;

        player.displayClientMessage(
                Component.literal("===== ☢ ")
                        .append(Component.translatable("geiger.title"))
                        .append(Component.literal(" ☢ ====="))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)),
                false);

        player.displayClientMessage(
                Component.translatable("geiger.chunkRad")
                        .append(Component.literal(" " + chunkPrefix + rads + " RAD/s"))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)),
                false);

        player.displayClientMessage(
                Component.translatable("geiger.envRad")
                        .append(Component.literal(" " + envPrefix + env + " RAD/s"))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)),
                false);

        player.displayClientMessage(
                Component.translatable("geiger.playerRad")
                        .append(Component.literal(" " + radPrefix + eRad + " RAD"))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)),
                false);

//        player.displayClientMessage(
//                Component.translatable("geiger.playerRes")
//                        .append(Component.literal(" " + ChatFormatting.GREEN + res + "% (" + resKoeff + ")"))
//                        .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)),
//                false);
    }

    public static void printDosimeterData(Player player) {

        double env = ((int)(LivingProperties.getRadBuf(player) * 10D)) / 10D;
        boolean limit = false;

        if(env > 3.6D) {
            env = 3.6D;
            limit = true;
        }

        String envPrefix = getPrefixFromRad(env);

        player.displayClientMessage(
                Component.literal("===== ☢ ")
                        .append(Component.translatable("geiger.title.dosimeter"))
                        .append(Component.literal(" ☢ ====="))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)),
                false);

        player.displayClientMessage(
                Component.translatable("geiger.envRad")
                        .append(Component.literal(" " + envPrefix + (limit ? ">" : "") + env + " RAD/s"))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)),
                false);
    }

    public static String getPrefixFromRad(double rads) {
        if (rads == 0) return ChatFormatting.GREEN.toString();
        else if (rads < 1) return ChatFormatting.YELLOW.toString();
        else if (rads < 10) return ChatFormatting.GOLD.toString();
        else if (rads < 100) return ChatFormatting.RED.toString();
        else if (rads < 1000) return ChatFormatting.DARK_RED.toString();
        else return ChatFormatting.DARK_GRAY.toString();
    }

    public static void printDiagnosticData(Player player) {
        double digamma = ((int)(LivingProperties.getDigamma(player) * 100)) / 100D;
        double halflife = ((int)((1D - Math.pow(0.5, digamma)) * 10000)) / 100D;

        player.displayClientMessage(
                Component.literal("===== Ϝ ")
                        .append(Component.translatable("digamma.title"))
                        .append(Component.literal(" Ϝ ====="))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE)),
                false);

        player.displayClientMessage(
                Component.translatable("digamma.playerDigamma").withStyle(ChatFormatting.LIGHT_PURPLE)
                        .append(Component.literal(" " + digamma + " DRX").withStyle(ChatFormatting.RED)),
                false);

        player.displayClientMessage(
                Component.translatable("digamma.playerHealth").withStyle(ChatFormatting.LIGHT_PURPLE)
                        .append(Component.literal(" " + halflife + " %").withStyle(ChatFormatting.RED)),
                false);

        player.displayClientMessage(
                Component.translatable("digamma.playerRes").withStyle(ChatFormatting.LIGHT_PURPLE)
                        .append(Component.literal(" " + "N/A").withStyle(ChatFormatting.BLUE)),
                false);
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
            float radEnv = LivingProperties.getRadEnv(entity);
            LivingProperties.setRadEnv(entity, radEnv + amount);
        }

        if (entity instanceof ServerPlayer player) {

//            switch(type) {
//                case FARADAY:			if(ArmorUtil.checkForFaraday(player))	return; break;
//                case HAZMAT:			if(ArmorUtil.checkForHazmat(player))	return; break;
//                case HAZMAT_HEAVY:			if(ArmorUtil.checkForHaz2(player))		return; break;
//                case DIGAMMA:			if(ArmorUtil.checkForDigamma(player))	return; if(ArmorUtil.checkForDigamma2(player))	return; break;
//                case DIGAMMA_ROBE:			if(ArmorUtil.checkForDigamma2(player))	return; break;
//            }

            if (player.isCreative() || player.isSpectator() && type != ContaminationType.NONE && type != ContaminationType.DIGAMMA_ROBE) {
                return;
            }

            if (player.tickCount < 200) {
                return;
            }
        }

        if(hazard == HazardType.RADIATION && isRadImmune(entity))
            return;

        switch(hazard) {
            case RADIATION: LivingProperties.incrementRadiation(entity, amount * (type == ContaminationType.RAD_BYPASS ? 1 : calculateRadiationMod(entity))); break;
            case DIGAMMA: LivingProperties.incrementDigamma(entity, amount); break;
        }
    }
}
