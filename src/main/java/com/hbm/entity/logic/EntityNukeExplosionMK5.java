package com.hbm.entity.logic;

import com.hbm.HBMsNTM;
import com.hbm.config.ServerConfig;
import com.hbm.entity.ModEntities;
import com.hbm.entity.effect.EntityFalloutRain;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.ExplosionNukeRayBalefire;
import com.hbm.explosion.ExplosionNukeRayBatched;
import com.hbm.interfaces.IExplosionRay;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityNukeExplosionMK5 extends EntityExplosionChunkloading {

    //Strength of the blast
    public int strength;
    //How many rays are calculated per tick
    public int speed;
    public int length;
    private long explosionStart;
    public boolean fallout = true;
    private int falloutAdd = 0;
    private boolean balefire = false;

    private IExplosionRay explosion;

    public EntityNukeExplosionMK5(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (strength == 0) {
            this.clearChunkLoader();
            this.discard();
        }

        if (!level().isClientSide) loadChunk((int) Math.floor(getX() / 16D), (int) Math.floor(getZ() / 16D));

        //TODO: make advancement for this

        if (!level().isClientSide && fallout && explosion != null && this.tickCount < 10 && strength >= 75) {
            radiate(2_500_000F / (this.tickCount * 5 + 1), this.length * 2);
        }

        ExplosionNukeGeneric.dealDamage(level(), getX(), getY(), getZ(), this.length * 2);

        if (explosion == null) {
            explosionStart = System.currentTimeMillis();
            if (!balefire) {
                explosion = new ExplosionNukeRayBatched(level(), (int) getX(), (int) getY(), (int) getZ(), strength, speed, length);
            } else {
                explosion = new ExplosionNukeRayBalefire(level(), (int) getX(), (int) getY(), (int) getZ(), strength, speed, length);
            }
        }

        if (!explosion.isComplete()) {
            explosion.cacheChunksTick(ServerConfig.MK5.getAsInt());
            explosion.destructionTick(ServerConfig.MK5.getAsInt());
        } else {
            if (ServerConfig.ENABLE_EXTENDED_LOGGING.getAsBoolean() && explosionStart != 0) {
                HBMsNTM.LOGGER.info("[NUKE] Explosion complete. Time elapsed: {}ms", (System.currentTimeMillis() - explosionStart));
            }
            if (fallout) {
                EntityFalloutRain fallout = new EntityFalloutRain(ModEntities.FALLOUT_RAIN.get(), level());
                fallout.setPos(getX(), getY(), getZ());
                fallout.setScale((int) (this.length * 2.5 + falloutAdd) * ServerConfig.FALLOUT_RANGE.getAsInt() / 100);
                level().addFreshEntity(fallout);
            }
            this.clearChunkLoader();
            this.discard();
        }
    }

    private void radiate(float rads, double range) {
        AABB box = new AABB(getX(), getY(), getZ(), getX(), getY(), getZ()).inflate(range);
        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, box);

        for (LivingEntity e : entities) {
            Vec3 vec = new Vec3(e.getX() - getX(), (e.getEyeY()) - getY(), e.getZ() - getZ());
            double len = vec.length();
            vec = vec.normalize();

            float res = 0;

            for (int i = 1; i < len; i++) {
                BlockPos pos = new BlockPos(
                        (int) Math.floor(getX() + vec.x * i),
                        (int) Math.floor(getY() + vec.y * i),
                        (int) Math.floor(getZ() + vec.z * i)
                );
                BlockState state = level().getBlockState(pos);
                res += state.getExplosionResistance(level(), pos, null);
            }

            if (res < 1) res = 1;

            float eRads = rads;
            eRads /= res;
            eRads /= (float)(len * len);

            ContaminationUtil.contaminate(e, HazardType.RADIATION, ContaminationType.RAD_BYPASS, eRads);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (explosion != null) explosion.cancel();
        super.remove(reason);
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("tickCount");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("tickCount", this.tickCount);
    }

    public static EntityNukeExplosionMK5 statFac(Level level, int strength, double x, double y, double z) {
        if (ServerConfig.ENABLE_EXTENDED_LOGGING.getAsBoolean() && !level.isClientSide) {
            HBMsNTM.LOGGER.info("[NUKE] Initialized explosion at {} / {} / {} with strength {}!", x, y, z, strength);
        }

        if (strength == 0) strength = 25;
        strength *= 2;

        EntityNukeExplosionMK5 mk5 = new EntityNukeExplosionMK5(ModEntities.NUKE_MK5.get(), level);
        mk5.strength = strength;
        mk5.speed = (int) Math.ceil(100000D / mk5.strength);
        mk5.setPos(x, y, z);
        mk5.length = mk5.strength / 2;
        mk5.loadChunk((int) Math.floor(x / 16D), (int) Math.floor(z / 16D));
        return mk5;
    }

    public static EntityNukeExplosionMK5 statFacNoRad(Level level, int strength, double x, double y, double z) {
        EntityNukeExplosionMK5 mk5 = statFac(level, strength, x, y, z);
        mk5.fallout = false;
        return mk5;
    }

    public static EntityNukeExplosionMK5 statFacBalefire(Level level, int strength, double x, double y, double z) {
        EntityNukeExplosionMK5 mk5 = statFac(level, strength, x, y, z);
        mk5.fallout = false;
        mk5.balefire = true;
        return mk5;
    }

    public EntityNukeExplosionMK5 moreFallout(int fallout) {
        falloutAdd = fallout;
        return this;
    }
}
