package com.hbm.render.entity.projectile;

import com.hbm.entity.projectile.BombletZeta;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBombletZeta extends EntityRenderer<BombletZeta> {

    public RenderBombletZeta(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(BombletZeta bomb, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        RenderContext.setup(NtmRenderTypes.FVBO_NC.apply(this.getTextureLocation(bomb)), poseStack, packedLight, OverlayTexture.NO_OVERLAY);

        RenderContext.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, bomb.yRotO, bomb.yRot) - 90.0F));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, bomb.xRotO, bomb.xRot)));
        RenderContext.scale(0.5F, 0.5F, 0.5F);

        ResourceManager.bomblet_zeta.renderAll();

        RenderContext.end();
    }

    @Override
    public ResourceLocation getTextureLocation(BombletZeta bomb) {
        return ResourceManager.BOMBLET_ZETA_TEX;
    }
}
