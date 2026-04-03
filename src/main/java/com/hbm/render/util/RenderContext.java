package com.hbm.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;

/** I need more context */
public class RenderContext {

    private static final ThreadLocal<RenderContext> INSTANCE = ThreadLocal.withInitial(RenderContext::new);

    private RenderType renderType;
    private VertexConsumer consumer;
    private PoseStack poseStack;
    private int packedLight = LightTexture.FULL_BRIGHT;
    private int packedOverlay = OverlayTexture.NO_OVERLAY;
    private float r = 1.0F;
    private float g = 1.0F;
    private float b = 1.0F;
    private float a = 1.0F;

    private RenderContext() { }

    public static void setLight(int packedLight) {
        INSTANCE.get().packedLight = packedLight;
    }

    public static void setOverlay(int packedOverlay) {
        INSTANCE.get().packedOverlay = packedOverlay;
    }

    public static void setColor(float r, float g, float b, float a) {
        RenderContext context = INSTANCE.get();
        context.r = r;
        context.g = g;
        context.b = b;
        context.a = a;
    }

    public static void setColor(int color) {
        float a = ((color >> 24) & 0xFF) / 255.0F;
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        setColor(r, g, b, a);
    }

    public static void setup(PoseStack poseStack, int packedLight, int packedOverlay) {
        RenderContext context = INSTANCE.get();
        context.poseStack = poseStack;
        context.packedLight = packedLight;
        context.packedOverlay = packedOverlay;
        context.poseStack.pushPose();
    }

    public static void setup(RenderType renderType, PoseStack poseStack, int packedLight, int packedOverlay) {
        RenderContext context = INSTANCE.get();
        context.renderType = renderType;
        context.poseStack = poseStack;
        context.packedLight = packedLight;
        context.packedOverlay = packedOverlay;
        context.poseStack.pushPose();
    }

    public static void setup(VertexConsumer consumer, PoseStack poseStack, int packedLight, int packedOverlay) {
        RenderContext context = INSTANCE.get();
        context.consumer = consumer;
        context.poseStack = poseStack;
        context.packedLight = packedLight;
        context.packedOverlay = packedOverlay;
        context.poseStack.pushPose();
    }

    public static void end() {
        RenderContext context = INSTANCE.get();
        context.consumer = null;
        context.renderType = null;
        context.packedLight = LightTexture.FULL_BRIGHT;
        context.packedOverlay = OverlayTexture.NO_OVERLAY;
        context.r = context.g = context.b = context.a = 1.0F;
        context.poseStack.popPose();
    }

    public static VertexConsumer consumer() {
        return INSTANCE.get().consumer;
    }

    public static RenderType renderType() {
        return INSTANCE.get().renderType;
    }

    public static PoseStack poseStack() {
        return INSTANCE.get().poseStack;
    }

    public static PoseStack.Pose pose() {
        return INSTANCE.get().poseStack.last();
    }

    public static int light() {
        return INSTANCE.get().packedLight;
    }

    public static int overlay() {
        return INSTANCE.get().packedOverlay;
    }

    public static float r() { return INSTANCE.get().r; }
    public static float g() { return INSTANCE.get().g; }
    public static float b() { return INSTANCE.get().b; }
    public static float a() { return INSTANCE.get().a; }

    public static void pushPose() {
        INSTANCE.get().poseStack.pushPose();
    }

    public static void popPose() {
        INSTANCE.get().poseStack.popPose();
    }

    public static void translate(double x, double y, double z) {
        INSTANCE.get().poseStack.translate(x, y, z);
    }

    public static void scale(float x, float y, float z) {
        INSTANCE.get().poseStack.scale(x, y, z);
    }

    public static void mulPose(Quaternionf quaternionf) {
        INSTANCE.get().poseStack.mulPose(quaternionf);
    }

    public static void rotateX(float degrees) {
        INSTANCE.get().poseStack.mulPose(Axis.XP.rotationDegrees(degrees));
    }

    public static void rotateY(float degrees) {
        INSTANCE.get().poseStack.mulPose(Axis.YP.rotationDegrees(degrees));
    }

    public static void rotateZ(float degrees) {
        INSTANCE.get().poseStack.mulPose(Axis.ZP.rotationDegrees(degrees));
    }

    public static void setRenderType(RenderType type) {
        INSTANCE.get().renderType = type;
    }
}