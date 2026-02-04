package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.lib.ModSounds;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.CustomRenderTypes;
import com.hbm.util.old.TessColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class NukeTorexParticle extends ParticleNT {

    private int tickCount = 0;

    public double coreHeight = 3;
    public double convectionHeight = 3;
    public double torusWidth = 3;
    public double rollerSize = 1;
    public double heat = 1;
    public double lastSpawnY = -1;
    public final List<Cloudlet> cloudlets = new ArrayList<>();

    public boolean didPlaySound = false;
    public boolean didShake = false;

    protected int type = 0;
    protected float scale = 1;

    public NukeTorexParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public void tick() {

        this.tickCount++;

        double s = 1.5;
        double cs = 1.5;
        int maxAge = this.getMaxAge();

        if (this.tickCount == 1) setScale((float) s);

        if (lastSpawnY == -1) {
            lastSpawnY = y - 3;
        }

        if (tickCount < 100) this.level.setSkyFlashTime(3);

        int spawnTarget = level.getHeight(Heightmap.Types.WORLD_SURFACE, (int) x, (int) z) - 3;
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
            double x = this.x + random.nextGaussian() * range;
            double z = this.z + random.nextGaussian() * range;
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
                this.cloudlets.add(new Cloudlet(vec.x + this.x, level.getHeight(Heightmap.Types.WORLD_SURFACE, (int) (vec.x + this.x), (int) (vec.z + this.z)), vec.z + this.z, rot, 0, shockLife, TorexType.SHOCK)
                        .setScale(7F, 2F)
                        .setMotion(tickCount > 15 ? 0.75 : 0));
            }

            if (!didPlaySound) {
                handleSound(this, tickCount);
            }
        }

        // spawn ring clouds
        if (tickCount < 130 * s) {
            lifetime *= s;
            for (int i = 0; i < 2; i++) {
                Cloudlet cloud = new Cloudlet(this.x, this.y + coreHeight, this.z, (float)(random.nextDouble() * 2D * Math.PI), 0, lifetime, TorexType.RING);
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
                    Cloudlet cloud = new Cloudlet(this.x + vec.x, this.y + coreHeight - 5 + j * s, this.z + vec.z, angle, 0, (int) ((20 + tickCount / 10) * (1 + random.nextDouble() * 0.1)), TorexType.CONDENSATION);
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
                    Cloudlet cloud = new Cloudlet(this.x + vec.x, this.y + coreHeight + 25 + j * cs, this.z + vec.z, angle, 0, (int) ((20 + tickCount / 10) * (1 + random.nextDouble() * 0.1)), TorexType.CONDENSATION);
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

        if (this.tickCount > maxAge) this.remove();
    }

    public NukeTorexParticle setScale(float scale) {
        this.scale = scale;
        this.coreHeight = this.coreHeight / 1.5D * scale;
        this.convectionHeight = this.convectionHeight / 1.5D * scale;
        this.torusWidth = this.torusWidth / 1.5D * scale;
        this.rollerSize = this.rollerSize / 1.5D * scale;
        return this;
    }

    public NukeTorexParticle setType(int type) {
        this.type = type;
        return this;
    }

    public double getSimulationSpeed() {

        int lifetime = getMaxAge();
        int simSlow = lifetime / 4;
        int simStop = lifetime / 2;
        int life = this.tickCount;

        if (life > simStop) {
            return 0D;
        }

        if (life > simSlow) {
            return 1D - ((double)(life - simSlow) / (double)(simStop - simSlow));
        }

        return 1.0D;
    }

    public double getScale() {
        return this.scale;
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
        int life = this.tickCount;

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
            this.rangeMod = 0.3F + random.nextFloat() * 0.7F;
            this.colorMod = 0.8F + random.nextFloat() * 0.2F;
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
                    NukeTorexParticle.this.x - this.posX,
                    0,
                    NukeTorexParticle.this.z - this.posZ
            );

            double simPosX = NukeTorexParticle.this.x + simPos.length();
            double simPosZ = NukeTorexParticle.this.z;

            if (this.type == TorexType.STANDARD) {
                Vec3 convection = getConvectionMotion(simPosX, simPosZ);
                Vec3 lift = getLiftMotion(simPosX);

                double factor = Mth.clamp(
                        (this.posY - NukeTorexParticle.this.y) / NukeTorexParticle.this.coreHeight,
                        0.0, 1.0
                );

                this.motionX = convection.x * factor + lift.x * (1.0 - factor);
                this.motionY = convection.y * factor + lift.y * (1.0 - factor);
                this.motionZ = convection.z * factor + lift.z * (1.0 - factor);

            } else if (this.type == TorexType.SHOCK) {
                double factor = Mth.clamp((this.posY - NukeTorexParticle.this.y) / NukeTorexParticle.this.coreHeight, 0, 1);

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
            Vec3 delta = new Vec3(posX - NukeTorexParticle.this.x, 0, posZ - NukeTorexParticle.this.z);
            double speed = 0.00002 * NukeTorexParticle.this.tickCount;

            return new Vec3(delta.x * speed, 0, delta.z * speed);
        }

        private Vec3 getRingMotion(double simPosX, double simPosZ) {
            if (simPosX > NukeTorexParticle.this.x + torusWidth * 2) {
                return new Vec3(0, 0, 0);
            }

            /* the position of the torus' outer ring center */
            Vec3 torusPos = new Vec3(
                    NukeTorexParticle.this.x + torusWidth,
                    NukeTorexParticle.this.y + coreHeight * 0.5,
                    NukeTorexParticle.this.z
            );

            /* the difference between the cloudlet and the torus' ring center */
            Vec3 delta = new Vec3(
                    torusPos.x - simPosX,
                    torusPos.y - this.posY,
                    torusPos.z - simPosZ
            );

            /* the distance this cloudlet wants to achieve to the torus' ring center */
            double roller = NukeTorexParticle.this.rollerSize * this.rangeMod * 0.25;
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
                    NukeTorexParticle.this.x + torusWidth,
                    NukeTorexParticle.this.y + coreHeight,
                    NukeTorexParticle.this.z
            );

            /* the difference between the cloudlet and the torus' ring center */
            Vec3 delta = new Vec3(
                    torusPos.x - simPosX,
                    torusPos.y - this.posY,
                    torusPos.z - simPosZ
            );

            /* the distance this cloudlet wants to achieve to the torus' ring center */
            double roller = NukeTorexParticle.this.rollerSize * this.rangeMod;
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
            double scale = Mth.clamp(1D - (simPosX - (NukeTorexParticle.this.x + torusWidth)), 0, 1);

            Vec3 motion = new Vec3(
                    NukeTorexParticle.this.x - this.posX,
                    (NukeTorexParticle.this.y + convectionHeight) - this.posY,
                    NukeTorexParticle.this.z - this.posZ
            );

            motion = motion.normalize();

            motion = new Vec3(motion.x * scale, motion.y * scale, motion.z * scale);

            return motion;
        }

        private void updateColor() {
            this.prevColor = this.color;

            double exX = NukeTorexParticle.this.x;
            double exY = NukeTorexParticle.this.y + NukeTorexParticle.this.coreHeight;
            double exZ = NukeTorexParticle.this.z;

            double distX = exX - posX;
            double distY = exY - posY;
            double distZ = exZ - posZ;


            double distSq = distX * distX + distY * distY + distZ * distZ;
            distSq /= NukeTorexParticle.this.heat;
            double dist = Math.sqrt(distSq);

            dist = Math.max(dist, 1);
            double col = 2.0 / dist;

            int type = NukeTorexParticle.this.type;

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
            float scale = (float) NukeTorexParticle.this.getScale();

            Vec3 base = new Vec3(
                    prevPosX + (posX - prevPosX) * partialTicks,
                    prevPosY + (posY - prevPosY) * partialTicks,
                    prevPosZ + (posZ - prevPosZ) * partialTicks
            );

            if (this.type != TorexType.SHOCK) {  //no rescale for the shockwave as this messes with the positions
                double x = (base.x - NukeTorexParticle.this.x) * scale + NukeTorexParticle.this.x;
                double y = (base.y - NukeTorexParticle.this.y) * scale + NukeTorexParticle.this.y;
                double z = (base.z - NukeTorexParticle.this.z) * scale + NukeTorexParticle.this.z;
                base = new Vec3(x, y, z);
            }

            return base;
        }

        public Vec3 getInterpColor(float partialTicks) {
            if (this.type == TorexType.CONDENSATION) {
                return new Vec3(1F, 1F, 1F);
            }

            double greying = NukeTorexParticle.this.getGreying();

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
            float alpha = (1F - ((float) age / (float) cloudletLife)) * NukeTorexParticle.this.getAlpha();
            if (this.type == TorexType.CONDENSATION) alpha *= 0.25;
            return alpha;
        }

        private float startingScale = 1;
        private float growingScale = 5F;

        public float getScale() {
            float base = startingScale + ((float) age / (float) cloudletLife) * growingScale;
            if (this.type != TorexType.SHOCK) base *= (float) NukeTorexParticle.this.getScale();
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


    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.translate(this.x - camPos.x, this.y - camPos.y, this.z - camPos.z);
        FogRenderer.setupNoFog();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        cloudletWrapper(partialTicks, poseStack, buffer);
        if (this.tickCount < 101) flashWrapper(partialTicks, poseStack, buffer);
        if (this.tickCount < 10 && System.currentTimeMillis() - HBMsNTMClient.flashTimestamp > 1_000) HBMsNTMClient.flashTimestamp = System.currentTimeMillis();
        if (this.didPlaySound && !this.didShake && System.currentTimeMillis() - HBMsNTMClient.shakeTimestamp > 1_000) {
            HBMsNTMClient.shakeTimestamp = System.currentTimeMillis();
            this.didShake = true;
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.hurtDuration = 15;
                player.hurtTime = 15;
            }
        }
        buffer.endBatch();
        poseStack.popPose();
    }

    private static final ResourceLocation CLOUDLET = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/base_particle.png");
    private static final ResourceLocation FLASH = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/flare.png");

    private void cloudletWrapper(float partialTicks, PoseStack poseStack, MultiBufferSource buffer) {
        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.entitySmothNoDepth(CLOUDLET));

        ArrayList<Cloudlet> cloudlets = new ArrayList<>(this.cloudlets);
        cloudlets.sort(cloudSorter);

        for (Cloudlet cloudlet : cloudlets) {
            Vec3 vec = cloudlet.getInterpPos(partialTicks);
            double x = vec.x - this.x;
            double y = vec.y - this.y;
            double z = vec.z - this.z;
            Matrix4f matrix = poseStack.last().pose();
            renderCloudlet(matrix, consumer, (float) x, (float) y, (float) z, cloudlet, partialTicks);
        }
    }

    private void flashWrapper(float partialTicks, PoseStack poseStack, MultiBufferSource buffer) {
        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.entityAdditive(FLASH));

        double age = Math.min(this.tickCount + partialTicks, 100);
        float alpha = (float) ((100D - age) / 100F);

        Random rand = new Random(this.hashCode());
        for (int i = 0; i < 3; i++) {
            float x = (float) (rand.nextGaussian() * 0.5F * this.rollerSize);
            float y = (float) (rand.nextGaussian() * 0.5F * this.rollerSize);
            float z = (float) (rand.nextGaussian() * 0.5F * this.rollerSize);
            Matrix4f matrix = poseStack.last().pose();
            renderFlash(matrix, consumer, x, (float) (y + this.coreHeight), z, (float) (25 * this.rollerSize), alpha);
        }
    }


    private void renderCloudlet(Matrix4f matrix, VertexConsumer consumer, float posX, float posY, float posZ, Cloudlet cloud, float partialTicks) {

        float alpha = cloud.getAlpha();
        float scale = cloud.getScale();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

        float brightness = cloud.type == TorexType.CONDENSATION ? 0.9F : 0.75F * cloud.colorMod;
        Vec3 interpColor = cloud.getInterpColor(partialTicks);

        int color = TessColorUtil.getColorRGBA_F((float)interpColor.x * brightness, (float)interpColor.y * brightness, (float)interpColor.z * brightness, alpha);
        int overlay = OverlayTexture.NO_OVERLAY;

        consumer.addVertex(matrix, posX - l.x - u.x, posY - l.y - u.y, posZ - l.z - u.z)
                .setColor(color)
                .setUv(1, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, posX - l.x + u.x, posY - l.y + u.y, posZ - l.z + u.z)
                .setColor(color)
                .setUv(1, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, posX + l.x + u.x, posY + l.y + u.y, posZ + l.z + u.z)
                .setColor(color)
                .setUv(0, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, posX + l.x - u.x, posY + l.y - u.y, posZ + l.z - u.z)
                .setColor(color)
                .setUv(0, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    private void renderFlash(Matrix4f matrix, VertexConsumer consumer, float posX, float posY, float posZ, float scale, float alpha) {

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

        int color = TessColorUtil.getColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
        int overlay = OverlayTexture.NO_OVERLAY;

        consumer.addVertex(matrix, posX - l.x - u.x, posY - l.y - u.y, posZ - l.z - u.z)
                .setColor(color)
                .setUv(1, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, posX - l.x + u.x, posY - l.y + u.y, posZ - l.z + u.z)
                .setColor(color)
                .setUv(1, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, posX + l.x + u.x, posY + l.y + u.y, posZ + l.z + u.z)
                .setColor(color)
                .setUv(0, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, posX + l.x - u.x, posY + l.y - u.y, posZ + l.z - u.z)
                .setColor(color)
                .setUv(0, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    private final Comparator<Cloudlet> cloudSorter = (ca, cb) -> {
        Player player = Minecraft.getInstance().player;
        if (player == null) return 0;
        double dist1 = player.distanceToSqr(ca.posX, ca.posY, ca.posZ);
        double dist2 = player.distanceToSqr(cb.posX, cb.posY, cb.posZ);
        return Double.compare(dist2, dist1);
    };

    @Override
    public RenderType getRenderType() {
        return RenderType.cutout();
    }

    public enum TorexType {
        STANDARD,
        SHOCK,
        RING,
        CONDENSATION
    }

    public static void handleSound(NukeTorexParticle particle, int tickCount) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            double dist = Math.sqrt(player.distanceToSqr(particle.x, particle.y, particle.z));
            double radius = (tickCount * 1.5 + 1) * 1.5;
            if (dist < radius) {
                particle.level.playLocalSound(particle.z, particle.y, particle.z, ModSounds.NUCLEAR_EXPLOSION.get(), SoundSource.AMBIENT, 10_000F, 1F, false);
                particle.didPlaySound = true;
            }
        }
    }

}
