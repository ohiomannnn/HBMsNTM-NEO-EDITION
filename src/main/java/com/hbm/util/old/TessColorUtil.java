package com.hbm.util.old;

public class TessColorUtil {

    public static int getColorRGBA_F(float r, float g, float b, float a) {
        return pack((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
    }

    // packs color with clamping for ARGB use
    public static int pack(int r, int g, int b, int a) {
        if (r > 255) {
            r = 255;
        }

        if (g > 255) {
            g = 255;
        }

        if (b > 255) {
            b = 255;
        }

        if (a > 255) {
            a = 255;
        }

        if (r < 0) {
            r = 0;
        }

        if (g < 0) {
            g = 0;
        }

        if (b < 0) {
            b = 0;
        }

        if (a < 0) {
            a = 0;
        }

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
