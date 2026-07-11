package com.hbm.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class ProjectileNT extends Projectile {

    protected ProjectileNT(EntityType<? extends ProjectileNT> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = this.getBoundingBox().getSize() * 4.0;
        if(Double.isNaN(size)) size = 4.0;
        size *= 64.0;
        return distance < size * size;
    }

    @Override public boolean canUsePortal(boolean allowPassengers) { return true; }

    @Override
    public void tick() {
        super.tick();

        HitResult hr = this.getHitResult(this::canHitEntity);
        if(hr.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hr)) {
            this.hitTargetOrDeflectSelf(hr);
        }

        this.checkInsideBlocks();

        Vec3 delta = this.getDeltaMovement();
        double dX = this.getX() + delta.x * this.getMotionMultiplier();
        double dY = this.getY() + delta.y * this.getMotionMultiplier();
        double dZ = this.getZ() + delta.z * this.getMotionMultiplier();

        this.rotation();

        float drag = this.getAirDrag();
        if(this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                this.level.addParticle(ParticleTypes.BUBBLE, dX - delta.x * 0.25, dY - delta.y * 0.25, dZ - delta.z * 0.25, delta.x, delta.y, delta.z);
            }
            drag = this.getWaterDrag();
        }

        this.setDeltaMovement(delta.scale(drag));
        this.applyGravity();
        this.setPos(dX, dY, dZ);
    }

    private HitResult getHitResult(Predicate<Entity> filter) {

        Vec3 pos = this.position();
        Vec3 delta = this.getDeltaMovement();
        Vec3 added = pos.add(delta);

        HitResult hr = BlockHitResult.miss(Vec3.ZERO, Direction.DOWN, BlockPos.ZERO);
        if(!this.isSpectral()) hr = this.level.clip(new ClipContext(pos, added, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if(hr.getType() != HitResult.Type.MISS) added = hr.getLocation();

        EntityHitResult ehr = this.getEntityHitResult(pos, added, this.getBoundingBox().expandTowards(delta).inflate(1.0), filter);
        if(ehr != null) hr = ehr;

        return hr;
    }

    @Nullable
    private EntityHitResult getEntityHitResult(Vec3 startVec, Vec3 endVec, AABB bounding, Predicate<Entity> filter) {
        double nearest = Double.MAX_VALUE;
        Entity hitEntity = null;

        for(Entity entity : this.level.getEntities(this, bounding, filter)) {
            AABB aabb = entity.getBoundingBox().inflate(0.3);
            Optional<Vec3> optional = aabb.clip(startVec, endVec);
            if(optional.isPresent()) {
                // if penetration is enabled, run impact for all intersecting entities...
                if(this.doesPenetrate()) {
                    this.hitTargetOrDeflectSelf(new EntityHitResult(entity));
                } else {
                    double distance = startVec.distanceToSqr(optional.get());
                    if(distance < nearest) {
                        hitEntity = entity;
                        nearest = distance;
                    }
                }
            }
        }

        // ...if not, only run it for the closest hit result
        return hitEntity == null ? null : new EntityHitResult(hitEntity);
    }

    protected void rotation() {

        Vec3 delta = this.getDeltaMovement();
        double distance = delta.horizontalDistance();
        this.yRot = (float) (Math.atan2(delta.x, delta.z) * 180.0 / Math.PI);
        for(
                this.xRot = (float) (Math.atan2(delta.y, distance) * 180.0 / Math.PI) - 90F;
                this.xRot - this.xRotO < -180.0F;
                this.xRotO -= 360.0F
        );

        while(this.xRot - this.xRotO >= 180.0F) this.xRotO += 360.0F;
        while(this.yRot - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while(this.yRot - this.yRotO >= 180.0F) this.yRotO += 360.0F;

        this.xRot = Mth.lerp(0.2F, this.xRotO, this.xRot);
        this.yRot = Mth.lerp(0.2F, this.yRotO, this.yRot);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    protected double getMotionMultiplier() {
        return 1.0;
    }

    protected float getAirDrag() {
        return 0.99F;
    }

    protected float getWaterDrag() {
        return 0.8F;
    }

    public boolean doesPenetrate() {
        return false;
    }

    public boolean isSpectral() {
        return false;
    }
}