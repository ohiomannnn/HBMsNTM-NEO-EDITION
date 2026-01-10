package com.hbm.render.entity.projectile;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.entity.projectile.Meteor;
import com.hbm.entity.projectile.Shrapnel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderMeteor extends EntityRenderer<Meteor> {

    private static final ResourceLocation METEOR_LOCATION = HBMsNTM.withDefaultNamespaceNT("textures/block/block_meteor_molten.png");

    public RenderMeteor(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(Meteor meteor, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.0F, 1.0F, 1.0F);

        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        float rotation = (meteor.tickCount % 360 + partialTicks) * 10;
        poseStack.mulPose(Axis.XP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));

        poseStack.scale(5.0F, 5.0F, 5.0F);
        renderBlock(this.getTextureLocation(meteor), poseStack);

        poseStack.popPose();
    }

    public void renderBlock(ResourceLocation loc, PoseStack poseStack) {
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, loc);

        Matrix4f matrix = poseStack.last().pose();

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix, -0.5F, -0.5F, -0.5F).setUv(1, 0);
        buffer.addVertex(matrix, +0.5F, -0.5F, -0.5F).setUv(0, 0);
        buffer.addVertex(matrix, +0.5F, +0.5F, -0.5F).setUv(0, 1);
        buffer.addVertex(matrix, -0.5F, +0.5F, -0.5F).setUv(1, 1);

        buffer.addVertex(matrix, -0.5F, -0.5F, +0.5F).setUv(1, 0);
        buffer.addVertex(matrix, -0.5F, -0.5F, -0.5F).setUv(0, 0);
        buffer.addVertex(matrix, -0.5F, +0.5F, -0.5F).setUv(0, 1);
        buffer.addVertex(matrix, -0.5F, +0.5F, +0.5F).setUv(1, 1);

        buffer.addVertex(matrix, +0.5F, -0.5F, +0.5F).setUv(1, 0);
        buffer.addVertex(matrix, -0.5F, -0.5F, +0.5F).setUv(0, 0);
        buffer.addVertex(matrix, -0.5F, +0.5F, +0.5F).setUv(0, 1);
        buffer.addVertex(matrix, +0.5F, +0.5F, +0.5F).setUv(1, 1);

        buffer.addVertex(matrix, +0.5F, -0.5F, -0.5F).setUv(1, 0);
        buffer.addVertex(matrix, +0.5F, -0.5F, +0.5F).setUv(0, 0);
        buffer.addVertex(matrix, +0.5F, +0.5F, +0.5F).setUv(0, 1);
        buffer.addVertex(matrix, +0.5F, +0.5F, -0.5F).setUv(1, 1);

        buffer.addVertex(matrix, -0.5F, -0.5F, +0.5F).setUv(1, 0);
        buffer.addVertex(matrix, +0.5F, -0.5F, +0.5F).setUv(0, 0);
        buffer.addVertex(matrix, +0.5F, -0.5F, -0.5F).setUv(0, 1);
        buffer.addVertex(matrix, -0.5F, -0.5F, -0.5F).setUv(1, 1);

        buffer.addVertex(matrix, +0.5F, +0.5F, +0.5F).setUv(1, 0);
        buffer.addVertex(matrix, -0.5F, +0.5F, +0.5F).setUv(0, 0);
        buffer.addVertex(matrix, -0.5F, +0.5F, -0.5F).setUv(0, 1);
        buffer.addVertex(matrix, +0.5F, +0.5F, -0.5F).setUv(1, 1);
        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.enableCull();

    }

    @Override
    public ResourceLocation getTextureLocation(Meteor meteor) {
        return METEOR_LOCATION;
    }
}
