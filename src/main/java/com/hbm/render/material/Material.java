package com.hbm.render.material;

import net.minecraft.client.renderer.texture.TextureAtlas;
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
        ALWAYS
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
        ENTITY
    }

    public static class Builder {

        public ResourceLocation texture;
        public CutoutMode cutout;
        public boolean blur;
        public boolean mipmap;
        public boolean backfaceCulling;
        public Transparency transparency;
        public DepthTest depthTest;
        public WriteMask writeMask;
        public ShadeMode lightingMode;

        public Builder() {

            this.texture = TextureAtlas.LOCATION_BLOCKS;
            this.cutout = CutoutMode.OFF;
            this.blur = false;
            this.mipmap = false;
            this.backfaceCulling = true;
            this.transparency = Transparency.OPAQUE;
            this.depthTest = DepthTest.LEQUAL;
            this.writeMask = WriteMask.COLOR_DEPTH;
            this.lightingMode = ShadeMode.ENTITY;
        }

        public Builder texture(ResourceLocation value) {
            this.texture = value;
            return this;
        }

        public Builder cutout(CutoutMode value) {
            this.cutout = value;
            return this;
        }

        public Builder blur(boolean value) {
            this.blur = value;
            return this;
        }

        public Builder mipmap(boolean value) {
            this.mipmap = value;
            return this;
        }

        public Builder backfaceCulling(boolean value) {
            this.backfaceCulling = value;
            return this;
        }

        public Builder transparency(Transparency value) {
            this.transparency = value;
            return this;
        }

        public Builder depthTest(DepthTest value) {
            this.depthTest = value;
            return this;
        }

        public Builder writeMask(WriteMask value) {
            this.writeMask = value;
            return this;
        }

        public Builder lightingMode(ShadeMode value) {
            this.lightingMode = value;
            return this;
        }

        public Material build() {
            return new Material(this.texture, this.cutout, this.blur, this.mipmap, this.backfaceCulling, this.transparency, this.depthTest, this.writeMask, this.lightingMode);
        }
    }
}