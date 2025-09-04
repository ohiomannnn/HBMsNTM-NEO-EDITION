package com.hbm.explosion.vanillalike.standard;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.explosion.vanillalike.interfaces.ICustomDamageHandler;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CustomDamageHandlerAmat implements ICustomDamageHandler {

    protected float radiation;

    public CustomDamageHandlerAmat(float radiation) {
        this.radiation = radiation;
    }

    @Override
    public void handleAttack(ExplosionVNT explosion, Entity entity, double distanceScaled) {
        if (entity instanceof LivingEntity livingEntity)
            ContaminationUtil.contaminate(livingEntity, HazardType.RADIATION, ContaminationType.CREATIVE, (float) (radiation * (1D - distanceScaled) * explosion.size));
    }
}