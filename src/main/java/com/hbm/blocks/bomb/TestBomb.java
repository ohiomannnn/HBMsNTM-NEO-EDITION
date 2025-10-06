package com.hbm.blocks.bomb;

import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.interfaces.IBomb;
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

            ExplosionVNT xnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 10F);
            xnt.setEntityProcessor(new EntityProcessorCross(2D));
            xnt.setPlayerProcessor(new PlayerProcessorStandard());
            xnt.setSFX(new ExplosionEffectWeapon(5, 1F, 0.3F));
            xnt.explode();

            return BombReturnCode.DETONATED;
        }
        return BombReturnCode.UNDEFINED;
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, EntityTNTPrimedBase entity) {
        explode(level, (int) x, (int) y, (int) z);
    }
}
