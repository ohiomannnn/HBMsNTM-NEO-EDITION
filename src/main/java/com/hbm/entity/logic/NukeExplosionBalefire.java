package com.hbm.entity.logic;

import com.hbm.HBMsNTM;
import com.hbm.config.ModConfigs;
import com.hbm.explosion.ExplosionBalefire;
import com.hbm.explosion.ExplosionNukeGeneric;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NukeExplosionBalefire extends ChunkloadingEntity {

    public int age = 0;
    public int destructionRange = 0;
    public ExplosionBalefire exp;
    public int speed = 1;
    public boolean did = false;

    public NukeExplosionBalefire(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        age = compoundTag.getInt("age");
        destructionRange = compoundTag.getInt("destructionRange");
        speed = compoundTag.getInt("speed");
        did = compoundTag.getBoolean("did");

        exp = new ExplosionBalefire((int) this.getX(), (int) this.getY(), (int) this.getZ(), this.level(), this.destructionRange);
        exp.readFromNbt(compoundTag, "exp_");

        this.did = true;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("age", age);
        compoundTag.putInt("destructionRange", destructionRange);
        compoundTag.putInt("speed", speed);
        compoundTag.putBoolean("did", did);

        if(exp != null)
            exp.saveToNbt(compoundTag, "exp_");
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.did) {
            if(ModConfigs.COMMON.ENABLE_EXTENDED_LOGGING.get() && !level().isClientSide)
                HBMsNTM.LOGGER.info("[NUKE] Initialized BF explosion at {} / {} / {} with strength {}!", getX(), getY(), getZ(), destructionRange);

            exp = new ExplosionBalefire((int) this.getX(), (int) this.getY(), (int) this.getZ(), this.level(), this.destructionRange);

            this.did = true;
        }

        speed += 1; // increase speed to keep up with expansion

        boolean flag = false;
        for (int i = 0; i < this.speed; i++) {
            flag = exp.update();

            if (flag) {
                this.discard();
            }
        }

        if (!flag) {
            ExplosionNukeGeneric.dealDamage(this.level(), this.getX(), this.getY(), this.getZ(), this.destructionRange * 2);
        }

        age++;
    }
}
