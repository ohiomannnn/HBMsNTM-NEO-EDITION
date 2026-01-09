package com.hbm.render.entity.effect;

import com.hbm.entity.logic.Bomber;
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
public class RenderBomber extends EntityRenderer<Bomber> {

    public RenderBomber(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(Bomber bomber, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(bomber.yRotO + (bomber.getYRot() - bomber.yRotO) * partialTicks - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(bomber.xRotO + (bomber.getXRot() - bomber.xRotO) * partialTicks));

        VertexConsumer consumer;
        int i = bomber.getBomberStyle();

        consumer = switch (i) {
            case 0 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DORNIER_1_TEX));
            case 1 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DORNIER_1_TEX));
            case 2 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DORNIER_2_TEX));
            case 3 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DORNIER_1_TEX));
            case 4 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DORNIER_4_TEX));
            case 5 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.B29_0_TEX));
            case 6 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.B29_1_TEX));
            case 7 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.B29_2_TEX));
            case 8 -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.B29_3_TEX));
            default -> buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DORNIER_1_TEX));
        };

        poseStack.mulPose(Axis.XP.rotationDegrees((float) (Math.sin((bomber.tickCount + partialTicks) * 0.05F) * 10)));

        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                poseStack.scale(5F, 5F, 5F);
                poseStack.mulPose(Axis.YN.rotationDegrees(90F));
                ResourceManager.dornier.renderAll(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                poseStack.scale(30F / 3.1F, 30F / 3.1F, 30F / 3.1F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                ResourceManager.b29.renderAll(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
                break;
            default:
                ResourceManager.dornier.renderAll(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
                break;
        }

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Bomber bomber) {
        return ResourceManager.DORNIER_1_TEX;
    }
}
