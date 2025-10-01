package com.hbm.util;

import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class BobMathUtil {

    public static Vector3f interpVec(Vector3f v1, Vector3f v2, float t) {
        return new Vector3f(v1).lerp(v2, t);
    }

    public static float remap(float num, float min1, float max1, float min2, float max2) {
        return ((num - min1) / (max1 - min1)) * (max2 - min2) + min2;
    }

    public static float remap01_clamp(float num, float min1, float max1) {
        return Mth.clamp((num - min1) / (max1 - min1), 0.0f, 1.0f);
    }

    /**
     * Adjusted sqrt, approaches standard sqrt but sqrt(x) is never bigger than x
     * <p>
     *      ____________
     *     /       1    |      1
     * _  / x + ――――― - ―――――
     *  \/      (x + 2)²     x + 2
     *
     */
    public static double squirt(double x) {
        return Math.sqrt(x + 1D / ((x + 2D) * (x + 2D))) - 1D / (x + 2D);
    }
}
