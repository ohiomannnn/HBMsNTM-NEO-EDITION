package com.hbm.render.util;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class OffsetVertexConsumer implements VertexConsumer {
    private final VertexConsumer con;
    private final float uOffset, vOffset;

    public OffsetVertexConsumer(VertexConsumer wrapped, float uOffset, float vOffset) {
        this.con = wrapped;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
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
        con.setUv(u + uOffset, v + vOffset);
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