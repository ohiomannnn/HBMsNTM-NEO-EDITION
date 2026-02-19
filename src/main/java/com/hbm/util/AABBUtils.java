package com.hbm.util;

import com.hbm.HBMsNTM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class AABBUtils {

    @Nullable
    public static RayTraceResult clip(Iterable<AABB> boxes, Vec3 start, Vec3 end, BlockPos pos) {
        double[] adouble = new double[] { 1.0D };
        Direction direction = null;
        double d0 = end.x - start.x;
        double d1 = end.y - start.y;
        double d2 = end.z - start.z;

        for(AABB aabb : boxes) {
            direction = getDirection(aabb.move(pos), start, adouble, direction, d0, d1, d2);
        }

        if (direction == null) {
            return null;
        } else {
            double d3 = adouble[0];
            return new RayTraceResult(start.add(d3 * d0, d3 * d1, d3 * d2), direction, pos);
        }
    }

    @Nullable
    private static Direction getDirection(AABB aabb, Vec3 start, double[] minDistance, @Nullable Direction facing, double deltaX, double deltaY, double deltaZ) {
        if (deltaX > 1.0E-7) {
            facing = clipPoint(minDistance, facing, deltaX, deltaY, deltaZ, aabb.minX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, Direction.WEST, start.x, start.y, start.z);
        } else if (deltaX < -1.0E-7) {
            facing = clipPoint(minDistance, facing, deltaX, deltaY, deltaZ, aabb.maxX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, Direction.EAST, start.x, start.y, start.z);
        }

        if (deltaY > 1.0E-7) {
            facing = clipPoint(minDistance, facing, deltaY, deltaZ, deltaX, aabb.minY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, Direction.DOWN, start.y, start.z, start.x);
        } else if (deltaY < -1.0E-7) {
            facing = clipPoint(minDistance, facing, deltaY, deltaZ, deltaX, aabb.maxY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, Direction.UP, start.y, start.z, start.x);
        }

        if (deltaZ > 1.0E-7) {
            facing = clipPoint(minDistance, facing, deltaZ, deltaX, deltaY, aabb.minZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, Direction.NORTH, start.z, start.x, start.y);
        } else if (deltaZ < -1.0E-7) {
            facing = clipPoint(minDistance, facing, deltaZ, deltaX, deltaY, aabb.maxZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, Direction.SOUTH, start.z, start.x, start.y);
        }

        return facing;
    }

    @Nullable
    private static Direction clipPoint(double[] minDistance, @Nullable Direction prevDirection, double distanceSide, double distanceOtherA, double distanceOtherB, double minSide, double minOtherA, double maxOtherA, double minOtherB, double maxOtherB, Direction hitSide, double startSide, double startOtherA, double startOtherB) {
        double d0 = (minSide - startSide) / distanceSide;
        double d1 = startOtherA + d0 * distanceOtherA;
        double d2 = startOtherB + d0 * distanceOtherB;
        if ((double)0.0F < d0 && d0 < minDistance[0] && minOtherA - 1.0E-7 < d1 && d1 < maxOtherA + 1.0E-7 && minOtherB - 1.0E-7 < d2 && d2 < maxOtherB + 1.0E-7) {
            minDistance[0] = d0;
            return hitSide;
        } else {
            return prevDirection;
        }
    }

    public static RayTraceResult calculateIntercept(AABB daPos, Vec3 pos, Vec3 nextPos)
    {
        Vec3 vec32 = getIntermediateWithXValue(pos, nextPos, daPos.minX);
        Vec3 vec33 = getIntermediateWithXValue(pos, nextPos, daPos.maxX);
        Vec3 vec34 = getIntermediateWithYValue(pos, nextPos, daPos.minY);
        Vec3 vec35 = getIntermediateWithYValue(pos, nextPos, daPos.maxY);
        Vec3 vec36 = getIntermediateWithZValue(pos, nextPos, daPos.minZ);
        Vec3 vec37 = getIntermediateWithZValue(pos, nextPos, daPos.maxZ);

        if (!isVecInYZ(daPos, vec32))
        {
            vec32 = null;
        }

        if (!isVecInYZ(daPos, vec33))
        {
            vec33 = null;
        }

        if (!isVecInXZ(daPos, vec34))
        {
            vec34 = null;
        }

        if (!isVecInXZ(daPos, vec35))
        {
            vec35 = null;
        }

        if (!isVecInXY(daPos, vec36))
        {
            vec36 = null;
        }

        if (!isVecInXY(daPos, vec37))
        {
            vec37 = null;
        }

        Vec3 vec38 = null;

        if (vec32 != null && (vec38 == null || pos.distanceToSqr(vec32) < pos.distanceToSqr(vec38)))
        {
            vec38 = vec32;
        }

        if (vec33 != null && (vec38 == null || pos.distanceToSqr(vec33) < pos.distanceToSqr(vec38)))
        {
            vec38 = vec33;
        }

        if (vec34 != null && (vec38 == null || pos.distanceToSqr(vec34) < pos.distanceToSqr(vec38)))
        {
            vec38 = vec34;
        }

        if (vec35 != null && (vec38 == null || pos.distanceToSqr(vec35) < pos.distanceToSqr(vec38)))
        {
            vec38 = vec35;
        }

        if (vec36 != null && (vec38 == null || pos.distanceToSqr(vec36) < pos.distanceToSqr(vec38)))
        {
            vec38 = vec36;
        }

        if (vec37 != null && (vec38 == null || pos.distanceToSqr(vec37) < pos.distanceToSqr(vec38)))
        {
            vec38 = vec37;
        }

        if (vec38 == null)
        {
            return null;
        }
        else
        {
            byte b0 = -1;

            if (vec38 == vec32)
            {
                b0 = 4;
            }

            if (vec38 == vec33)
            {
                b0 = 5;
            }

            if (vec38 == vec34)
            {
                b0 = 0;
            }

            if (vec38 == vec35)
            {
                b0 = 1;
            }

            if (vec38 == vec36)
            {
                b0 = 2;
            }

            if (vec38 == vec37)
            {
                b0 = 3;
            }

            return new RayTraceResult(vec38, Direction.values()[b0], BlockPos.ZERO);
        }
    }

    /**
     * Checks if the specified vector is within the YZ dimensions of the bounding box. Args: Vec3D
     */
    private static boolean isVecInYZ(AABB daPos, Vec3 p_72333_1_)
    {
        return p_72333_1_ == null ? false : p_72333_1_.y >= daPos.minY && p_72333_1_.y <= daPos.maxY && p_72333_1_.z >= daPos.minZ && p_72333_1_.z <= daPos.maxZ;
    }

    /**
     * Checks if the specified vector is within the XZ dimensions of the bounding box. Args: Vec3D
     */
    private static boolean isVecInXZ(AABB daPos, Vec3 p_72315_1_)
    {
        return p_72315_1_ == null ? false : p_72315_1_.x >= daPos.minX && p_72315_1_.x <= daPos.maxX && p_72315_1_.z >= daPos.minZ && p_72315_1_.z <= daPos.maxZ;
    }

    /**
     * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
     */
    private static boolean isVecInXY(AABB daPos, Vec3 p_72319_1_)
    {
        return p_72319_1_ == null ? false : p_72319_1_.x >= daPos.minX && p_72319_1_.x <= daPos.maxX && p_72319_1_.y >= daPos.minY && p_72319_1_.y <= daPos.maxY;
    }

    /**
     * Returns a new vector with x value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public static Vec3 getIntermediateWithXValue(Vec3 daVec, Vec3 vec3, double var) {
        double d1 = vec3.x - daVec.x;
        double d2 = vec3.y - daVec.y;
        double d3 = vec3.z - daVec.z;

        if (d1 * d1 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d4 = (var - daVec.x) / d1;
            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3(daVec.x + d1 * d4, daVec.y + d2 * d4, daVec.z + d3 * d4) : null;
        }
    }

    /**
     * Returns a new vector with y value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public static Vec3 getIntermediateWithYValue(Vec3 daVec, Vec3 vec3, double var) {
        double d1 = vec3.x - daVec.x;
        double d2 = vec3.y - daVec.y;
        double d3 = vec3.z - daVec.z;

        if (d2 * d2 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d4 = (var - daVec.y) / d2;
            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3(daVec.x + d1 * d4, daVec.y + d2 * d4, daVec.z + d3 * d4) : null;
        }
    }

    /**
     * Returns a new vector with z value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public static Vec3 getIntermediateWithZValue(Vec3 daVec, Vec3 vec3, double var) {
        double d1 = vec3.x - daVec.x;
        double d2 = vec3.y - daVec.y;
        double d3 = vec3.z - daVec.z;

        if (d3 * d3 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d4 = (var - daVec.z) / d3;
            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3(daVec.x + d1 * d4, daVec.y + d2 * d4, daVec.z + d3 * d4) : null;
        }
    }
}
