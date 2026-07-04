package com.hbm.handler;

import com.hbm.config.NtmConfig;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.extprop.HbmLivingAttachments.ContaminationEffect;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.items.weapon.sedna.factory.ConfettiUtil;
import com.hbm.lib.ModAttachments;
import com.hbm.main.NuclearTechModClient;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.particle.NtmParticles;
import com.hbm.particle.helper.FlameCreator;
import com.hbm.particle.helper.IParticleCreator;
import com.hbm.particle.vanilla.NbtParticleOption;
import com.hbm.registry.NtmBiomes;
import com.hbm.registry.NtmDamageTypes;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import com.hbm.util.DamageResistanceHandler.DamageClass;
import com.hbm.util.SoundUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityEffectHandler {
    public static void tick(LivingEntity entity) {
        if(entity.tickCount % 20 == 0) {
            HbmLivingAttachments.setRadBuf(entity, HbmLivingAttachments.getRadEnv(entity));
            HbmLivingAttachments.setRadEnv(entity, 0);
        }

        ResourceKey<Biome> biome = entity.level.getBiome(entity.blockPosition).getKey();
        double radiation = 0;

        if(biome == NtmBiomes.CRATER_OUTER) radiation = NtmConfig.COMMON.CRATER_OUTER_RAD.get();
        if(biome == NtmBiomes.CRATER) radiation = NtmConfig.COMMON.CRATER_RAD.get();
        if(biome == NtmBiomes.CRATER_INNER) radiation = NtmConfig.COMMON.CRATER_INNER_RAD.get();

        if(entity.isInWater()) radiation *= NtmConfig.COMMON.CRATER_WATER_MULT.get();

        if(radiation > 0) {
            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE,(float) radiation / 20F);
        }

        handleContamination(entity);
        handleRadiationEffect(entity);
        handleRadiationFX(entity);
        handleDigamma(entity);
        handleLungDisease(entity);
        handleOil(entity);
        handleTemperature(entity);
        if(entity instanceof Player player) handleFauxLadder(player);
    }

    private static void handleFauxLadder(Player player) {

        HbmPlayerAttachments props = HbmPlayerAttachments.getData(player);

        if (props.isOnLadder) {
            double climbSpeed = 0.15;

            Vec3 motion = player.getDeltaMovement();
            double motionX = motion.x;
            double motionY = motion.y;
            double motionZ = motion.z;

            if (motionX < -climbSpeed) motionX = -climbSpeed;
            if (motionX > climbSpeed) motionX = climbSpeed;
            if (motionZ < -climbSpeed) motionZ = -climbSpeed;
            if (motionZ > climbSpeed) motionZ = climbSpeed;

            player.resetFallDistance();

            if (motionY < -climbSpeed) motionY = -climbSpeed;
            if (player.isCrouching() && motionY < 0.0D) motionY = 0.0D;
            if (player.horizontalCollision) motionY = 0.2D;

            player.setDeltaMovement(motionX, motionY, motionZ);

            props.isOnLadder = false;
            player.setData(ModAttachments.PLAYER_ATTACHMENT.get(), props);

            //if(!player.level.isRemote) ArmorUtil.resetFlightTime(player);
        }
    }

    private static void handleContamination(LivingEntity entity) {
        if (entity.level().isClientSide) return;

        List<ContaminationEffect> contamination = HbmLivingAttachments.getCont(entity);
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

        float eRad = HbmLivingAttachments.getRadiation(entity);

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
        if (eRad > 2500) HbmLivingAttachments.setRadiation(entity, 2500);

        /// EFFECTS ///
        if (eRad >= 1000) {
            entity.hurt(entity.damageSources().source(NtmDamageTypes.RADIATION), Float.MAX_VALUE);
            HbmLivingAttachments.setRadiation(entity, 0);
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

        if(!level.isClientSide) {

            if(ContaminationUtil.isRadImmune(entity)) return;

            float rad = ChunkRadiationManager.proxy.getRadiation(level, entity.blockPosition());

            if(level.dimension() == Level.NETHER && NtmConfig.COMMON.HELL_RAD.get() > 0 && rad < NtmConfig.COMMON.HELL_RAD.get()) {
                rad = (float) NtmConfig.COMMON.HELL_RAD.getAsDouble();
            }

            if(rad > 0) ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, rad / 20F);

            if(entity instanceof Player player && player.isCreative()) return;
            if(entity instanceof Player player && player.isSpectator()) return;

            RandomSource random = RandomSource.create(entity.getId());

            int r600 = random.nextInt(600);
            int r1200 = random.nextInt(1200);

            if(HbmLivingAttachments.getRadiation(entity) > 600) {

                if((level.getGameTime() + r600) % 600 < 20 && canVomit(entity)) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("count", 25);
                    tag.putInt("entity", entity.getId());
                    IParticleCreator.addParticle(level, new NbtParticleOption(NtmParticles.VOMIT_BLOOD.get(), tag), entity.getX(), entity.getY(), entity.getZ(), 25.0);

                    if((level.getGameTime() + r600) % 600 == 1) {
                        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), NtmSoundEvents.VOMIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 19));
                    }
                }

            } else if(HbmLivingAttachments.getRadiation(entity) > 200 && (level.getGameTime() + r1200) % 1200 < 20 && canVomit(entity)) {

                CompoundTag tag = new CompoundTag();
                tag.putInt("count", 15);
                tag.putInt("entity", entity.getId());
                IParticleCreator.addParticle(level, new NbtParticleOption(NtmParticles.VOMIT_NORMAL.get(), tag), entity.getX(), entity.getY(), entity.getZ(), 25.0);

                if((level.getGameTime() + r1200) % 1200 == 1) {
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), NtmSoundEvents.VOMIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 19));
                }
            }

            if(HbmLivingAttachments.getRadiation(entity) > 900 && (level.getGameTime() + random.nextInt(10)) % 10 == 0) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("count", 1);
                tag.put("state", NbtUtils.writeBlockState(Blocks.REDSTONE_BLOCK.defaultBlockState()));
                tag.putInt("entity", entity.getId());
                IParticleCreator.addParticle(level, new NbtParticleOption(NtmParticles.SWEAT.get(), tag), entity.getX(), entity.getY(), entity.getZ(), 25.0);
            }
        } else {
            float radiation = HbmLivingAttachments.getRadiation(entity);
            if(entity instanceof Player && radiation > 600) {
                CompoundTag tag = new CompoundTag();
                tag.putString("type", "radiation");
                tag.putInt("count", radiation > 900 ? 4 : radiation > 800 ? 2 : 1);
                NuclearTechModClient.effectNT(tag);
            }
        }
    }

    private static void handleDigamma(LivingEntity entity) {
        Level level = entity.level;

        if(!level.isClientSide) {
            float digamma = HbmLivingAttachments.getDigamma(entity);

            if(digamma < 0.1F) return;

            int chance = Math.max(10 - (int) (digamma), 1);

            if(chance == 1 || entity.random.nextInt(chance) == 0) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("count", 1);
                tag.put("state", NbtUtils.writeBlockState(Blocks.SOUL_SAND.defaultBlockState()));
                tag.putInt("entity", entity.getId());
                IParticleCreator.addParticle(level, new NbtParticleOption(NtmParticles.SWEAT.get(), tag), entity.getX(), entity.getY(), entity.getZ(), 25.0);
            }
        }
    }

    private static void handleLungDisease(LivingEntity entity) {
        Level level = entity.level;

        if (!level.isClientSide) {
            if (entity instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    HbmLivingAttachments.setBlackLung(entity, 0);
                    HbmLivingAttachments.setAsbestos(entity, 0);
                    return;
                } else {
                    int bl = HbmLivingAttachments.getBlackLung(entity);

                    if (bl > 0 && bl < HbmLivingAttachments.maxBlacklung * 0.5) {
                        HbmLivingAttachments.setBlackLung(entity, HbmLivingAttachments.getBlackLung(entity) - 1);
                    }
                }
            }

            double blacklung = Math.min(HbmLivingAttachments.getBlackLung(entity), HbmLivingAttachments.maxBlacklung);
            double asbestos = Math.min(HbmLivingAttachments.getAsbestos(entity), HbmLivingAttachments.maxAsbestos);
            // TODO Add pollution someday

            boolean coughs = blacklung / HbmLivingAttachments.maxBlacklung > 0.25D || asbestos / HbmLivingAttachments.maxAsbestos > 0.25D;

            if (!coughs) return;

            boolean coughsCoal = blacklung / HbmLivingAttachments.maxBlacklung > 0.5D;
            boolean coughsALotOfCoal = blacklung / HbmLivingAttachments.maxBlacklung > 0.8D;
            boolean coughsBlood = asbestos / HbmLivingAttachments.maxAsbestos > 0.75D || blacklung / HbmLivingAttachments.maxBlacklung > 0.75D;

            double blacklungDelta = 1D - (blacklung / (double) HbmLivingAttachments.maxBlacklung);
            double asbestosDelta = 1D - (asbestos / (double) HbmLivingAttachments.maxAsbestos);


            double total = 1 - (blacklungDelta * asbestosDelta);

            if(total > 0.75D) {
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 2));
            }

            if(total > 0.95D) {
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
            }

            total = 1 - (blacklungDelta * asbestosDelta);
            int freq = Math.max((int) (1000 - 950 * total), 20);


            if(level.getGameTime() % freq == entity.getId() % freq) {
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), NtmSoundEvents.COUGH, SoundSource.PLAYERS, 1.0F, 1.0F);

                if(coughsBlood) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("count", 5);
                    tag.putInt("entity", entity.getId());
                    IParticleCreator.addParticle(level, new NbtParticleOption(NtmParticles.VOMIT_BLOOD.get(), tag), entity.getX(), entity.getY(), entity.getZ(), 25.0);
                }

                if(coughsCoal) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("count", coughsALotOfCoal ? 50 : 10);
                    tag.putInt("entity", entity.getId());
                    IParticleCreator.addParticle(level, new NbtParticleOption(NtmParticles.VOMIT_SMOKE.get(), tag), entity.getX(), entity.getY(), entity.getZ(), 25.0);
                }
            }
        }
    }

    private static void handleOil(LivingEntity entity) {
        if(entity.level.isClientSide) return;

        int oil = HbmLivingAttachments.getOil(entity);

        if(oil > 0) {
            if(entity.isOnFire()) {
                HbmLivingAttachments.setOil(entity, 0);
                entity.level.explode(null, entity.position.x, entity.position.y + entity.dimensions.height() / 2, entity.position.z, 3F, true, ExplosionInteraction.MOB);
            } else {
                HbmLivingAttachments.setOil(entity, oil - 1);
            }

            if(entity.tickCount % 5 == 0) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("count", 1);
                tag.put("state", NbtUtils.writeBlockState(Blocks.COAL_BLOCK.defaultBlockState()));
                tag.putInt("entity", entity.getId());
                IParticleCreator.addParticle(entity.level, new NbtParticleOption(NtmParticles.SWEAT.get(), tag), entity.getX(), entity.getY(), entity.getZ(), 25.0);
            }
        }
    }

    private static void handleTemperature(LivingEntity entity) {
        if(entity.level.isClientSide) return;

        HbmLivingAttachments attachments = HbmLivingAttachments.getData(entity);
        RandomSource random = entity.random;

        if(!entity.isAlive() || entity instanceof Player player && player.isSpectator()) return;

        if(entity.fireImmune()) {
            attachments.fire = 0;
            attachments.phosphorus = 0;
        }

        double x = entity.position.x;
        double y = entity.position.y;
        double z = entity.position.z;

        float width = entity.dimensions.width();
        float height = entity.dimensions.height();

        if(entity.isInWaterOrRain()) attachments.fire = 0;

        if(attachments.fire > 0) {
            attachments.fire--;
            if((entity.tickCount + entity.id) % 15 == 0) SoundUtils.playAtEntity(entity, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1F, 1.5F + random.nextFloat() * 0.5F);
            if((entity.tickCount + entity.id) % 40 == 0) entity.hurt(entity.damageSources().onFire(), 2F);
            FlameCreator.composeEffect(entity.level, x - width / 2 + width * random.nextDouble(), y + random.nextDouble() * height, z - width / 2 + width * random.nextDouble(), FlameCreator.META_FIRE);
        }

        if(attachments.phosphorus > 0) {
            attachments.phosphorus--;
            if((entity.tickCount + entity.id) % 15 == 0) SoundUtils.playAtEntity(entity, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1F, 1.5F + random.nextFloat() * 0.5F);
            if((entity.tickCount + entity.id) % 40 == 0) entity.hurt(entity.damageSources().onFire(), 5F);
            FlameCreator.composeEffect(entity.level, x - width / 2 + width * random.nextDouble(), y + random.nextDouble() * height, z - width / 2 + width * random.nextDouble(), FlameCreator.META_FIRE);
        }

        if(attachments.balefire > 0) {
            attachments.balefire--;
            if((entity.tickCount + entity.id) % 15 == 0) SoundUtils.playAtEntity(entity, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1F, 1.5F + random.nextFloat() * 0.5F);
            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 5F);
            if((entity.tickCount + entity.id) % 20 == 0) entity.hurt(entity.damageSources().onFire(), 5F);
            FlameCreator.composeEffect(entity.level, x - width / 2 + width * random.nextDouble(), y + random.nextDouble() * height, z - width / 2 + width * random.nextDouble(), FlameCreator.META_BALEFIRE);
        }

        if(attachments.blackFire > 0) {
            attachments.blackFire--;
            if((entity.tickCount + entity.id) % 10 == 0) SoundUtils.playAtEntity(entity, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1F, 1.5F + random.nextFloat() * 0.5F);
            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 5F);
            if((entity.tickCount + entity.id) % 10 == 0) entity.hurt(entity.damageSources().onFire(), 10F);
            FlameCreator.composeEffect(entity.level, x - width / 2 + width * random.nextDouble(), y + random.nextDouble() * height, z - width / 2 + width * random.nextDouble(), FlameCreator.META_BLACK);
        }

        if(attachments.fire > 0 || attachments.phosphorus > 0 || attachments.balefire > 0 || attachments.blackFire > 0) if(!entity.isAlive()) ConfettiUtil.createConfetti(entity, DamageClass.FIRE);
    }

    private static boolean canVomit(Entity entity) {
        return entity.getType().getCategory() != MobCategory.WATER_CREATURE;
    }
}
