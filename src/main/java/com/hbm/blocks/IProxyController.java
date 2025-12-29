package com.hbm.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Custom handling that tells TileEntityProxyBase where the core for a given proxy block is.
 * DummyableBlock doesn't implement this since it already has its own standardized core finding code.
 *
 * @author hbm
 */
public interface IProxyController {
    BlockEntity getCore(Level level, BlockPos pos);
}
