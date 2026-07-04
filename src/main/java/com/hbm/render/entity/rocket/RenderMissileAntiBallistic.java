package com.hbm.render.entity.rocket;

import com.hbm.entity.missile.MissileAntiBallistic;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMissileAntiBallistic extends EntityRenderer<MissileAntiBallistic> {

    public RenderMissileAntiBallistic(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MissileAntiBallistic missile, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        RenderContext.setup(poseStack, packedLight, OverlayTexture.NO_OVERLAY);

        RenderContext.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, missile.yRotO, missile.yRot) - 90.0F));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, missile.xRotO, missile.xRot)));
        RenderContext.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, missile.yRotO, missile.yRot) - 90.0F));

        RenderSystem.setShaderTexture(0, this.getTextureLocation(missile));
        ResourceManager.missileABM.renderAll();

        RenderContext.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MissileAntiBallistic missile) {
        return ResourceManager.MISSILE_AA_TEX;
    }
}
