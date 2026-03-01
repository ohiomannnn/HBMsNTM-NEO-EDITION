package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface IModelCustom {
    void renderAll(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay);
    default void renderAll(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {}
    void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay);
    default void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {}
    void renderOnly(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, String... groupNames);
    void renderAllExcept(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, String... excludedGroupNames);
}
