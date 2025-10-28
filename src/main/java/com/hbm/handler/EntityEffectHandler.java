package com.hbm.handler;

import com.hbm.HBMsNTMClient;
import com.hbm.config.MainConfig;
import com.hbm.extprop.LivingProperties;
import com.hbm.extprop.LivingProperties.ContaminationEffect;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModDamageSource;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.*;
import com.hbm.world.biome.ModBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityEffectHandler {
    public static void tick(LivingEntity entity) {
        if (entity.tickCount % 20 == 0) {
            LivingProperties.setRadBuf(entity, LivingProperties.getRadEnv(entity));
            LivingProperties.setRadEnv(entity, 0);
        }

        ResourceKey<Biome> biome = entity.level().getBiome(new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ())).getKey();
        double radiation = 0;

        if (biome == ModBiomes.CRATER_OUTER) radiation = MainConfig.COMMON.CRATER_OUTER_RAD.get();
        if (biome == ModBiomes.CRATER) radiation = MainConfig.COMMON.CRATER_RAD.get();
        if (biome == ModBiomes.CRATER_INNER) radiation = MainConfig.COMMON.CRATER_INNER_RAD.get();

        if (entity.isInWater()) radiation *= MainConfig.COMMON.CRATER_WATER_MULT.get();

        if (radiation > 0) {
            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE,(float) radiation / 20F);
        }

        handleContamination(entity);
        handleRadiationEffect(entity);
        handleRadiationFX(entity);
        handleDigamma(entity);
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

        if (entity instanceof Player player && player.isCreative()) return;
        if (entity instanceof Player player && player.isSpectator()) return;

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

        if (eRad < 200 || ContaminationUtil.isRadImmune(entity)) return;
        if (eRad > 2500) LivingProperties.setRadiation(entity, 2500);

        /// EFFECTS ///
        if (eRad >= 1000) {
            DamageSource src = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.RADIATION));
            entity.hurt(src, Float.MAX_VALUE);
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

        if (!level.isClientSide) {

            if (ContaminationUtil.isRadImmune(entity)) return;

            int ix = Mth.floor(entity.getX());
            int iy = Mth.floor(entity.getY());
            int iz = Mth.floor(entity.getZ());

            float rad = ChunkRadiationManager.proxy.getRadiation(level, ix, iy, iz);

            if (level.dimension() == Level.NETHER && MainConfig.COMMON.HELL_RAD.get() > 0 && rad < MainConfig.COMMON.HELL_RAD.get())
                rad = (float) 0.01;

            if (rad > 0) ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, rad / 20F);

            if (entity instanceof Player player && player.isCreative()) return;
            if (entity instanceof Player player && player.isSpectator()) return;

            Random rand = new Random(entity.getId());

            int r600 = rand.nextInt(600);
            int r1200 = rand.nextInt(1200);

            if (LivingProperties.getRadiation(entity) > 600) {

                if ((level.getGameTime() + r600) % 600 < 20 && canVomit(entity)) {
                    CompoundTag tag = new CompoundTag();
                    tag.putString("type", "vomit");
                    tag.putString("mode", "blood");
                    tag.putInt("count", 25);
                    tag.putInt("entity", entity.getId());
                    PacketDistributor.sendToPlayersNear(
                            (ServerLevel) level,
                            null,
                            entity.getX(), entity.getY(), entity.getZ(),
                            25,
                            new AuxParticle(tag, entity.getX(), entity.getY(), entity.getZ())
                    );

                    if ((level.getGameTime() + r600) % 600 == 1) {
                        level.playSound(null, ix, iy, iz, ModSounds.VOMIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 19));
                    }
                }

            } else if (LivingProperties.getRadiation(entity) > 200 && (level.getGameTime() + r1200) % 1200 < 20 && canVomit(entity)) {

                CompoundTag tag = new CompoundTag();
                tag.putString("type", "vomit");
                tag.putString("mode", "normal");
                tag.putInt("count", 15);
                tag.putInt("entity", entity.getId());
                PacketDistributor.sendToPlayersNear((ServerLevel) level, null, entity.getX(), entity.getY(), entity.getZ(), 25, new AuxParticle(tag, entity.getX(), entity.getY(), entity.getZ()));

                if ((level.getGameTime() + r1200) % 1200 == 1) {
                    level.playSound(null, ix, iy, iz, ModSounds.VOMIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 19));
                }
            }

            if (LivingProperties.getRadiation(entity) > 900 && (level.getGameTime() + rand.nextInt(10)) % 10 == 0) {
                CompoundTag tag = new CompoundTag();
                tag.putString("type", "sweat");
                tag.putInt("count", 1);
                tag.put("BlockState", NbtUtils.writeBlockState(Blocks.REDSTONE_BLOCK.defaultBlockState()));
                tag.putInt("entity", entity.getId());
                PacketDistributor.sendToPlayersNear((ServerLevel) level, null, entity.getX(), entity.getY(), entity.getZ(), 25, new AuxParticle(tag, entity.getX(), entity.getY(), entity.getZ()));
            }

            float radiation = LivingProperties.getRadiation(entity);
            // i mean its working
            if (entity instanceof Player && radiation > 600) {
                CompoundTag tag = new CompoundTag();
                tag.putString("type", "radiation");
                tag.putInt("count", radiation > 900 ? 4 : radiation > 800 ? 2 : 1);
                HBMsNTMClient.effectNT(tag);
            }
        }
    }

    private static void handleDigamma(LivingEntity entity) {
        Level level = entity.level();

        if (!level.isClientSide) {
            float digamma = LivingProperties.getDigamma(entity);

            if (digamma < 0.1F)
                return;

            int chance = Math.max(10 - (int) (digamma), 1);

            if (chance == 1 || entity.getRandom().nextInt(chance) == 0) {
                CompoundTag nbt = new CompoundTag();
                nbt.putString("type", "sweat");
                nbt.putInt("count", 1);
                nbt.put("BlockState", NbtUtils.writeBlockState(Blocks.SOUL_SAND.defaultBlockState()));
                nbt.putInt("entity", entity.getId());
                PacketDistributor.sendToPlayersNear((ServerLevel) level, null, entity.getX(), entity.getY(), entity.getZ(), 25, new AuxParticle(nbt, entity.getX(), entity.getY(), entity.getZ()));
            }
        }
    }

    private static void handleLungDisease(LivingEntity entity) {
        Level level = entity.level();

        if (!level.isClientSide) {
            if (entity instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    LivingProperties.setBlackLung(entity, 0);
                    LivingProperties.setAsbestos(entity, 0);
                    return;
                } else {
                    int bl = LivingProperties.getBlackLung(entity);

                    if (bl > 0 && bl < LivingProperties.maxBlacklung * 0.5) {
                        LivingProperties.setBlackLung(entity, LivingProperties.getBlackLung(entity) - 1);
                    }
                }
            }


        }
    }

    private static boolean canVomit(Entity entity) {
        return entity.getType().getCategory() != MobCategory.WATER_CREATURE;
    }
}
