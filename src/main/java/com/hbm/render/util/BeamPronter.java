package com.hbm.render.util;

import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.Random;

public class BeamPronter {

    public static Random rand = new Random();

    public enum WaveType {
        RANDOM, SPIRAL
    }

    public enum BeamType {
        SOLID, LINE
    }

    private static boolean depthMask = false;

    public static void prontBeamWithDepth(PoseStack poseStack, Vec3NT skeleton, WaveType wave, BeamType beam, int outerColor, int innerColor, int start, int segments, float size, int layers, float thickness) {
        depthMask = true;
        prontBeam(poseStack, skeleton, wave, beam, outerColor, innerColor, start, segments, size, layers, thickness);
        depthMask = false;
    }

    public static void prontBeam(PoseStack poseStack, Vec3NT skeleton, WaveType wave, BeamType beam, int outerColor, int innerColor, int start, int segments, float size, int layers, float thickness) {

        poseStack.pushPose();
        RenderSystem.depthMask(depthMask);

        float sYaw = (float) (Math.atan2(skeleton.xCoord, skeleton.zCoord) * 180F / Math.PI);
        float sqrt = Mth.sqrt((float)(skeleton.xCoord * skeleton.xCoord + skeleton.zCoord * skeleton.zCoord));
        float sPitch = (float) (Math.atan2(skeleton.yCoord, sqrt) * 180F / Math.PI);

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(sYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(sPitch - 90));

        poseStack.pushPose();

        if (beam == BeamType.SOLID) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Matrix4f matrix = poseStack.last().pose();

        Vec3NT unit = new Vec3NT(0, 1, 0);
        rand.setSeed(start);
        double length = unit.length();
        double segLength = length / segments;
        double lastX = 0;
        double lastY = 0;
        double lastZ = 0;

        for (int i = 0; i <= segments; i++) {

            Vec3NT spinner = new Vec3NT(size, 0, 0);

            if (wave == WaveType.SPIRAL) {
                spinner.rotateAroundYRad(Math.PI * start / 180.0);
                spinner.rotateAroundYRad(Math.PI * 45.0 / 180.0 * i);
            } else if (wave == WaveType.RANDOM) {
                spinner.rotateAroundYRad(Math.PI * 2 * rand.nextFloat());
                spinner.rotateAroundYRad(Math.PI * 2 * rand.nextFloat());
            }

            double pX = unit.xCoord * segLength * i + spinner.xCoord;
            double pY = unit.yCoord * segLength * i + spinner.yCoord;
            double pZ = unit.zCoord * segLength * i + spinner.zCoord;

            if (beam == BeamType.LINE && i > 0) {
                drawLine(matrix, outerColor, (float)pX, (float)pY, (float)pZ, (float)lastX, (float)lastY, (float)lastZ);
            }

            if (beam == BeamType.SOLID && i > 0) {
                float radius = thickness / layers;

                for(int j = 1; j <= layers; j++) {
                    float inter = (layers > 1) ? (float)(j - 1) / (float)(layers - 1) : 0f;

                    int r1 = (outerColor >> 16) & 0xFF;
                    int g1 = (outerColor >> 8) & 0xFF;
                    int b1 = outerColor & 0xFF;

                    int r2 = (innerColor >> 16) & 0xFF;
                    int g2 = (innerColor >> 8) & 0xFF;
                    int b2 = innerColor & 0xFF;

                    int r = (int)(r1 + (r2 - r1) * inter);
                    int g = (int)(g1 + (g2 - g1) * inter);
                    int b = (int)(b1 + (b2 - b1) * inter);
                    int color = (r << 16) | (g << 8) | b;

                    float rj = radius * j;

                    drawQuad(matrix, color, (float)(lastX + rj), (float)lastY, (float)(lastZ + rj), (float)(lastX + rj), (float)lastY, (float)(lastZ - rj), (float)(pX + rj), (float)pY, (float)(pZ - rj), (float)(pX + rj), (float)pY, (float)(pZ + rj));
                    drawQuad(matrix, color, (float)(lastX - rj), (float)lastY, (float)(lastZ + rj), (float)(lastX - rj), (float)lastY, (float)(lastZ - rj), (float)(pX - rj), (float)pY, (float)(pZ - rj), (float)(pX - rj), (float)pY, (float)(pZ + rj));
                    drawQuad(matrix, color, (float)(lastX + rj), (float)lastY, (float)(lastZ + rj), (float)(lastX - rj), (float)lastY, (float)(lastZ + rj), (float)(pX - rj), (float)pY, (float)(pZ + rj), (float)(pX + rj), (float)pY, (float)(pZ + rj));
                    drawQuad(matrix, color, (float)(lastX + rj), (float)lastY, (float)(lastZ - rj), (float)(lastX - rj), (float)lastY, (float)(lastZ - rj), (float)(pX - rj), (float)pY, (float)(pZ - rj), (float)(pX + rj), (float)pY, (float)(pZ - rj));
                }
            }

            lastX = pX;
            lastY = pY;
            lastZ = pZ;
        }

        if (beam == BeamType.LINE) {
            drawLine(matrix, innerColor, 0, 0, 0, 0, (float)length, 0);
        }

        if (beam == BeamType.SOLID) {
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
        }

        poseStack.popPose();
        RenderSystem.depthMask(true);

        poseStack.popPose();
    }

    private static void drawLine(Matrix4f matrix, int color, float x1, float y1, float z1, float x2, float y2, float z2) {

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        buffer.addVertex(matrix, x1, y1, z1).setColor(color);
        buffer.addVertex(matrix, x2, y2, z2).setColor(color);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private static void drawQuad(Matrix4f matrix, int color, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        buffer.addVertex(matrix, x1, y1, z1).setColor(color);
        buffer.addVertex(matrix, x2, y2, z2).setColor(color);
        buffer.addVertex(matrix, x3, y3, z3).setColor(color);
        buffer.addVertex(matrix, x4, y4, z4).setColor(color);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }
}