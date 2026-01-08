package com.hbm.render.util;

import com.hbm.HBMsNTM;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GaugeUtil {

    public enum Gauge {
        ROUND_SMALL(HBMsNTM.withDefaultNamespaceNT("textures/gui/gauges/small_round.png"), 18, 18, 13),
        ROUND_LARGE(HBMsNTM.withDefaultNamespaceNT("textures/gui/gauges/large_round.png"), 36, 36, 13),
        BOW_SMALL(HBMsNTM.withDefaultNamespaceNT("textures/gui/gauges/small_bow.png"), 18, 18, 13),
        BOW_LARGE(HBMsNTM.withDefaultNamespaceNT("textures/gui/gauges/large_bow.png"), 36, 36, 13),
        WIDE_SMALL(HBMsNTM.withDefaultNamespaceNT("textures/gui/gauges/small_wide.png"), 18, 12, 7),
        WIDE_LARGE(HBMsNTM.withDefaultNamespaceNT("textures/gui/gauges/large_wide.png"), 36, 24, 11),
        BAR_SMALL(HBMsNTM.withDefaultNamespaceNT("textures/gui/gauges/small_bar.png"), 36, 12, 16);

        ResourceLocation texture;
        int width;
        int height;
        int count;

        Gauge(ResourceLocation texture, int width, int height, int count) {
            this.texture = texture;
            this.width = width;
            this.height = height;
            this.count = count;
        }
    }

    /**
     *
     * @param gauge The gauge enum to use
     * @param x The x coord in the GUI (left)
     * @param y The y coord in the GUI (top)
     * @param z The z-level (from GUI.zLevel)
     * @param progress Double from 0-1 how far the gauge has progressed
     */
    public static void renderGauge(Gauge gauge, float x, float y, float z, double progress) {
        int frameNum = (int) Math.round((gauge.count - 1) * progress);
        float singleFrame = (float) (1D / gauge.count);
        float frameOffset = singleFrame * frameNum;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.addVertex(x, 				    y + gauge.height, 	z).setUv(0, 	frameOffset + singleFrame);
        buf.addVertex(x + gauge.width, 	y + gauge.height, 	z).setUv(1, 	frameOffset + singleFrame);
        buf.addVertex(x + gauge.width, 	   y, 				    z).setUv(1, 	frameOffset);
        buf.addVertex(x, 				       y, 					z).setUv(0, 	frameOffset);
        BufferUploader.drawWithShader(buf.buildOrThrow());
    }

    public static void drawSmoothGauge(int x, int y, float z, double progress, double tipLength, double backLength, double backSide, int color) {
        drawSmoothGauge(x, y, z, progress, tipLength, backLength, backSide, color, 0x000000);
    }

    public static void drawSmoothGauge(int x, int y, float z, double progress, double tipLength, double backLength, double backSide, int color, int colorOuter) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        progress = Mth.clamp(progress, 0.0, 1.0);

        float angle = (float) Math.toRadians(-progress * 270 - 45);

        Vec3NT tip = new Vec3NT(0, (float) tipLength, 0);
        Vec3NT left = new Vec3NT((float) backSide, (float) -backLength, 0);
        Vec3NT right = new Vec3NT((float) -backSide, (float) -backLength, 0);

        tip.rotateAroundZDeg(angle);
        left.rotateAroundZDeg(angle);
        right.rotateAroundZDeg(angle);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        double mult = 1.5;

        // Outer
        buffer.addVertex((float)(x + tip.xCoord * mult), (float)(y + tip.yCoord * mult), z)
                .setColor(colorOuter);
        buffer.addVertex((float)(x + left.xCoord * mult), (float)(y + left.yCoord * mult), z)
                .setColor(colorOuter);
        buffer.addVertex((float)(x + right.xCoord * mult), (float)(y + right.yCoord * mult), z)
                .setColor(colorOuter);

        // Inner
        buffer.addVertex((float)(x + tip.xCoord), (float)(y + tip.yCoord), z)
                .setColor(color);
        buffer.addVertex((float)(x + left.xCoord), (float)(y + left.yCoord), z)
                .setColor(color);
        buffer.addVertex((float)(x + right.xCoord), (float)(y + right.yCoord), z)
                .setColor(color);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.disableBlend();
    }
}
