package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;

public class Test {
    private final VertexBuffer vertexBuffer;

    public Test() {
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.build(Tesselator.getInstance());
    }

    private void build(Tesselator tess) {
        BufferBuilder builder = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);

        builder.addVertex(0, 0, 0)
                .setColor(255, 255, 255, 255)
                .setUv(0f, 0f)
                .setLight(240)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0, 1, 0);

        builder.addVertex(1, 0, 0)
                .setColor(255, 255, 255, 255)
                .setUv(1f, 0f)
                .setLight(240)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0, 1, 0);

        builder.addVertex(1, 0, 1)
                .setColor(255, 255, 255, 255)
                .setUv(1f, 1f)
                .setLight(240)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0, 1, 0);

        builder.addVertex(0, 0, 1)
                .setColor(255, 255, 255, 255)
                .setUv(0f, 1f)
                .setLight(240)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0, 1, 0);

        MeshData meshData = builder.buildOrThrow();

        vertexBuffer.bind();
        vertexBuffer.upload(meshData);
        VertexBuffer.unbind();
    }

    public void render(PoseStack poseStack, Matrix4f projectionMatrix, ShaderInstance shaderInstance) {
        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();

        vertexBuffer.bind();
        vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
        VertexBuffer.unbind();

        Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
    }
}
