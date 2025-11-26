package com.hbm.particle.helper;

import com.hbm.lib.ModSounds;
import com.hbm.particle.ExplosionSmallParticle;
import com.hbm.particle.ParticleDust;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ExplosionSmallCreator implements IParticleCreator {

    public static final double SPEED_OF_SOUND = (17.15D) * 0.5;

    public static void composeEffect(Level level, double x, double y, double z, int cloudCount, float cloudScale, float cloudSpeedMultiplier) {

        CompoundTag tag = new CompoundTag();
        tag.putString("type", "explosionSmall");
        tag.putInt("cloudCount", cloudCount);
        tag.putFloat("cloudScale", cloudScale);
        tag.putFloat("cloudSpeedMultiplier", cloudSpeedMultiplier);
        tag.putInt("debris", 15);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, x, y, z, 200, tag);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {

        int cloudCount = tag.getInt("cloudCount");
        float cloudScale = tag.getFloat("cloudScale");
        float cloudSpeedMultiplier = tag.getFloat("cloudSpeedMultiplier");
        int debris = tag.getInt("debris");

        double distSq = player.distanceToSqr(x, y, z);
        float soundRange = 200F;

        if (distSq <= soundRange * soundRange) {
            double dist = Math.sqrt(distSq);
            SoundEvent sound = dist <= soundRange * 0.4 ? ModSounds.EXPLOSION_SMALL_NEAR.get() : ModSounds.EXPLOSION_SMALL_FAR.get();
            SimpleSoundInstance instance = new SimpleSoundInstance(sound, SoundSource.BLOCKS, 100.0F, 0.9F + rand.nextFloat() * 0.2F, rand, x,y,z);
            Minecraft.getInstance().getSoundManager().playDelayed(instance, (int) (dist / SPEED_OF_SOUND));
        }

        for (int i = 0; i < cloudCount; i++) {
            ExplosionSmallParticle particle = new ExplosionSmallParticle(level, x, y, z, cloudScale, cloudSpeedMultiplier);
            Minecraft.getInstance().particleEngine.add(particle);
        }

        BlockPos base = BlockPos.containing(x, y, z);
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
                ParticleDust particle = new ParticleDust(level, x, y + 0.1, z, rand.nextGaussian() * 0.2, 0.5F + rand.nextDouble() * 0.7, rand.nextGaussian() * 0.2, state);
                particle.setLifetime(50 + rand.nextInt(20));
                Minecraft.getInstance().particleEngine.add(particle);
            }
        }
    }
}
