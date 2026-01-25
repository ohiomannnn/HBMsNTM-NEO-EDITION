package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface IModelCustom {
    void renderAll(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay);
    void renderPart(String partName, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay);
    void renderOnly(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, String... groupNames);
    void renderAllExcept(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, String... excludedGroupNames);
}
