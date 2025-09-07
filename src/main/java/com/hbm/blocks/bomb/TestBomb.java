package com.hbm.blocks.bomb;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.handler.radiation.ChunkRadiationHandlerPRISM;
import com.hbm.handler.radiation.ChunkRadiationManager;
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
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (!level.isClientSide()) {
            ChunkRadiationHandlerPRISM prism = (ChunkRadiationHandlerPRISM) ChunkRadiationManager.proxy;
            for (int y = 0; y < 256; y += 16) {
                prism.setRadiation(level, pos.getX(), y, pos.getZ(), 100F);
            }
            prism.updateSystem();
        }
    }
}
