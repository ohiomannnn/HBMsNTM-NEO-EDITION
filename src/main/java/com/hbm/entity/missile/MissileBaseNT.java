package com.hbm.entity.missile;

import api.hbm.entity.IRadarDetectableNT;
import com.hbm.HBMsNTMClient;
import com.hbm.entity.projectile.ThrowableInterp;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockMutatorFire;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCross;
import com.hbm.items.weapon.MissileItem;
import com.hbm.util.RayTraceResult;
import com.hbm.util.Vec3NT;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public abstract class MissileBaseNT extends ThrowableInterp implements IRadarDetectableNT {

    private static final TicketType<UUID> CHUNK_TICKET = TicketType.create("chunkloading_missle", Comparator.comparing(UUID::toString), 0);
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

    public static final EntityDataAccessor<Direction> ROT = SynchedEntityData.defineId(MissileBaseNT.class, EntityDataSerializers.DIRECTION);

    public MissileBaseNT(EntityType<? extends MissileBaseNT> entityType, Level level) {
        super(entityType, level);

        startX = (int) this.getX();
        startZ = (int) this.getZ();
        targetX = (int) this.getX();
        targetZ = (int) this.getZ();
    }

    public MissileBaseNT(EntityType<? extends MissileBaseNT> entityType, Level level, double x, double y, double z, int a, int b) {
        this(entityType, level);

        this.moveTo(x, y, z, 0, 0);
        startX = (int) this.getX();
        startZ = (int) this.getZ();
        targetX = a;
        targetZ = b;

        this.deltaMovement = new Vec3(this.deltaMovement.x, 2 ,this.deltaMovement.z);

        Vec3 vector = new Vec3(targetX - startX, 0, targetZ - startZ);
        accelXZ = decelY = 1 / vector.length();
        decelY *= 2;
        velocity = 0;

        this.yRot = (float) (Math.atan2(targetX - this.position.x, targetZ - this.position.z) * 180.0D / Math.PI);
    }

    /** Auto-generates radar blip level and all that from the item */
    public abstract ItemStack getMissileItemForInfo();

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
        return !params.smartMode || !(this.deltaMovement.y >= 0);
    }

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        super.tick();

        if (velocity < 4) velocity += Mth.clamp(this.tickCount / 60D * 0.05D, 0, 0.05);

        if (!level().isClientSide) {
            if (hasPropulsion()) {
                this.deltaMovement = new Vec3(this.deltaMovement.x, this.deltaMovement.y - decelY * velocity, this.deltaMovement.z);

                Vec3 vector = new Vec3(targetX - startX, 0, targetZ - startZ);
                vector = vector.normalize();
                vector = new Vec3(vector.x * accelXZ, vector.y, vector.z * accelXZ);

                if (this.deltaMovement.y > 0) {
                    this.deltaMovement = new Vec3(this.deltaMovement.x + vector.x * velocity, this.deltaMovement.y, this.deltaMovement.z);
                    this.deltaMovement = new Vec3(this.deltaMovement.x, this.deltaMovement.y, this.deltaMovement.z + vector.z * velocity);
                }

                if (this.deltaMovement.y < 0) {
                    this.deltaMovement = new Vec3(this.deltaMovement.x - vector.x * velocity, this.deltaMovement.y, this.deltaMovement.z);
                    this.deltaMovement = new Vec3(this.deltaMovement.x, this.deltaMovement.y, this.deltaMovement.z - vector.z * velocity);
                }
            } else {
                this.deltaMovement = new Vec3(this.deltaMovement.x * 0.99, this.deltaMovement.y, this.deltaMovement.z * 0.99);

                if (this.deltaMovement.y > -1.5) {
                    this.deltaMovement = new Vec3(this.deltaMovement.x, this.deltaMovement.y - 0.05, this.deltaMovement.z);
                }
            }

            if (this.deltaMovement.y < -velocity && this.isCluster) {
                cluster();
                this.discard();
                return;
            }

            this.yRot = (float) (Math.atan2(targetX - this.position.x, targetZ - this.position.z) * 180.0D / Math.PI);
            float f2 = (float) Math.sqrt(this.deltaMovement.x * this.deltaMovement.x + this.deltaMovement.z * this.deltaMovement.z);
            for (this.xRot = (float) (Math.atan2(this.deltaMovement.y, f2) * 180.0D / Math.PI) - 90; this.xRot - this.xRotO < -180.0F; this.xRotO -= 360.0F);

            this.updateChunkTicket();
        } else {
            this.spawnContrail();
        }

        while (this.xRot - this.xRotO >= 180.0F) this.xRotO += 360.0F;
        while (this.yRot - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while (this.yRot - this.yRotO >= 180.0F) this.yRotO += 360.0F;
    }

    public boolean hasPropulsion() {
        return true;
    }

    protected void spawnContrail() {
        this.spawnContraolWithOffset(0, 0, 0);
    }

    protected void spawnContraolWithOffset(double offsetX, double offsetY, double offsetZ) {
        Vec3 vec = new Vec3(this.xo - this.position.x, this.yo - this.position.y, this.zo - this.position.z);
        double len = vec.length();
        vec = vec.normalize();
        Vec3NT thrust = new Vec3NT(0, 1, 0);
        thrust.rotateAroundZRad(this.xRot * (float) Math.PI / 180F);
        thrust.rotateAroundYRad((this.yRot + 90) * (float) Math.PI / 180F);

        for (int i = 0; i < Math.max(Math.min(len, 10), 1); i++) {
            double j = i - len;
            CompoundTag tag = new CompoundTag();
            tag.putDouble("posX", this.position.x - vec.x * j + offsetX);
            tag.putDouble("posY", this.position.y - vec.y * j + offsetY);
            tag.putDouble("posZ", this.position.z - vec.z * j + offsetZ);
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

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.setDeltaMovement(tag.getDouble("moX"), tag.getDouble("moY"), tag.getDouble("moZ"));
        this.setPos(tag.getDouble("posX"), tag.getDouble("posY"), tag.getDouble("posZ"));

        this.decelY = tag.getDouble("decel");
        this.accelXZ = tag.getDouble("accel");
        this.targetX = tag.getInt("tX");
        this.targetZ = tag.getInt("tZ");
        this.startX = tag.getInt("sX");
        this.startZ = tag.getInt("sZ");
        this.velocity = tag.getDouble("velocity");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putDouble("moX", this.deltaMovement.x);
        tag.putDouble("moY", this.deltaMovement.y);
        tag.putDouble("moZ", this.deltaMovement.z);
        tag.putDouble("poX", this.position.x);
        tag.putDouble("poY", this.position.y);
        tag.putDouble("poZ", this.position.z);
        tag.putDouble("decel", this.decelY);
        tag.putDouble("accel", this.accelXZ);
        tag.putInt("tX", this.targetX);
        tag.putInt("tZ", this.targetZ);
        tag.putInt("sX", this.startX);
        tag.putInt("sZ", this.startZ);
        tag.putDouble("veloc", this.velocity);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerable()) return false;
        if (this.health > 0 && !this.level().isClientSide) {
            this.health -= amount;
            if (this.health <= 0) this.killMissile();
        }
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
                ExplosionLarge.spawnShrapnelShower(serverLevel, this.getX(), this.getY(), this.getZ(), this.deltaMovement.x, this.deltaMovement.y, this.deltaMovement.z, 15, 0.075);
                ExplosionLarge.spawnMissileDebris(serverLevel, this.getX(), this.getY(), this.getZ(), this.deltaMovement.x, this.deltaMovement.y, this.deltaMovement.z, 0.25, this.getDebris(), this.getDebrisRareDrop());
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            this.discard();
            this.onMissileImpact(result);
        }
    }

    public abstract void onMissileImpact(RayTraceResult result);
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ROT, Direction.NORTH);
    }

    protected void updateChunkTicket() {
        if (!level().isClientSide && level() instanceof ServerLevel server) {
            ChunkPos newPos = new ChunkPos(this.blockPosition());
            if (!newPos.equals(this.loadedChunk)) {
                if (this.loadedChunk != null) {
                    server.getChunkSource().removeRegionTicket(CHUNK_TICKET, this.loadedChunk, 3, this.getUUID());
                }
                this.loadedChunk = newPos;
                server.getChunkSource().addRegionTicket(CHUNK_TICKET, this.loadedChunk, 3, this.getUUID());
            }
        }
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!level().isClientSide && level() instanceof ServerLevel server) {
            this.loadedChunk = new ChunkPos(this.blockPosition());
            server.getChunkSource().addRegionTicket(CHUNK_TICKET, this.loadedChunk, 3, this.getUUID());
        }
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        if (!level().isClientSide && loadedChunk != null && level() instanceof ServerLevel server) {
            server.getChunkSource().removeRegionTicket(CHUNK_TICKET, this.loadedChunk, 3, this.getUUID());
            this.loadedChunk = null;
        }
    }

    public void explodeStandard(float strength, int resolution, boolean fire) {
        ExplosionVNT vnt = new ExplosionVNT(this.level(), this.getX(), this.getY(), this.getZ(), strength)
                .setBlockAllocator(new BlockAllocatorStandard(resolution))
                .setBlockProcessor(new BlockProcessorStandard().setNoDrop().withBlockEffect(fire ? new BlockMutatorFire() : null))
                .setEntityProcessor(new EntityProcessorCross(7.5D).withRangeMod(2));
        vnt.explode();
    }

    @Override
    public String getUnlocalizedName() {
        ItemStack item = this.getMissileItemForInfo();
        if (item.getItem() instanceof MissileItem missileItem) {
            return switch (missileItem.tier) {
                case TIER0 -> "radar.target.tier0";
                case TIER1 -> "radar.target.tier1";
                case TIER2 -> "radar.target.tier2";
                case TIER3 -> "radar.target.tier3";
                case TIER4 -> "radar.target.tier4";
            };
        }

        return "Unknown";
    }

    @Override
    public int getBlipLevel() {
        ItemStack item = this.getMissileItemForInfo();
        if (item.getItem() instanceof MissileItem missileItem) {
            return switch (missileItem.tier) {
                case TIER0 -> IRadarDetectableNT.TIER0;
                case TIER1 -> IRadarDetectableNT.TIER1;
                case TIER2 -> IRadarDetectableNT.TIER2;
                case TIER3 -> IRadarDetectableNT.TIER3;
                case TIER4 -> IRadarDetectableNT.TIER4;
            };
        }

        return IRadarDetectableNT.SPECIAL;
    }
}
