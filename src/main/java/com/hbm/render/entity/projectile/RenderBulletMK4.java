package com.hbm.render.entity.projectile;

import com.hbm.entity.projectile.BulletBaseMK4;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderBulletMK4 extends EntityRenderer<BulletBaseMK4> {

    public RenderBulletMK4(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BulletBaseMK4 bullet, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(bullet.config == null) bullet.config = bullet.getBulletConfig();
        if(bullet.config == null) return;

        RenderContext.setup(poseStack, packedLight, OverlayTexture.NO_OVERLAY);

        if(bullet.config.renderRotations) {
            RenderContext.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, bullet.yRotO, bullet.yRot) - 90.0F));
            RenderContext.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, bullet.xRotO, bullet.xRot) + 180));
        }

        if(bullet.config.renderer != null) {
            bullet.config.renderer.accept(bullet, partialTicks);
        }

        RenderContext.end();
    }

    @Override public ResourceLocation getTextureLocation(BulletBaseMK4 buller) { return ResourceManager.EMPTY; }
}
