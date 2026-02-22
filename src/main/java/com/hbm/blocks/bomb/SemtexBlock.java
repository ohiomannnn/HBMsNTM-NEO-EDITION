package com.hbm.blocks.bomb;

import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.level.Level;

public class SemtexBlock extends TNTBaseBlock {

    public SemtexBlock(Properties properties) { super(properties); }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {
        ExplosionVNT.createExplosion(level, entity, x, y, z, 12F, true);
    }
}
