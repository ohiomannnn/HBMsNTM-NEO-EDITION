package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.awt.*;

public interface IModelCustomOld {
    void renderAll(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay);
    default void renderAll(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {}
    void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay);
    default void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {}
    default void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, Color color) { this.renderPart(partName, poseStack, consumer, packedLight, packedOverlay, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0F); }
    void renderOnly(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, String... groupNames);
    void renderAllExcept(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, String... excludedGroupNames);
}
