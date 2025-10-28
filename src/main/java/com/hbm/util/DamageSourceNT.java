package com.hbm.util;

import com.hbm.lib.ModDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class DamageSourceNT extends DamageSource {

    public DamageSourceNT(Level level, String fromNameAndPath) {
        super(ModDamageSource.create(level, fromNameAndPath.toLowerCase(Locale.US)).typeHolder());
    }


}
