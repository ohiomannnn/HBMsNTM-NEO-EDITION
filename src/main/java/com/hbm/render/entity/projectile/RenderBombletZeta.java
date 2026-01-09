package com.hbm.render.entity.projectile;

import com.hbm.entity.projectile.BombletZeta;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBombletZeta extends EntityRenderer<BombletZeta> {

    public RenderBombletZeta(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(BombletZeta bombletZeta, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(bombletZeta.yRotO + (bombletZeta.getYRot() - bombletZeta.yRotO) * partialTicks - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(bombletZeta.xRotO + (bombletZeta.getXRot() - bombletZeta.xRotO) * partialTicks));
        poseStack.scale(0.5F, 0.5F, 0.5F);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(bombletZeta)));
        ResourceManager.bomblet_zeta.renderAll(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BombletZeta bombletZeta) {
        return ResourceManager.BOMBLET_ZETA_TEX;
    }
}
