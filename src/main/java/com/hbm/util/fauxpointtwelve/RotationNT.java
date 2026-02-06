package com.hbm.util.fauxpointtwelve;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class RotationNT {
    /**
     * Adjusted code from NTM 1.12 (com.hbm.lib.ForgeDirection)
     */
    public static Rotation getBlockRotation(Direction dir) {
        return switch (dir) {
            case NORTH -> Rotation.NONE;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.COUNTERCLOCKWISE_90;
            case WEST -> Rotation.CLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }
}
