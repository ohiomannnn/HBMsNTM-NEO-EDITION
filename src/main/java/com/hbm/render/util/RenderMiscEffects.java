package com.hbm.render.util;

import com.hbm.main.NuclearTechMod;
import com.hbm.render.CustomRenderTypes;
import com.hbm.render.loader.IModelCustomOld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class RenderMiscEffects {

    public static final ResourceLocation GLINT = NuclearTechMod.withDefaultNamespace("textures/misc/glint.png");
    public static final ResourceLocation GLINT_BF = NuclearTechMod.withDefaultNamespace("textures/misc/glint_bf.png");

    public static void renderClassicGlint(MultiBufferSource buffer, PoseStack poseStack, int packedLight, int packedOverlay, float partialTicks, ResourceLocation texture, IModelCustomOld model, String part, float colorMod, float r, float g, float b, float speed, float scale) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        float offset = player.tickCount + partialTicks;

        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.GLINT_NT.apply(texture));

        for (int k = 0; k < 2; ++k) {

            float angle = 30.0F - (float) k * 60.0F;
            float movement = offset * (0.001F + (float) k * 0.003F) * speed;

            VertexConsumer glintConsumer = new SecondOffsetVC(consumer, scale, angle, 0.0F, movement);

            float glintColor = 0.76F;

            if ("all".equals(part)) model.renderAll(poseStack, glintConsumer, packedLight, packedOverlay, r * glintColor, g * glintColor, b * glintColor, 1.0F);
            else model.renderPart(part, poseStack, glintConsumer, packedLight, packedOverlay, r * glintColor, g * glintColor, b * glintColor, 1.0F);
        }
    }

    public static void renderClassicGlint(MultiBufferSource buffer, PoseStack poseStack, int packedLight, int packedOverlay, float partialTicks, ResourceLocation texture, IModelCustomOld model, String part, float r, float g, float b, float speed, float scale) {
        renderClassicGlint(buffer, poseStack, packedLight, packedOverlay, partialTicks, texture, model, part, 0.5F, r, g, b, speed, scale);
    }

    public static void renderClassicGlint(MultiBufferSource buffer, PoseStack poseStack, int packedLight, int packedOverlay, float partialTicks, ResourceLocation texture, IModelCustomOld model, String part) {
        renderClassicGlint(buffer, poseStack, packedLight, packedOverlay, partialTicks, texture, model, part, 0.5F, 0.25F, 0.8F, 20.0F, 1F / 3F);
    }
}
