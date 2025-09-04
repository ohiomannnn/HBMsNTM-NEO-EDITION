package com.hbm.entity.grenade;


import com.hbm.lib.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class EntityGrenadeBouncyBase extends Projectile {

    protected LivingEntity thrower;
    protected String throwerName;
    protected int timer = 0;

    public EntityGrenadeBouncyBase(EntityType<? extends EntityGrenadeBouncyBase> type, Level level) {
        super(type, level);
        this.setBoundingBox(this.getBoundingBox().inflate(0.25F));
    }

    public EntityGrenadeBouncyBase(EntityType<? extends EntityGrenadeBouncyBase> type, Level level, LivingEntity living) {
        super(type, level);
        this.thrower = living;
        this.setPos(living.getX(), living.getEyeY(), living.getZ());

        float f = 0.4F;
        float yaw = living.getYRot();
        float pitch = living.getXRot();

        double x = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F)) * f;
        double z = Mth.cos(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F)) * f;
        double y = -Mth.sin((pitch + getPitchOffset()) * ((float)Math.PI / 180F)) * f;

        this.shoot(x, y, z, getVelocity(), 1.0F);
    }

    public EntityGrenadeBouncyBase(EntityType<? extends EntityGrenadeBouncyBase> type, Level level, double x, double y, double z) {
        super(type, level);
        this.setPos(x, y, z);
    }

    protected float getVelocity() {
        return 1.5F;
    }

    protected float getPitchOffset() {
        return 0.0F;
    }

    protected float getGravityVelocity() {
        return 0.03F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double dist) {
        double d1 = this.getBoundingBox().getSize() * 4.0D;
        d1 *= 64.0D;
        return dist < d1 * d1;
    }

    @Override
    public void tick() {
        super.tick();

        setXRot(getXRot() - (float) getDeltaMovement().length() * 25);

        Vec3 motion = getDeltaMovement();

        move(MoverType.SELF, motion);

        boolean bounced = false;

        if (this.horizontalCollision || this.verticalCollision) {
            if (this.verticalCollision) {
                motion = new Vec3(motion.x, -motion.y * getBounceMod(), motion.z);
                bounced = true;
            }
            if (this.horizontalCollision) {
                motion = new Vec3(motion.x * getBounceMod(), motion.y, motion.z * getBounceMod());
                bounced = true;
            }

            if (bounced && motion.length() > 0.10) {
                this.level().playSound(null, this.blockPosition(),
                        ModSounds.GRENADE_BOUNCE.get(), net.minecraft.sounds.SoundSource.NEUTRAL,
                        2.0F, 1.0F);
            }
        }

        motion = motion.add(0, -getGravityVelocity(), 0);

        double friction = this.isInWater() ? 0.8 : 0.99;
        motion = new Vec3(motion.x * friction, motion.y * friction, motion.z * friction);

        setDeltaMovement(motion);

        timer++;
        if (timer >= getMaxTimer() && !this.level().isClientSide) {
            explode();
            timer = 0;
        }

        setYRot((float) (Math.atan2(motion.x, motion.z) * (180 / Math.PI)));
    }


    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("timer", timer);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        timer = tag.getInt("timer");
    }

    public LivingEntity getThrower() {
        return thrower;
    }

    public abstract void explode();

    protected abstract int getMaxTimer();

    protected abstract double getBounceMod();
}