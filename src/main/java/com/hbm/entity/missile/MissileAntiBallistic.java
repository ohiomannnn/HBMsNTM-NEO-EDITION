package com.hbm.entity.missile;

import api.hbm.entity.IRadarDetectableNT;
import com.hbm.blockentity.machine.MachineRadarBlockEntity;
import com.hbm.entity.projectile.ProjectileLerping;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.EntityProcessorCross;
import com.hbm.explosion.vanillant.standard.ExplosionEffectWeapon;
import com.hbm.particle.NtmParticles;
import com.hbm.util.particle.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MissileAntiBallistic extends ProjectileLerping implements IRadarDetectableNT {

    public static final double BASE_SPEED = 1.5;

    public Entity tracking;
    public double velocity;
    protected int activationTimer;

    public MissileAntiBallistic(EntityType<? extends MissileAntiBallistic> entityType, Level level) {
        super(entityType, level);

        this.yRot = 90F;
        this.setDeltaMovement(new Vec3(0, BASE_SPEED, 0));
    }

    @Override
    protected double getMotionMultiplier() {
        return velocity;
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return false;
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override
    public void tick() {
        super.tick();

        if(!this.level.isClientSide) {
            if(velocity < 6) velocity += 0.1;

            if(activationTimer < 40) {
                activationTimer++;
                this.setDeltaMovement(new Vec3(0, BASE_SPEED, 0));
            } else {
                Entity prevTracking = this.tracking;

                if(this.tracking == null || this.tracking.isRemoved()) this.targetMissile();

                if(prevTracking == null && this.tracking != null) {
                    if(this.level instanceof ServerLevel serverLevel) {
                        ExplosionLarge.spawnShock(serverLevel, this.position().x, this.position().y, this.position().z, 24, 3F);
                    }
                }
                if(this.tracking != null && this.tracking.isAlive()) {
                    this.aimAtTarget();
                } else {
                    if(this.tickCount > 600) this.discard();
                }
            }

            if(this.position().y > 2000 && (this.tracking == null || this.tracking.isRemoved())) this.discard();

        } else {
            Vec3 motion = this.getDeltaMovement().normalize();
            double x = this.position().x - motion.x;
            double y = this.position().y - motion.y;
            double z = this.position().z - motion.z;
            ParticleUtil.addParticle(this.level, NtmParticles.ABM_CONTRAIL.get(), x, y, z);
        }
    }

    /** Detects and caches nearby EntityMissileBaseNT */
    protected void targetMissile() {

        Entity closest = null;
        double dist = 1_000;

        for(Entity e : MachineRadarBlockEntity.matchingEntities) {
            if(e.level.dimension() != this.level.dimension()) continue;
            if(!(e instanceof MissileBase)) continue; //can only lock onto missiles
            if(e instanceof MissileStealth) continue; //cannot lack onto missiles with stealth coating

            Vec3 delta = this.position().vectorTo(e.position());

            if(delta.length() < dist) closest = e;
        }

        this.tracking = closest;
    }

    /** Predictive targeting system */
    protected void aimAtTarget() {

        Vec3 delta = this.position().vectorTo(tracking.position());
        double intercept = delta.length() / (BASE_SPEED * this.velocity);
        Vec3 predicted = new Vec3(tracking.position().x + (tracking.position().x - tracking.xo) * intercept, tracking.position().y + (tracking.position().y - tracking.yo) * intercept, tracking.position().z + (tracking.position().z - tracking.zo) * intercept);
        Vec3 motion = this.position().vectorTo(predicted).normalize();

        if(delta.length() < 10 && activationTimer >= 40) {
            this.discard();
            ExplosionVNT vnt = new ExplosionVNT(this.level, this.position().x, this.position().y, this.position().z, 15F)
                    .setEntityProcessor(new EntityProcessorCross(7.5D).withRangeMod(2))
                    .setSFX(new ExplosionEffectWeapon(15, 5F, 2F));
            vnt.explode();
        }

        this.setDeltaMovement(motion.multiply(BASE_SPEED, BASE_SPEED, BASE_SPEED));
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if(this.activationTimer >= 40) {
            this.discard();
            if(this.level instanceof ServerLevel serverLevel) {
                ExplosionLarge.explode(serverLevel, this.position().x, this.position().y, this.position().z, 20, true, false, false);
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected float getAirDrag() {
        return 1F;
    }

    @Override
    protected float getWaterDrag() {
        return 1F;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.velocity = tag.getDouble("Velocity");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putDouble("Velocity", this.velocity);
    }

    @Override public boolean shouldRenderAtSqrDistance(double distance) { return true; }

    @Override
    public String getUnlocalizedName() {
        return "radar.target.abm";
    }

    @Override
    public int getBlipLevel() {
        return IRadarDetectableNT.TIER_AB;
    }

    @Override
    public boolean canBeSeenBy(Object radar) {
        return true;
    }

    @Override
    public boolean paramsApplicable(RadarScanParams params) {
        return params.scanMissiles;
    }

    @Override
    public boolean suppliesRedstone(RadarScanParams params) {
        return false;
    }
}
