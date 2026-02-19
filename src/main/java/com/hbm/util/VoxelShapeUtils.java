package com.hbm.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class VoxelShapeUtils {
    @Nullable
    public static RayTraceResult clip(VoxelShape shape, Vec3 startVec, Vec3 endVec, BlockPos pos) {
        if (shape.isEmpty()) {
            return null;
        } else {
            Vec3 vec3 = endVec.subtract(startVec);
            if (vec3.lengthSqr() < 1.0E-7) {
                return null;
            } else {
                Vec3 vec31 = startVec.add(vec3.scale(0.001));
                return shape.shape.isFullWide(findIndex(shape, Direction.Axis.X, vec31.x - (double)pos.getX()), findIndex(shape, Direction.Axis.Y, vec31.y - (double)pos.getY()), findIndex(shape, Direction.Axis.Z, vec31.z - (double)pos.getZ())) ? new RayTraceResult(vec31, Direction.getNearest(vec3.x, vec3.y, vec3.z).getOpposite(), pos) : AABBUtils.clip(shape.toAabbs(), startVec, endVec, pos);
            }
        }
    }

    protected static int findIndex(VoxelShape shape, Direction.Axis axis, double position) {
        return Mth.binarySearch(0, shape.shape.getSize(axis) + 1, (var) -> position < get(shape, axis, var)) - 1;
    }

    protected static double get(VoxelShape shape, Direction.Axis axis, int index) {
        return shape.getCoords(axis).getDouble(index);
    }
}
