package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.items.weapon.sedna.factory.ConfettiUtil;
import com.hbm.util.DamageResistanceHandler.DamageClass;
import com.hbm.util.DamageSourceNT;
import com.hbm.util.EntityDamageUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

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
        if (!entity.isAlive()) return;
        if (source.exploder == entity) amount *= 0.5F;
        DamageSourceNT dmg = getDamage(entity.level(), clazz);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.hurt(entity.level().damageSources().source(DamageTypes.EXPLOSION), amount);
            if (!entity.isAlive()) ConfettiUtil.decideConfetti(livingEntity, dmg);
        } else {
            entity.hurt(dmg, amount);
        }
    }

    @Override
    public float calculateDamage(double distanceScaled, double density, double knockback, float size) {
        if (density < 0.125) return 0; //shitty hack
        return (float) (fixedDamage * (1 - distanceScaled));
    }

    public static DamageSourceNT getDamage(Level level, DamageClass dmgClass) {

        String name = "minecraft:generic";

        switch(dmgClass) {
            case IN_FIRE: name = "minecraft:in_fire"; break;
            case EXPLOSION: name = "minecraft:explosion"; break;
            case ELECTRIC, LASER, SUBATOMIC, PHYSICAL: break;
        }

        DamageSourceNT dmg = new DamageSourceNT(level, name);

        switch(dmgClass) {
            case PHYSICAL: dmg.setProjectile(); break;
            case IN_FIRE: dmg.setFireDamage(); break;
            case EXPLOSION: dmg.setExplosion(); break;
            case ELECTRIC, LASER, SUBATOMIC: break;
        }

        return dmg;
    }
}
