package com.hbm.handler.compat;

import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SableCompat {

    public static BlockPos getProj(Level level, BlockPos pos) {

        Vec3 toTransform = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        SubLevelAccess subLevelAccess = SableCompanion.INSTANCE.getContaining(level, pos);

        if(subLevelAccess != null) {
            Pose3dc pose = subLevelAccess.logicalPose();

            // Transform the position to global space
            toTransform = pose.transformPosition(toTransform);
        }

        toTransform = SableCompanion.INSTANCE.projectOutOfSubLevel(level, toTransform);

        return BlockPos.containing(toTransform);
    }
}
