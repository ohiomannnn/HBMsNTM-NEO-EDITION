package com.hbm.blocks.machine.heater;

import com.hbm.blocks.DummyableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class AbstractHeaterBlock extends DummyableBlock {

    protected AbstractHeaterBlock(Properties properties) {
        super(properties);
    }

    protected void clearMatching(Level level, BlockPos center, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        for(int dx = minX; dx <= maxX; dx++) {
            for(int dy = minY; dy <= maxY; dy++) {
                for(int dz = minZ; dz <= maxZ; dz++) {
                    BlockPos target = new BlockPos(cx + dx, cy + dy, cz + dz);
                    if(level.getBlockState(target).getBlock() == this) {
                        level.removeBlock(target, false);
                    }
                }
            }
        }
    }
}
