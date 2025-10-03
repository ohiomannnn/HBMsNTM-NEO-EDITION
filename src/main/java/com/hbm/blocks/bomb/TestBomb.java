package com.hbm.blocks.bomb;

import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCross;
import com.hbm.explosion.vanillant.standard.PlayerProcessorStandard;
import com.hbm.interfaces.IBomb;
import com.hbm.particle.helper.ExplosionCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

// block for testing explosive shit
public class TestBomb extends DetonatableBlock implements IBomb {
    public TestBomb(Properties properties) {
        super(properties, 0, 0, 0, false, false);
    }

    @Override
    public BombReturnCode explode(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);

            ExplosionVNT xnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 35F);
            xnt.setBlockAllocator(new BlockAllocatorStandard(24));
            xnt.setBlockProcessor(new BlockProcessorStandard().setNoDrop());
            xnt.setEntityProcessor(new EntityProcessorCross(5D).withRangeMod(1.5F));
            xnt.setPlayerProcessor(new PlayerProcessorStandard());
            xnt.explode();
            ExplosionCreator.composeEffectLarge(level, x + 0.5, y + 0.5, z + 0.5);

            return BombReturnCode.DETONATED;
        }
        return BombReturnCode.UNDEFINED;
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, EntityTNTPrimedBase entity) {
        explode(level, (int) x, (int) y, (int) z);
    }
}
