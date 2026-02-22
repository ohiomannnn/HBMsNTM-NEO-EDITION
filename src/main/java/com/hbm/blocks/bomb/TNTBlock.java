package com.hbm.blocks.bomb;

import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.level.Level;

public class TNTBlock extends TNTBaseBlock {

    public TNTBlock(Properties properties) { super(properties); }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {
        ExplosionVNT.createExplosion(level, entity, x, y, z, 10F, true);
    }
}
