package com.hbm.render.entity.effect;

import com.hbm.HBMsNTM;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

public class RenderQuasar<T extends Entity> extends RenderBlackHole<T> {
    protected static final ResourceLocation QUASAR = HBMsNTM.withDefaultNamespaceNT("textures/entity/bholed.png");

    public RenderQuasar(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        float size = getEntitySize(entity);

        poseStack.scale(size, size, size);

        Matrix4f matrix = poseStack.last().pose();

        renderSphere(matrix, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        renderDisc(entity, partialTicks, poseStack);
        renderJets(entity, poseStack);

        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    @Override
    protected int[] getColorFromIteration(int iteration, float alpha) {
        float r = 1.0F;
        float g = (float) Math.pow(iteration / 15F, 2);
        float b = (float) Math.pow(iteration / 15F, 2);

        return new int[]{(int)(r * 255), (int)(g * 255), (int)(b * 255), (int) (alpha * 255)};
    }

    @Override
    protected ResourceLocation discTex() {
        return QUASAR;
    }

    @Override
    protected int steps() {
        return 15;
    }
}
