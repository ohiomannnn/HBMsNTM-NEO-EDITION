package com.hbm.items.weapon.sedna;

import com.hbm.lib.ModDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class DamageSourceSednaNoAttacker extends DamageSource {

    private boolean isFire;
    private boolean isProjectile;
    private boolean isExplosion;

    public DamageSourceSednaNoAttacker(Level level, String name) {
        super(ModDamageSource.create(level, name.toLowerCase(Locale.US)).typeHolder());
    }

    public DamageSourceSednaNoAttacker setFireDamage() {
        this.isFire = true;
        return this;
    }

    public DamageSourceSednaNoAttacker setProjectile() {
        this.isProjectile = true;
        return this;
    }

    public DamageSourceSednaNoAttacker setExplosion() {
        this.isExplosion = true;
        return this;
    }

    public boolean isFireDamage() {
        return isFire;
    }

    public boolean isProjectile() {
        return isProjectile;
    }

    public boolean isExplosion() {
        return isExplosion;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
        return Component.translatable("death.senda" + this.type(), livingEntity.getDisplayName());
    }
}
