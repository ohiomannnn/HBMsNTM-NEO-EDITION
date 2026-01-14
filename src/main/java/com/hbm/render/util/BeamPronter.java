package com.hbm.render.util;

import com.hbm.render.CustomRenderTypes;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

public class BeamPronter {

    public static RandomSource rand = RandomSource.create();

    public enum WaveType {
        RANDOM, SPIRAL
    }

    public enum BeamType {
        SOLID, LINE
    }

    public static void prontBeam(PoseStack poseStack, MultiBufferSource buffer, Vec3NT skeleton, WaveType wave, BeamType beam, int outerColor, int innerColor, int start, int segments, float size, int layers, float thickness) {

        poseStack.pushPose();

        float sYRot = (float) (Math.atan2(skeleton.xCoord, skeleton.zCoord) * 180F / Math.PI);
        float sqrt = (float) Math.sqrt(skeleton.xCoord * skeleton.xCoord + skeleton.zCoord * skeleton.zCoord);
        float sXRot = (float) (Math.atan2(skeleton.yCoord, sqrt) * 180F / Math.PI);

        poseStack.mulPose(Axis.YP.rotationDegrees(180F));
        poseStack.mulPose(Axis.YP.rotationDegrees(sYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(sXRot - 90));

        VertexConsumer consumer;
        if (beam == BeamType.SOLID) {
            consumer = buffer.getBuffer(CustomRenderTypes.GLOW);
        } else {
            consumer = buffer.getBuffer(RenderType.lines());
        }

        poseStack.pushPose();
        Matrix4f matrix = poseStack.last().pose();

        Vec3NT unit = new Vec3NT(0, 1, 0);
        rand.setSeed(start);
        float length = (float) skeleton.length();
        float segLength = length / segments;
        float lastX = 0;
        float lastY = 0;
        float lastZ = 0;

        for (int i = 0; i <= segments; i++) {

            Vec3NT spinner = new Vec3NT(size, 0, 0);

            if (wave == WaveType.SPIRAL) {
                spinner.rotateAroundYRad(Math.PI * start / 180.0);
                spinner.rotateAroundYRad(Math.PI * 45.0 / 180.0 * i);
            } else if (wave == WaveType.RANDOM) {
                spinner.rotateAroundYRad(Math.PI * 2 * rand.nextFloat());
                spinner.rotateAroundYRad(Math.PI * 2 * rand.nextFloat());
            }

            float pX = (float) (unit.xCoord * segLength * i + spinner.xCoord);
            float pY = (float) (unit.yCoord * segLength * i + spinner.yCoord);
            float pZ = (float) (unit.zCoord * segLength * i + spinner.zCoord);

            if (beam == BeamType.LINE && i > 0) {
                consumer.addVertex(matrix, pX, pY, pZ).setColor(outerColor).setNormal(0, 1, 0);
                consumer.addVertex(matrix, lastX, lastY, lastZ).setColor(outerColor).setNormal(0, 1, 0);
            }

            if (beam == BeamType.SOLID && i > 0) {

                float radius = thickness / layers;

                for (int j = 1; j <= layers; j++) {

                    float inter = (float) (j - 1) / (float) (layers - 1);

                    int r1 = ARGB32.red(outerColor);
                    int g1 = ARGB32.green(outerColor);
                    int b1 = ARGB32.blue(outerColor);

                    int r2 = ARGB32.red(innerColor);
                    int g2 = ARGB32.green(innerColor);
                    int b2 = ARGB32.blue(innerColor);

                    int r = ((int)(r1 + (r2 - r1) * inter)) << 16;
                    int g = ((int)(g1 + (g2 - g1) * inter)) << 8;
                    int b = ((int)(b1 + (b2 - b1) * inter));

                    int color = 0xFF000000 | r | g | b;

                    consumer.addVertex(matrix, (lastX + (radius * j)), lastY, (lastZ + (radius * j))).setColor(color);
                    consumer.addVertex(matrix, (lastX + (radius * j)), lastY, (lastZ - (radius * j))).setColor(color);
                    consumer.addVertex(matrix, (pX + (radius * j)), pY, (pZ - (radius * j))).setColor(color);
                    consumer.addVertex(matrix, (pX + (radius * j)), pY, (pZ + (radius * j))).setColor(color);

                    consumer.addVertex(matrix, lastX - (radius * j), lastY, lastZ + (radius * j)).setColor(color);
                    consumer.addVertex(matrix, lastX - (radius * j), lastY, lastZ - (radius * j)).setColor(color);
                    consumer.addVertex(matrix, pX - (radius * j), pY, pZ - (radius * j)).setColor(color);
                    consumer.addVertex(matrix, pX - (radius * j), pY, pZ + (radius * j)).setColor(color);

                    consumer.addVertex(matrix, lastX + (radius * j), lastY, lastZ + (radius * j)).setColor(color);
                    consumer.addVertex(matrix, lastX - (radius * j), lastY, lastZ + (radius * j)).setColor(color);
                    consumer.addVertex(matrix, pX - (radius * j), pY, pZ + (radius * j)).setColor(color);
                    consumer.addVertex(matrix, pX + (radius * j), pY, pZ + (radius * j)).setColor(color);

                    consumer.addVertex(matrix, lastX + (radius * j), lastY, lastZ - (radius * j)).setColor(color);
                    consumer.addVertex(matrix, lastX - (radius * j), lastY, lastZ - (radius * j)).setColor(color);
                    consumer.addVertex(matrix, pX - (radius * j), pY, pZ - (radius * j)).setColor(color);
                    consumer.addVertex(matrix, pX + (radius * j), pY, pZ - (radius * j)).setColor(color);
                }
            }

            lastX = pX;
            lastY = pY;
            lastZ = pZ;
        }

        if (beam == BeamType.LINE) {
            consumer.addVertex(matrix, 0, 0, 0).setColor(innerColor).setNormal(0, 1, 0);
            consumer.addVertex(matrix, 0, (float) skeleton.length(), 0).setColor(innerColor).setNormal(0, 1, 0);
        }

        poseStack.popPose();

        poseStack.popPose();
    }
}