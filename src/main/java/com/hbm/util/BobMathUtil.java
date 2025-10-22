package com.hbm.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class BobMathUtil {

    //finally!
    public static int min(int... nums) {
        int smallest = Integer.MAX_VALUE;
        for(int num : nums) if(num < smallest) smallest = num;
        return smallest;
    }
    public static int max(int... nums) {
        int largest = Integer.MIN_VALUE;
        for(int num : nums) if(num > largest) largest = num;
        return largest;
    }
    public static long min(long... nums) {
        long smallest = Long.MAX_VALUE;
        for(long num : nums) if(num < smallest) smallest = num;
        return smallest;
    }
    public static long max(long... nums) {
        long largest = Long.MIN_VALUE;
        for(long num : nums) if(num > largest) largest = num;
        return largest;
    }
    public static float min(float... nums) {
        float smallest = Float.MAX_VALUE;
        for(float num : nums) if(num < smallest) smallest = num;
        return smallest;
    }
    public static float max(float... nums) {
        float largest = Float.MIN_VALUE;
        for(float num : nums) if(num > largest) largest = num;
        return largest;
    }
    public static double min(double... nums) {
        double smallest = Double.MAX_VALUE;
        for(double num : nums) if(num < smallest) smallest = num;
        return smallest;
    }
    public static double max(double... nums) {
        double largest = Double.MIN_VALUE;
        for(double num : nums) if(num > largest) largest = num;
        return largest;
    }

    public static double safeClamp(double val, double min, double max) {

        val = Mth.clamp(val, min, max);

        if(Double.isNaN(val)) {
            val = (min + max) / 2D;
        }

        return val;
    }

    public static Vec3 interpVec(Vec3 vec1, Vec3 vec2, float interp) {
        return new Vec3(
                interp(vec1.x,  vec2.x, interp),
                interp(vec1.y,  vec2.y, interp),
                interp(vec1.z,  vec2.z, interp)
        );
    }

    public static double interp(double x, double y, float interp) { return x + (y - x) * interp; }
    public static double interp(double x, double y, double interp) { return x + (y - x) * interp; }

    public static double getAngleFrom2DVecs(double x1, double z1, double x2, double z2) {

        double upper = x1 * x2 + z1 * z2;
        double lower = Math.sqrt(x1 * x1 + z1 * z1) * Math.sqrt(x2 * x2 + z2 * z2);

        double result = Math.toDegrees(Math.cos(upper / lower));

        if(result >= 180)
            result -= 180;

        return result;
    }

    public static double getCrossAngle(Vec3 vel, Vec3 rel) {

        vel.normalize();
        rel.normalize();

        double vecProd = rel.x * vel.x + rel.y * vel.y + rel.z * vel.z;
        double bot = rel.length() * vel.length();
        double angle = Math.acos(vecProd / bot) * 180 / Math.PI;

        if(angle >= 180)
            angle -= 180;

        return angle;
    }

    public static float remap(float num, float min1, float max1, float min2, float max2){
        return ((num - min1) / (max1 - min1)) * (max2 - min2) + min2;
    }

    public static float remap01(float num, float min1, float max1){
        return (num - min1) / (max1 - min1);
    }

    public static float remap01_clamp(float num, float min1, float max1){
        return Mth.clamp((num - min1) / (max1 - min1), 0, 1);
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
