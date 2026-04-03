package com.hbm.render.entity.effect;

import com.hbm.entity.logic.Bomber;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBomber extends EntityRenderer<Bomber> {

    public RenderBomber(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(Bomber bomber, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        RenderContext.setup(NtmRenderTypes.FVBO_NC.apply(this.getTextureLocation(bomber)), poseStack, packedLight, OverlayTexture.NO_OVERLAY);
        RenderContext.mulPose(Axis.YP.rotationDegrees(bomber.yRotO + (bomber.getYRot() - bomber.yRotO) * partialTicks - 90.0F));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(90F));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(bomber.xRotO + (bomber.getXRot() - bomber.xRotO) * partialTicks));

        RenderContext.mulPose(Axis.XP.rotationDegrees((float) (Math.sin((bomber.tickCount + partialTicks) * 0.05F) * 10)));

        int i = bomber.getBomberStyle();

        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                RenderContext.scale(5F, 5F, 5F);
                RenderContext.mulPose(Axis.YN.rotationDegrees(90F));
                ResourceManager.dornier.renderAll();
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                RenderContext.scale(30F / 3.1F, 30F / 3.1F, 30F / 3.1F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                ResourceManager.b29.renderAll();
                break;
            default:
                ResourceManager.dornier.renderAll();
                break;
        }

        RenderContext.end();
    }

    @Override
    public ResourceLocation getTextureLocation(Bomber bomber) {
        int i = bomber.getBomberStyle();
        return switch (i) {
            case 0 -> ResourceManager.DORNIER_1_TEX;
            case 1 -> ResourceManager.DORNIER_1_TEX;
            case 2 -> ResourceManager.DORNIER_2_TEX;
            case 3 -> ResourceManager.DORNIER_1_TEX;
            case 4 -> ResourceManager.DORNIER_4_TEX;
            case 5 -> ResourceManager.B29_0_TEX;
            case 6 -> ResourceManager.B29_1_TEX;
            case 7 -> ResourceManager.B29_2_TEX;
            case 8 -> ResourceManager.B29_3_TEX;
            default -> ResourceManager.DORNIER_1_TEX;
        };
    }
}
