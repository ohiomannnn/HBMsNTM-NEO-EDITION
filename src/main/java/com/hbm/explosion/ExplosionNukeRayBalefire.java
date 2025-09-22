package com.hbm.explosion;

import com.hbm.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

// wtf this not used in original code???
public class ExplosionNukeRayBalefire extends ExplosionNukeRayBatched {

    private final Level level;

    public ExplosionNukeRayBalefire(Level level, int x, int y, int z, int strength, int speed, int length) {
        super(level, x, y, z, strength, speed, length);
        this.level = level;
    }

    @Override
    protected void handleTip(int x, int y, int z) {

        BlockPos pos = new BlockPos(x, y, z);
        BlockPos above = pos.above();

        if (level.random.nextInt(5) == 0 && level.getBlockState(above).isAir()) {
            level.setBlock(above, ModBlocks.BALEFIRE.get().defaultBlockState(), 3);
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }
}
