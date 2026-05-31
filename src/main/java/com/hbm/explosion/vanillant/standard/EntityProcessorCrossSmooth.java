package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.items.weapon.sedna.BulletConfig;
import com.hbm.items.weapon.sedna.factory.ConfettiUtil;
import com.hbm.util.DamageResistanceHandler.DamageClass;
import com.hbm.util.EntityDamageUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityProcessorCrossSmooth extends EntityProcessorCross {

    protected float fixedDamage;
    protected float pierceDT = 0;
    protected float pierceDR = 0;
    protected DamageClass clazz = DamageClass.EXPLOSION;

    public EntityProcessorCrossSmooth(double nodeDist, float fixedDamage) {
        super(nodeDist);
        this.fixedDamage = fixedDamage;
        this.setAllowSelfDamage();
    }

    public EntityProcessorCrossSmooth setupPiercing(float pierceDT, float pierceDR) {
        this.pierceDT = pierceDT;
        this.pierceDR = pierceDR;
        return this;
    }

    public EntityProcessorCrossSmooth setDamageClass(DamageClass clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public void attackEntity(Entity entity, ExplosionVNT source, float amount) {
        if(!entity.isAlive()) return;
        if(source.exploder == entity) amount *= 0.5F;
        DamageSource dmg = BulletConfig.getDamage(entity.level, null, source.exploder instanceof LivingEntity living ? living : null, clazz);
        if(entity instanceof LivingEntity living) {
            EntityDamageUtil.hurtNT(living, dmg, amount, true, false, 0F, pierceDT, pierceDR);
            if(!entity.isAlive()) ConfettiUtil.createConfetti(living, clazz);
        } else {
            entity.hurt(dmg, amount);
        }
    }

    @Override
    public float calculateDamage(double distanceScaled, double density, double knockback, float size) {
        if(density < 0.125) return 0; //shitty hack
        return (float) (fixedDamage * (1 - distanceScaled));
    }
}
