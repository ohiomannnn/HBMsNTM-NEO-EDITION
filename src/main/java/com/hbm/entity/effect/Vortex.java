package com.hbm.entity.effect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Vortex extends BlackHole {

    protected static final EntityDataAccessor<Float> SHRINK_RATE = SynchedEntityData.defineId(Vortex.class, EntityDataSerializers.FLOAT);

    public Vortex(EntityType<? extends Vortex> entityType, Level level) {
        super(entityType, level);
    }

    public Vortex setShrinkRate(float shrinkRate) {
        this.entityData.set(SHRINK_RATE, shrinkRate);
        return this;
    }

    public float getShrinkRate() {
        return this.entityData.get(SHRINK_RATE);
    }

    @Override
    public void tick() {

        this.entityData.set(SIZE, this.getSize() - getShrinkRate());

        if (this.getSize() <= 0) {
            this.discard();
            return;
        }

        super.tick();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(SHRINK_RATE, 0.0025F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.entityData.set(SHRINK_RATE, tag.getFloat("ShrinkRate"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putFloat("ShrinkRate", getShrinkRate());
    }
}
