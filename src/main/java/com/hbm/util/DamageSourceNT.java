package com.hbm.util;

import com.hbm.lib.ModDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class DamageSourceNT extends DamageSource {

    private boolean isFire;
    private boolean isProjectile;
    private boolean isExplosion;
    private boolean isLaser;

    public DamageSourceNT(Level level, String fromNameAndPath) {
        super(ModDamageSource.create(level, fromNameAndPath.toLowerCase(Locale.US)).typeHolder());
    }

    public DamageSourceNT setFireDamage() {
        this.isFire = true;
        return this;
    }

    public DamageSourceNT setProjectile() {
        this.isProjectile = true;
        return this;
    }

    public DamageSourceNT setExplosion() {
        this.isExplosion = true;
        return this;
    }

    public DamageSourceNT setLaser() {
        this.isLaser = true;
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

    public boolean isLaser() {
        return isLaser;
    }
}
