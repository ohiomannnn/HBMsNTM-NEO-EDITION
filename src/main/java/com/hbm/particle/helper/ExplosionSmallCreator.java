package com.hbm.particle.helper;

import com.hbm.lib.ModSounds;
import com.hbm.particle.ModParticles;
import com.hbm.particle.ParticleAura;
import com.hbm.particle.ParticleExplosionSmall;
import com.hbm.particle.VomitPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosionSmallCreator implements IParticleCreator{

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

        float dist = (float) player.distanceToSqr(x, y, z);
        float soundRange = 200F;

        if (dist <= soundRange) {
            boolean near = dist <= soundRange * 0.4;
            if (near) {
                level.playLocalSound(x, y, z,
                        ModSounds.EXPLOSION_SMALL_NEAR.get(),
                        net.minecraft.sounds.SoundSource.BLOCKS,
                        4.0F, 0.9F + rand.nextFloat() * 0.2F, false);
            } else {
                level.playLocalSound(x, y, z,
                        ModSounds.EXPLOSION_SMALL_FAR.get(),
                        net.minecraft.sounds.SoundSource.BLOCKS,
                        4.0F, 0.9F + rand.nextFloat() * 0.2F, true);
            }
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
                VomitPart fx = new VomitPart(level, x, y + 0.1, z, rand.nextGaussian() * 0.2, 0.5F + rand.nextDouble() * 0.7, rand.nextGaussian() * 0.2, 0F, 0F, 0F, ModParticles.AURA_SPITES);
                Minecraft.getInstance().particleEngine.add(fx);
                //level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), x, y + 0.1, z, rand.nextGaussian() * 0.2, 0.5 + rand.nextDouble() * 0.7, rand.nextGaussian() * 0.2);
            }
        }
    }
}
