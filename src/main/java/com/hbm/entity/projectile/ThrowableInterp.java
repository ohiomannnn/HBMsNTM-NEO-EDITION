package com.hbm.entity.projectile;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class ThrowableInterp extends ProjectileNT {

    protected int turnProgress;
    protected double syncPosX;
    protected double syncPosY;
    protected double syncPosZ;
    protected double syncYaw;
    protected double syncPitch;
    @OnlyIn(Dist.CLIENT) protected double velocityX;
    @OnlyIn(Dist.CLIENT) protected double velocityY;
    @OnlyIn(Dist.CLIENT) protected double velocityZ;

    protected ThrowableInterp(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {

        if (!this.level().isClientSide) {
            super.tick();
        } else {
            this.xo = this.getX();
            this.yo = this.getY();
            this.zo = this.getZ();
            if (this.turnProgress > 0) {
                double interpX = this.getX() + (this.syncPosX - this.getX()) / (double) this.turnProgress;
                double interpY = this.getY() + (this.syncPosY - this.getY()) / (double) this.turnProgress;
                double interpZ = this.getZ() + (this.syncPosZ - this.getZ()) / (double) this.turnProgress;

                double d = Mth.wrapDegrees(this.syncYaw - (double) this.getYRot());
                this.setYRot((float) ((double) this.getYRot() + d / (double) this.turnProgress));
                this.setXRot((float) ((double) this.getXRot() + (this.syncPitch - (double) this.getXRot()) / (double) this.turnProgress));

                --this.turnProgress;
                this.setPos(interpX, interpY, interpZ);
            } else {
                this.setPos(this.getX(), this.getY(), this.getZ());
            }
        }
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
        this.velocityX = x;
        this.velocityY = y;
        this.velocityZ = z;
        super.setDeltaMovement(x, y, z);
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int theNumberThree) {
        this.syncPosX = x;
        this.syncPosY = y;
        this.syncPosZ = z;
        this.syncYaw = yaw;
        this.syncPitch = pitch;
        this.turnProgress = theNumberThree + this.approachNum();
        this.setDeltaMovement(velocityX, velocityY, velocityZ);
    }

    /**
     * @return a number added to the basic "3" of the approach progress value. Larger numbers make the approach smoother, but lagging behind the true value more.
     */
    public int approachNum() {
        return 0;
    }
}
