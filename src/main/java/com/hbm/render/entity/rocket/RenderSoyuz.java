package com.hbm.render.entity.rocket;

import com.hbm.entity.missile.Soyuz;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.RenderContext;
import com.hbm.render.util.SoyuzPronter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderSoyuz extends EntityRenderer<Soyuz> {

    public RenderSoyuz(Context context) {
        super(context);
    }

    @Override
    public void render(Soyuz soyuz, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        RenderContext.setup(poseStack, packedLight, OverlayTexture.NO_OVERLAY);

        int type = soyuz.getSkin();
        SoyuzPronter.prontSoyuz(type);

        RenderContext.end();
    }

    @Override
    public ResourceLocation getTextureLocation(Soyuz soyuz) {
        return ResourceManager.EMPTY;
    }
}
