package com.hbm.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class DebrisBase extends Entity {

    public static final EntityDataAccessor<Integer> DEB_TYPE = SynchedEntityData.defineId(DebrisBase.class, EntityDataSerializers.INT);

    public float rot;
    public float lastRot;
    protected boolean hasSizeSet = false;

    public DebrisBase(EntityType<? extends DebrisBase> entityType, Level level) {
        super(entityType, level);
    }

    public abstract boolean interactFirst(Player player);

    protected abstract int getLifetime();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DEB_TYPE, 0);
        this.rot = this.lastRot = this.random.nextFloat() * 360F;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(DEB_TYPE, tag.getInt("debtype"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("debtype", this.entityData.get(DEB_TYPE));
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {

        int range = 128;
        return distance < range * range;
    }
}
