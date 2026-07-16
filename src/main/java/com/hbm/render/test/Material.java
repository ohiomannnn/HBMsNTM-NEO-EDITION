package com.hbm.render.test;

import net.minecraft.resources.ResourceLocation;

public record Material(
        ResourceLocation texture,
        CutoutMode cutout,
        boolean blur,
        boolean mipmap,
        boolean backfaceCulling,
        Transparency transparency,
        DepthTest depthTest,
        WriteMask writeMask,
        ShadeMode lightingMode
) {

    public enum CutoutMode {
        OFF(-1f),
        HALF(0.5f),
        ONE_TENTH(0.1f);

        public final float threshold;
        CutoutMode(float threshold) { this.threshold = threshold; }
    }

    public enum Transparency {
        OPAQUE,
        ADDITIVE,
        LIGHTNING,
        GLINT,
        CRUMBLING,
        TRANSLUCENT,
        ORDER_INDEPENDENT
    }

    public enum DepthTest {
        OFF,
        NEVER,
        LESS,
        EQUAL,
        LEQUAL,
        GREATER,
        NOTEQUAL,
        GEQUAL,
        ALWAYS,
    }

    public enum WriteMask {
        COLOR_DEPTH,
        COLOR,
        DEPTH;

        public boolean color() { return this == COLOR_DEPTH || this == COLOR; }
        public boolean depth() { return this == COLOR_DEPTH || this == DEPTH; }
    }

    public enum ShadeMode {
        OFF,
        ENTITY,
    }
}