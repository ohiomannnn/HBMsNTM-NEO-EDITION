package com.hbm.entity.projectile;

import com.hbm.entity.NtmEntityTypes;
import com.hbm.items.weapon.sedna.BulletConfig;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BulletBaseMK4 extends ProjectileLerping {

    public BulletConfig config;
    //used for rendering tracers
    public float velocity;
    public float prevVelocity;
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
    public BulletBaseMK4(Level level, LivingEntity living, BulletConfig config, float damage, float gunSpread, Vec3 pos, Vec3 delta) {
        this(level);

        this.setOwner(living);
        this.setBulletConfig(config);

        this.damage = damage;

        this.moveTo(pos, 0, 0);
        this.setPos(this.position());

        this.setDeltaMovement(delta);

        this.shoot(delta.x, delta.y, delta.z, 1F, this.config.spread + gunSpread);
    }

    /** For standard guns */
    public BulletBaseMK4(LivingEntity living, BulletConfig config, float baseDamage, float gunSpread, double sideOffset, double heightOffset, double frontOffset) {
        this(living.level);

        this.setOwner(living);
        this.setBulletConfig(config);

        this.damage = baseDamage * this.config.damageMult;

        this.moveTo(living.getX(), living.getY() + living.getEyeHeight(), living.getZ(), living.yRot, living.xRot);

        Vec3 offset = new Vec3(sideOffset, heightOffset, frontOffset);
        offset = offset.xRot(-this.xRot / 180F * (float) Math.PI);
        offset = offset.yRot(-this.yRot / 180F * (float) Math.PI);

        Vec3 positionOffset = this.position().add(offset);
        this.setPos(positionOffset);

        float xd = -Mth.sin(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI);
        float yd = (-Mth.sin(this.xRot / 180.0F * (float) Math.PI));
        float zd = Mth.cos(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI);
        this.shoot(xd, yd, zd, 1F, gunSpread);
    }

    /** For turrets - angles are in radians, and pitch is negative! */
    public BulletBaseMK4(Level level, BulletConfig config, float baseDamage, float gunSpread, float yRot, float xRot) {
        this(level);

        this.setBulletConfig(config);

        this.damage = baseDamage * this.config.damageMult;

        this.yRotO = this.yRot = yRot * 180F / (float) Math.PI;
        this.xRotO = this.xRot = -xRot * 180F / (float) Math.PI;

        float xd = -Mth.sin(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI);
        float yd = (-Mth.sin(this.xRot / 180.0F * (float) Math.PI));
        float zd = Mth.cos(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI);
        this.shoot(xd, yd, zd, 1F, gunSpread);
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

        if(this.lockonTarget != null && this.lockonTarget.isAlive()) {
            Vec3 deltaMotion = this.getDeltaMovement();
            double vel = deltaMotion.length();
            Vec3 delta = new Vec3(lockonTarget.getX() - this.getX(), lockonTarget.getY() + lockonTarget.getBbHeight() / 2D - this.getY(), lockonTarget.getZ() - this.getZ());
            float turn = Math.min(0.005F * this.tickCount, 1F);
            Vec3 newDeltaMotion = new Vec3(
                    Mth.lerp(deltaMotion.x, delta.x, turn),
                    Mth.lerp(deltaMotion.y, delta.y, turn),
                    Mth.lerp(deltaMotion.z, delta.z, turn)).normalize().scale(vel);
            this.setDeltaMovement(newDeltaMotion);
            if(this.level instanceof ServerLevel serverLevel) {
                serverLevel.getChunkSource().broadcast(this, new ClientboundTeleportEntityPacket(this));
            }
        }

        this.prevVelocity = this.velocity;
        this.velocity = (float) Math.sqrt(dX * dX + dY * dY + dZ * dZ);

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
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult ehr) {

        if(!this.isAlive()) return;
        if(this.config.onEntityHit != null) this.config.onEntityHit.accept(this, ehr);
    }

    @Override protected double getHeadingForceMult() { return 1.0; }
    @Override protected double getDefaultGravity() { return this.config.gravity; }
    @Override protected double getMotionMult() { return this.config.velocity + this.accel; }
    @Override protected float getAirDrag() { return 1F; }
    @Override protected float getWaterDrag() { return 1F; }
    @Override public boolean doesPenetrate() { return this.config.doesPenetrate; }
    @Override public boolean isSpectral() { return this.config.isSpectral; }

    @Override
    protected boolean canHitEntity(Entity target) {
        if(!target.canBeHitByProjectile()) {
            return false;
        } else {
            return this.config.impactsEntities;
        }
    }
}
