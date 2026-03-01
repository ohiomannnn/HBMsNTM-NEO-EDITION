package com.hbm.entity.logic;

import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.explosion.ExplosionFleija;
import com.hbm.explosion.ExplosionHurtUtil;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.ExplosionSolinium;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NukeExplosionMK3 extends ChunkloadingEntity {

    public int age = 0;
    public int destructionRange = 0;
    public ExplosionFleija expl;
    public ExplosionSolinium sol;
    public int speed = 1;
    public float coefficient = 1;
    public float coefficient2 = 1;
    public boolean did = false;
    public boolean did2 = false;
    public boolean waste = true;
    //Extended Type
    public int extType = 0;

    public NukeExplosionMK3(EntityType<? extends NukeExplosionMK3> type, Level level) {
        super(type, level);
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            updateChunkTicket();
        }

        if (!this.did) {
            if (extType == 0) expl = new ExplosionFleija((int) this.position.x, (int) this.position.y, (int) this.position.z, this.level, this.destructionRange, this.coefficient, this.coefficient2);
            if (extType == 1) sol = new ExplosionSolinium((int) this.position.x, (int) this.position.y, (int) this.position.z, this.level, this.destructionRange, this.coefficient, this.coefficient2);

            this.did = true;
        }

        speed += 1;	//increase speed to keep up with expansion

        boolean flag = false;

        for (int i = 0; i < this.speed; i++) {
            if (extType == 0) {
                if (expl.update()) {
                    this.discard();
                }
            }
            if (extType == 1) {
                if (sol.update()) {
                    this.discard();
                }
            }
        }

        if (!flag) {
            this.level.playSound(null, this.position.x, this.position.y, this.position.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);

            if (extType != 1) {
                ExplosionNukeGeneric.dealDamage(this.level, this.position.x, this.position.y, this.position.z, this.destructionRange * 2);
            } else {
                ExplosionHurtUtil.doRadiation(this.level, this.position.x, this.position.y, this.position.z, 15000, 250000, this.destructionRange);
            }

        }

        age++;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("age");
        destructionRange = tag.getInt("destructionRange");
        speed = tag.getInt("speed");
        coefficient = tag.getFloat("coefficient");
        coefficient2 = tag.getFloat("coefficient2");
        did = tag.getBoolean("did");
        did2 = tag.getBoolean("did2");
        waste = tag.getBoolean("waste");
        extType = tag.getInt("extType");

        long time = tag.getLong("milliTime");

        if (MainConfig.COMMON.LIMIT_EXPLOSION_LIFESPAN.get() > 0 && System.currentTimeMillis() - time > MainConfig.COMMON.LIMIT_EXPLOSION_LIFESPAN.get() * 1000) {
            this.discard();
        }

        if (extType == 0) {
            expl = new ExplosionFleija((int) this.position.x, (int) this.position.y, (int) this.position.z, this.level, this.destructionRange, this.coefficient, this.coefficient2);
            expl.readFromNbt(tag, "expl_");
        }
        if (extType == 1) {
            sol = new ExplosionSolinium((int) this.position.x, (int) this.position.y, (int) this.position.z, this.level, this.destructionRange, this.coefficient, this.coefficient2);
            sol.readFromNbt(tag, "sol_");
        }
        this.did = true;

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("age", age);
        tag.putInt("destructionRange", destructionRange);
        tag.putInt("speed", speed);
        tag.putFloat("coefficient", coefficient);
        tag.putFloat("coefficient2", coefficient2);
        tag.putBoolean("did", did);
        tag.putBoolean("did2", did2);
        tag.putBoolean("waste", waste);
        tag.putInt("extType", extType);

        tag.putLong("milliTime", System.currentTimeMillis());

        if (expl != null) expl.saveToNbt(tag, "expl_");
        if (sol != null) sol.saveToNbt(tag, "sol_");
    }

    public static NukeExplosionMK3 statFacFleija(Level level, double x, double y, double z, int range) {

        NukeExplosionMK3 entity = new NukeExplosionMK3(ModEntityTypes.NUKE_MK3.get(), level);
        entity.setPos(x, y, z);
        entity.destructionRange = range;
        entity.speed = MainConfig.COMMON.BLAST_SPEED.get();
        entity.coefficient = 1.0F;
        entity.waste = false;

        return entity;
    }

    public NukeExplosionMK3 makeSol() {
        this.extType = 1;
        return this;
    }
}
