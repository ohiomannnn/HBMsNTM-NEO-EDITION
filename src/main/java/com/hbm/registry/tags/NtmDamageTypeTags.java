package com.hbm.registry.tags;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public interface NtmDamageTypeTags {

    TagKey<DamageType> IS_ENERGY = key("is_energy");
    TagKey<DamageType> ABSOLUTE = key("absolute");

    private static TagKey<DamageType> key(String name) { return TagKey.create(Registries.DAMAGE_TYPE, NuclearTechMod.withDefaultNamespace(name)); }

}
