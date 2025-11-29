package com.hbm.entity.projectile;

import com.hbm.entity.IProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class ProjectileNT extends Entity implements IProjectile {

    private static final EntityDataAccessor<Byte> STUCK_IN = SynchedEntityData.defineId(ProjectileNT.class, EntityDataSerializers.BYTE);

    private BlockPos stuckPos;
    private int stuckBlockState;
    protected boolean inGround;
    public int throwableShake;
    protected LivingEntity thrower;
    private String throwerUUID;
    public int ticksInGround;
    private int ticksInAir;

    public ProjectileNT(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(STUCK_IN, (byte) 0);
    }

    public void setStuckIn(@Nullable Direction side) {
        this.entityData.set(STUCK_IN, side == null ? (byte) -1 : (byte) side.ordinal());
    }

    public @Nullable Direction getStuckIn() {
        byte val = this.entityData.get(STUCK_IN);
        return val == -1 ? null : Direction.values()[val];
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double perimeter = this.getBoundingBox().getSize() * 4.0D;
        perimeter *= 64.0D;
        return distance < perimeter * perimeter;
    }

    public ProjectileNT(EntityType<?> entityType, Level level, LivingEntity thrower) {
        this(entityType, level);
        this.thrower = thrower;
        this.moveTo(thrower.getX(), thrower.getY() + (double) thrower.getEyeHeight(), thrower.getZ(), thrower.getYRot(), thrower.getXRot());
        this.setPos(Mth.cos(this.getYRot() / 180.0F * (float) Math.PI) * 0.16F, 0.1D, Mth.sin(this.getXRot() / 180.0F * (float) Math.PI) * 0.16F);
        float velocity = 0.4F;
        this.setDeltaMovement(
                -Mth.sin(this.getYRot() / 180.0F * (float) Math.PI) * Mth.cos(this.getXRot() / 180.0F * (float) Math.PI) * velocity,
                Mth.cos(this.getYRot() / 180.0F * (float) Math.PI) * Mth.cos(this.getXRot()  / 180.0F * (float) Math.PI) * velocity,
                -Mth.sin((this.getXRot() + this.throwAngle()) / 180.0F * (float) Math.PI) * velocity
        );
        this.setThrowableHeading(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z, this.throwForce(), 1.0F);
    }

    public ProjectileNT(EntityType<?> entityType, Level level, double x, double y, double z) {
        this(entityType, level);
        this.ticksInGround = 0;
        this.setPos(x, y ,z);
    }

    protected float throwForce() {
        return 1.5F;
    }

    protected double headingForceMult() {
        return 0.0075D;
    }

    protected float throwAngle() {
        return 0.0F;
    }

    protected double motionMult() {
        return 1.0D;
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {

        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        double throwLen = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= throwLen;
        motionY /= throwLen;
        motionZ /= throwLen;

        motionX += this.random.nextGaussian() * headingForceMult() * inaccuracy;
        motionY += this.random.nextGaussian() * headingForceMult() * inaccuracy;
        motionZ += this.random.nextGaussian() * headingForceMult() * inaccuracy;

        motionX *= velocity;
        motionY *= velocity;
        motionZ *= velocity;

        this.setDeltaMovement(motionX, motionY, motionZ);

        double hyp = Math.sqrt(motionX * motionX + motionZ * motionZ);
        this.setYRot((float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI));
        this.setXRot((float)(Math.atan2(motionY, hyp) * 180.0D / Math.PI));

        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        this.ticksInGround = 0;
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
        this.setDeltaMovement(new Vec3(x, y, z));

        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {

            double hyp = Math.sqrt(x * x + z * z);
            float yaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
            float pitch = (float)(Math.atan2(y, hyp) * 180.0D / Math.PI);

            this.setYRot(yaw);
            this.setXRot(pitch);

            this.yRotO = yaw;
            this.xRotO = pitch;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.throwableShake > 0) {
            this.throwableShake--;
        }

        if (this.inGround) {

            if (this.level().getBlockState(this.stuckPos).equals(this.stuckBlockState)) {
                this.ticksInGround++;

                if (this.groundDespawn() > 0 && this.ticksInGround >= this.groundDespawn()) {
                    this.discard();
                }

                return;

            } else {
                this.inGround = false;
                Vec3 mov1 = getDeltaMovement();
                this.setDeltaMovement(
                        mov1.x * (random.nextFloat() * 0.2),
                        mov1.y * (random.nextFloat() * 0.2),
                        mov1.z * (random.nextFloat() * 0.2)
                );
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }

        } else {

            this.ticksInAir++;

            Vec3 start = this.position();
            Vec3 motion = this.getDeltaMovement().scale(motionMult());
            Vec3 end = start.add(motion);

            HitResult hit = null;

            if (!isSpectral()) hit = ProjectileUtil.getEntityHitResult(this, start, end, this.getBoundingBox().expandTowards(motion), e -> true, 0.3);

            if (hit == null && doesImpactEntities() && !this.level().isClientSide()) {

                EntityHitResult eHit = ProjectileUtil.getEntityHitResult(
                        this.level(),
                        this,
                        start,
                        end,
                        this.getBoundingBox().expandTowards(motion).inflate(0.3),
                        entity -> (entity.canBeCollidedWith() && (entity != thrower || this.ticksInAir >= this.selfDamageDelay()) && entity.isAlive())
                );

                if (eHit != null) {
                    if (this.doesPenetrate()) {
                        this.onImpact(eHit);
                    } else {
                        hit = eHit;
                    }
                }
            }

            if (hit != null) {
                if (hit.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult bHit = (BlockHitResult) hit;
                    if (this.level().getBlockState(bHit.getBlockPos()).is(Blocks.NETHER_PORTAL)) {
                        this.handlePortal();
                    } else {
                        this.onImpact(hit);
                    }
                } else {
                    this.onImpact(hit);
                }
            }

            if (this.onGround()) {
                Vec3 mov2 = this.getDeltaMovement();
                double hyp = Math.sqrt(mov2.x * mov2.x + mov2.z * mov2.z);

                for (this.setXRot((float) (Math.atan2(mov2.y, hyp) * 180.0D / Math.PI)); this.getXRot() - this.xRotO < -180.0F; this.xRotO -= 360.0F) ;

                while (this.getXRot() - this.xRotO >= 180.0F) this.xRotO += 360.0F;
                while (this.getYRot() - this.yRotO < -180.0F) this.yRotO -= 360.0F;
                while (this.getYRot() - this.yRotO >= 180.0F) this.yRotO += 360.0F;

                this.setXRot(this.xRotO + (this.getXRot() - this.xRotO) * 0.2F);
                this.setYRot(this.yRotO + (this.getYRot() - this.yRotO) * 0.2F);
            }
        }

        float drag = this.getAirDrag();
        double gravity = this.getGravityVelocity();

        Vec3 motion = this.getDeltaMovement();

        if (this.isInWater()) {
            for (int i = 0; i < 4; i++) {
                double f = 0.25;
                this.level().addParticle(ParticleTypes.BUBBLE, this.getX() - motion.x * f, this.getY() - motion.y * f, this.getZ() - motion.z * f, motion.x, motion.y, motion.z);
            }

            drag = this.getWaterDrag();
        }
        Vec3 dragMotion = this.getDeltaMovement()
                .multiply(drag, drag, drag)
                .add(0, -gravity, 0);
        this.setDeltaMovement(dragMotion);
    }

    public boolean doesImpactEntities() {
        return true;
    }

    public boolean doesPenetrate() {
        return false;
    }

    public boolean isSpectral() {
        return false;
    }

    public int selfDamageDelay() {
        return 5;
    }

    public void getStuck(BlockPos pos, @Nullable Direction side) {
        this.stuckPos = pos;
        this.stuckBlockState = Block.getId(this.level().getBlockState(pos));
        this.inGround = true;
        this.setDeltaMovement(0, 0, 0);
        this.setStuckIn(side);
        this.tickCount = 0;
    }

    public double getGravityVelocity() {
        return 0.03D;
    }

    protected abstract void onImpact(HitResult hitResult);

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        tag.put("StuckPos", NbtUtils.writeBlockPos(this.stuckPos));
        tag.putInt("StuckBlockState", this.stuckBlockState);
        tag.putByte("Shake", (byte) this.throwableShake);
        tag.putByte("InGround", (byte) (this.inGround ? 1 : 0));

        if ((this.throwerUUID == null || this.throwerUUID.isEmpty()) && this.thrower != null && this.thrower instanceof Player) {
            this.throwerUUID = this.thrower.getStringUUID();
        }

        tag.putString("OwnerName", this.throwerUUID == null ? "" : this.throwerUUID);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        NbtUtils.readBlockPos(tag, "StuckPos").ifPresent(pos -> this.stuckPos = pos);

        this.stuckBlockState = tag.getInt("StuckBlockState");
        this.throwableShake = tag.getByte("Shake");
        this.inGround = tag.getByte("InGround") == 1;
        this.throwerUUID = tag.getString("OwnerName");

        if (this.throwerUUID.isEmpty()) {
            this.throwerUUID = null;
        }
    }

    public void setThrower(LivingEntity thrower) {
        this.thrower = thrower;
    }

    public LivingEntity getThrower() {
        if (this.thrower == null && this.throwerUUID != null && !this.throwerUUID.isEmpty()) {
            this.thrower = this.level().getPlayerByUUID(UUID.fromString(this.throwerUUID));
        }
        return this.thrower;
    }

    protected float getAirDrag() {
        return 0.99F;
    }

    protected float getWaterDrag() {
        return 0.8F;
    }

    protected int groundDespawn() {
        return 1200;
    }
}