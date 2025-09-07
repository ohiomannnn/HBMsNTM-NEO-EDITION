package com.hbm.explosion;


import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModSounds;
import com.hbm.world.WorldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ExplosionNukeSmall {

    public static void explode(Level level, double posX, double posY, double posZ, MukeParams params) {

        if (params.particle != null) {
            CompoundTag data = new CompoundTag();
            data.putString("type", params.particle);

            if (params.particle.equals("muke") &&
                    (MainRegistry.polaroidID == 11 || level.random.nextInt(100) == 0)) {
                data.putBoolean("balefire", true);
            }

            PacketThreading.createAllAroundThreadedPacket(
                    new AuxParticlePacketNT(data, posX, posY + 0.5, posZ),
                    new PacketDistributor.TargetPoint(level.dimension(), posX, posY, posZ, 250)
            );
        }

        level.playSound(null, posX, posY, posZ, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);

        if (params.shrapnelCount > 0) {
            ExplosionLarge.spawnShrapnels(level, posX, posY, posZ, params.shrapnelCount);
        }

        if (params.miniNuke && !params.safe) {
            new ExplosionNT(level, null, posX, posY, posZ, params.blastRadius)
                    .addAllAttrib(params.explosionAttribs)
                    .overrideResolution(params.resolution)
                    .explode();
        }

        if (params.killRadius > 0) {
            ExplosionNukeGeneric.dealDamage(level, posX, posY, posZ, params.killRadius);
        }

        // Большой взрыв
        if (!params.miniNuke) {
            WorldUtil.loadAndSpawnEntityInWorld(
                    EntityNukeExplosionMK5.statFac(level, (int) params.blastRadius, posX, posY, posZ)
            );
        }

        // Радиация
        if (params.miniNuke) {
            float radMod = params.radiationLevel / 3F;

            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (Math.abs(i) + Math.abs(j) < 4) {
                        ChunkRadiationManager.proxy.incrementRad(
                                level,
                                (int) Math.floor(posX + i * 16),
                                (int) Math.floor(posY),
                                (int) Math.floor(posZ + j * 16),
                                (float) 50 / (Math.abs(i) + Math.abs(j) + 1) * radMod
                        );
                    }
                }
            }
        }
    }

    public static MukeParams PARAMS_SAFE = new MukeParams() {{
        safe = true;
        killRadius = 45F;
        radiationLevel = 2F;
    }};
    public static MukeParams PARAMS_TOTS = new MukeParams() {{
        blastRadius = 10F;
        killRadius = 30F;
        particle = "tinytot";
        shrapnelCount = 0;
        resolution = 32;
        radiationLevel = 1;
    }};
    public static MukeParams PARAMS_LOW = new MukeParams() {{
        blastRadius = 15F;
        killRadius = 45F;
        radiationLevel = 2;
    }};
    public static MukeParams PARAMS_MEDIUM = new MukeParams() {{
        blastRadius = 20F;
        killRadius = 55F;
        radiationLevel = 3;
    }};
    public static MukeParams PARAMS_HIGH = new MukeParams() {{
        miniNuke = false;
        blastRadius = 150; // change to BombConfig.fatmanRadius;
        shrapnelCount = 0;
    }};

    public static class MukeParams {
        public boolean miniNuke = true;
        public boolean safe = false;
        public float blastRadius;
        public float killRadius;
        public float radiationLevel = 1F;
        public String particle = "muke";
        public int shrapnelCount = 25;
        public int resolution = 64;
        public ExplosionNT.ExAttrib[] explosionAttribs = new ExplosionNT.ExAttrib[]{
                ExplosionNT.ExAttrib.FIRE, ExplosionNT.ExAttrib.NOPARTICLE, ExplosionNT.ExAttrib.NOSOUND,
                ExplosionNT.ExAttrib.NODROP, ExplosionNT.ExAttrib.NOHURT
        };
    }
}
