package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.particle.helper.SkeletonCreator.EnumSkeletonType;
import com.hbm.render.entity.effect.SkeletonModel;
import com.hbm.util.old.TessColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SkeletonParticle extends TextureSheetParticle {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/skeleton.png");
    public static final ResourceLocation TEXTURE_EXT = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/skoilet.png");
    public static final ResourceLocation TEXTURE_BLOOD = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/skeleton_blood.png");
    public static final ResourceLocation TEXTURE_BLOOD_EXT = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/skoilet_blood.png");
    protected EnumSkeletonType type;

    public ResourceLocation useTexture;
    public ResourceLocation useTextureExt;

    private float momentumYaw;
    private float momentumPitch;
    private int initialDelay;

    public double prevRotationPitch;
    public double prevRotationYaw;
    public double rotationPitch;
    public double rotationYaw;

    private SkeletonModel model;

    public SkeletonParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, EnumSkeletonType type) {
        super(level, x, y, z);
        this.type = type;

        this.lifetime = 1200 + random.nextInt(20);

        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.gravity = 0.02F;
        this.initialDelay = 20;

        this.momentumPitch = random.nextFloat() * 5 * (random.nextBoolean() ? 1 : -1);
        this.momentumYaw = random.nextFloat() * 5 * (random.nextBoolean() ? 1 : -1);

        this.useTexture = TEXTURE;
        this.useTextureExt = TEXTURE_EXT;

        this.model = new SkeletonModel(Minecraft.getInstance().getEntityModels().bakeLayer(SkeletonModel.SKELETON_PART_LAYER));
    }

    public SkeletonParticle makeGib() {
        this.initialDelay = -2; // skip post delay motion randomization
        this.useTexture = TEXTURE_BLOOD;
        this.useTextureExt = TEXTURE_BLOOD_EXT;
        this.gravity = 0.04F;
        this.lifetime = 600 + random.nextInt(20);
        return this;
    }

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100);

    @Override
    public void move(double x, double y, double z) {
        double d0 = x;
        double d1 = y;
        double d2 = z;

        if (this.hasPhysics && (x != 0.0 || y != 0.0 || z != 0.0) && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(x, y, z), this.getBoundingBox(), this.level, List.of());
            x = vec3.x;
            y = vec3.y;
            z = vec3.z;
        }

        if (x != 0.0 || y != 0.0 || z != 0.0) {
            this.setBoundingBox(this.getBoundingBox().move(x, y, z));
            this.setLocationFromBoundingbox();
        }

        this.onGround = d1 != y && d1 < 0.0;

        if (this.onGround) {
            this.xd = 0.0;
            this.yd = 0.0;
            this.zd = 0.0;
        } else {
            if (d0 != x) this.xd = 0.0;
            if (d2 != z) this.zd = 0.0;
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        if (initialDelay-- > 0) return;

        if (initialDelay == -1) {
            this.xd = random.nextGaussian() * 0.025;
            this.zd = random.nextGaussian() * 0.025;
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        boolean wasOnGround = this.onGround;

        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98D;
        this.yd *= 0.98D;
        this.zd *= 0.98D;

        if (!this.onGround) {
            this.rotationPitch += this.momentumPitch;
            this.rotationYaw += this.momentumYaw;
        } else {
            this.xd = 0;
            this.yd = 0;
            this.zd = 0;

            if (!wasOnGround) {
                level.playLocalSound(x, y, z, SoundEvents.SKELETON_HURT, SoundSource.AMBIENT, 0.25F, 0.8F + random.nextFloat() * 0.4F, false);
            }
        }
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();

        Vec3 camPos = camera.getPosition();
        float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x);
        float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y);
        float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z);

        poseStack.pushPose();
        poseStack.translate(pX, pY, pZ);

        poseStack.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp(partialTicks, this.prevRotationYaw, this.rotationYaw)));
        poseStack.mulPose(Axis.XP.rotationDegrees((float) Mth.lerp(partialTicks, this.prevRotationPitch, this.rotationPitch)));

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTexture(type)));

        float timeLeft = this.lifetime - (this.age + partialTicks);
        if (timeLeft < 40) {
            this.alpha = timeLeft / 40F;
        } else {
            this.alpha = 1F;
        }

        int color = TessColorUtil.getColorRGBA_F(rCol, gCol, bCol, alpha);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        model.render(poseStack, consumer, this.getLightColor(partialTicks), OverlayTexture.NO_OVERLAY, color, this.type);

        buffer.endBatch();
        poseStack.popPose();
    }

    private ResourceLocation getTexture(EnumSkeletonType type) {
        boolean usingNormal = useTexture.equals(TEXTURE);
        boolean usingNormalVill = useTextureExt.equals(TEXTURE_EXT);
        if (type == EnumSkeletonType.SKULL_VILLAGER) return usingNormalVill ? TEXTURE_EXT : TEXTURE_BLOOD_EXT;
        return usingNormal ? TEXTURE : TEXTURE_BLOOD;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            int use = level.random.nextInt(4);
            EnumSkeletonType skeletonType = switch (use) {
                case 0 -> EnumSkeletonType.LIMB;
                case 1 -> EnumSkeletonType.SKULL;
                case 2 -> EnumSkeletonType.TORSO;
                case 3 -> EnumSkeletonType.SKULL_VILLAGER;
                default -> throw new IllegalStateException("Unexpected value: " + use);
            };
            return new SkeletonParticle(level, x, y, z, 1F, 1F ,1F, skeletonType);
        }
    }
}
