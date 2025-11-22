package com.hbm.render.entity.projectile;

import com.hbm.HBMsNTM;
import com.hbm.entity.projectile.Shrapnel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderShrapnel extends EntityRenderer<Shrapnel> {

    private static final ResourceLocation SHRAPNEL = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/entity/shrapnel.png");

    private final ModelShrapnel<Shrapnel> model;

    public RenderShrapnel(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelShrapnel<>(context.bakeLayer(ModelShrapnel.LAYER_LOCATION));
    }

    @Override
    public void render(Shrapnel entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        float rotation = (entity.tickCount % 360) * 10 + partialTicks;
        poseStack.mulPose(Axis.XP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));

        if (entity.getShrapnelType() >= 2) { //scale up lava blobs
            poseStack.scale(3F, 3F, 3F);
        }

        VertexConsumer vertexconsumer = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Shrapnel entityShrapnel) {
        return SHRAPNEL;
    }
}
