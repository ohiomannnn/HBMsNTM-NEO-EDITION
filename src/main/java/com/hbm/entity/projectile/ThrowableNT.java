package com.hbm.entity.projectile;

import com.hbm.entity.IProjectile;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class ThrowableNT extends Entity implements IProjectile {

    private static final EntityDataAccessor<Direction> STUCK_SIDE = SynchedEntityData.defineId(ThrowableNT.class, EntityDataSerializers.DIRECTION);
    public int throwableShake;
    protected boolean inGround;
    protected LivingEntity thrower;
    private int stuckBlockX = -1;
    private int stuckBlockY = -1;
    private int stuckBlockZ = -1;
    private Block stuckBlock;
    private UUID throwerName;
    private int ticksInGround;
    private int ticksInAir;

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
        double posY = 0.1D;
        double posZ = this.position.x - (double) (Mth.sin(this.yRot / 180.0F * (float) Math.PI) * 0.16F);
        this.setPos(posX, posY, posZ);
        float velocity = 0.4F;
        double motionX = -Mth.sin(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI) * velocity;
        double motionZ = Mth.cos(this.yRot / 180.0F * (float) Math.PI) * Mth.cos(this.xRot / 180.0F * (float) Math.PI) * velocity;
        double motionY = -Mth.sin((this.xRot + throwAngle()) / 180.0F * (float) Math.PI) * velocity;
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
        //Euclidean Distance ^^

        motionX /= throwLen;
        motionY /= throwLen;
        motionZ /= throwLen;

        motionX += random.nextGaussian() * headingForceMult() * (double) inaccuracy;
        motionY += random.nextGaussian() * headingForceMult() * (double) inaccuracy;
        motionZ += random.nextGaussian() * headingForceMult() * (double) inaccuracy;

        motionX *= velocity;
        motionY *= velocity;
        motionZ *= velocity;

        //Motion should be fine as a double

        this.deltaMovement = new Vec3(motionX, motionY, motionZ);
        float hyp = (float) Math.sqrt(motionX * motionX + motionZ * motionZ);
        this.yRotO = this.yRot = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
        this.xRotO = this.xRot = (float) (Math.atan2(motionY, hyp) * 180.0D / Math.PI);
        this.ticksInGround = 0;
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
            if (level().getBlockState(new BlockPos(stuckBlockX, stuckBlockY, stuckBlockZ)).getBlock() == stuckBlock) {
                ++ticksInGround;

                if (groundDespawn() > 0 && ticksInGround == groundDespawn()) {
                    this.discard();
                }

                return;
            }

            inGround = false;
//                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
//                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
//                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
            //Randomizing motion why?? Im assuming for unpredicadbility but no

            ticksInGround = 0;
            ticksInAir = 0;
        } else {


            ++ticksInAir;
            double mm = motionMult();
            Vec3 nextPos = new Vec3(this.position.x + this.deltaMovement.x * mm, this.position.y + this.deltaMovement.y * mm, this.position.z + this.deltaMovement.z * mm);

            HitResult hitResult = null;
            if (!isSpectral()) hitResult = Library.rayTraceBlocks(this.level(), this.position, nextPos, false, true, false);

            if (hitResult != null) {
                nextPos = new Vec3(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
            }

            if (!level().isClientSide && doesImpactEntities()) {
                Entity hitEntity = null;
                List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().contract(this.deltaMovement.x * mm, this.deltaMovement.y * mm, this.deltaMovement.z * mm).inflate(1.0D, 1.0D, 1.0D));
                double nearest = 0.0D;
                LivingEntity thrower = this.getThrower();
                HitResult nonPenImpact = null;

                for (Entity entity : entities) {

                    if (entity.canBeCollidedWith() && (entity != thrower || this.ticksInAir >= this.selfDamageDelay()) && entity.isAlive()) {
                        double hitbox = 0.3F;
                        AABB aabb = entity.getBoundingBox().inflate(hitbox, hitbox, hitbox);
                        HitResult hitRes = getHitResultOnViewVector(this, this.position, nextPos, Entity::isInvulnerable);
                    }
                }
            }
        }
    }

    public static HitResult getHitResultOnViewVector(Entity projectile, Vec3 pos, Vec3 nextPos, Predicate<Entity> filter) {
        return getHitResult(pos, projectile, filter, nextPos, projectile.level(), 0.0F, ClipContext.Block.COLLIDER);
    }

    private static HitResult getHitResult(Vec3 pos, Entity projectile, Predicate<Entity> filter, Vec3 deltaMovement, Level level, float margin, ClipContext.Block clipContext) {
        Vec3 vec3 = pos.add(deltaMovement);
        HitResult hitresult = level.clip(new ClipContext(pos, vec3, clipContext, ClipContext.Fluid.NONE, projectile));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec3 = hitresult.getLocation();
        }

        HitResult hitresult1 = ProjectileUtil.getEntityHitResult(level, projectile, pos, vec3, projectile.getBoundingBox().expandTowards(deltaMovement).inflate((double)1.0F), filter, margin);
        if (hitresult1 != null) {
            hitresult = hitresult1;
        }

        return hitresult;
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

    protected abstract void onImpact(BlockHitResult result);

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    public LivingEntity getThrower() {
        if (thrower == null && throwerName != null && throwerName.toString().length() > 0) {
            thrower = level().getPlayerByUUID(throwerName);
        }
        return thrower;
    }

    public void setThrower(LivingEntity thrower) {
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
