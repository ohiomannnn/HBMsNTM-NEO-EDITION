package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
import com.hbm.util.Tuple.Pair;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpentCasingParticle extends ParticleNT {

    private static float dScale = 0.05F, smokeJitter = 0.001F;

    private final int maxSmokeGen;
    private final double smokeLift;
    private final int nodeLife;

    private final List<Pair<Vector3d, Double>> smokeNodes = new ArrayList<>();

    private final SpentCasing config;
    private final boolean isSmoking;

    private float momentumPitch, momentumYaw;

    public float xRotO, yRotO;

    public SpentCasingParticle(ClientLevel level, double x, double y, double z, double mx, double my, double mz, float momentumPitch, float momentumYaw, SpentCasing config, boolean smoking, int smokeLife, double smokeLift, int nodeLife) {
        super(level, x, y, z);

        this.momentumPitch = momentumPitch;
        this.momentumYaw = momentumYaw;
        this.config = config;

        this.lifetime = config.getMaxAge();
        this.setSize(2 * dScale * Math.max(config.getScaleX(), config.getScaleZ()), dScale * config.getScaleY());

        this.isSmoking = smoking;
        this.maxSmokeGen = smokeLife;
        this.smokeLift = smokeLift;
        this.nodeLife = nodeLife;

        this.xo = x;
        this.yo = y;
        this.zo = z;

        this.xd = mx;
        this.yd = my;
        this.zd = mz;

        // i am at a loss for words as to what the fuck is going on here, but this is needed, stop asking, fuck you
        this.setPos(x, y, z);

        this.gravity = 1F;
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if(this.age++ >= this.lifetime) this.remove();

        this.yd -= 0.04D * (double) this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98D;
        this.yd *= 0.98D;
        this.zd *= 0.98D;

        if(this.onGround) {
            this.xd *= 0.7D;
            this.zd *= 0.7D;

            this.xRot = (float) (Math.floor(this.xRot / 180F + 0.5F)) * 180F;
            this.momentumYaw *= 0.7F;
            this.onGround = false;
        }

        if(age > maxSmokeGen && !smokeNodes.isEmpty()) smokeNodes.clear();

        if(isSmoking && age <= maxSmokeGen) {

            for(Pair<Vector3d, Double> pair : smokeNodes) {
                Vector3d node = pair.getKey();

                node.add(random.nextGaussian() * smokeJitter, random.nextGaussian() * smokeJitter, smokeLift * dScale);

                pair.value = Math.max(0, pair.value - (1D / (double) nodeLife));
            }

            if(age < maxSmokeGen) {
                smokeNodes.add(new Pair<>(new Vector3d(0, 0, 0), smokeNodes.isEmpty() ? 0.0D : 1D));
            }
        }

        xRotO = xRot;
        yRotO = yRot;

        xRot += momentumPitch;
        yRot += momentumYaw;

        if(Math.abs(xRotO - xRot) > 180) {
            if(xRotO < xRot) xRotO += 360;
            if(xRotO > xRot) xRotO -= 360;
        }

        if(Math.abs(yRotO - yRot) > 180) {
            if(yRotO < yRot) yRotO += 360;
            if(yRotO > yRot) yRotO -= 360;
        }
    }

    @Override
    public void move(double x, double y, double z) {

        //Handle block collision
        double d0 = x;
        double d1 = y;
        double d2 = z;

        if(!this.noClip && (x != 0.0 || y != 0.0 || z != 0.0) && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 pos = new Vec3(x, y, z);
            Vec3 vec3 = Entity.collideBoundingBox(null, pos, this.getBoundingBox(), this.level, List.of());
            boolean xEqual = Mth.equal(pos.x, vec3.x);
            boolean zEqual = Mth.equal(pos.z, vec3.z);
            this.horizontalCollision = !xEqual || !zEqual;
            this.verticalCollision = pos.y != vec3.y;
            x = vec3.x;
            y = vec3.y;
            z = vec3.z;
        }

        if(x != 0.0 || y != 0.0 || z != 0.0) {
            this.setBoundingBox(this.getBoundingBox().move(x, y, z));
            this.setLocationFromBoundingbox();
        }

        this.onGround = d1 != y && d1 < 0.0;

        //Handles bounces
        if (d0 != x) {
            this.xd *= -0.25D;

            if(Math.abs(momentumYaw) > 1e-7)
                momentumYaw *= -0.75F;
            else
                momentumYaw = (float) random.nextGaussian() * 10F * this.config.getBounceYaw();
        }

        if (d1 != y) {
            this.yd *= -0.5D;

            boolean rotFromSpeed = Math.abs(this.yd) > 0.04;
            if(rotFromSpeed || Math.abs(momentumPitch) > 1e-7) {
                momentumPitch *= -0.75F;
                if(rotFromSpeed) {
                    float mult = (float) BobMathUtil.safeClamp(d1 / 0.2F, -1F, 1F);
                    momentumPitch += random.nextGaussian() * 10F * this.config.getBouncePitch() * mult;
                    momentumYaw += (float) random.nextGaussian() * 10F * this.config.getBounceYaw() * mult;
                }
            }
        }

        if (d2 != z) {
            this.zd *= -0.25D;

            if(Math.abs(momentumYaw) > 1e-7)
                momentumYaw *= -0.75F;
            else
                momentumYaw = (float) random.nextGaussian() * 10F * this.config.getBounceYaw();
        }

        if(this.config.getSound() != null && verticalCollision && Math.abs(d1) >= 0.2) {
            this.level.playLocalSound(x, y, z, this.config.getSound().get(), SoundSource.AMBIENT, NtmSoundEvents.PLINK_LARGE.equals(this.config.getSound()) ? 1F : 0.5F, 1F + this.random.nextFloat() * 0.2F, true);
        }

    }

    /** Used for frame-perfect translation of smoke */
    private boolean setupDeltas = false;
    private double prevRenderX;
    private double prevRenderY;
    private double prevRenderZ;

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        Vec3 camPos = camera.getPosition();
        float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x);
        float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y);
        float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z);

        if(!setupDeltas) {
            prevRenderX = pX;
            prevRenderY = pY;
            prevRenderZ = pZ;
            setupDeltas = true;
        }

        PoseStack poseStack = new PoseStack();
        poseStack.translate(pX, pY, pZ);
        RenderContext.setup(poseStack, this.getLightColor(), OverlayTexture.NO_OVERLAY);
        RenderSystem.setShaderTexture(0, ResourceManager.CASINGS_TEX);

        RenderContext.translate(pX, pY - this.bbHeight / 4 + config.getScaleY() * 0.01F, pZ);

        RenderContext.scale(dScale, dScale, dScale);

        RenderContext.mulPose(Axis.YP.rotationDegrees(180 - Mth.lerp(partialTicks, yRotO, yRot)));
        RenderContext.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, xRotO, xRotO)));

        RenderContext.scale(config.getScaleX(), config.getScaleY(), config.getScaleZ());

        int index = 0;
        for(String name : config.getType().partNames) {
            int col = this.config.getColors()[index]; //unsafe on purpose, set your colors properly or else...!
            Color color = new Color(col);
            RenderContext.setColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F);
            ResourceManager.casings.renderPart(name);
            index++;
        }

        RenderContext.setColor(1F, 1F, 1F, 1F);
        RenderContext.end();

        prevRenderX = pX;
        prevRenderY = pY;
        prevRenderZ = pZ;
    }

    @Override public RenderType getRenderType() { return null; }
}
