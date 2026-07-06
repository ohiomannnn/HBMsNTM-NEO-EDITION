package com.hbm.entity.missile;

import api.hbm.entity.IRadarDetectableNT;
import com.hbm.entity.projectile.ThrowableInterp;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.particle.NtmParticles;
import com.hbm.util.BobMathUtil;
import com.hbm.util.RayTraceResult;
import com.hbm.util.particle.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MissileAntiBallistic extends ThrowableInterp implements IRadarDetectableNT {

    public static final double BASE_SPEED = 1.5;

    public Entity tracking;
    public double velocity;
    protected int activationTimer;

    public MissileAntiBallistic(EntityType<? extends MissileAntiBallistic> entityType, Level level) {
        super(entityType, level);

        this.setDeltaMovement(new Vec3(0, BASE_SPEED, 0));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);

        this.xRotO = this.xRot;
        this.yRotO = this.yRot;
    }

    @Override
    protected double motionMult() {
        return velocity;
    }

    @Override
    public boolean doesImpactEntities() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level.isClientSide) {
            if(velocity < 6) velocity += 0.1;

            if(activationTimer < 40) {
                activationTimer++;
                this.setDeltaMovement(new Vec3(0, BASE_SPEED, 0));
            } else {
                Entity prevTracking = this.tracking;

                if(this.tracking == null || this.tracking.isRemoved()) this.targetMissile();

                if(prevTracking == null && this.tracking != null) {
                    if(this.level instanceof ServerLevel serverLevel) {
                        ExplosionLarge.spawnShock(serverLevel, this.position().x, this.position().y, this.position().z, 24, 3F);
                    }
                }
                if(this.tracking != null && this.tracking.isAlive()) {
                    this.aimAtTarget();
                } else {
                    if(this.tickCount > 600) this.discard();
                }
            }

            if(this.position().y > 2000 && (this.tracking == null || this.tracking.isRemoved())) this.discard();

        } else {
            Vec3 motion = this.getDeltaMovement().normalize();
            double x = this.position().x - motion.x;
            double y = this.position().y - motion.y;
            double z = this.position().z - motion.z;
            ParticleUtil.addParticle(this.level, NtmParticles.ABM_CONTRAIL.get(), x, y, z);
        }

        float f2 = BobMathUtil.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        this.yRot = (float) (Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * 180.0D / Math.PI);
        for(
                this.xRot = (float) (Math.atan2(this.getDeltaMovement().y, f2) * 180.0D / Math.PI) - 90;
                this.xRot - this.xRotO < -180.0F;
                this.xRotO -= 360.0F
        );
        while(this.xRot - this.xRotO >= 180.0F) this.xRotO += 360.0F;
        while(this.yRot - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while(this.yRot - this.yRotO >= 180.0F) this.yRotO += 360.0F;
    }

    // todo make TileEntityMachineRadarNT
    public static final List<Class<?>> classes = new ArrayList<>();
    public static final List<Entity> matchingEntities = new ArrayList<>();

    /**
     * Iterates over every entity in the world and add them to the matchingEntities list if the class is in the detectable list
     * From this compiled list, radars can easily grab the required entities since we can assume that the total amount of detectable entities is comparatively low
     */
    public static void updateSystem(MinecraftServer server) {
        matchingEntities.clear();

        for(ServerLevel serverLevel : server.getAllLevels()) {
            for(Entity entity : serverLevel.getEntities().getAll()) {
                for(Class<?> clazz : classes) {
                    if(clazz.isAssignableFrom(entity.getClass())) {
                        matchingEntities.add(entity);
                        break;
                    }
                }
            }
        }
    }

    /** Registers a class that if an entity inherits that class, it is picked up by the system */
    public static void registerEntityClasses() {
        classes.add(IRadarDetectableNT.class);
        classes.add(Player.class);
    }

    /** Detects and caches nearby EntityMissileBaseNT */
    protected void targetMissile() {

        Entity closest = null;
        double dist = 1_000;

        for(Entity e : matchingEntities) {
            if(e.level.dimension() != this.level.dimension()) continue;
            if(!(e instanceof MissileBaseNT)) continue; //can only lock onto missiles
            if(e instanceof MissileStealth) continue; //cannot lack onto missiles with stealth coating

            Vec3 delta = this.position().vectorTo(e.position());

            if(delta.length() < dist) closest = e;
        }

        this.tracking = closest;
    }

    /** Predictive targeting system */
    protected void aimAtTarget() {

        Vec3 delta = this.position().vectorTo(tracking.position());
        double intercept = delta.length() / (BASE_SPEED * this.velocity);
        Vec3 predicted = new Vec3(tracking.position().x + (tracking.position().x - tracking.xo) * intercept, tracking.position().y + (tracking.position().y - tracking.yo) * intercept, tracking.position().z + (tracking.position().z - tracking.zo) * intercept);
        Vec3 motion = this.position().vectorTo(predicted).normalize();

        if(delta.length() < 10 && activationTimer >= 40) {
            this.discard();
            if(this.level instanceof ServerLevel serverLevel) {
                ExplosionLarge.explode(serverLevel, this.position().x, this.position().y, this.position().z, 15, true, false, false);
            }
        }

        this.setDeltaMovement(motion.multiply(BASE_SPEED, BASE_SPEED, BASE_SPEED));
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(this.activationTimer >= 40) {
            this.discard();
            if(this.level instanceof ServerLevel serverLevel) {
                ExplosionLarge.explode(serverLevel, this.position().x, this.position().y, this.position().z, 20, true, false, false);
            }
        }
    }

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
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.velocity = tag.getDouble("Velocity");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putDouble("Velocity", this.velocity);
    }

    @Override public boolean shouldRenderAtSqrDistance(double distance) { return true; }

    @Override
    public String getUnlocalizedName() {
        return "radar.target.abm";
    }

    @Override
    public int getBlipLevel() {
        return IRadarDetectableNT.TIER_AB;
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
        return false;
    }
}
