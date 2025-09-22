package com.hbm.handler;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.extprop.LivingProperties;
import com.hbm.extprop.LivingProperties.ContaminationEffect;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModSounds;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityEffectHandler {
    public static void tick(LivingEntity entity) {
        if (entity.tickCount % 20 == 0) {
            LivingProperties.setRadBuf(entity, LivingProperties.getRadEnv(entity));
            LivingProperties.setRadEnv(entity, 0);
        }

        handleContamination(entity);
        handleRadiationEffect(entity);
        handleRadiationFX(entity);
    }


    private static void handleContamination(LivingEntity entity) {
        if (entity.level().isClientSide) return;

        List<ContaminationEffect> contamination = LivingProperties.getCont(entity);
        List<ContaminationEffect> rem = new ArrayList<>();

        for (ContaminationEffect con : contamination) {
            ContaminationUtil.contaminate(entity, HazardType.RADIATION, con.ignoreArmor ? ContaminationType.RAD_BYPASS : ContaminationType.CREATIVE, con.getRad());
            con.time--;
            if (con.time <= 0) rem.add(con);
        }

        contamination.removeAll(rem);
    }

    /** Handles entity transformation via radiation and applied potion effects */
    private static void handleRadiationEffect(LivingEntity entity) {

        if (!entity.isAlive()) return;
        if (entity.level().isClientSide) return;
        if (entity instanceof Player player && player.getAbilities().instabuild) return;

        Level level = entity.level();

        float eRad = LivingProperties.getRadiation(entity);

        if (entity instanceof Cow && !(entity instanceof MushroomCow) && eRad >= 50) {
            MushroomCow cow = new MushroomCow(EntityType.MOOSHROOM, level);
            cow.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
            level.addFreshEntity(cow);
            entity.discard();
        } else if (entity instanceof Villager && eRad >= 500) {
            Zombie zombie = new Zombie(EntityType.ZOMBIE, level);
            zombie.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
            level.addFreshEntity(zombie);
            entity.discard();
        }

        if(eRad < 200 || ContaminationUtil.isRadImmune(entity)) return;
        if(eRad > 2500) LivingProperties.setRadiation(entity, 2500);

        /// EFFECTS ///
        if(eRad >= 1000) {
            entity.setHealth(0.0F);
            LivingProperties.setRadiation(entity, 0);
        } else if (eRad >= 800) {
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0));
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2));
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2));
            if (level.random.nextInt(500) == 0) entity.addEffect(new MobEffectInstance(MobEffects.POISON, 3 * 20, 2));
            if (level.random.nextInt(700) == 0) entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 3 * 20, 1));
        } else if (eRad >= 600) {
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0));
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2));
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2));
            if (level.random.nextInt(500) == 0) entity.addEffect(new MobEffectInstance(MobEffects.POISON, 3 * 20, 1));
        } else if (eRad >= 400) {
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0));
            if (level.random.nextInt(500) == 0) entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 0));
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 1));
        } else if (eRad >= 200) {
            if (level.random.nextInt(300) == 0) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 20, 0));
            if (level.random.nextInt(500) == 0) entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 0));
        }
    }
    /** Handles contamination from the chunk, the dimension as well as particle effects related to radiation sickness */
    private static void handleRadiationFX(LivingEntity entity) {
        Level level = entity.level();

        if (!level.isClientSide){

            if (ContaminationUtil.isRadImmune(entity)) return;

            int ix = Mth.floor(entity.getX());
            int iy = Mth.floor(entity.getY());
            int iz = Mth.floor(entity.getZ());

            float rad = ChunkRadiationManager.getProxy().getRadiation(level, ix, iy, iz);

            if (level.dimension() == Level.NETHER)
                rad = (float) 0.01;

            if (rad > 0) ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, rad / 20F);

            if (entity instanceof Player player && player.getAbilities().instabuild) return;

            Random rand = new Random(entity.getId());

            int r600 = rand.nextInt(600);
            int r1200 = rand.nextInt(1200);

            if (LivingProperties.getRadiation(entity) > 600 && (level.getGameTime() + r600) % 600 < 20 && canVomit(entity)) {

                CompoundTag nbt = new CompoundTag();
                nbt.putString("type", "vomit");
                nbt.putString("mode", "blood");
                nbt.putInt("count", 25);
                nbt.putInt("entity", entity.getId());
                HBMsNTMClient.effectNT(nbt);

                if((level.getGameTime() + r600) % 600 == 1) {
                    level.playSound(null, ix, iy, iz, ModSounds.VOMIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 19));
                }

            } else if (LivingProperties.getRadiation(entity) > 200 && (level.getGameTime() + r1200) % 1200 < 20 && canVomit(entity)) {

                CompoundTag nbt = new CompoundTag();
                nbt.putString("type", "vomit");
                nbt.putString("mode", "normal");
                nbt.putInt("count", 15);
                nbt.putInt("entity", entity.getId());
                HBMsNTMClient.effectNT(nbt);

                if((level.getGameTime() + r1200) % 1200 == 1) {
                    level.playSound(null, ix, iy, iz, ModSounds.VOMIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 19));
                }
            } else {
                float radiation = LivingProperties.getRadiation(entity);

                if (entity instanceof Player && radiation > 600) {
                    CompoundTag tag = new CompoundTag();
                    tag.putString("type", "radiation");
                    tag.putInt("count", radiation > 900 ? 4 : radiation > 800 ? 2 : 1);
                    HBMsNTMClient.effectNT(tag);
                }
            }
        }
    }

    private static boolean canVomit(Entity entity) {
        return entity.getType().getCategory() != MobCategory.WATER_CREATURE;
    }
}
