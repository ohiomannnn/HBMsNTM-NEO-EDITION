package com.hbm.render.entity.rocket;

import com.hbm.entity.missile.MissileBaseNT;
import com.hbm.entity.missile.MissileTier3;
import com.hbm.entity.missile.MissileTier3.MissileBurst;
import com.hbm.entity.missile.MissileTier3.MissileDrill;
import com.hbm.entity.missile.MissileTier3.MissileInferno;
import com.hbm.entity.missile.MissileTier3.MissileRain;
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

public class RenderMissileHuge extends EntityRenderer<MissileTier3> {

    public RenderMissileHuge(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MissileTier3 missile, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        RenderContext.setup(NtmRenderTypes.FVBO.apply(this.getTextureLocation(missile)), poseStack, packedLight, OverlayTexture.NO_OVERLAY);

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

        ResourceManager.missileHuge.renderAll();

        RenderContext.end();
    }

    @Override
    public ResourceLocation getTextureLocation(MissileTier3 missile) {
        if (missile instanceof MissileBurst)   return ResourceManager.MISSILE_HUGE_HE_TEX;
        if (missile instanceof MissileInferno) return ResourceManager.MISSILE_HUGE_IN_TEX;
        if (missile instanceof MissileRain)    return ResourceManager.MISSILE_HUGE_CL_TEX;
        if (missile instanceof MissileDrill)   return ResourceManager.MISSILE_HUGE_BU_TEX;
        return ResourceManager.EMPTY;
    }
}
