package com.hbm.render.util;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class SecondOffsetVC implements VertexConsumer {
    private final VertexConsumer con;

    private final float scale;
    private final float cosA, sinA;
    private final float translateX, translateY;

    public SecondOffsetVC(VertexConsumer wrapped, float scale, float rotationDegrees, float translateX, float translateY) {
        this.con = wrapped;
        this.scale = scale;
        float rad = (float) Math.toRadians(rotationDegrees);
        this.cosA = (float) Math.cos(rad);
        this.sinA = (float) Math.sin(rad);
        this.translateX = translateX;
        this.translateY = translateY;
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        con.addVertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        con.setColor(r, g, b, a);
        return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {

        float u1 = u + translateX;
        float v1 = v + translateY;
        float u2 = u1 * cosA - v1 * sinA;
        float v2 = u1 * sinA + v1 * cosA;
        float u3 = u2 * scale;
        float v3 = v2 * scale;

        con.setUv(u3, v3);
        return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        con.setUv1(u, v);
        return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        con.setUv2(u, v);
        return this;
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        con.setNormal(x, y, z);
        return this;
    }
}