package com.hbm.particle.helper;

import com.hbm.lib.ModSounds;
import com.hbm.particle.MukeWaveParticle;
import com.hbm.particle.ParticleDebris;
import com.hbm.particle.RocketFlameParticle;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.wiaj.WorldInAJar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ExplosionCreator implements IParticleCreator {

    public static final double SPEED_OF_SOUND = (17.15D) * 0.5;

    public static void composeEffect(Level level, double x, double y, double z,
                                     int cloudCount, float cloudScale, float cloudSpeedMultiplier, float waveScale,
                                     int debrisCount, int debrisSize, int debrisRetry,
                                     float debrisVelocity, float debrisHorizontalDeviation,
                                     float debrisVerticalOffset, float soundRange) {

        CompoundTag tag = new CompoundTag();
        tag.putString("type", "explosionLarge");
        tag.putByte("cloudCount", (byte) cloudCount);
        tag.putFloat("cloudScale", cloudScale);
        tag.putFloat("cloudSpeedMultiplier", cloudSpeedMultiplier);
        tag.putFloat("waveScale", waveScale);
        tag.putByte("debrisCount", (byte) debrisCount);
        tag.putByte("debrisSize", (byte) debrisSize);
        tag.putShort("debrisRetry", (short) debrisRetry);
        tag.putFloat("debrisVelocity", debrisVelocity);
        tag.putFloat("debrisHorizontalDeviation", debrisHorizontalDeviation);
        tag.putFloat("debrisVerticalOffset", debrisVerticalOffset);
        tag.putFloat("soundRange", soundRange);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, x, y, z, Math.max(300, (int) soundRange), tag);
        }
    }

    /** Downscaled for small bombs */
    public static void composeEffectSmall(Level level, double x, double y, double z) {
        composeEffect(level, x, y, z, 10, 2F, 0.5F, 25F, 5, 8, 20, 0.75F, 1F, -2F, 150);
    }

    /** Development version */
    public static void composeEffectStandard(Level level, double x, double y, double z) {
        composeEffect(level, x, y, z, 15, 5F, 1F, 45F, 10, 16, 50, 1F, 3F, -2F, 200);
    }

    /** Upscaled version, ATACMS go brrt */
    public static void composeEffectLarge(Level level, double x, double y, double z) {
        composeEffect(level, x, y, z, 30, 6.5F, 2F, 65F, 25, 16, 50, 1.25F, 3F, -2F, 350);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag data) {

        int cloudCount = data.getByte("cloudCount");
        float cloudScale = data.getFloat("cloudScale");
        float cloudSpeedMultiplier = data.getFloat("cloudSpeedMultiplier");
        float waveScale = data.getFloat("waveScale");
        int debrisCount = data.getByte("debrisCount");
        int debrisSize = data.getByte("debrisSize");
        int debrisRetry = data.getShort("debrisRetry");
        float debrisVelocity = data.getFloat("debrisVelocity");
        float debrisHorizontalDeviation = data.getFloat("debrisHorizontalDeviation");
        float debrisVerticalOffset = data.getFloat("debrisVerticalOffset");
        float soundRange = data.getFloat("soundRange");

        double dist = Math.sqrt(player.distanceToSqr(x, y, z));

        if (dist <= soundRange) {
            SoundEvent sound = dist <= soundRange * 0.4 ? ModSounds.EXPLOSION_LARGE_NEAR.get() : ModSounds.EXPLOSION_LARGE_FAR.get();
            SimpleSoundInstance instance = new SimpleSoundInstance(sound, SoundSource.BLOCKS, 1000.0F, 0.9F + rand.nextFloat() * 0.2F, rand, x, y, z);
            Minecraft.getInstance().getSoundManager().playDelayed(instance, (int) (dist / SPEED_OF_SOUND));
        }

        // SMOKE PLUME
        for (int i = 0; i < cloudCount; i++) {
            RocketFlameParticle particle = new RocketFlameParticle(level, x, y, z);
            particle.xo = particle.x;
            particle.yo = particle.y;
            particle.zo = particle.z;
            particle.quadSize = cloudScale;
            particle.xd = rand.nextGaussian() * 0.5 * cloudSpeedMultiplier;
            particle.yd = rand.nextDouble() * 3 * cloudSpeedMultiplier;
            particle.zd = rand.nextGaussian() * 0.5 * cloudSpeedMultiplier;
            particle.lifetime = 70 + rand.nextInt(20);
            particle.noClip = true;

            ParticleEngineNT.INSTANCE.add(particle);
        }

        // WAVE
        MukeWaveParticle wave = new MukeWaveParticle(level, x, y + 2, z);
        wave.setup(waveScale, (int) (25F * waveScale / 45));
        ParticleEngineNT.INSTANCE.add(wave);

        // DEBRIS
        for (int c = 0; c < debrisCount; c++) {

            double oX = rand.nextGaussian() * debrisHorizontalDeviation;
            double oY = debrisVerticalOffset;
            double oZ = rand.nextGaussian() * debrisHorizontalDeviation;
            double cX = x + oX + 0.5;
            double cY = y + oY + 0.5;
            double cZ = z + oZ + 0.5;

            Vec3 motion = new Vec3(debrisVelocity, 0, 0)
                    .zRot((float) -Math.toRadians(45 + rand.nextFloat() * 25))
                    .yRot((float) (rand.nextDouble() * Math.PI * 2));
            ParticleDebris debris = new ParticleDebris(level, x, y, z, motion.x, motion.y, motion.z);
            WorldInAJar wiaj = new WorldInAJar(debrisSize, debrisSize, debrisSize);
            debris.setWorldInAJar(wiaj);

            if (debrisSize > 0) {
                int middle = debrisSize / 2 - 1;

                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (int k = 0; k < 2; k++) {
                            wiaj.setBlock(middle + i, middle + j, middle + k, level.getBlockState(BlockPos.containing(cX + i, cY + j, cZ + k)));
                        }
                    }
                }

                for (int layer = 2; layer <= (debrisSize / 2); layer++) {
                    for (int i = 0; i < debrisRetry; i++) {
                        int jx = -layer + rand.nextInt(layer * 2 + 1);
                        int jy = -layer + rand.nextInt(layer * 2 + 1);
                        int jz = -layer + rand.nextInt(layer * 2 + 1);

                        // very scary part
                        if (
                                !wiaj.getBlock(middle + jx + 1, middle + jy, middle + jz).isAir() ||
                                !wiaj.getBlock(middle + jx - 1, middle + jy, middle + jz).isAir() ||
                                !wiaj.getBlock(middle + jx, middle + jy + 1, middle + jz).isAir() ||
                                !wiaj.getBlock(middle + jx, middle + jy - 1, middle + jz).isAir() ||
                                !wiaj.getBlock(middle + jx, middle + jy, middle + jz + 1).isAir() ||
                                !wiaj.getBlock(middle + jx, middle + jy, middle + jz - 1).isAir()) {

                            wiaj.setBlock(middle + jx, middle + jy, middle + jz, level.getBlockState(BlockPos.containing(cX + jx, cY + jy, cZ + jz)));
                        }
                    }
                }
            }

            Minecraft.getInstance().particleEngine.add(debris);
        }
    }
}