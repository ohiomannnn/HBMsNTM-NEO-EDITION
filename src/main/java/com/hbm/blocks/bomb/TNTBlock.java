package com.hbm.blocks.bomb;

import com.hbm.entity.item.TNTPrimedBase;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;

public class TNTBlock extends TNTBaseBlock {

    public TNTBlock(Properties properties) { super(properties); }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {
        level.explode(null, x, y, z, 10F, ExplosionInteraction.TNT);
    }
}
