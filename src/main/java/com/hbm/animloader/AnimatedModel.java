package com.hbm.animloader;

import com.hbm.util.BobMathUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimatedModel {

    public AnimationController controller;

    public String name = "";

    public Matrix4f transform = new Matrix4f();

    public boolean hasGeometry = true;
    public boolean hasTransform = false;

    public String geo_name = "";
    public AnimatedModel parent;
    public List<AnimatedModel> children = new ArrayList<>();
    private List<BakedQuad> quads;

    public AnimatedModel() {}

    public void renderAnimated(long sysTime, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderAnimated(sysTime, poseStack, buffer, light, overlay, null);
    }

    public void renderAnimated(long sysTime, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, IAnimatedModelCallback c) {
        if (controller == null || controller.activeAnim == null || controller.activeAnim == AnimationWrapper.EMPTY) {
            render(poseStack, buffer, light, overlay, c);
            return;
        }

        AnimationWrapper activeAnim = controller.activeAnim;
        int numKeyFrames = activeAnim.anim.numKeyFrames;
        int diff = (int) (sysTime - activeAnim.startTime);
        diff *= activeAnim.speedScale;

        if (diff > activeAnim.anim.length) {
            int diff2 = diff % activeAnim.anim.length;
            switch (activeAnim.endResult.type) {
                case END -> {
                    controller.activeAnim = AnimationWrapper.EMPTY;
                    render(poseStack, buffer, light, overlay, c);
                    return;
                }
                case REPEAT -> activeAnim.startTime = sysTime - diff2;
                case REPEAT_REVERSE -> {
                    activeAnim.startTime = sysTime - diff2;
                    activeAnim.reverse = !activeAnim.reverse;
                }
                case START_NEW -> {
                    activeAnim.cloneStats(activeAnim.endResult.next);
                    activeAnim.startTime = sysTime - diff2;
                }
                case STAY -> activeAnim.startTime = sysTime - activeAnim.anim.length;
            }
        }

        diff = (int) (sysTime - activeAnim.startTime);
        if (activeAnim.reverse) diff = activeAnim.anim.length - diff;
        diff *= activeAnim.speedScale;

        float remappedTime = Mth.clamp(BobMathUtil.remap(diff, 0, activeAnim.anim.length, 0, numKeyFrames - 1), 0, numKeyFrames - 1);
        float diffN = BobMathUtil.remap01_clamp(diff, 0, activeAnim.anim.length);

        int index = (int) remappedTime;
        int first = index;
        int next = (index < numKeyFrames - 1) ? index + 1 : first;

        renderWithIndex(poseStack, buffer, light, overlay, fract(remappedTime), first, next, diffN, c);
        controller.activeAnim.prevFrame = first;
    }

    protected void renderWithIndex(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, float inter, int firstIndex, int nextIndex, float diffN, IAnimatedModelCallback c) {
        poseStack.pushPose();
        boolean hidden = false;

        if (hasTransform) {
            Transform[] transforms = controller.activeAnim.anim.objectTransforms.get(name);
            if (transforms != null &&
                    firstIndex >= 0 && nextIndex >= 0 &&
                    firstIndex < transforms.length && nextIndex < transforms.length) {
                hidden = transforms[firstIndex].hidden;
                transforms[firstIndex].interpolateAndApply(transforms[nextIndex], inter, poseStack);
            } else {
                poseStack.mulPose(transform);
            }
        }

        if (c != null) {
            hidden |= c.onRender(controller.activeAnim.prevFrame, firstIndex, -1, diffN, name);
        }

        if (hasGeometry && !hidden) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.solid());
            renderGeometry(poseStack, consumer, light, overlay);
        }

        if (c != null) {
            c.postRender(controller.activeAnim.prevFrame, firstIndex, -1, diffN, name);
        }

        for (AnimatedModel m : children) {
            m.renderWithIndex(poseStack, buffer, light, overlay, inter, firstIndex, nextIndex, diffN, c);
        }

        poseStack.popPose();
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, IAnimatedModelCallback c) {
        poseStack.pushPose();
        boolean hidden = false;

        if (hasTransform) {
            poseStack.mulPose(transform);
        }

        if (c != null) {
            hidden = c.onRender(-1, -1, -1, -1, name);
        }

        if (hasGeometry && !hidden) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.solid());
            renderGeometry(poseStack, consumer, light, overlay);
        }

        if (c != null) {
            c.postRender(-1, -1, -1, -1, name);
        }

        for (AnimatedModel m : children) {
            m.render(poseStack, buffer, light, overlay, c);
        }

        poseStack.popPose();
    }

    protected void renderGeometry(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        if (quads == null) return;
        for (var quad : quads) {
            consumer.putBulkData(poseStack.last(), quad, 1f, 1f, 1f, 1f, light, overlay);
        }
    }

    private static float fract(float number) {
        return (float) (number - Math.floor(number));
    }

    public interface IAnimatedModelCallback {
        boolean onRender(int prevFrame, int currentFrame, int model, float diffN, String modelName);
        default void postRender(int prevFrame, int currentFrame, int model, float diffN, String modelName) {}
    }
}
