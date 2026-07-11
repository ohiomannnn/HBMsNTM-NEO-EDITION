package com.hbm.entity.projectile;

import com.hbm.entity.NtmEntityTypes;
import com.hbm.items.weapon.sedna.BulletConfig;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BulletBaseMK4 extends ProjectileLerping {

    public BulletConfig config;
    //used for rendering tracers
    public double velocity;
    public double prevVelocity;
    public double accel;
    public float damage;
    public int ricochets = 0;
    @Nullable public Entity lockonTarget = null;

    private static final EntityDataAccessor<Integer> BULLET_CONFIG = SynchedEntityData.defineId(BulletBaseMK4.class, EntityDataSerializers.INT);

    public BulletBaseMK4(EntityType<? extends BulletBaseMK4> entityType, Level level) {
        super(entityType, level);
    }

    public BulletBaseMK4(Level level) { super(NtmEntityTypes.BULLET_MK4.get(), level); }

    /** For submunitions! */
    public BulletBaseMK4(Level level, LivingEntity entity, BulletConfig config, float damage, float gunSpread, Vec3 pos, Vec3 delta) {
        this(level);

        this.setOwner(entity);
        this.setBulletConfig(config);

        this.damage = damage;

        this.moveTo(pos, 0, 0);
        this.setPos(this.position());

        this.setDeltaMovement(delta);

        this.shoot(delta.x, delta.y, delta.z, 1.0F, this.config.spread + gunSpread);
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

        this.xo = this.position.x;
        this.yo = this.position.y;
        this.zo = this.position.z;

        super.tick();

        double dX = this.position.x - this.xo;
        double dY = this.position.y - this.yo;
        double dZ = this.position.z - this.zo;

        // todo entity target

        this.prevVelocity = this.velocity;
        this.velocity = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

        if(!level.isClientSide && this.tickCount > config.expires) this.discard();

        if(this.config.onUpdate != null) this.config.onUpdate.accept(this);
    }

    @Override
    protected void onHit(HitResult hr) {
        super.onHit(hr);

        if(!level.isClientSide) {

            if(this.config.onImpact != null) this.config.onImpact.accept(this, hr);
            if(!this.isAlive()) return;
            if(this.config.onRicochet != null) this.config.onRicochet.accept(this, hr);
            if(this.config.onEntityHit != null) this.config.onEntityHit.accept(this, hr);
        }
    }

    @Override protected double getDefaultGravity() { return this.config.gravity; }
    @Override protected double getMotionMultiplier() { return this.config.velocity + this.accel; }
    @Override protected float getAirDrag() { return 1F; }
    @Override protected float getWaterDrag() { return 1F; }

    @Override
    protected boolean canHitEntity(Entity target) {
        if(!target.canBeHitByProjectile()) {
            return false;
        } else {
            return this.config.impactsEntities;
        }
    }

    @Override public boolean doesPenetrate() { return this.config.doesPenetrate; }
    @Override public boolean isSpectral() { return this.config.isSpectral; }
}
