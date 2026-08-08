package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.render.item.weapon.sedna.ItemRenderWeaponBase;
import com.hbm.render.material.Material;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
import com.hbm.util.Tuple.Pair;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpentCasingParticle extends ParticleNT {

    private static final float D_SCALE = 0.05F, SMOKE_JITTER = 0.001F;

    private static Material material;

    private final int maxSmokeGen;
    private final double smokeLift;
    private final int nodeLife;

    private final List<Pair<Vector3f, Double>> smokeNodes = new ArrayList<>();

    private final SpentCasing config;
    private final boolean isSmoking;

    private float momentumPitch, momentumYaw;

    public float xRotO, yRotO;

    public SpentCasingParticle(ClientLevel level, double x, double y, double z, double mx, double my, double mz, float momentumPitch, float momentumYaw, SpentCasing config, boolean smoking, int smokeLife, double smokeLift, int nodeLife) {
        super(level, x, y, z);
        if(material == null) material = Material.builder().texture(ResourceManager.CASINGS_TEX).build();

        this.momentumPitch = momentumPitch;
        this.momentumYaw = momentumYaw;
        this.config = config;

        this.lifetime = config.getMaxAge();
        this.setSize(2 * D_SCALE * Math.max(config.getScaleX(), config.getScaleZ()), D_SCALE * config.getScaleY());

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

            for(Pair<Vector3f, Double> pair : smokeNodes) {
                Vector3f node = pair.getKey();

                node.add((float) (random.nextGaussian() * SMOKE_JITTER), (float) (smokeLift * SMOKE_JITTER), (float) (random.nextGaussian() * SMOKE_JITTER));

                pair.value = Math.max(0, pair.value - (1D / (double) nodeLife));
            }

            if(age < maxSmokeGen) {
                smokeNodes.add(new Pair<>(new Vector3f(0, 0, 0), smokeNodes.isEmpty() ? 0.0D : 1D));
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

        // Handle block collision
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

        // Handles bounces
        if(d0 != x) {
            this.xd *= -0.25D;

            if(Math.abs(momentumYaw) > 1e-7) {
                momentumYaw *= -0.75F;
            } else {
                momentumYaw = (float) random.nextGaussian() * 10F * this.config.getBounceYaw();
            }
        }

        if(d1 != y) {
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

        if(d2 != z) {
            this.zd *= -0.25D;

            if(Math.abs(momentumYaw) > 1e-7) {
                momentumYaw *= -0.75F;
            } else {
                momentumYaw = (float) random.nextGaussian() * 10F * this.config.getBounceYaw();
            }
        }

        if(this.config.getSound() != null && verticalCollision && Math.abs(d1) >= 0.2) {
            NuclearTechMod.proxy.playLocalSound(this.x, this.y, this.z, this.config.getSound().get(), SoundSource.AMBIENT, NtmSoundEvents.PLINK_LARGE.equals(this.config.getSound()) ? 1F : 0.5F, 1F + this.random.nextFloat() * 0.2F);
        }
    }

    @Override
    protected void setLocationFromBoundingbox() {
        AABB aabb = this.getBoundingBox();
        this.x = (aabb.minX + aabb.maxX) / (double)2.0F;
        this.y = aabb.minY + this.bbHeight / 2;
        this.z = (aabb.minZ + aabb.maxZ) / (double)2.0F;
    }

    /** Used for frame-perfect translation of smoke */
    private boolean setupDeltas = false;
    private float prevRenderX;
    private float prevRenderY;
    private float prevRenderZ;

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {

        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        Vec3 camPos = camera.getPosition();
        float worldX = (float) Mth.lerp(partialTicks, this.xo, this.x);
        float worldY = (float) Mth.lerp(partialTicks, this.yo, this.y);
        float worldZ = (float) Mth.lerp(partialTicks, this.zo, this.z);

        float pX = worldX - (float) camPos.x;
        float pY = worldY - (float) camPos.y;
        float pZ = worldZ - (float) camPos.z;

        if(!setupDeltas) {
            prevRenderX = worldX;
            prevRenderY = worldY;
            prevRenderZ = worldZ;
            setupDeltas = true;
        }

        PoseStack poseStack = new PoseStack();
        RenderContext.setup(poseStack, this.getLightColor(), OverlayTexture.NO_OVERLAY);

        RenderContext.translate(pX, pY + config.getScaleY() * 0.01F, pZ);

        RenderContext.scale(D_SCALE, D_SCALE, D_SCALE);

        RenderContext.mulPose(Axis.YP.rotationDegrees(180 - Mth.lerp(partialTicks, yRotO, yRot)));
        RenderContext.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, xRotO, xRot)));

        RenderContext.scale(config.getScaleX(), config.getScaleY(), config.getScaleZ());

        int index = 0;
        for(String name : config.getType().partNames) {
            int col = this.config.getColors()[index]; //unsafe on purpose, set your colors properly or else...!
            Color color = new Color(col);
            RenderContext.setColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F);
            ResourceManager.casings.renderPart(material, name);
            index++;
        }

        RenderContext.setColor(1F, 1F, 1F, 1F);
        RenderContext.end();

        poseStack.pushPose();
        poseStack.translate(pX, pY, pZ);

        Matrix4f matrix = poseStack.last().pose();

        if(!smokeNodes.isEmpty()) {
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer consumer = buffer.getBuffer(ItemRenderWeaponBase.SMOKE);

            float scale = config.getScaleX() * 0.5F * D_SCALE;
            Vector3f vec = new Vector3f(scale, 0, 0);
            float yaw = Mth.lerp(partialTicks, player.yRotO, player.yRot);
            vec.rotateY((float) Math.toRadians(-yaw));

            float deltaX = prevRenderX - worldX;
            float deltaY = prevRenderY - worldY;
            float deltaZ = prevRenderZ - worldZ;

            for(Pair<Vector3f, Double> pair : smokeNodes) {
                Vector3f pos = pair.getKey();
                pos.x += deltaX;
                pos.y += deltaY;
                pos.z += deltaZ;
            }

            for(int i = 0; i < smokeNodes.size() - 1; i++) {
                final Pair<Vector3f, Double> node = smokeNodes.get(i), past = smokeNodes.get(i + 1);
                final Vector3f nodeLoc = node.getKey(), pastLoc = past.getKey();
                float nodeAlpha = node.getValue().floatValue();
                float pastAlpha = past.getValue().floatValue();

                float timeAlpha = 1F - (float) age / (float) maxSmokeGen;
                nodeAlpha *= timeAlpha;
                pastAlpha *= timeAlpha;

                float clamped = Math.clamp(nodeAlpha, 0F, 1F);
                float clampedPast = Math.clamp(pastAlpha, 0F, 1F);
                int packedLight = this.getLightColor();

                consumer.addVertex(matrix, nodeLoc.x, nodeLoc.y, nodeLoc.z).setColor(1F, 1F, 1F, clamped).setLight(packedLight);
                consumer.addVertex(matrix, nodeLoc.x + vec.x, nodeLoc.y, nodeLoc.z + vec.z).setColor(1F, 1F, 1F, 0F).setLight(packedLight);
                consumer.addVertex(matrix, pastLoc.x + vec.x, pastLoc.y, pastLoc.z + vec.z).setColor(1F, 1F, 1F, 0F).setLight(packedLight);
                consumer.addVertex(matrix, pastLoc.x, pastLoc.y, pastLoc.z).setColor(1F, 1F, 1F, clampedPast).setLight(packedLight);

                consumer.addVertex(matrix, nodeLoc.x, nodeLoc.y, nodeLoc.z).setColor(1F, 1F, 1F, clamped).setLight(packedLight);
                consumer.addVertex(matrix, nodeLoc.x - vec.x, nodeLoc.y, nodeLoc.z - vec.z).setColor(1F, 1F, 1F, 0F).setLight(packedLight);
                consumer.addVertex(matrix, pastLoc.x - vec.x, pastLoc.y, pastLoc.z - vec.z).setColor(1F, 1F, 1F, 0F).setLight(packedLight);
                consumer.addVertex(matrix, pastLoc.x, pastLoc.y, pastLoc.z).setColor(1F, 1F, 1F, clampedPast).setLight(packedLight);
            }
            buffer.endLastBatch();
        }

        poseStack.popPose();

        prevRenderX = worldX;
        prevRenderY = worldY;
        prevRenderZ = worldZ;
    }

    @Override public RenderType getRenderType() { return null; }
}
