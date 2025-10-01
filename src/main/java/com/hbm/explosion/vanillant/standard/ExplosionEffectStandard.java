package com.hbm.explosion.vanillant.standard;

import java.util.List;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.IExplosionSFX;
import com.hbm.packets.PacketsDispatcher;
import com.hbm.packets.toclient.AuxParticlePacket;
import com.hbm.packets.toclient.ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ExplosionEffectStandard implements IExplosionSFX {

    @Override
    public void doEffect(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {

        if (level.isClientSide())
            return;

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(
                    null,
                    x, y, z,
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    4.0F,
                    (1.0F + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.2F) * 0.7F
            );

            PacketDistributor.sendToPlayersNear(
                    serverLevel,
                    null,
                    x, y, z,
                    250,
                    new ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket(x, y, z, explosion.size, explosion.compat.getToBlow())
            );
        }
    }

    public static void performClient(Level level, double x, double y, double z, float size, List<BlockPos> affectedBlocks) {

        if (size >= 2.0F) {
            level.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
        } else {
            level.addParticle(ParticleTypes.EXPLOSION, x, y, z, 1.0D, 0.0D, 0.0D);
        }

        RandomSource random = level.random;
        int count = affectedBlocks.size();

        for (int i = 0; i < count; i++) {
            BlockPos pos = affectedBlocks.get(i);

            double oX = (double) pos.getX() + random.nextFloat();
            double oY = (double) pos.getY() + random.nextFloat();
            double oZ = (double) pos.getZ() + random.nextFloat();

            double dX = oX - x;
            double dY = oY - y;
            double dZ = oZ - z;

            double delta = Mth.sqrt((float)(dX * dX + dY * dY + dZ * dZ));
            dX /= delta;
            dY /= delta;
            dZ /= delta;

            double mod = 0.5D / (delta / (double) size + 0.1D);
            mod *= (random.nextFloat() * random.nextFloat() + 0.3F);
            dX *= mod;
            dY *= mod;
            dZ *= mod;

            level.addParticle(ParticleTypes.EXPLOSION,
                    (oX + x) / 2.0D, (oY + y) / 2.0D, (oZ + z) / 2.0D,
                    dX, dY, dZ
            );

            level.addParticle(ParticleTypes.SMOKE,
                    oX, oY, oZ,
                    dX, dY, dZ
            );
        }
    }
}
