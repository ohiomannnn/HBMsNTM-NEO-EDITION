package com.hbm.blocks.bomb;

import com.hbm.entity.item.TNTPrimedBase;
import net.minecraft.world.level.Level;

public class DynamiteBlock extends TNTBaseBlock {

    public DynamiteBlock(Properties properties) { super(properties); }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {
        level.explode(entity, x, y, z, 8F, true, Level.ExplosionInteraction.TNT);
    }
}
