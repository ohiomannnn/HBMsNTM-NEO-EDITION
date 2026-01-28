package com.hbm.render.util;

import com.hbm.HBMsNTM;
import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class DiamondPronter {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/models/misc/danger_diamond.png");

    public static void pront(MultiBufferSource buffer, PoseStack poseStack, int brightness, int poison, int flammability, int reactivity, EnumSymbol symbol) {

        poseStack.pushPose();

        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.entitySmoth(TEXTURE));

        float p = 1F/256F;
        float s = 1F/139F;

        Matrix4f matrix = poseStack.last().pose();

        consumer.addVertex(matrix, 0.0F, 0.5F, -0.5F)
                .setColor(1F, 1F, 1F, 1F)
                .setUv(p * 144, p * 45)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix, 0.0F, 0.5F, 0.5F)
                .setColor(1F, 1F, 1F, 1F)
                .setUv(p * 5, p * 45)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix, 0.0F, -0.5F, 0.5F)
                .setColor(1F, 1F, 1F, 1F)
                .setUv(p * 5, p * 184)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix, 0.0F, -0.5F, -0.5F)
                .setColor(1F, 1F, 1F, 1F)
                .setUv(p * 144, p * 184)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);

        float width = 10F * s;
        float height = 14F * s;

        if (poison >= 0 && poison < 6) {

            float oY = 0;
            float oZ = 33 * s;

            int x = 5 + (poison - 1) * 24;
            int y = 5;

            if (poison == 0) x = 125;

            consumer.addVertex(matrix, 0.01F, height + oY, -width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 20) * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, height + oY, width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -height + oY, width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, (y + 28) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -height + oY, -width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 20) * p, (y + 28) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
        }

        if (flammability >= 0 && flammability < 6) {

            float oY = 33 * s;
            float oZ = 0;

            int x = 5 + (flammability - 1) * 24;
            int y = 5;

            if (flammability == 0) x = 125;

            consumer.addVertex(matrix, 0.01F, height + oY, -width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 20) * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, height + oY, width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -height + oY, width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, (y + 28) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -height + oY, -width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 20) * p, (y + 28) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
        }

        if (reactivity >= 0 && reactivity < 6) {

            float oY = 0;
            float oZ = -33 * s;

            int x = 5 + (reactivity - 1) * 24;
            int y = 5;

            if (reactivity == 0) x = 125;

            consumer.addVertex(matrix, 0.01F, height + oY, -width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 20) * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, height + oY, width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -height + oY, width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, (y + 28) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -height + oY, -width + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 20) * p, (y + 28) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
        }

        float symSize = 59F/2F * s;

        if (symbol != EnumSymbol.NONE) {

            float oY = -33 * s;
            float oZ = 0;

            int x = symbol.x;
            int y = symbol.y;

            consumer.addVertex(matrix, 0.01F, symSize + oY, -symSize + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 59) * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, symSize + oY, symSize + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, y * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -symSize + oY, symSize + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv(x * p, (y + 59) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(matrix, 0.01F, -symSize + oY, -symSize + oZ)
                    .setColor(1F, 1F, 1F, 1F)
                    .setUv((x + 59) * p, (y + 59) * p)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(brightness)
                    .setNormal(0.0F, 1.0F, 0.0F);
        }

        poseStack.popPose();
    }
}
