package com.hbm.particle.helper;

import com.hbm.HBMsNTMClient;
import com.hbm.particle.AshesParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class AshesCreator implements IParticleCreator {

    public static void composeEffect(Level level, Entity toPulverize, int ashesCount, float ashesScale) {

        CompoundTag tag = new CompoundTag();
        tag.putString("type", "ashes");
        tag.putInt("entityID", toPulverize.getId());
        tag.putInt("ashesCount", ashesCount);
        tag.putFloat("ashesScale", ashesScale);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, toPulverize.getX(), toPulverize.getY(), toPulverize.getZ(), 100, tag);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {
        int entityID = tag.getInt("entityID");
        Entity entity = level.getEntity(entityID);
        if (entity == null) return;

        HBMsNTMClient.vanish(entityID);

        int amount = tag.getInt("ashesCount");
        float scale = tag.getFloat("ashesScale");

        for(int i = 0; i < amount; i++) {
            AshesParticle particle = new AshesParticle(level,
                    entity.getX() + (entity.getBbWidth() + scale * 2) * (level.random.nextDouble() - 0.5),
                    entity.getY() + entity.getBbHeight() * level.random.nextDouble(),
                    entity.getZ() + (entity.getBbWidth() + scale * 2) * (level.random.nextDouble() - 0.5),
                    scale
            );
            Minecraft.getInstance().particleEngine.add(particle);
            Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.FLAME, particle.getPos().x, particle.getPos().y, particle.getPos().z, 0.0, 0.0, 0.0);
        }
    }
}
