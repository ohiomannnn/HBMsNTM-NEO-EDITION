package com.hbm.particle.helper;

import com.hbm.lib.ModSounds;
import com.hbm.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class ExplosionSmallCreator implements IParticleCreator{

    public static final double SPEED_OF_SOUND = (17.15D) * 0.5;

    public static void composeEffect(Level level, double x, double y, double z, int cloudCount, float cloudScale, float cloudSpeedMult) {

        CompoundTag data = new CompoundTag();
        data.putString("type", "explosionSmall");
        data.putInt("cloudCount", cloudCount);
        data.putFloat("cloudScale", cloudScale);
        data.putFloat("cloudSpeedMult", cloudSpeedMult);
        data.putInt("debris", 15);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, x, y, z, 200, data);
        }
    }

    @Override
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {

        int cloudCount = tag.getInt("cloudCount");
        float cloudScale = tag.getFloat("cloudScale");
        float cloudSpeedMult = tag.getFloat("cloudSpeedMult");
        int debris = tag.getInt("debris");

        double distSq = player.distanceToSqr(x, y, z);
        float soundRange = 200F;

        if (distSq <= soundRange * soundRange) {
            double dist = Math.sqrt(distSq);
            SoundEvent sound = dist <= soundRange * 0.4 ? ModSounds.EXPLOSION_SMALL_NEAR.get() : ModSounds.EXPLOSION_SMALL_FAR.get();
            SimpleSoundInstance instance = new SimpleSoundInstance(
                    sound,
                    SoundSource.BLOCKS,
                    100.0F,
                    0.9F + rand.nextFloat() * 0.2F,
                    rand,
                    x,y,z
            );
            Minecraft.getInstance().getSoundManager().playDelayed(instance, (int) (dist / SPEED_OF_SOUND));
        }


        for (int i = 0; i < cloudCount; i++) {
            ParticleExplosionSmall particle = new ParticleExplosionSmall(level, x, y, z, cloudScale, cloudSpeedMult, ModParticles.EXPLOSION_SMALL_SPRITES);
            Minecraft.getInstance().particleEngine.add(particle);
        }

        BlockPos base = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
        BlockState state = Blocks.AIR.defaultBlockState();
        BlockPos.MutableBlockPos cursor = base.mutable();
        for (Direction dir : Direction.values()) {
            cursor.set(base).move(dir);
            BlockState candidate = level.getBlockState(cursor);
            if (!candidate.isAir()) {
                state = candidate;
                break;
            }
        }

        if (!state.isAir()) {
            for (int i = 0; i < debris; i++) {
                ParticleDust fx = new ParticleDust(level,
                        x, y + 0.1, z,
                        rand.nextGaussian() * 0.2,
                        0.5F + rand.nextDouble() * 0.7,
                        rand.nextGaussian() * 0.2,
                        state
                );
                fx.setLifetime(50 + rand.nextInt(20));
                Minecraft.getInstance().particleEngine.add(fx);
            }
        }
    }
}
