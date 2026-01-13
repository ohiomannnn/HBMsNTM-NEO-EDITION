package com.hbm.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class Vec3NT {

    public double xCoord;
    public double yCoord;
    public double zCoord;

    public Vec3NT(double x, double y, double z) {
        if (x == -0.0D) {
            x = 0.0D;
        }

        if (y == -0.0D) {
            y = 0.0D;
        }

        if (z == -0.0D) {
            z = 0.0D;
        }

        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public Vec3NT(Vec3 vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3NT normalizeSelf() {
        double len = Mth.sqrt((float) (this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord));
        if(len < 1.0E-4D) {
            return multiply(0D);
        } else {
            return multiply(1D / len);
        }
    }

    public Vec3NT add(double x, double y, double z) {
        this.xCoord += x;
        this.yCoord += y;
        this.zCoord += z;
        return this;
    }

    public Vec3NT add(Vec3 vec) {
        this.xCoord += vec.x;
        this.yCoord += vec.y;
        this.zCoord += vec.z;
        return this;
    }

    public Vec3NT multiply(double m) {
        this.xCoord *= m;
        this.yCoord *= m;
        this.zCoord *= m;
        return this;
    }

    public Vec3NT multiply(double x, double y, double z) {
        this.xCoord *= x;
        this.yCoord *= y;
        this.zCoord *= z;
        return this;
    }

    public double distanceTo(double x, double y, double z) {
        double dX = x - this.xCoord;
        double dY = y - this.yCoord;
        double dZ = z - this.zCoord;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public Vec3NT setComponents(double x, double y, double z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        return this;
    }

    public double length() {
        return Math.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
    }

    public Vec3NT rotateAroundXRad(double alpha) {
        double cos = Math.cos(alpha);
        double sin = Math.sin(alpha);
        double x = this.xCoord;
        double y = this.yCoord * cos + this.zCoord * sin;
        double z = this.zCoord * cos - this.yCoord * sin;
        return this.setComponents(x, y, z);
    }

    public Vec3NT rotateAroundYRad(double alpha) {
        double cos = Math.cos(alpha);
        double sin = Math.sin(alpha);
        double x = this.xCoord * cos + this.zCoord * sin;
        double y = this.yCoord;
        double z = this.zCoord * cos - this.xCoord * sin;
        return this.setComponents(x, y, z);
    }

    public Vec3NT rotateAroundZRad(double alpha) {
        double cos = Math.cos(alpha);
        double sin = Math.sin(alpha);
        double x = this.xCoord * cos + this.yCoord * sin;
        double y = this.yCoord * cos - this.xCoord * sin;
        double z = this.zCoord;
        return this.setComponents(x, y, z);
    }

    public Vec3NT rotateAroundXDeg(double alpha) {
        return this.rotateAroundXRad(alpha / 180D * Math.PI);
    }

    public Vec3NT rotateAroundYDeg(double alpha) {
        return this.rotateAroundYRad(alpha / 180D * Math.PI);
    }

    public Vec3NT rotateAroundZDeg(double alpha) {
        return this.rotateAroundZRad(alpha / 180D * Math.PI);
    }

    public static double getMinX(Vec3NT... vecs) {
        double min = Double.POSITIVE_INFINITY;
        for(Vec3NT vec : vecs) if(vec.xCoord < min) min = vec.xCoord;
        return min;
    }

    public static double getMinY(Vec3NT... vecs) {
        double min = Double.POSITIVE_INFINITY;
        for(Vec3NT vec : vecs) if(vec.yCoord < min) min = vec.yCoord;
        return min;
    }

    public static double getMinZ(Vec3NT... vecs) {
        double min = Double.POSITIVE_INFINITY;
        for(Vec3NT vec : vecs) if(vec.zCoord < min) min = vec.zCoord;
        return min;
    }

    public static double getMaxX(Vec3NT... vecs) {
        double max = Double.NEGATIVE_INFINITY;
        for(Vec3NT vec : vecs) if(vec.xCoord > max) max = vec.xCoord;
        return max;
    }

    public static double getMaxY(Vec3NT... vecs) {
        double max = Double.NEGATIVE_INFINITY;
        for(Vec3NT vec : vecs) if(vec.yCoord > max) max = vec.yCoord;
        return max;
    }

    public static double getMaxZ(Vec3NT... vecs) {
        double max = Double.NEGATIVE_INFINITY;
        for(Vec3NT vec : vecs) if(vec.zCoord > max) max = vec.zCoord;
        return max;
    }
}
