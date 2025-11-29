package com.hbm.render.entity.projectile;

import com.hbm.HBMsNTM;
import com.hbm.entity.missile.MissileTier1;
import com.hbm.entity.projectile.Shrapnel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RenderMissileTEST extends EntityRenderer<Entity> {

    private static final ResourceLocation SHRAPNEL = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/entity/shrapnel.png");

    private final ModelShrapnel<Shrapnel> model;

    public RenderMissileTEST(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelShrapnel<>(context.bakeLayer(ModelShrapnel.LAYER_LOCATION));
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.0F, 1.0F, 1.0F);

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
        poseStack.mulPose(Axis.YN.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 90.0F));

        poseStack.scale(3F, 10F, 3F);

        VertexConsumer vertexconsumer = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Entity test) {
        return SHRAPNEL;
    }
}
