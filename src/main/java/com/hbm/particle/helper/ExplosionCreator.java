package com.hbm.particle.helper;

import com.hbm.lib.ModSounds;
import com.hbm.particle.ModParticles;
import com.hbm.particle.ParticleDebris;
import com.hbm.particle.ParticleMukeWave;
import com.hbm.particle.ParticleRocketFlame;
import com.hbm.wiaj.WorldInAJar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExplosionCreator implements IParticleCreator {

    public static void composeEffect(Level level, double x, double y, double z,
                                     int cloudCount, float cloudScale, float cloudSpeedMult, float waveScale,
                                     int debrisCount, int debrisSize, int debrisRetry,
                                     float debrisVelocity, float debrisHorizontalDeviation,
                                     float debrisVerticalOffset, float soundRange) {

        CompoundTag data = new CompoundTag();
        data.putString("type", "explosionLarge");
        data.putByte("cloudCount", (byte) cloudCount);
        data.putFloat("cloudScale", cloudScale);
        data.putFloat("cloudSpeedMult", cloudSpeedMult);
        data.putFloat("waveScale", waveScale);
        data.putByte("debrisCount", (byte) debrisCount);
        data.putByte("debrisSize", (byte) debrisSize);
        data.putShort("debrisRetry", (short) debrisRetry);
        data.putFloat("debrisVelocity", debrisVelocity);
        data.putFloat("debrisHorizontalDeviation", debrisHorizontalDeviation);
        data.putFloat("debrisVerticalOffset", debrisVerticalOffset);
        data.putFloat("soundRange", soundRange);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, x, y, z, Math.max(300, (int) soundRange), data);
        }
    }

    public static void composeEffectSmall(Level level, double x, double y, double z) {
        composeEffect(level, x, y, z, 10, 2F, 0.5F, 25F, 5, 8, 20, 0.75F, 1F, -2F, 150);
    }

    public static void composeEffectStandard(Level level, double x, double y, double z) {
        composeEffect(level, x, y, z, 15, 5F, 1F, 45F, 10, 16, 50, 1F, 3F, -2F, 200);
    }

    public static void composeEffectLarge(Level level, double x, double y, double z) {
        composeEffect(level, x, y, z, 30, 6.5F, 2F, 65F, 25, 16, 50, 1.25F, 3F, -2F, 350);
    }

    @Override
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag data) {

        int cloudCount = data.getByte("cloudCount");
        float cloudScale = data.getFloat("cloudScale");
        float cloudSpeedMult = data.getFloat("cloudSpeedMult");
        float waveScale = data.getFloat("waveScale");
        int debrisCount = data.getByte("debrisCount");
        int debrisSize = data.getByte("debrisSize");
        int debrisRetry = data.getShort("debrisRetry");
        float debrisVelocity = data.getFloat("debrisVelocity");
        float debrisHorizontalDeviation = data.getFloat("debrisHorizontalDeviation");
        float debrisVerticalOffset = data.getFloat("debrisVerticalOffset");
        float soundRange = data.getFloat("soundRange");

        float dist = (float) player.distanceToSqr(x, y, z);

        if (dist <= soundRange * soundRange) {
            boolean near = dist <= (soundRange * soundRange * 0.16);
            if (near) {
                level.playLocalSound(x, y, z,
                        ModSounds.EXPLOSION_LARGE_NEAR.get(),
                        net.minecraft.sounds.SoundSource.BLOCKS,
                        4.0F, 0.9F + rand.nextFloat() * 0.2F, false);
            } else {
                // this is not what original playDelayedSound but whatever
                level.playLocalSound(x, y, z,
                        ModSounds.EXPLOSION_LARGE_FAR.get(),
                        net.minecraft.sounds.SoundSource.BLOCKS,
                        4.0F, 0.9F + rand.nextFloat() * 0.2F, true);
            }
        }

        // WAVE
        ParticleMukeWave wave = new ParticleMukeWave(level, x, y + 2, z, ModParticles.MUKE_WAVE_SPRITES);
        wave.setup(waveScale, (int) (25F * waveScale / 45));
        Minecraft.getInstance().particleEngine.add(wave);

        // SMOKE PLUME
        for (int i = 0; i < cloudCount; i++) {
            ParticleRocketFlame fx = new ParticleRocketFlame(level, x, y, z, ModParticles.ROCKET_FLAME_SPRITES)
                    .setMaxAge(70 + rand.nextInt(20))
                    .setScale(cloudScale)
                    .setMotion(
                            rand.nextGaussian() * 0.5 * cloudSpeedMult,
                            rand.nextDouble() * 3 * cloudSpeedMult,
                            rand.nextGaussian() * 0.5 * cloudSpeedMult
                    )
                    .resetPrevPos();

            Minecraft.getInstance().particleEngine.add(fx);
        }

        // DEBRIS
        for (int c = 0; c < debrisCount; c++) {
            double oX = rand.nextGaussian() * debrisHorizontalDeviation;
            double oY = debrisVerticalOffset;
            double oZ = rand.nextGaussian() * debrisHorizontalDeviation;

            int cX = (int) Math.floor(x + oX + 0.5);
            int cY = (int) Math.floor(y + oY + 0.5);
            int cZ = (int) Math.floor(z + oZ + 0.5);

            Vec3 motion = new Vec3(debrisVelocity, 0, 0);
            motion = motion.yRot((float) (rand.nextDouble() * Math.PI * 2));
            motion = motion.zRot((float) Math.toRadians(-(45 + rand.nextFloat() * 25)));

            ParticleDebris particle = new ParticleDebris(level, x,y,z);
            particle.setDeltaMovement(motion);

            WorldInAJar wiaj = new WorldInAJar(debrisSize, debrisSize, debrisSize);
            particle.world = wiaj;

            if (debrisSize > 0) {
                int middle = debrisSize / 2 - 1;

                for (int i = 0; i < 2; i++)
                    for (int j = 0; j < 2; j++)
                        for (int k = 0; k < 2; k++) {
                            BlockPos pos = new BlockPos(cX + i, cY + j, cZ + k);
                            wiaj.setBlock(middle + i, middle + j, middle + k, level.getBlockState(pos));
                        }

                for (int layer = 2; layer <= (debrisSize / 2); layer++) {
                    for (int i = 0; i < debrisRetry; i++) {
                        int jx = -layer + rand.nextInt(layer * 2 + 1);
                        int jy = -layer + rand.nextInt(layer * 2 + 1);
                        int jz = -layer + rand.nextInt(layer * 2 + 1);

                        BlockPos pos = new BlockPos(cX + jx, cY + jy, cZ + jz);

                        // very scary
                        if (!wiaj.getBlock(middle + jx + 1, middle + jy, middle + jz).isAir()
                                || !wiaj.getBlock(middle + jx - 1, middle + jy, middle + jz).isAir()
                                || !wiaj.getBlock(middle + jx, middle + jy + 1, middle + jz).isAir()
                                || !wiaj.getBlock(middle + jx, middle + jy - 1, middle + jz).isAir()
                                || !wiaj.getBlock(middle + jx, middle + jy, middle + jz + 1).isAir()
                                || !wiaj.getBlock(middle + jx, middle + jy, middle + jz - 1).isAir()) {

                            wiaj.setBlock(middle + jx, middle + jy, middle + jz, level.getBlockState(pos));
                        }
                    }
                }
            }

            Minecraft.getInstance().particleEngine.add(particle);
        }
    }
}