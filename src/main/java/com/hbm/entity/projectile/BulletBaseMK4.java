package com.hbm.entity.projectile;

import com.hbm.entity.NtmEntityTypes;
import com.hbm.items.weapon.sedna.BulletConfig;
import com.hbm.util.BobMathUtil;
import com.hbm.util.RayTraceResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import javax.annotation.Nullable;

public class BulletBaseMK4 extends ThrowableInterp implements IEntityWithComplexSpawn {

    public BulletConfig config;
    //used for rendering tracers
    public double velocity;
    public double prevVelocity;
    public double accel;
    public float damage;
    public int ricochets = 0;
    @Nullable public Entity lockonTarget = null;

    // private!!! use getters
    private static final EntityDataAccessor<Integer> BULLET_CONFIG = SynchedEntityData.defineId(BulletBaseMK4.class, EntityDataSerializers.INT);

    public BulletBaseMK4(EntityType<? extends ThrowableInterp> entityType, Level level) {
        super(entityType, level);
    }

    /** Shortcut */
    public BulletBaseMK4(Level level) { super(NtmEntityTypes.BULLET_MK4.get(), level); }

    /** For submunitions! */
    public BulletBaseMK4(Level level, LivingEntity entity, BulletConfig config, float damage, float gunSpread, Vec3 pos, Vec3 deltaMov) {
        this(level);

        this.thrower = entity;
        this.setBulletConfig(config);

        this.damage = damage;

        this.moveTo(pos, 0, 0);
        this.setPos(this.position());

        this.setDeltaMovement(deltaMov);

        this.setThrowableHeading(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z, 1.0F, this.config.spread + gunSpread);
    }

    /** For standard guns */
    public BulletBaseMK4(LivingEntity entity, BulletConfig config, float baseDamage, float gunSpread, double sideOffset, double heightOffset, double frontOffset) {
        this(entity.level);

        this.thrower = entity;
        this.setBulletConfig(config);

        this.damage = baseDamage * this.config.damageMult;

        this.moveTo(thrower.position.add(0, thrower.getEyeHeight(), 0), thrower.yRot, thrower.xRot);

        double motionX = -Mth.sin(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI);
        double motionY = -Mth.sin(this.xRot / 180.0F * (float) Math.PI);
        double motionZ = Mth.cos(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI);

        this.setThrowableHeading(motionX, motionY, motionZ, 1.0F, gunSpread);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

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

        if(!level.isClientSide && !this.inGround && !this.onGround && velocity > 0) {

            float motionHorizontal = BobMathUtil.sqrt(dX * dX + dZ * dZ);
            this.yRot = BobMathUtil.atan2(dX, dZ) * 180.0F / BobMathUtil.PI;
            for(
                    this.xRot = (BobMathUtil.atan2(this.deltaMovement.y, motionHorizontal) * 180.0F / BobMathUtil.PI) - 90F;
                    this.xRot - this.xRotO < -180.0F;
                    this.xRotO -= 360.0F
            );
            while(this.xRot - this.xRotO >= 180.0F) this.xRotO += 360.0F;
            while(this.yRot - this.yRotO < -180.0F) this.yRotO -= 360.0F;
            while(this.yRot - this.yRotO >= 180.0F) this.yRotO += 360.0F;
        }

        if(!level.isClientSide && this.tickCount > config.expires) this.discard();

        if(this.config.onUpdate != null) this.config.onUpdate.accept(this);
    }

    @Override
    protected void onImpact(RayTraceResult rtr) {
        if(!level.isClientSide) {

            if(this.config.onImpact != null) this.config.onImpact.accept(this, rtr);
            if(!this.isAlive() || this.inGround) return;
            if(this.config.onRicochet != null) this.config.onRicochet.accept(this, rtr);
            if(this.config.onEntityHit != null) this.config.onEntityHit.accept(this, rtr);
        }
    }

    @Override protected double headingForceMult() { return 1D; }
    @Override public double getGravityVelocity() { return this.config.gravity; }
    @Override protected double motionMult() { return this.config.velocity + this.accel; }
    @Override protected float getAirDrag() { return 1F; }
    @Override protected float getWaterDrag() { return 1F; }

    @Override public boolean doesImpactEntities() { return this.config.impactsEntities; }
    @Override public boolean doesPenetrate() { return this.config.doesPenetrate; }
    @Override public boolean isSpectral() { return this.config.isSpectral; }
    @Override public int selfDamageDelay() { return this.config.selfDamageDelay; }

    @Override public void writeSpawnData(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.thrower != null ? thrower.getId() : -1);
    }

    @Override public void readSpawnData(RegistryFriendlyByteBuf buf) {
        Entity e = level.getEntity(buf.readInt());
        if(e instanceof LivingEntity livingEntity) this.thrower = livingEntity;
    }
}
