package com.hbm.block.bomb;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TestBomb extends Block {
    public TestBomb(Properties properties) {
        super(properties);
    }
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            ExplosionVNT explosion = new ExplosionVNT(level, x, y, z, 15.0F);
            explosion.makeAmat();
            explosion.explode();
        }
    }

}
