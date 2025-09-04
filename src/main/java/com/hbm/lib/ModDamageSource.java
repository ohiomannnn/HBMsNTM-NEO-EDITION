package com.hbm.lib;

import com.hbm.HBMsNTM;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSource {
    public static final ResourceKey<DamageType> DIGAMMA
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "digamma"));
    public static final ResourceKey<DamageType> ASBESTOS
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "asbestos"));
    public static final ResourceKey<DamageType> BLACKLUNG
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "blacklung"));
    public static final ResourceKey<DamageType> TAINT
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "taint"));
}
