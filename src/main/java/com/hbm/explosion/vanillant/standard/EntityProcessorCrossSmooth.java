package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.items.weapon.sedna.factory.ConfettiUtil;
import com.hbm.util.DamageResistanceHandler.DamageClass;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityProcessorCrossSmooth extends EntityProcessorCross {

    protected float fixedDamage;
    protected float pierceDT = 0;
    protected float pierceDR = 0;
    protected DamageClass clazz = DamageClass.EXPLOSIVE;

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
        if (!entity.isAlive()) return;
        if (source.exploder == entity) amount *= 0.5F;
        DamageSource dmg = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.ON_FIRE));//BulletConfig.getDamage(null, source.exploder instanceof EntityLivingBase ? (EntityLivingBase) source.exploder : null, clazz);
        if (!(entity instanceof LivingEntity)) {
            entity.hurt(dmg, amount);
        } else {
            //EntityDamageUtil.attackEntityFromNT((LivingEntity) entity, dmg, amount, true, false, 0F, pierceDT, pierceDR);
            entity.hurt(dmg, amount);
            if(!entity.isAlive()) ConfettiUtil.decideConfetti((LivingEntity) entity, dmg);
        }
    }

    @Override
    public float calculateDamage(double distanceScaled, double density, double knockback, float size) {
        if (density < 0.125) return 0; //shitty hack
        return (float) (fixedDamage * (1 - distanceScaled));
    }
}
