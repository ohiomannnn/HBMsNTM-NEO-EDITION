package com.hbm.entity.logic;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.lib.ModDamageTypes;
import com.hbm.lib.ModSounds;
import com.hbm.particle.helper.ExplosionSmallCreator;
import com.hbm.util.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class PlaneBase extends ChunkloadingEntity {

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYRot;
    protected double lerpXRot;

    protected PlaneBase(EntityType<?> type, Level level) {
        super(type, level);
    }

    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(PlaneBase.class, EntityDataSerializers.FLOAT);
    public float getHealth() { return this.entityData.get(HEALTH); }
    public void setHealth(float health) { this.entityData.set(HEALTH, health); }
    public int timer = getLifetime();

    public float getMaxHealth() { return 50F; }
    public int getLifetime() { return 200; }

    @Override public boolean canBeCollidedWith() { return this.getHealth() > 0; }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(ModDamageTypes.NUCLEAR_BLAST)) return false;
        if (this.isInvulnerable()) return false;
        if (this.isAlive() && !level().isClientSide && this.getHealth() > 0) {
            setHealth(getHealth() - amount);
            if (this.getHealth() <= 0) this.killPlane();
        }
        return true;
    }

    protected void killPlane() {
        ExplosionSmallCreator.composeEffect(level(), this.getX(), this.getY(), this.getZ(), 25, 3.5F, 2F);
        level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.PLANE_SHOT_DOWN, SoundSource.AMBIENT, 25.0F, 1.0F);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yRot;
        this.lerpXRot = xRot;
        this.lerpSteps = steps + 1;
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
        return this.lerpSteps > 0 ? (float) this.lerpXRot : this.getXRot();
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? (float) this.lerpYRot : this.getYRot();
    }

    @Override
    public void tick() {

        if (!level().isClientSide) {
            updateChunkTicket();
        }

        if (level().isClientSide) {
            if (this.lerpSteps > 0) {
                this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
                --this.lerpSteps;
            } else {
                this.reapplyPosition();
            }
        } else {
            this.xo = this.xOld = this.getX();
            this.yo = this.yOld = this.getY();
            this.zo = this.zOld = this.getZ();

            this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

            this.rotation();

            if (this.getHealth() <= 0) {
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.025, this.getDeltaMovement().z);

                for (int i = 0; i < 10; i++)
                    ParticleUtil.spawnGasFlame(this.level(),
                            this.getX() + random.nextGaussian() * 0.5 - this.getDeltaMovement().x * 2,
                            this.getY() + random.nextGaussian() * 0.5 - this.getDeltaMovement().y * 2,
                            this.getZ() + random.nextGaussian() * 0.5 - this.getDeltaMovement().z * 2,
                            0.0, 0.1, 0.0);

                if ((!level().getBlockState(this.blockPosition()).isAir()) || this.getY() < this.level().getMinBuildHeight()) {
                    this.discard();
                    new ExplosionVNT(this.level(), this.getX(), this.getY(), this.getZ(), 15F).makeStandard().explode();
                    level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.PLANE_CRASH, SoundSource.AMBIENT, 25.0F, 1.0F);
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
        float motionHorizontal = Mth.sqrt((float) (this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z));
        this.setYRot((float) (Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * 180.0D / Math.PI));
        for (this.setXRot((float) (Math.atan2(this.getDeltaMovement().y, motionHorizontal) * 180.0D / Math.PI) - 90); this.getXRot() - this.xRotO < -180.0F; this.xRotO -= 360.0F);
        while (this.getXRot() - this.xRotO >= 180.0F) this.xRotO += 360.0F;
        while (this.getYRot() - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while (this.getYRot() - this.yRotO >= 180.0F) this.yRotO += 360.0F;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HEALTH, getMaxHealth());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("TickCount");
        this.entityData.set(HEALTH, tag.getFloat("Health"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("TickCount", this.tickCount);
        tag.putFloat("Health", getHealth());
    }

    @Override public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }
}
