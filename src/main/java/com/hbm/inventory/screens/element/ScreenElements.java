package com.hbm.inventory.screens.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class ScreenElements {

    public static void drawSmoothGauge(int x, int y, float progress, float tipLength, float backLength, float backSide, int color) {
        drawSmoothGauge(x, y, progress, tipLength, backLength, backSide, color, 0xFF000000);
    }

    private static final Vector3f tip = new Vector3f();
    private static final Vector3f left = new Vector3f();
    private static final Vector3f right = new Vector3f();

    public static void drawSmoothGauge(int x, int y, float progress, float tipLength, float backLength, float backSide, int color, int colorOuter) {

        progress = Mth.clamp(progress, 0, 1);

        // -progress * 270 - 45 became this because we are using Vector3f
        float angle = (float) Math.toRadians(progress * 270 + 45);

        tip.set(0, tipLength, 0);
        left.set(backSide, -backLength, 0);
        right.set(-backSide, -backLength, 0);

        tip.rotateZ(angle);
        left.rotateZ(angle);
        right.rotateZ(angle);

        float mult = 1.5F;

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        buffer.addVertex(x + tip.x * mult, y + tip.y * mult, 1F).setColor(colorOuter);
        buffer.addVertex(x + left.x * mult, y + left.y * mult, 1F).setColor(colorOuter);
        buffer.addVertex(x + right.x * mult, y + right.y * mult, 1F).setColor(colorOuter);
        buffer.addVertex(x + tip.x, y + tip.y, 1F).setColor(color);
        buffer.addVertex(x + left.x, y + left.y, 1F).setColor(color);
        buffer.addVertex(x + right.x, y + right.y, 1F).setColor(color);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }
}
