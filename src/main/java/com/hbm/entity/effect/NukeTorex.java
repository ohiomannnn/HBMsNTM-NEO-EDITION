package com.hbm.entity.effect;

import com.hbm.entity.ModEntityTypes;
import com.hbm.render.entity.effect.RenderTorex;
import com.hbm.util.BobMathUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Toroidial Convection Simulation Explosion Effect
 * Tor                             Ex
 */
public class NukeTorex extends Entity {

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(NukeTorex.class, EntityDataSerializers.FLOAT);
    // balefire or not
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(NukeTorex.class, EntityDataSerializers.INT);

    public double coreHeight = 3;
    public double convectionHeight = 3;
    public double torusWidth = 3;
    public double rollerSize = 1;
    public double heat = 1;
    public double lastSpawnY = -1;
    public final List<Cloudlet> cloudlets = new ArrayList<>();

    public boolean didPlaySound = false;
    public boolean didShake = false;

    public NukeTorex(EntityType<? extends NukeTorex> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setInvulnerable(true);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SCALE, 1.0f);
        builder.define(TYPE, 0);
    }

    @Override
    public void tick() {
        super.tick();

        double s = 1.5;
        double cs = 1.5;
        int maxAge = this.getMaxAge();

        if (level().isClientSide) {

            if (this.tickCount == 1) setScale((float) s);

            if (lastSpawnY == -1) {
                lastSpawnY = getY() - 3;
            }

            if (tickCount < 100) this.level().setSkyFlashTime(3);

            int spawnTarget = level().getHeight(Heightmap.Types.WORLD_SURFACE, (int) getX(), (int) getZ()) - 3;
            double moveSpeed = 0.5D;

            if (Math.abs(spawnTarget - lastSpawnY) < moveSpeed) {
                lastSpawnY = spawnTarget;
            } else {
                lastSpawnY += moveSpeed * Math.signum(spawnTarget - lastSpawnY);
            }

            // spawn mush clouds
            double range = (torusWidth - rollerSize) * 0.25;
            double simSpeed = getSimulationSpeed();
            int toSpawn = (int) Math.ceil(10 * simSpeed * simSpeed);
            int lifetime = Math.min((tickCount * tickCount) + 200, maxAge - tickCount + 200);

            for (int i = 0; i < toSpawn; i++) {
                double x = getX() + random.nextGaussian() * range;
                double z = getZ() + random.nextGaussian() * range;
                Cloudlet cloud = new Cloudlet(x, lastSpawnY, z, (float)(random.nextDouble() * 2D * Math.PI), 0, lifetime);
                cloud.setScale(1F + this.tickCount * 0.005F * (float) cs, 5F * (float) cs);
                cloudlets.add(cloud);
            }

            // spawn shock clouds
            if (tickCount < 150) {
                int cloudCount = tickCount * 5;
                int shockLife = Math.max(300 - tickCount * 20, 50);

                for (int i = 0; i < cloudCount; i++) {
                    float rot = (float) (Math.PI * 2 * random.nextDouble());
                    Vec3 vec = new Vec3((tickCount * 1.5 + random.nextDouble()) * 1.5, 0, 0)
                            .yRot(rot);
                    this.cloudlets.add(new Cloudlet(vec.x + getX(), level().getHeight(Heightmap.Types.WORLD_SURFACE, (int) (vec.x + getX()), (int) (vec.z + getZ())), vec.z + getZ(), rot, 0, shockLife, TorexType.SHOCK)
                            .setScale(7F, 2F)
                            .setMotion(tickCount > 15 ? 0.75 : 0));
                }

                if (!didPlaySound) {
                    RenderTorex.handleSound(this, tickCount);
                }
            }

            // spawn ring clouds
            if (tickCount < 130 * s) {
                lifetime *= s;
                for (int i = 0; i < 2; i++) {
                    Cloudlet cloud = new Cloudlet(getX(), getY() + coreHeight, getZ(), (float)(random.nextDouble() * 2D * Math.PI), 0, lifetime, TorexType.RING);
                    cloud.setScale(1F + this.tickCount * 0.0025F * (float) (cs * cs), 3F * (float) (cs * cs));
                    cloudlets.add(cloud);
                }
            }

            // spawn condensation clouds
            if (tickCount > 130 * s && tickCount < 600 * s) {

                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 4; j++) {
                        float angle = (float) (Math.PI * 2 * random.nextDouble());
                        Vec3 vec = new Vec3(torusWidth + rollerSize * (5 + random.nextDouble()), 0, 0)
                                .zRot((float) (Math.PI / 45 * j))
                                .yRot(angle);
                        Cloudlet cloud = new Cloudlet(getX() + vec.x, getY() + coreHeight - 5 + j * s, getZ() + vec.z, angle, 0, (int) ((20 + tickCount / 10) * (1 + random.nextDouble() * 0.1)), TorexType.CONDENSATION);
                        cloud.setScale(0.125F * (float) (cs), 3F * (float) (cs));
                        cloudlets.add(cloud);
                    }
                }
            }

            if (tickCount > 200 * s && tickCount < 600 * s) {

                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 4; j++) {
                        float angle = (float) (Math.PI * 2 * random.nextDouble());
                        Vec3 vec = new Vec3(torusWidth + rollerSize * (3 + random.nextDouble() * 0.5), 0, 0)
                                .zRot((float) (Math.PI / 45 * j))
                                .yRot(angle);
                        Cloudlet cloud = new Cloudlet(getX() + vec.x, getY() + coreHeight + 25 + j * cs, getZ() + vec.z, angle, 0, (int) ((20 + tickCount / 10) * (1 + random.nextDouble() * 0.1)), TorexType.CONDENSATION);
                        cloud.setScale(0.125F * (float) (cs), 3F * (float) (cs));
                        cloudlets.add(cloud);
                    }
                }
            }

            for (Cloudlet cloud : cloudlets) {
                cloud.update();
            }

            coreHeight += 0.15 / s;
            torusWidth += 0.05 / s;
            rollerSize = torusWidth * 0.35;
            convectionHeight = coreHeight + rollerSize;

            int maxHeat = (int) (50 * cs);
            heat = maxHeat - Math.pow((double) (maxHeat * this.tickCount) / maxAge, 1);

            cloudlets.removeIf(x -> x.isDead);
        }

        if (this.tickCount > maxAge) this.discard();
    }

    public NukeTorex setScale(float scale) {
        if (!level().isClientSide) this.entityData.set(SCALE, scale);
        this.coreHeight = this.coreHeight / 1.5D * scale;
        this.convectionHeight = this.convectionHeight / 1.5D * scale;
        this.torusWidth = this.torusWidth / 1.5D * scale;
        this.rollerSize = this.rollerSize / 1.5D * scale;
        return this;
    }

    public NukeTorex setType(int type) {
        this.entityData.set(TYPE, type);
        return this;
    }

    public double getSimulationSpeed() {

        int lifetime = getMaxAge();
        int simSlow = lifetime / 4;
        int simStop = lifetime / 2;
        int life = NukeTorex.this.tickCount;

        if (life > simStop) {
            return 0D;
        }

        if (life > simSlow) {
            return 1D - ((double)(life - simSlow) / (double)(simStop - simSlow));
        }

        return 1.0D;
    }

    public double getScale() {
        return this.entityData.get(SCALE);
    }

    public double getGreying() {
        int lifetime = getMaxAge();
        int greying = lifetime * 3 / 4;

        if (this.tickCount > greying) {
            return 1 + ((double) (this.tickCount - greying) / (double) (lifetime - greying));
        }

        return 1D;
    }

    public float getAlpha() {

        int lifetime = getMaxAge();
        int fadeOut = lifetime * 3 / 4;
        int life = NukeTorex.this.tickCount;

        if (life > fadeOut) {
            float fac = (float)(life - fadeOut) / (float)(lifetime - fadeOut);
            return 1F - fac;
        }

        return 1.0F;
    }

    public int getMaxAge() {
        double s = this.getScale();
        return (int) (45 * 20 * s);
    }

    public class Cloudlet {
        public double posX, posY, posZ;
        public double prevPosX, prevPosY, prevPosZ;
        public double motionX, motionY, motionZ;
        public int age;
        public int cloudletLife;
        public float angle;
        public boolean isDead = false;
        float rangeMod;
        public float colorMod;
        public Vec3 color;
        public Vec3 prevColor;
        public TorexType type;

        public Cloudlet(double posX, double posY, double posZ, float angle, int age, int maxAge) {
            this(posX, posY, posZ, angle, age, maxAge, TorexType.STANDARD);
        }

        public Cloudlet(double posX, double posY, double posZ, float angle, int age, int maxAge, TorexType type) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.age = age;
            this.cloudletLife = maxAge;
            this.angle = angle;
            this.rangeMod = 0.3F + NukeTorex.this.random.nextFloat() * 0.7F;
            this.colorMod = 0.8F + NukeTorex.this.random.nextFloat() * 0.2F;
            this.type = type;

            this.updateColor();
        }

        private void update() {
            age++;

            if (age > cloudletLife) {
                this.isDead = true;
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            Vec3 simPos = new Vec3(
                    NukeTorex.this.getX() - this.posX,
                    0,
                    NukeTorex.this.getZ() - this.posZ
            );

            double simPosX = NukeTorex.this.getX() + simPos.length();
            double simPosZ = NukeTorex.this.getZ();

            if (this.type == TorexType.STANDARD) {
                Vec3 convection = getConvectionMotion(simPosX, simPosZ);
                Vec3 lift = getLiftMotion(simPosX);

                double factor = Mth.clamp(
                        (this.posY - NukeTorex.this.getY()) / NukeTorex.this.coreHeight,
                        0.0, 1.0
                );

                this.motionX = convection.x * factor + lift.x * (1.0 - factor);
                this.motionY = convection.y * factor + lift.y * (1.0 - factor);
                this.motionZ = convection.z * factor + lift.z * (1.0 - factor);

            } else if (this.type == TorexType.SHOCK) {
                double factor = Mth.clamp((this.posY - NukeTorex.this.getY()) / NukeTorex.this.coreHeight, 0, 1);

                Vec3 motion = new Vec3(1, 0, 0).yRot(this.angle);

                this.motionX = motion.x * factor;
                this.motionY = motion.y * factor;
                this.motionZ = motion.z * factor;

            } else if (this.type == TorexType.RING) {
                Vec3 motion = getRingMotion(simPosX, simPosZ);
                this.motionX = motion.x;
                this.motionY = motion.y;
                this.motionZ = motion.z;

            } else if (this.type == TorexType.CONDENSATION) {
                Vec3 motion = getCondensationMotion();
                this.motionX = motion.x;
                this.motionY = motion.y;
                this.motionZ = motion.z;
            }

            double mult = this.motionMult * getSimulationSpeed();

            this.posX += this.motionX * mult;
            this.posY += this.motionY * mult;
            this.posZ += this.motionZ * mult;

            this.updateColor();
        }

        private Vec3 getCondensationMotion() {
            Vec3 delta = new Vec3(posX - NukeTorex.this.getX(), 0, posZ - NukeTorex.this.getZ());
            double speed = 0.00002 * NukeTorex.this.tickCount;

            return new Vec3(delta.x * speed, 0, delta.z * speed);
        }

        private Vec3 getRingMotion(double simPosX, double simPosZ) {
            if (simPosX > NukeTorex.this.getX() + torusWidth * 2) {
                return new Vec3(0, 0, 0);
            }

            /* the position of the torus' outer ring center */
            Vec3 torusPos = new Vec3(
                    NukeTorex.this.getX() + torusWidth,
                    NukeTorex.this.getY() + coreHeight * 0.5,
                    NukeTorex.this.getZ()
            );

            /* the difference between the cloudlet and the torus' ring center */
            Vec3 delta = new Vec3(
                    torusPos.x - simPosX,
                    torusPos.y - this.posY,
                    torusPos.z - simPosZ
            );

            /* the distance this cloudlet wants to achieve to the torus' ring center */
            double roller = NukeTorex.this.rollerSize * this.rangeMod * 0.25;
            /* the distance between this cloudlet and the torus' outer ring perimeter */
            double dist = delta.length() / roller - 1.0;

            /* euler function based on how far the cloudlet is away from the perimeter */
            double func = 1D - Math.pow(Math.E, -dist); // [0;1]
            /* just an approximation, but it's good enough */
            float angle = (float) (func * Math.PI * 0.5D); // [0;90°]

            /* vector going from the ring center in the direction of the cloudlet, stopping at the perimeter */
            /* rotate by the approximate angle */
            Vec3 rot = new Vec3(-delta.x / dist, -delta.y / dist, -delta.z / dist).zRot(angle);

            /* the direction from the cloudlet to the target position on the perimeter */
            Vec3 motion = new Vec3(
                    torusPos.x + rot.x - simPosX,
                    torusPos.y + rot.y - this.posY,
                    torusPos.z + rot.z - simPosZ
            );

            double speed = 0.001D;
            motion = new Vec3(motion.x * speed, motion.y * speed, motion.z * speed).yRot(this.angle);

            motion = motion.normalize();

            return motion;
        }

        /* simulated on a 2D-plane along the X/Y axis */
        private Vec3 getConvectionMotion(double simPosX, double simPosZ) {

            /* the position of the torus' outer ring center */
            Vec3 torusPos = new Vec3(
                    NukeTorex.this.getX() + torusWidth,
                    NukeTorex.this.getY() + coreHeight,
                    NukeTorex.this.getZ()
            );

            /* the difference between the cloudlet and the torus' ring center */
            Vec3 delta = new Vec3(
                    torusPos.x - simPosX,
                    torusPos.y - this.posY,
                    torusPos.z - simPosZ
            );

            /* the distance this cloudlet wants to achieve to the torus' ring center */
            double roller = NukeTorex.this.rollerSize * this.rangeMod;
            /* the distance between this cloudlet and the torus' outer ring perimeter */
            double dist = delta.length() / roller - 1D;

            /* euler function based on how far the cloudlet is away from the perimeter */
            double func = 1.0 - Math.pow(Math.E, -dist); // [0;1]
            /* just an approximation, but it's good enough */
            float angle = (float) (func * Math.PI * 0.5D); // [0;90°]

            /* vector going from the ring center in the direction of the cloudlet, stopping at the perimeter */
            /* rotate by the approximate angle */
            Vec3 rot = new Vec3(-delta.x / dist, -delta.y / dist, -delta.z / dist).zRot(angle);

            /* the direction from the cloudlet to the target position on the perimeter */
            Vec3 motion = new Vec3(
                    torusPos.x + rot.x - simPosX,
                    torusPos.y + rot.y - this.posY,
                    torusPos.z + rot.z - simPosZ
            ).yRot(this.angle);

            motion = motion.normalize();

            return motion;
        }

        private Vec3 getLiftMotion(double simPosX) {
            double scale = Mth.clamp(1D - (simPosX - (NukeTorex.this.getX() + torusWidth)), 0, 1);

            Vec3 motion = new Vec3(
                    NukeTorex.this.getX() - this.posX,
                    (NukeTorex.this.getY() + convectionHeight) - this.posY,
                    NukeTorex.this.getZ() - this.posZ
            );

            motion = motion.normalize();

            motion = new Vec3(motion.x * scale, motion.y * scale, motion.z * scale);

            return motion;
        }

        private void updateColor() {
            this.prevColor = this.color;

            double exX = NukeTorex.this.getX();
            double exY = NukeTorex.this.getY() + NukeTorex.this.coreHeight;
            double exZ = NukeTorex.this.getZ();

            double distX = exX - posX;
            double distY = exY - posY;
            double distZ = exZ - posZ;


            double distSq = distX * distX + distY * distY + distZ * distZ;
            distSq /= NukeTorex.this.heat;
            double dist = Math.sqrt(distSq);

            dist = Math.max(dist, 1);
            double col = 2.0 / dist;

            int type = NukeTorex.this.entityData.get(NukeTorex.TYPE);

            if (type == 1) {
                this.color = new Vec3(
                        Math.max(col * 1, 0.25),
                        Math.max(col * 2, 0.25),
                        Math.max(col * 0.5, 0.25)
                );
            } else if (type == 2) {
                Color color = Color.getHSBColor(this.angle / 2F / (float) Math.PI, 1F, 1F);
                if (this.type == TorexType.RING) {
                    this.color = new Vec3(
                            Math.max(col * 1, 0.25),
                            Math.max(col * 1, 0.25),
                            Math.max(col * 1, 0.25)
                    );
                } else {
                    this.color = new Vec3(color.getRed() / 255D, color.getGreen() / 255D, color.getBlue() / 255D);
                }
            } else {
                this.color = new Vec3(
                        Math.max(col * 2, 0.25),
                        Math.max(col * 1.5, 0.25),
                        Math.max(col * 0.5, 0.25)
                );
            }
        }

        public Vec3 getInterpPos(float partialTicks) {
            float scale = (float) NukeTorex.this.getScale();

            Vec3 base = new Vec3(
                    prevPosX + (posX - prevPosX) * partialTicks,
                    prevPosY + (posY - prevPosY) * partialTicks,
                    prevPosZ + (posZ - prevPosZ) * partialTicks
            );

            if (this.type != TorexType.SHOCK) {  //no rescale for the shockwave as this messes with the positions
                double x = (base.x - NukeTorex.this.getX()) * scale + NukeTorex.this.getX();
                double y = (base.y - NukeTorex.this.getY()) * scale + NukeTorex.this.getY();
                double z = (base.z - NukeTorex.this.getZ()) * scale + NukeTorex.this.getZ();
                base = new Vec3(x, y, z);
            }

            return base;
        }

        public Vec3 getInterpColor(float partialTicks) {
            if (this.type == TorexType.CONDENSATION) {
                return new Vec3(1F, 1F, 1F);
            }

            double greying = NukeTorex.this.getGreying();

            if (this.type == TorexType.RING) {
                greying += 1;
            }

            return new Vec3(
                    (prevColor.x + (color.x - prevColor.x) * partialTicks) * greying,
                    (prevColor.y + (color.y - prevColor.y) * partialTicks) * greying,
                    (prevColor.z + (color.z - prevColor.z) * partialTicks) * greying
            );
        }

        public float getAlpha() {
            float alpha = (1F - ((float) age / (float) cloudletLife)) * NukeTorex.this.getAlpha();
            if (this.type == TorexType.CONDENSATION) alpha *= 0.25;
            return alpha;
        }

        private float startingScale = 1;
        private float growingScale = 5F;

        public float getScale() {
            float base = startingScale + ((float) age / (float) cloudletLife) * growingScale;
            if (this.type != TorexType.SHOCK) base *= (float) NukeTorex.this.getScale();
            return base;
        }

        public Cloudlet setScale(float start, float grow) {
            this.startingScale = start;
            this.growingScale = grow;
            return this;
        }

        private double motionMult = 1F;

        public Cloudlet setMotion(double mult) {
            this.motionMult = mult;
            return this;
        }
    }

    public enum TorexType {
        STANDARD,
        SHOCK,
        RING,
        CONDENSATION
    }

    @Override protected void addAdditionalSaveData(CompoundTag compoundTag) {}
    @Override protected void readAdditionalSaveData(CompoundTag compoundTag) {}

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    public static void statFacStandard(Level level, double x, double y, double z, float scale) {
        statFac(level, x, y, z, scale, 0);
    }

    public static void statFacBale(Level level, double x, double y, double z, float scale) {
        statFac(level, x, y, z, scale, 1);
    }

    private static void statFac(Level level, double x, double y, double z, float scale, int type) {
        NukeTorex torex = new NukeTorex(ModEntityTypes.NUKE_TOREX.get(), level).setScale(Mth.clamp((float) BobMathUtil.squirt(scale * 0.01) * 1.5F, 0.5F, 5F));
        if (type == 1 || type == 0) torex.setType(type);
        torex.moveTo(x, y, z);;
        level.addFreshEntity(torex);
    }
}