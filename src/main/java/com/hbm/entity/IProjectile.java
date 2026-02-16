package com.hbm.entity;

public interface IProjectile {

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    void setThrowableHeading(double motionX, double motionY, double motionZ, float velocity, float inaccuracy);
}
