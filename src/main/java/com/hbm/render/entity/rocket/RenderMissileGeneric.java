package com.hbm.render.entity.rocket;

import com.hbm.entity.missile.MissileTier1;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMissileGeneric extends EntityRenderer<MissileTier1> {

    public RenderMissileGeneric(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MissileTier1 entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(packedLight, entity.yRotO, entity.yRot) - 90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(packedLight, entity.xRotO, entity.xRot)));
        poseStack.mulPose(Axis.ZN.rotationDegrees(Mth.lerp(packedLight, entity.yRotO, entity.yRot) - 90.0F));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MISSILE_V2_HE_TEX));
        ResourceManager.missileV2.renderAll(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MissileTier1 entityMissileTier1) {
        return ResourceManager.MISSILE_V2_HE_TEX;
    }
}
