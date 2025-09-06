package com.hbm.util;

public class MathUtil {
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
