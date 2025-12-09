package com.hbm.blocks.gas;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;

public class GasExplosiveBlock extends GasFlammableBlock  {

    public GasExplosiveBlock(Properties properties) {
        super(properties);
    }

    protected void combust(Level level, BlockPos pos) {
        level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);

        ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3F)
                .setBlockAllocator(new BlockAllocatorStandard())
                .setBlockProcessor(new BlockProcessorNoDamage().withBlockEffect(new BlockMutatorFire()))
                .setEntityProcessor(new EntityProcessorStandard())
                .setPlayerProcessor(new PlayerProcessorStandard())
                .setSFX(new ExplosionEffectStandard());
        vnt.explode();
    }
}
