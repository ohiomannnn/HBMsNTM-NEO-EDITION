package com.hbm.items.weapon.sedna.factory;

import com.hbm.entity.projectile.BulletBaseMK4;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

public class LegoClient {

    private static final RenderType BULLET = RenderType.create(
            "bullet_render_type", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 1024,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_LIGHTMAP_SHADER)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(true)
    );

    public static BiConsumer<BulletBaseMK4, Float> RENDER_STANDARD_BULLET = (bullet, partialTick) -> {
        float length = Mth.lerp(partialTick, bullet.prevVelocity, bullet.velocity);
        if(length <= 0) return;
        renderBulletStandard(0xFFFFBF00, 0xFFFFFFFF, length, false);
    };

    public static void renderBulletStandard(int dark, int light, float length, boolean fullbright) {
        renderBulletStandard(dark, light, length, 0.03125F, 0.03125F * 0.25F, fullbright);
    }
    public static void renderBulletStandard(int dark, int light, float length, float widthF, float widthB, boolean fullbright) {

        Matrix4f matrix = RenderContext.poseStack().last().pose();
        int packedLight = fullbright ? 240 : RenderContext.light();

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = buffer.getBuffer(BULLET);

        consumer.addVertex(matrix, length, widthB, -widthB).setColor(dark).setLight(packedLight);
        consumer.addVertex(matrix, length, widthB, widthB).setColor(dark).setLight(packedLight);

        consumer.addVertex(matrix, 0, widthF, widthF).setColor(light).setLight(packedLight);
        consumer.addVertex(matrix, 0, widthF, -widthF).setColor(light).setLight(packedLight);

        consumer.addVertex(matrix, length, -widthB, -widthB).setColor(dark).setLight(packedLight);
        consumer.addVertex(matrix, length, -widthB, widthB).setColor(dark).setLight(packedLight);

        consumer.addVertex(matrix, 0, -widthF, widthF).setColor(light).setLight(packedLight);
        consumer.addVertex(matrix, 0, -widthF, -widthF).setColor(light).setLight(packedLight);

        consumer.addVertex(matrix, length, -widthB, widthB).setColor(dark).setLight(packedLight);
        consumer.addVertex(matrix, length, widthB, widthB).setColor(dark).setLight(packedLight);

        consumer.addVertex(matrix, 0, widthF, widthF).setColor(light).setLight(packedLight);
        consumer.addVertex(matrix, 0, -widthF, widthF).setColor(light).setLight(packedLight);

        consumer.addVertex(matrix, length, -widthB, -widthB).setColor(dark).setLight(packedLight);
        consumer.addVertex(matrix, length, widthB, -widthB).setColor(dark).setLight(packedLight);

        consumer.addVertex(matrix, 0, widthF, -widthF).setColor(light).setLight(packedLight);
        consumer.addVertex(matrix, 0, -widthF, -widthF).setColor(light).setLight(packedLight);

        consumer.addVertex(matrix, length, widthB, widthB).setColor(dark).setLight(packedLight);
        consumer.addVertex(matrix, length, widthB, -widthB).setColor(dark).setLight(packedLight);
        consumer.addVertex(matrix, length, -widthB, -widthB).setColor(dark).setLight(packedLight);
        consumer.addVertex(matrix, length, -widthB, widthB).setColor(dark).setLight(packedLight);

        consumer.addVertex(matrix, 0, widthF, widthF).setColor(light).setLight(packedLight);
        consumer.addVertex(matrix, 0, widthF, -widthF).setColor(light).setLight(packedLight);
        consumer.addVertex(matrix, 0, -widthF, -widthF).setColor(light).setLight(packedLight);
        consumer.addVertex(matrix, 0, -widthF, widthF).setColor(light).setLight(packedLight);

        buffer.endBatch();
    }
}
