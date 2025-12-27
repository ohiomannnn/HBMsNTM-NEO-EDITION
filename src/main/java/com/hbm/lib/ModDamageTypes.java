package com.hbm.lib;

import com.hbm.HBMsNTM;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.level.Level;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> DIGAMMA = create("digamma");
    public static final ResourceKey<DamageType> RADIATION = create("radiation");
    public static final ResourceKey<DamageType> ASBESTOS = create("asbestos");
    public static final ResourceKey<DamageType> BLACKLUNG = create("blacklung");
    public static final ResourceKey<DamageType> BLACK_HOLE = create("black_hole");
    public static final ResourceKey<DamageType> TAINT = create("taint");
    public static final ResourceKey<DamageType> BANG = create("bang");
    public static final ResourceKey<DamageType> LEAD = create("lead");
    public static final ResourceKey<DamageType> NUCLEAR_BLAST = create("nuclear_blast");
    public static final ResourceKey<DamageType> SHRAPNEL = create("shrapnel");
    public static final ResourceKey<DamageType> RUBBLE = create("rubble");
    public static final ResourceKey<DamageType> MONOXIDE = create("monoxide");

    private static ResourceKey<DamageType> create(String name) { return ResourceKey.create(Registries.DAMAGE_TYPE, HBMsNTM.withDefaultNamespaceNT(name)); }

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(DIGAMMA, defaultType(pathOf(DIGAMMA)));
        context.register(RADIATION, defaultType(pathOf(RADIATION)));
        context.register(ASBESTOS, defaultType(pathOf(ASBESTOS)));
        context.register(BLACKLUNG, defaultType(pathOf(BLACKLUNG)));
        context.register(BLACK_HOLE, defaultType(pathOf(BLACK_HOLE)));
        context.register(TAINT, defaultType(pathOf(TAINT)));
        context.register(BANG, defaultType(pathOf(BANG)));
        context.register(LEAD, defaultType(pathOf(LEAD)));
        context.register(NUCLEAR_BLAST, defaultType(pathOf(NUCLEAR_BLAST)));
        context.register(SHRAPNEL, defaultType(pathOf(SHRAPNEL)));
        context.register(RUBBLE, defaultType(pathOf(RUBBLE)));
        context.register(MONOXIDE, defaultType(pathOf(MONOXIDE)));
    }

    // HELPERS
    private static String pathOf(ResourceKey<DamageType> damage) { return damage.location().getPath(); }
    private static DamageType defaultType(String messageId) {
        return new DamageType(messageId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f, DamageEffects.HURT, DeathMessageType.DEFAULT);
    }

    public static DamageSource create(Level level, String fromNameAndPath) throws IllegalArgumentException {
        ResourceLocation location = ResourceLocation.tryParse(fromNameAndPath);
        if (location == null) {
            throw new IllegalArgumentException("Invalid ResourceLocation: " + fromNameAndPath);
        }
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, location);
        Holder<DamageType> holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
        return new DamageSource(holder);
    }
}
