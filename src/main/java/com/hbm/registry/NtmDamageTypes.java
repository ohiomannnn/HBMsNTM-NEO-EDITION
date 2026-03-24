package com.hbm.registry;

import com.hbm.NuclearTechMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public interface NtmDamageTypes {
    ResourceKey<DamageType> NUCLEAR_BLAST = key("nuclear_blast"); // explosion
    ResourceKey<DamageType> MUD_POISONING = key("mud_poisoning"); // bypasses armor
    ResourceKey<DamageType> ACID = key("acid");
    //public static final ResourceKey<DamageType> euthanizedSelf = create("euthanizedSelf"); // bypasses armor
    //public static final ResourceKey<DamageType> euthanizedSelf2  = create("euthanizedSelf"); // bypasses armor
    ResourceKey<DamageType> TAU_BLAST = key("tau_blast");
    ResourceKey<DamageType> RADIATION = key("radiation");
    ResourceKey<DamageType> DIGAMMA = key("digamma");
    ResourceKey<DamageType> SUICIDE = key("suicide");

    ResourceKey<DamageType> ASBESTOS = key("asbestos");
    ResourceKey<DamageType> BLACKLUNG = key("blacklung");
    ResourceKey<DamageType> BLACK_HOLE = key("black_hole");
    ResourceKey<DamageType> TAINT = key("taint");
    ResourceKey<DamageType> BANG = key("bang");
    ResourceKey<DamageType> LEAD = key("lead");
    ResourceKey<DamageType> SHRAPNEL = key("shrapnel");
    ResourceKey<DamageType> RUBBLE = key("rubble");
    ResourceKey<DamageType> MONOXIDE = key("monoxide");

    ResourceKey<DamageType> LASER = key("laser");
    ResourceKey<DamageType> MICROWAVE = key("microwave");
    ResourceKey<DamageType> SUBATOMIC = key("subatomic");
    ResourceKey<DamageType> ELECTRIC = key("electric");

    private static ResourceKey<DamageType> key(String name) { return ResourceKey.create(Registries.DAMAGE_TYPE, NuclearTechMod.withDefaultNamespace(name)); }

    static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(NUCLEAR_BLAST, new DamageType("nuclearBlast", 0.1F));
        context.register(DIGAMMA, defaultType(pathOf(DIGAMMA)));
        context.register(RADIATION, defaultType(pathOf(RADIATION)));
        context.register(ASBESTOS, defaultType(pathOf(ASBESTOS)));
        context.register(BLACKLUNG, defaultType(pathOf(BLACKLUNG)));
        context.register(BLACK_HOLE, defaultType(pathOf(BLACK_HOLE)));
        context.register(TAINT, defaultType(pathOf(TAINT)));
        context.register(BANG, defaultType(pathOf(BANG)));
        context.register(LEAD, defaultType(pathOf(LEAD)));
        context.register(SHRAPNEL, defaultType(pathOf(SHRAPNEL)));
        context.register(RUBBLE, defaultType(pathOf(RUBBLE)));
        context.register(MONOXIDE, defaultType(pathOf(MONOXIDE)));

        context.register(LASER, new DamageType("laser", 0.1F));
        context.register(MICROWAVE, new DamageType("microwave", 0.1F));
        context.register(SUBATOMIC, new DamageType("subatomic", 0.1F));
        context.register(ELECTRIC, new DamageType("electric", 0.1F));
    }

    // HELPERS
    private static String pathOf(ResourceKey<DamageType> damage) { return damage.location().getPath(); }
    private static DamageType defaultType(String messageId) {
        return new DamageType(messageId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f, DamageEffects.HURT, DeathMessageType.DEFAULT);
    }
}
