package com.hbm.handler;

import com.hbm.extprop.LivingProperties;
import net.minecraft.world.entity.LivingEntity;

public class EntityEffectHandler {
    public static void onUpdate(LivingEntity entity) {
        if (entity.tickCount % 20 == 0) {
            LivingProperties.setRadBuf(entity, LivingProperties.getRadEnv(entity));
            LivingProperties.setRadEnv(entity, 0);
        }
    }
}
