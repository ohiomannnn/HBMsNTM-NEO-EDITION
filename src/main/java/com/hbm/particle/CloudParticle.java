package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.particle.helper.CloudCreator.CloudType;
import com.hbm.render.NtmRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class CloudParticle extends ParticleNT {

    private static final ResourceLocation TEXTURE_FLEIJA = NuclearTechMod.withDefaultNamespace("textures/models/blast_fleija.png");
    private static final ResourceLocation TEXTURE_SOLINIUM = NuclearTechMod.withDefaultNamespace("textures/models/blast_solinium.png");

    public CloudType type;

    public CloudParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);

        this.lifetime = 100;
    }

    @Override
    public void tick() {

        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
        this.level.setSkyFlashTime(2);

        if (this.age++ >= this.lifetime) {
            this.dead = true;
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();

        Vec3 camPos = camera.getPosition();
        poseStack.translate(this.x - camPos.x, this.y - camPos.y, this.z - camPos.z);

        poseStack.scale(this.age, this.age, this.age);

        if (type != CloudType.RAINBOW) {
            ResourceManager.sphere.renderAll(poseStack, consumer, 240, OverlayTexture.NO_OVERLAY);
        } else {
            poseStack.scale(0.5F, 0.5F, 0.5F);
            ResourceManager.sphere.renderAll(poseStack, consumer, 240, OverlayTexture.NO_OVERLAY, this.level.random.nextInt(0x100), this.level.random.nextInt(0x100), this.level.random.nextInt(0x100), 1.0F);
            poseStack.scale(1/0.5F, 1/0.5F, 1/0.5F);

            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

            for (float i = 0.6F; i <= 1F; i += 0.1F) {

                poseStack.scale(i, i, i);
                VertexConsumer addConsumer = buffer.getBuffer(NtmRenderTypes.CLOUD_RAINBOW_ADDITIVE);
                ResourceManager.sphere.renderAll(poseStack, addConsumer, 240, OverlayTexture.NO_OVERLAY, this.level.random.nextInt(0x100), this.level.random.nextInt(0x100), this.level.random.nextInt(0x100), 1.0F);
                poseStack.scale(1/i, 1/i, 1/i);
            }

            buffer.endBatch();
        }

        poseStack.popPose();
    }

    @Override
    public RenderType getRenderType() {
        return switch (type) {
            case FLEIJA -> NtmRenderTypes.CLOUD.apply(TEXTURE_FLEIJA);
            case SOLINIUM -> NtmRenderTypes.CLOUD.apply(TEXTURE_SOLINIUM);
            case RAINBOW -> NtmRenderTypes.CLOUD_RAINBOW;
        };
    }
}
