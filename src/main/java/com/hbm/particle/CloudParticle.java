package com.hbm.particle;

import com.hbm.main.ResourceManager;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.particle.helper.CloudCreator.CloudType;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;

public class CloudParticle extends ParticleNT {

    public CloudType type = CloudType.FLEIJA;

    public CloudParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);

        this.lifetime = 100;
    }

    @Override
    public void tick() {

        this.level.setSkyFlashTime(5);

        if (this.age++ >= this.lifetime) {
            this.dead = true;
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        PoseStack poseStack = new PoseStack();
        Vec3 camPos = camera.getPosition();
        poseStack.translate(this.x - camPos.x, this.y - camPos.y, this.z - camPos.z);
        RenderContext.setup(poseStack, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

        RenderSystem.depthMask(false);
        RenderContext.enableCull(true);

        float baseScale = (this.age + partialTicks) * 2;
        float ageScale = baseScale / this.lifetime;

        RenderContext.pushPose();
        RenderContext.setRenderType(NtmRenderTypes.FVBO_NL_NT);

        float scale = ageScale * 1.2F;
        if(scale > 1) scale = Math.max(1 - (scale - 1) * 5, 0);
        scale *= 2 * baseScale;
        RenderContext.scale(scale, scale, scale);

        RenderContext.setColor(0F, 1F, 1F, 1F);
        ResourceManager.sphere_new.renderAll();

        RenderContext.setRenderType(NtmRenderTypes.FVBO_ADD_NL_NT);

        RenderContext.setColor(0F, 0.125F, 0.125F, 1F);
        float outerScale = 1.05F;
        for(int i = 0; i < 3; i++) {
            RenderContext.scale(outerScale, outerScale, outerScale);
            ResourceManager.sphere_new.renderAll();
        }

        RenderContext.popPose();

        RenderContext.pushPose();

        float shockwave = 5 * baseScale;
        RenderContext.scale(shockwave, shockwave, shockwave);
        float shockTint = (1F - ageScale) * 0.75F;
        RenderContext.setColor(shockTint, shockTint, shockTint, 1.0F);
        ResourceManager.sphere_new.renderAll();

        RenderContext.popPose();

        RenderSystem.depthMask(true);

        RenderContext.end();
    }

    @Override public RenderType getRenderType() { return null; }
}
