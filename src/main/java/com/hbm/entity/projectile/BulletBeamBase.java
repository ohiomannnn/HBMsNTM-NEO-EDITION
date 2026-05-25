package com.hbm.entity.projectile;

import com.hbm.entity.NtmEntityTypes;
import com.hbm.items.weapon.sedna.BulletConfig;
import com.hbm.util.RayTraceResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import javax.annotation.Nullable;

public class BulletBeamBase extends Entity implements IEntityWithComplexSpawn {

    public LivingEntity thrower;
    public BulletConfig config;
    public float damage;
    public double headingX;
    public double headingY;
    public double headingZ;
    public double beamLength;

    // private!!! use getters
    private static final EntityDataAccessor<Integer> BULLET_CONFIG = SynchedEntityData.defineId(BulletBeamBase.class, EntityDataSerializers.INT);

    public BulletBeamBase(EntityType<? extends BulletBeamBase> entityType, Level level) { super(entityType, level); }
    public BulletBeamBase(Level level) { super(NtmEntityTypes.BULLET_BEAM.get(), level); }

    public LivingEntity getThrower() { return this.thrower; }

    public BulletBeamBase(Level level, BulletConfig config, float baseDamage) {
        this(level);

        this.setBulletConfig(config);
        this.damage = baseDamage * this.config.damageMult;
    }

    public BulletBeamBase(LivingEntity entity, BulletConfig config, float baseDamage) {
        this(entity.level, config, baseDamage);
        this.thrower = entity;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double perimeter = this.getBoundingBox().getSize();
        if(Double.isNaN(perimeter)) perimeter = 1.0;
        perimeter *= 64.0 * 10.0;
        return distance < perimeter * perimeter;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BULLET_CONFIG, 0);
    }

    public void setBulletConfig(BulletConfig config) {
        this.config = config;
        this.getEntityData().set(BULLET_CONFIG, config.id);
    }

    @Nullable
    public BulletConfig getBulletConfig() {
        int id = this.getEntityData().get(BULLET_CONFIG);
        if(id < 0 || id > BulletConfig.configs.size()) return null;
        return BulletConfig.configs.get(id);
    }

    @Override
    public void tick() {
        if(config == null) config = this.getBulletConfig();

        if(config == null) {
            this.discard();
            return;
        }

        if(config.onUpdate != null) config.onUpdate.accept(this);

        this.tick();

        if(!level.isClientSide && this.tickCount > config.expires) this.discard();
    }

    protected void onImpact(RayTraceResult rtr) {
        if(!level.isClientSide) {
            if(this.config.onImpactBeam != null) this.config.onImpactBeam.accept(this, rtr);
        }
    }

    public boolean doesImpactEntities() { return this.config.impactsEntities; }
    public boolean doesPenetrate() { return this.config.doesPenetrate; }
    public boolean isSpectral() { return this.config.isSpectral; }

    @Override protected void addAdditionalSaveData(CompoundTag tag) { }
    @Override public boolean save(CompoundTag tag) { return false; }
    @Override protected void readAdditionalSaveData(CompoundTag tag) { this.discard(); }

    @Override public void writeSpawnData(RegistryFriendlyByteBuf buf) {
        buf.writeDouble(beamLength);
        buf.writeFloat(this.yRot);
        buf.writeFloat(this.xRot);
    }

    @Override public void readSpawnData(RegistryFriendlyByteBuf buf) {
        this.beamLength = buf.readDouble();
        this.yRot = buf.readFloat();
        this.xRot = buf.readFloat();
    }
}
