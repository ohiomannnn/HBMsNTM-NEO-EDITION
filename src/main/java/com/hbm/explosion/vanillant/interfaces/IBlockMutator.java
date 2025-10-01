package com.hbm.explosion.vanillant.interfaces;

import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockMutator {
    void mutatePre(ExplosionVNT explosion, Block block, BlockState state, int x, int y, int z);
    void mutatePost(ExplosionVNT explosion, int x, int y, int z);
}
