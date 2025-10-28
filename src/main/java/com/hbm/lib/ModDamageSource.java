package com.hbm.lib;

import com.hbm.HBMsNTM;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ModDamageSource {
    public static final ResourceKey<DamageType> DIGAMMA
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "digamma"));
    public static final ResourceKey<DamageType> RADIATION
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "radiation"));
    public static final ResourceKey<DamageType> ASBESTOS
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "asbestos"));
    public static final ResourceKey<DamageType> BLACKLUNG
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "blacklung"));
    public static final ResourceKey<DamageType> TAINT
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "taint"));
    public static final ResourceKey<DamageType> BANG
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "bang"));
    public static final ResourceKey<DamageType> LEAD
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "lead"));
    public static final ResourceKey<DamageType> NUCLEAR_BLAST
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "nuclear_blast"));
    public static final ResourceKey<DamageType> SHRAPNEL
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "shrapnel"));
    public static final ResourceKey<DamageType> RUBBLE
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "rubble"));
    public static final ResourceKey<DamageType> MONOXIDE
            = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "monoxide"));

    public static DamageSource create(Level level, String fromNameAndPath) {
        ResourceLocation location = ResourceLocation.tryParse(fromNameAndPath);
        if (location == null) {
            throw new IllegalArgumentException("Invalid ResourceLocation: " + fromNameAndPath);
        }
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, location);
        Holder<DamageType> holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
        return new DamageSource(holder);
    }
}
