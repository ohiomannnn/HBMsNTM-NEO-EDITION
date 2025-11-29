package com.hbm.entity.missile;

import api.hbm.entity.IRadarDetectableNT;
import com.hbm.HBMsNTMClient;
import com.hbm.entity.projectile.ThrowableInterp;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.util.Vec3NT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public abstract class MissileBaseNT extends ThrowableInterp implements IRadarDetectableNT {

    private static final TicketType<UUID> CHUNK_TICKET = TicketType.create("chunkloading_missle", Comparator.comparing(UUID::toString));
    private ChunkPos loadedChunk;
    public int startX;
    public int startZ;
    public int targetX;
    public int targetZ;
    public double velocity;
    public double decelY;
    public double accelXZ;
    public boolean isCluster = false;
    public int health = 50;

    public MissileBaseNT(EntityType<?> entityType, Level level) {
        super(entityType, level);

        startX = (int) this.getX();
        startZ = (int) this.getZ();
        targetX = (int) this.getX();
        targetZ = (int) this.getZ();
    }

    public MissileBaseNT(EntityType<?> entityType, Level level, float x, float y, float z, int a, int b) {
        this(entityType, level);

        this.moveTo(x, y, z, 0, 0);
        startX = (int) this.getX();
        startZ = (int) this.getZ();
        targetX = a;
        targetZ = b;

        this.setDeltaMovement(this.getDeltaMovement().add(0, 2, 0));

        Vec3 vector = new Vec3(targetX - startX, 0, targetZ - startZ);
        accelXZ = decelY = 1 / vector.length();
        decelY *= 2;
        velocity = 0;

        this.setYRot((float) (Math.atan2(targetX - getX(), targetZ - getZ()) * 180.0D / Math.PI));
    }

    public abstract ItemStack getMissileItemForInfo();

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        super.tick();

        if (velocity < 4) velocity += Mth.clamp(this.tickCount / 60D * 0.05D, 0, 0.05);

        if (!level().isClientSide) {
            if (hasPropulsion()) {
                Vec3 motion = this.getDeltaMovement();

                motion = motion.add(0, -decelY * velocity, 0);

                Vec3 vector = new Vec3(targetX - startX, 0, targetZ - startZ).normalize();
                vector = new Vec3(vector.x * accelXZ, 0, vector.z * accelXZ);

                if (motion.y > 0) {
                    motion = motion.add(vector.x * velocity, 0, vector.z * velocity);
                }

                if (motion.y < 0) {
                    motion = motion.add(-vector.x * velocity, 0, -vector.z * velocity);
                }

                this.setDeltaMovement(motion);
            } else {
                Vec3 motion = this.getDeltaMovement();
                motion = new Vec3(motion.x * 0.99, motion.y, motion.z * 0.99);

                if (motion.y > -1.5) {
                    motion = motion.add(0, -0.05, 0);
                }

                this.setDeltaMovement(motion);
            }

            if (this.getDeltaMovement().y < -velocity && this.isCluster) {
                cluster();
                this.discard();
                return;
            }
            this.setYRot((float)(Math.atan2(targetX - this.getX(), targetZ - this.getZ()) * 180.0D / Math.PI));
            Vec3 motion = this.getDeltaMovement();
            float f2 = (float)Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            this.setXRot((float)(Math.atan2(motion.y, f2) * 180.0D / Math.PI) - 90.0F);
            while (this.getXRot() - this.xRotO < -180.0F) this.xRotO -= 360.0F;
        } else {
            this.spawnContrail();
        }

        while (this.getXRot() - this.xRotO >= 180.0F) this.xRotO += 360.0F;
        while (this.getYRot() - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while (this.getYRot() - this.yRotO >= 180.0F) this.yRotO += 360.0F;
    }

    public boolean hasPropulsion() {
        return true;
    }

    protected void spawnContrail() {
        this.spawnContraolWithOffset(0, 0, 0);
    }

    protected void spawnContraolWithOffset(double offsetX, double offsetY, double offsetZ) {
        Vec3 vec = new Vec3(this.xo - this.getX(), this.yo - this.getY(), this.zo - this.getZ());
        double len = vec.length();
        vec = vec.normalize();
        Vec3NT thrust = new Vec3NT(0, 1, 0);
        thrust.rotateAroundXRad(this.getXRot() * (float) Math.PI / 180F);
        thrust.rotateAroundYRad((this.getYRot() + 90) * (float) Math.PI / 180F);

        for(int i = 0; i < Math.max(Math.min(len, 10), 1); i++) {
            double j = i - len;
            CompoundTag tag = new CompoundTag();
            tag.putDouble("posX", getX() - vec.x * j + offsetX);
            tag.putDouble("posY", getY() - vec.y * j + offsetY);
            tag.putDouble("posZ", getZ() - vec.z * j + offsetZ);
            tag.putString("type", "missileContrail");
            tag.putFloat("scale", this.getContrailScale());
            tag.putDouble("moX", -thrust.xCoord);
            tag.putDouble("moY", -thrust.yCoord);
            tag.putDouble("moZ", -thrust.zCoord);
            tag.putInt("maxAge", 60 + random.nextInt(20));
            HBMsNTMClient.effectNT(tag);
        }
    }

    protected float getContrailScale() {
        return 1F;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected double motionMult() {
        return velocity;
    }

    @Override
    public boolean doesImpactEntities() {
        return false;
    }

    protected void killMissile() {
        if (this.isAlive()) {
            this.discard();
            if (this.level() instanceof ServerLevel serverLevel) {
                ExplosionLarge.explode(serverLevel, this.getX(), this.getY(), this.getZ(), 5, true, false, true);
                ExplosionLarge.spawnShrapnelShower(serverLevel, this.getX(), this.getY(), this.getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z, 15, 0.075);
                ExplosionLarge.spawnMissileDebris(serverLevel, this.getX(), this.getY(), this.getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z, 0.25, this.getDebris(), this.getDebrisRareDrop());
            }
        }
    }

    @Override
    protected void onImpact(HitResult hitResult) {
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            this.onMissileImpact(hitResult);
            this.discard();
        }
    }

    public abstract void onMissileImpact(HitResult hitResult);
    public abstract List<ItemStack> getDebris();
    public abstract ItemStack getDebrisRareDrop();
    public void cluster() { }

    @Override
    public double getGravityVelocity() {
        return 0.0D;
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
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!level().isClientSide && level() instanceof ServerLevel server) {
            this.loadedChunk = new ChunkPos(this.blockPosition());
            server.getChunkSource().addRegionTicket(
                    CHUNK_TICKET,
                    this.loadedChunk,
                    2,
                    this.getUUID()
            );
        }
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        if (!level().isClientSide && loadedChunk != null && level() instanceof ServerLevel server) {
            server.getChunkSource().removeRegionTicket(
                    CHUNK_TICKET,
                    this.loadedChunk,
                    2,
                    this.getUUID()
            );
            this.loadedChunk = null;
        }
    }

    public void explodeStandard(float strength, int resolution, boolean fire) {
        ExplosionVNT vnt = new ExplosionVNT(this.level(), this.getX(), this.getY(), this.getZ(), strength)
                .setBlockAllocator(new BlockAllocatorStandard(resolution))
                .setBlockProcessor(new BlockProcessorStandard().setNoDrop().withBlockEffect(fire ? new BlockMutatorFire() : null))
                .setEntityProcessor(new EntityProcessorCross(7.5D).withRangeMod(2))
                .setPlayerProcessor(new PlayerProcessorStandard());
        vnt.explode();
    }

    @Override
    public String getUnlocalizedName() {
        return "Unknown";
    }

    @Override
    public int getBlipLevel() {
        return IRadarDetectableNT.SPECIAL;
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
        return !params.smartMode || !(this.getDeltaMovement().y >= 0);
    }
}
