package com.hbm.entity.logic;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.particle.NtmParticles;
import com.hbm.particle.helper.ExplosionSmallCreator;
import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.registry.NtmDamageTypes;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.BobMathUtil;
import com.hbm.util.SoundUtils;
import com.hbm.util.particle.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public abstract class PlaneBase extends Entity {

    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(PlaneBase.class, EntityDataSerializers.FLOAT);

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected float lerpYRot;
    protected float lerpXRot;

    protected PlaneBase(EntityType<? extends PlaneBase> type, Level level) {
        super(type, level);
    }

    public float getHealth() { return this.entityData.get(HEALTH); }
    public int timer = getLifetime();

    public float getMaxHealth() { return 50F; }
    public int getLifetime() { return 200; }

    @Override public boolean canBeCollidedWith() { return this.getHealth() > 0; }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.is(NtmDamageTypes.NUCLEAR_BLAST)) return false;
        if(this.isInvulnerable()) return false;
        if(this.isAlive() && !this.level.isClientSide && this.getHealth() > 0) {
            this.entityData.set(HEALTH, this.entityData.get(HEALTH) - amount);
            if(this.entityData.get(HEALTH) <= 0) this.killPlane();
        }
        return true;
    }

    protected void killPlane() {
        ExplosionSmallCreator.composeEffect(this.level, this.position.x, this.position.y, this.position.z, 25, 3.5F, 2F);
        SoundUtils.playAtEntity(this, NtmSoundEvents.PLANE_SHOT_DOWN.get(), SoundSource.HOSTILE, 25.0F, 1.0F);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yRot;
        this.lerpXRot = xRot;
        this.lerpSteps = 10;
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        this.setDeltaMovement(x, y, z);
    }

    @Override
    public double lerpTargetX() {
        return this.lerpSteps > 0 ? this.lerpX : this.getX();
    }

    @Override
    public double lerpTargetY() {
        return this.lerpSteps > 0 ? this.lerpY : this.getY();
    }

    @Override
    public double lerpTargetZ() {
        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
    }

    @Override
    public float lerpTargetXRot() {
        return this.lerpSteps > 0 ? this.lerpXRot : this.xRot;
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? this.lerpYRot : this.yRot;
    }

    @Override
    public void tick() {

        if(this.level.isClientSide) {
            if(this.lerpSteps > 0) {
                this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
                --this.lerpSteps;
            } else {
                this.reapplyPosition();
            }
        } else {
            this.xo = this.xOld = this.getX();
            this.yo = this.yOld = this.getY();
            this.zo = this.zOld = this.getZ();

            ChunkPos oldPos = this.chunkPosition();
            this.setPos(this.getX() + this.deltaMovement.x, this.getY() + this.deltaMovement.y, this.getZ() + this.deltaMovement.z);
            ChunkPos newPos = this.chunkPosition();
            if(oldPos != newPos) {
                if(this.level instanceof ServerLevel serverLevel) {
                    serverLevel.setChunkForced(oldPos.x, oldPos.z, false);
                    serverLevel.setChunkForced(newPos.x, newPos.z, true);
                }
            }

            this.rotation();

            if (this.getHealth() <= 0) {
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.025, this.getDeltaMovement().z);

                for (int i = 0; i < 10; i++) {
                    ParticleUtil.addParticle(this.level, new NbtParticleOptions(NtmParticles.GAS_FLAME.get(), new CompoundTag()), this.getX() + random.nextGaussian() * 0.5 - this.deltaMovement.x * 2, this.getY() + random.nextGaussian() * 0.5 - this.deltaMovement.y * 2, this.getZ() + random.nextGaussian() * 0.5 - this.deltaMovement.z * 2, 0.0F, 0.1F, 0.0F, 150.0);
                }

                if ((!level().getBlockState(this.blockPosition()).isAir()) || this.getY() < this.level().getMinBuildHeight()) {
                    this.discard();
                    new ExplosionVNT(this.level(), this.getX(), this.getY(), this.getZ(), 15F).makeStandard().explode();
                    level().playSound(null, this.getX(), this.getY(), this.getZ(), NtmSoundEvents.PLANE_CRASH, SoundSource.HOSTILE, 25.0F, 1.0F);
                }
            } else {
                this.setDeltaMovement(this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
            }

            if (this.tickCount > timer) {
                this.discard();
            }
        }
    }

    protected void rotation() {
        float motionHorizontal = BobMathUtil.sqrt(this.deltaMovement.x * this.deltaMovement.x + this.deltaMovement.z * this.deltaMovement.z);
        this.yRot = BobMathUtil.atan2(this.deltaMovement.x, this.deltaMovement.z) * 180.0F / BobMathUtil.PI;
        for(
                this.xRot = (BobMathUtil.atan2(this.deltaMovement.y, motionHorizontal) * 180.0F / BobMathUtil.PI) - 90F;
                this.xRot - this.xRotO < -180.0F;
                this.xRotO -= 360.0F
        );
        while(this.xRot - this.xRotO >= 180.0F) this.xRotO += 360.0F;
        while(this.yRot - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while(this.yRot - this.yRotO >= 180.0F) this.yRotO += 360.0F;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HEALTH, this.getMaxHealth());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("TickCount");
        this.entityData.set(HEALTH, tag.getFloat("Health"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("TickCount", this.tickCount);
        tag.putFloat("Health", this.getHealth());
    }

    @Override public boolean shouldRenderAtSqrDistance(double distance) { return true; }

    @Override
    public void onAddedToLevel() {

        if(this.level instanceof ServerLevel serverLevel) {
            serverLevel.setChunkForced(this.chunkPosition().x, this.chunkPosition().z, true);
        }
    }

    @Override
    public void onRemovedFromLevel() {

        if(this.level instanceof ServerLevel serverLevel) {
            serverLevel.setChunkForced(this.chunkPosition().x, this.chunkPosition().z, false);
        }
    }
}
