package com.hbm.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public record RenderStarter(MultiBufferSource bufferSource, PoseStack poseStack, int packedLight, int packedOverlay) { }
