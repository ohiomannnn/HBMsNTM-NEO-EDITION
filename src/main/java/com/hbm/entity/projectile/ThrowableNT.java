package com.hbm.entity.projectile;

import com.hbm.entity.IProjectile;
import com.hbm.lib.Library;
import com.hbm.util.AABBUtils;
import com.hbm.util.RayTraceResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class ThrowableNT extends Entity implements IProjectile {

    private static final EntityDataAccessor<Direction> STUCK_SIDE = SynchedEntityData.defineId(ThrowableNT.class, EntityDataSerializers.DIRECTION);
    public int throwableShake;
    protected boolean inGround;
    @Nullable private UUID throwerUUID;
    @Nullable protected Entity thrower;
    private int stuckBlockX = -1;
    private int stuckBlockY = -1;
    private int stuckBlockZ = -1;
    private BlockState stuckBlock;
    public int ticksInGround;
    protected int ticksInAir;

    public ThrowableNT(EntityType<? extends ThrowableNT> entityType, Level level) {
        super(entityType, level);
    }

    public ThrowableNT(EntityType<? extends ThrowableNT> entityType, Level level, double x, double y, double z) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    public ThrowableNT(EntityType<? extends ThrowableNT> entityType, Level level, LivingEntity thrower) {
        this(entityType, level);
        this.thrower = thrower;
        this.moveTo(thrower.position.x, thrower.getEyeY(), thrower.position.z, thrower.yRot, thrower.xRot);
        double posX = this.position.x - (double) (Mth.cos(this.yRot / 180.0F * (float) Math.PI) * 0.16F);
        double posY = this.position.y - 0.1D;
        double posZ = this.position.z - (double)(Mth.sin(this.yRot / 180.0F * (float)Math.PI) * 0.16F);
        this.setPos(posX, posY, posZ);
        float velocity = 0.4F;
        double motionX = -Mth.sin(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI) * velocity;
        double motionY = -Mth.sin((this.xRot + throwAngle()) / 180.0F * (float) Math.PI) * velocity;
        double motionZ = Mth.cos(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI) * velocity;
        this.setThrowableHeading(motionX, motionY, motionZ, throwForce(), 1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(STUCK_SIDE, Direction.NORTH);
    }

    public Direction getStuckIn() {
        return this.getEntityData().get(STUCK_SIDE);
    }

    public void setStuckIn(Direction direction) {
        this.getEntityData().set(STUCK_SIDE, direction);
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
    public void setThrowableHeading(double motionX, double motionY, double motionZ, float velocity, float inaccuracy) {
        float throwLen = (float) Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= throwLen;
        motionY /= throwLen;
        motionZ /= throwLen;
        motionX += random.nextGaussian() * headingForceMult() * (double) inaccuracy;
        motionY += random.nextGaussian() * headingForceMult() * (double) inaccuracy;
        motionZ += random.nextGaussian() * headingForceMult() * (double) inaccuracy;
        motionX *= velocity;
        motionY *= velocity;
        motionZ *= velocity;
        this.deltaMovement = new Vec3(motionX, motionY, motionZ);
        float hyp = (float) Math.sqrt(motionX * motionX + motionZ * motionZ);
        this.yRotO = this.yRot = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
        this.xRotO = this.xRot = (float) (Math.atan2(motionY, hyp) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(size)) size = 4.0F;
        size *= 64.0F;
        return distance < size * size;
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        this.setDeltaMovement(x, y, z);

        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double hyp = Math.sqrt(x * x + z * z);
            this.yRot = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
            this.xRot = (float) (Math.atan2(y, hyp) * 180.0D / Math.PI);
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (throwableShake > 0) {
            --throwableShake;
        }

        if (inGround) {
            if (level.getBlockState(new BlockPos(stuckBlockX, stuckBlockY, stuckBlockZ)) == stuckBlock) {
                ++ticksInGround;

                if (groundDespawn() > 0 && ticksInGround == groundDespawn()) {
                    this.discard();
                }

            } else {

                inGround = false;
                ticksInGround = 0;
                ticksInAir = 0;
            }
        } else {
            ticksInAir++;

            Vec3 pos = this.position;
            Vec3 nextPos = new Vec3(pos.x + this.deltaMovement.x * motionMult(), pos.y + this.deltaMovement.y * motionMult(), pos.z + this.deltaMovement.z * motionMult());
            RayTraceResult mop = null;
            if (!isSpectral()) mop = Library.rayTraceBlocks(this.level, pos, nextPos, false, true, false);
            pos = this.position;
            nextPos = new Vec3(pos.x + this.deltaMovement.x * motionMult(), pos.y + this.deltaMovement.y * motionMult(), pos.z + this.deltaMovement.z * motionMult());

            if (mop != null) {
                nextPos = new Vec3(mop.hitVec.x, mop.hitVec.y, mop.hitVec.z);
            }

            if (!this.level.isClientSide && doesImpactEntities()) {
                Entity hitEntity = null;
                List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().contract(this.deltaMovement.x * motionMult(), this.deltaMovement.y * motionMult(), this.deltaMovement.z * motionMult()).inflate(1.0D, 1.0D, 1.0D));
                double nearest = 0.0D;
                Entity thrower = this.getThrower();
                RayTraceResult nonPenImpact = null;

                for (Entity entity : entities) {

                    if ((entity != thrower || this.ticksInAir >= this.selfDamageDelay()) && entity.isAlive()) {
                        double hitbox = 0.3F;
                        AABB aabb = entity.getBoundingBox().inflate(hitbox, hitbox, hitbox);
                        RayTraceResult hitMop = AABBUtils.calculateIntercept(aabb, pos, nextPos);

                        if (hitMop != null) {

                            // if penetration is enabled, run impact for all intersecting entities
                            if (this.doesPenetrate()) {
                                this.onImpact(new RayTraceResult(entity, hitMop.hitVec));
                            } else {

                                double dist = pos.distanceTo(hitMop.hitVec);

                                if (dist < nearest || nearest == 0.0D) {
                                    hitEntity = entity;
                                    nearest = dist;
                                    nonPenImpact = hitMop;
                                }
                            }
                        }
                    }
                }

                // if not, only run it for the closest MOP
                if (!this.doesPenetrate() && hitEntity != null) {
                    mop = new RayTraceResult(hitEntity, nonPenImpact.hitVec);
                }
            }

            if (mop != null) {
                if(mop.typeOfHit == RayTraceResult.Type.BLOCK && this.level.getBlockState(mop.getBlockPos()).is(Blocks.NETHER_PORTAL)) {
                    this.handlePortal();
                } else {
                    this.onImpact(mop);
                }
            }

            if (!this.onGround()) {
                float hyp = (float) Math.sqrt(this.deltaMovement.x * this.deltaMovement.x + this.deltaMovement.z * this.deltaMovement.z);
                this.yRot = (float) (Math.atan2(this.deltaMovement.x, this.deltaMovement.z) * 180.0D / Math.PI);

                for (this.xRot = (float) (Math.atan2(this.deltaMovement.y, hyp) * 180.0D / Math.PI); this.xRot - this.xRotO < -180.0F; this.xRotO -= 360.0F);

                while (this.xRot - this.xRotO >= 180.0F) this.xRotO += 360.0F;
                while (this.yRot - this.yRotO < -180.0F) this.yRotO -= 360.0F;
                while (this.yRot - this.yRotO >= 180.0F) this.yRotO += 360.0F;

                this.xRot = this.xRotO + (this.xRot - this.xRotO) * 0.2F;
                this.yRot = this.yRotO + (this.yRot - this.yRotO) * 0.2F;
            }

            float drag = this.getAirDrag();
            double gravity = this.getGravityVelocity();

            this.position = new Vec3(this.position.x + this.deltaMovement.x * motionMult(), this.position.y + this.deltaMovement.y * motionMult(), this.position.z + this.deltaMovement.z * motionMult());

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, this.position.x - this.deltaMovement.x, this.position.y - this.deltaMovement.y, this.position.z - this.deltaMovement.z, this.deltaMovement.x, this.deltaMovement.y, this.deltaMovement.z);
                }

                drag = this.getWaterDrag();
            }

            this.deltaMovement = new Vec3(this.deltaMovement.x * (double) drag, this.deltaMovement.y * (double) drag, this.deltaMovement.z * (double) drag);
            this.deltaMovement = new Vec3(this.deltaMovement.x, this.deltaMovement.y - gravity, this.deltaMovement.z);

            this.setPos(this.position);
        }
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

    public void getStuck(BlockPos pos, Direction side) {
        this.stuckBlockX = pos.getX();
        this.stuckBlockY = pos.getY();
        this.stuckBlockZ = pos.getZ();
        this.stuckBlock = level.getBlockState(pos);
        this.inGround = true;
        this.deltaMovement = new Vec3(0, 0, 0);
        this.setStuckIn(side);
    }

    public double getGravityVelocity() {
        return 0.03D;
    }

    protected abstract void onImpact(RayTraceResult result);

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        tag.putInt("StuckX", this.stuckBlockX);
        tag.putInt("StuckY", this.stuckBlockY);
        tag.putInt("StuckZ", this.stuckBlockZ);
        tag.putInt("InBlock", Block.getId(this.stuckBlock));
        tag.putByte("Shake", (byte) this.throwableShake);
        tag.putBoolean("InGround", this.inGround);

        if (this.throwerUUID != null) {
            tag.putUUID("Owner", this.throwerUUID);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        this.stuckBlockX = tag.getInt("StuckX");
        this.stuckBlockY = tag.getInt("StuckY");
        this.stuckBlockZ = tag.getInt("StuckZ");
        this.stuckBlock = Block.stateById(tag.getInt("InBlock"));
        this.throwableShake = tag.getByte("Shake");
        this.inGround = tag.getBoolean("InGround");

        if (tag.hasUUID("Owner")) {
            this.throwerUUID = tag.getUUID("Owner");
            this.thrower = null;
        }
    }

    public Entity getThrower() {
        if (this.thrower != null && !this.thrower.isRemoved()) {
            return this.thrower;
        } else {
            if (this.throwerUUID != null) {
                if (this.level instanceof ServerLevel serverLevel) {
                    this.thrower = serverLevel.getEntity(this.throwerUUID);
                    return this.thrower;
                }
            }

            return null;
        }
    }

    public void setThrower(Entity thrower) {
        this.throwerUUID = thrower.getUUID();
        this.thrower = thrower;
    }

    /* ================================== Additional Getters =====================================*/

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
