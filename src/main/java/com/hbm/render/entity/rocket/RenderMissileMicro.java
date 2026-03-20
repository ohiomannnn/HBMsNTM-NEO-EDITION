package com.hbm.render.entity.rocket;

import com.hbm.entity.missile.MissileBaseNT;
import com.hbm.entity.missile.MissileTier0;
import com.hbm.entity.missile.MissileTier0.*;
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

public class RenderMissileMicro extends EntityRenderer<MissileTier0> {

    public RenderMissileMicro(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MissileTier0 missile, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, missile.yRotO, missile.yRot) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, missile.xRotO, missile.xRot)));
        poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, missile.yRotO, missile.yRot) - 90.0F));

        Direction facing = missile.getEntityData().get(MissileBaseNT.ROT);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(missile)));
        ResourceManager.missileMicro.renderAll(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MissileTier0 missile) {
        if (missile instanceof MissileMicro)       return ResourceManager.MISSILE_MICRO_TEX;
        if (missile instanceof MissileSchrabidium) return ResourceManager.MISSILE_MICRO_SCHRABIDIUM_TEX;
        if (missile instanceof MissileBHole)       return ResourceManager.MISSILE_MICRO_BHOLE_TEX;
        if (missile instanceof MissileTaint)       return ResourceManager.MISSILE_MICRO_TAINT_TEX;
        if (missile instanceof MissileEMP)         return ResourceManager.MISSILE_MICRO_EMP_TEX;
        return ResourceManager.EMPTY;
    }
}
