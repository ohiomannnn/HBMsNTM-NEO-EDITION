package com.hbm.explosion.vanillant.interfaces;

import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockMutator {
    void mutatePre(ExplosionVNT explosion, BlockState state, BlockPos pos);
    void mutatePost(ExplosionVNT explosion, BlockPos pos);
}
