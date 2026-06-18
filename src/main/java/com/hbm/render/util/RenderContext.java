package com.hbm.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;
import org.joml.Vector4f;

/** I need more context */
public class RenderContext {

    public static final ThreadLocal<RenderContext> INSTANCE = ThreadLocal.withInitial(RenderContext::new);

    public PoseStack poseStack = null;
    public int packedLight = LightTexture.FULL_BRIGHT;
    public int packedOverlay = OverlayTexture.NO_OVERLAY;
    public Vector4f color = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
    /** Same as GL11.GL_LIGHTING */
    public boolean lightning = true;

    public static void setLight(int packedLight) { INSTANCE.get().packedLight = packedLight; }
    public static void setOverlay(int packedOverlay) { INSTANCE.get().packedOverlay = packedOverlay; }

    public static void setLightning(boolean light) { INSTANCE.get().lightning = light; }

    public static void setColor(float r, float g, float b, float a) { INSTANCE.get().color = new Vector4f(r, g, b, a); }

    public static void setup(PoseStack poseStack, int packedLight, int packedOverlay) {
        RenderContext context = INSTANCE.get();

        context.packedLight = packedLight;
        context.packedOverlay = packedOverlay;

        context.poseStack = poseStack;
        context.poseStack.pushPose();
    }

    public static void end() {
        RenderContext context = INSTANCE.get();

        context.packedLight = LightTexture.FULL_BRIGHT;
        context.packedOverlay = OverlayTexture.NO_OVERLAY;

        context.color = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);

        context.poseStack.popPose();
    }

    public static PoseStack poseStack() {
        return INSTANCE.get().poseStack;
    }

    public static int light() { return INSTANCE.get().packedLight; }
    public static int overlay() { return INSTANCE.get().packedOverlay; }

    public static void pushPose() { INSTANCE.get().poseStack.pushPose(); }
    public static void popPose() { INSTANCE.get().poseStack.popPose(); }
    public static void translate(float x, float y, float z) { INSTANCE.get().poseStack.translate(x, y, z); }
    public static void scale(float x, float y, float z) { INSTANCE.get().poseStack.scale(x, y, z); }
    public static void mulPose(Quaternionf quaternionf) { INSTANCE.get().poseStack.mulPose(quaternionf); }
}