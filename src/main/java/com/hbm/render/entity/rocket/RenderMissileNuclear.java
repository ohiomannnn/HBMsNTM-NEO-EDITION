package com.hbm.render.entity.rocket;

import com.hbm.entity.missile.MissileBaseNT;
import com.hbm.entity.missile.MissileTier1;
import com.hbm.entity.missile.MissileTier4;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMissileNuclear extends EntityRenderer<MissileTier4> {

    public RenderMissileNuclear(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MissileTier4 entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.yRot) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.xRot)));
        poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.yRot) - 90.0F));

        Direction facing = entity.getEntityData().get(MissileBaseNT.ROT);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MISSILE_DOOMSDAY_TEX));
        ResourceManager.missileNuclear.renderAll(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MissileTier4 entityMissileTier1) {
        return ResourceManager.MISSILE_V2_HE_TEX;
    }
}
