package com.hbm.registry;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

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
    ResourceKey<DamageType> METEORITE = key("meteorite");

    ResourceKey<DamageType> PHYSICAL = key("physical");
    ResourceKey<DamageType> FIRE = key("fire");
    ResourceKey<DamageType> EXPLOSION = key("explosion");
    ResourceKey<DamageType> ELECTRIC = key("electric");
    ResourceKey<DamageType> LASER = key("laser");
    ResourceKey<DamageType> MICROWAVE = key("microwave");
    ResourceKey<DamageType> SUBATOMIC = key("subatomic");
    ResourceKey<DamageType> OTHER = key("other");

    private static ResourceKey<DamageType> key(String name) { return ResourceKey.create(Registries.DAMAGE_TYPE, NuclearTechMod.withDefaultNamespace(name)); }

    static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(NUCLEAR_BLAST, new DamageType("nuclearBlast", 0.1F));
        context.register(DIGAMMA, new DamageType("digamma", 0.1F));
        context.register(RADIATION, new DamageType("radiation", 0.1F));
        context.register(ASBESTOS, new DamageType("asbestos", 0.1F));
        context.register(BLACKLUNG, new DamageType("blacklung", 0.1F));
        context.register(BLACK_HOLE, new DamageType("blackhole", 0.1F));
        context.register(TAINT, new DamageType("taint", 0.1F));
        context.register(BANG, new DamageType("bang", 0.1F));
        context.register(LEAD, new DamageType("lead", 0.1F));
        context.register(SHRAPNEL, new DamageType("shrapnel", 0.1F));
        context.register(RUBBLE, new DamageType("rubble", 0.1F));
        context.register(MONOXIDE, new DamageType("monoxide", 0.1F));
        context.register(METEORITE, new DamageType("meteorite", 0.1F));

        context.register(PHYSICAL, new DamageType("sednaPhysical", 0.1F));
        context.register(FIRE, new DamageType("sednaFire", 0.1F));
        context.register(EXPLOSION, new DamageType("sednaExplosion", 0.1F));
        context.register(ELECTRIC, new DamageType("sednaElectric", 0.1F));
        context.register(LASER, new DamageType("sednaLaser", 0.1F));
        context.register(MICROWAVE, new DamageType("sednaMicrowave", 0.1F));
        context.register(SUBATOMIC, new DamageType("sednaSubatomic", 0.1F));
        context.register(OTHER, new DamageType("sednaOther", 0.1F));
    }
}
