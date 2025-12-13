package com.hbm.entity.logic;

import com.hbm.HBMsNTM;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntities;
import com.hbm.entity.effect.FalloutRain;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.ExplosionNukeRayBatched;
import com.hbm.interfaces.IExplosionRay;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NukeExplosionMK5 extends ChunkloadingEntity {

    //Strength of the blast
    public int strength;
    //How many rays are calculated per tick
    public int speed;
    public int length;
    private long explosionStart;
    public boolean fallout = true;
    private int falloutAdd = 0;

    private IExplosionRay explosion;

    public NukeExplosionMK5(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (strength == 0) {
            this.discard();
        }

        if (!level().isClientSide) {
            updateChunkTicket();
        }

        //TODO: make advancement for this

        if (!level().isClientSide && fallout && explosion != null && this.tickCount < 10 && strength >= 75) {
            radiate(2_500_000F / (this.tickCount * 5 + 1), this.length * 2);
        }

        ExplosionNukeGeneric.dealDamage(level(), getX(), getY(), getZ(), this.length * 2);

        if (explosion == null) {
            explosionStart = System.currentTimeMillis();
            explosion = new ExplosionNukeRayBatched(level(), (int) getX(), (int) getY(), (int) getZ(), strength, speed, length);
        }

        if (!explosion.isComplete()) {
            explosion.cacheChunksTick(MainConfig.COMMON.MK5.get());
            explosion.destructionTick(MainConfig.COMMON.MK5.get());
        } else {
            if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get() && explosionStart != 0) {
                HBMsNTM.LOGGER.info("[NUKE] Explosion complete. Time elapsed: {}ms", (System.currentTimeMillis() - explosionStart));
            }
            if (fallout) {
                FalloutRain fallout = new FalloutRain(ModEntities.NUKE_FALLOUT_RAIN.get(), level());
                fallout.setPos(getX(), getY(), getZ());
                fallout.setScale((int) (this.length * 2.5 + falloutAdd) * MainConfig.COMMON.FALLOUT_RANGE.get() / 100);
                this.level().addFreshEntity(fallout);
            }
            this.discard();
        }
    }

    private void radiate(float rads, double range) {
        AABB box = new AABB(getX(), getY(), getZ(), getX(), getY(), getZ()).inflate(range);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, box);

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

    public static NukeExplosionMK5 statFac(Level level, int strength, double x, double y, double z) {
        if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get() && !level.isClientSide) {
            HBMsNTM.LOGGER.info("[NUKE] Initialized explosion at {} / {} / {} with strength {}!", x, y, z, strength);
        }

        if (strength == 0) strength = 25;
        strength *= 2;

        NukeExplosionMK5 explosionMK5 = new NukeExplosionMK5(ModEntities.NUKE_MK5.get(), level);
        explosionMK5.strength = strength;
        explosionMK5.speed = (int) Math.ceil(100000D / explosionMK5.strength);
        explosionMK5.setPos(x, y, z);
        explosionMK5.length = explosionMK5.strength / 2;
        level.addFreshEntity(explosionMK5);
        return explosionMK5;
    }

    public NukeExplosionMK5 setNoRad() {
        fallout = false;
        return this;
    }

    public NukeExplosionMK5 setMoreFallout(int toAdd) {
        falloutAdd = toAdd;
        return this;
    }
}
