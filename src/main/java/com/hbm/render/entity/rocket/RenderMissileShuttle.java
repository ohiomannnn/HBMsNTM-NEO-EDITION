package com.hbm.render.entity.rocket;

import com.hbm.entity.missile.MissileBaseNT;
import com.hbm.entity.missile.MissileShuttle;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMissileShuttle extends EntityRenderer<MissileShuttle> {

    public RenderMissileShuttle(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MissileShuttle missile, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        RenderContext.setup(NtmRenderTypes.FVBO_NC.apply(this.getTextureLocation(missile)), poseStack, packedLight, OverlayTexture.NO_OVERLAY);

        RenderContext.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, missile.yRotO, missile.yRot) - 90.0F));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, missile.xRotO, missile.xRot)));
        RenderContext.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, missile.yRotO, missile.yRot) - 90.0F));

        Direction facing = missile.getEntityData().get(MissileBaseNT.ROT);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));

        ResourceManager.missileShuttle.renderAll();

        RenderContext.end();
    }

    @Override
    public ResourceLocation getTextureLocation(MissileShuttle missile) {
        return ResourceManager.MISSILE_SHUTTLE_TEX;
    }
}
