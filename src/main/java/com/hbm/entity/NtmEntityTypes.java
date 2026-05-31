package com.hbm.entity;

import com.hbm.main.NuclearTechMod;
import com.hbm.entity.effect.BlackHole;
import com.hbm.entity.effect.FalloutRain;
import com.hbm.entity.effect.RagingVortex;
import com.hbm.entity.effect.Vortex;
import com.hbm.entity.item.FallingBlockEntityNT;
import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.entity.logic.*;
import com.hbm.entity.missile.MissileShuttle;
import com.hbm.entity.missile.MissileStealth;
import com.hbm.entity.missile.MissileTier0.*;
import com.hbm.entity.missile.MissileTier1.*;
import com.hbm.entity.missile.MissileTier2.*;
import com.hbm.entity.missile.MissileTier3.*;
import com.hbm.entity.missile.MissileTier4.*;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.entity.projectile.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NtmEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, NuclearTechMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BulletBeamBase>> BULLET_BEAM = ENTITY_TYPES.register(
            "bullet_beam",
            () -> EntityType.Builder.<BulletBeamBase>of(BulletBeamBase::new, MobCategory.MISC)
                    .noSummon()
                    .fireImmune()
                    .setTrackingRange(250)
                    .sized(0.5F, 0.5F)
                    .build("bullet_beam")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<BulletBaseMK4>> BULLET_MK4 = ENTITY_TYPES.register(
            "bullet_mk4",
            () -> EntityType.Builder.<BulletBaseMK4>of(BulletBaseMK4::new, MobCategory.MISC)
                    .noSummon()
                    .fireImmune()
                    .setTrackingRange(250)
                    .sized(0.5F, 0.5F)
                    .build("bullet_mk4")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<NukeExplosionMK5>> NUKE_MK5 = ENTITY_TYPES.register("nuke_mk5", () -> EntityType.Builder.of(NukeExplosionMK5::new, MobCategory.MISC).sized(1.0F, 1.0F).build("nuke_mk5"));
    public static final DeferredHolder<EntityType<?>, EntityType<NukeExplosionMK3>> NUKE_MK3 = ENTITY_TYPES.register("nuke_mk3", () -> EntityType.Builder.of(NukeExplosionMK3::new, MobCategory.MISC).sized(1.0F, 1.0F).build("nuke_mk3"));
    public static final DeferredHolder<EntityType<?>, EntityType<NukeExplosionBalefire>> NUKE_BALEFIRE = ENTITY_TYPES.register("nuke_explosion_balefire", () -> EntityType.Builder.of(NukeExplosionBalefire::new, MobCategory.MISC).sized(1.0F, 1.0F).build("nuke_explosion_balefire"));

    public static final DeferredHolder<EntityType<?>, EntityType<FalloutRain>> FALLOUT_RAIN = ENTITY_TYPES.register(
            "fallout_rain",
            () -> EntityType.Builder.<FalloutRain>of(FalloutRain::new, MobCategory.MISC)
                    .setTrackingRange(1000)
                    .sized(4F, 20F)
                    .fireImmune()
                    .build("fallout_rain"));

    public static final DeferredHolder<EntityType<?>, EntityType<FallingBlockEntityNT>> FALLING_BLOCK = ENTITY_TYPES.register(
            "falling_block",
            () -> EntityType.Builder.of(FallingBlockEntityNT::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20)
                    .build("falling_block"));

    public static final DeferredHolder<EntityType<?>, EntityType<Duck>> DUCK = ENTITY_TYPES.register(
            "duck",
            () -> EntityType.Builder.of(Duck::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.7F)
                    .eyeHeight(0.644F)
                    .clientTrackingRange(10)
                    .build("duck"));

    public static final DeferredHolder<EntityType<?>, EntityType<CreeperNuclear>> CREEPER_NUCLEAR =
            ENTITY_TYPES.register("creeper_nuclear",
                    () -> EntityType.Builder.of(CreeperNuclear::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.7F)
                            .build("creeper_nuclear"));

    public static final DeferredHolder<EntityType<?>, EntityType<TNTPrimedBase>> TNT_PRIMED_BASE = ENTITY_TYPES.register(
            "tnt_primed_base",
            () -> EntityType.Builder.<TNTPrimedBase>of(TNTPrimedBase::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .eyeHeight(0.15F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build("tnt_primed_base"));

    public static final DeferredHolder<EntityType<?>, EntityType<Shrapnel>> SHRAPNEL = ENTITY_TYPES.register("shrapnel", () -> EntityType.Builder.of(Shrapnel::new, MobCategory.MISC).sized(0.25F, 0.25F).fireImmune().build("shrapnel"));
    public static final DeferredHolder<EntityType<?>, EntityType<Rubble>> RUBBLE = ENTITY_TYPES.register("rubble", () -> EntityType.Builder.of(Rubble::new, MobCategory.MISC).sized(0.25F, 0.25F).build("rubble"));

    public static final DeferredHolder<EntityType<?>, EntityType<Rocket>> ROCKET = ENTITY_TYPES.register("rocket", () -> EntityType.Builder.of(Rocket::new, MobCategory.MISC).sized(0.5F, 0.5F).build("rocket"));

    public static final DeferredHolder<EntityType<?>, EntityType<DeathBlast>> DEATH_BLAST = ENTITY_TYPES.register(
            "death_blast",
            () -> EntityType.Builder.of(DeathBlast::new, MobCategory.MISC)
                    .setTrackingRange(1000)
                    .build("death_blast"));

    public static final DeferredHolder<EntityType<?>, EntityType<Bomber>> BOMBER = ENTITY_TYPES.register(
            "bomber",
            () -> EntityType.Builder.<Bomber>of(Bomber::new, MobCategory.MISC)
                    .sized(8F, 4F)
                    .setTrackingRange(250)
                    .build("bomber"));

    public static final DeferredHolder<EntityType<?>, EntityType<MissileMicro>>       MISSILE_MICRO =       ENTITY_TYPES.register("missile_micro",       () -> EntityType.Builder.of(MissileMicro::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_micro"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileSchrabidium>> MISSILE_SCHRABIDIUM = ENTITY_TYPES.register("missile_schrabidium", () -> EntityType.Builder.of(MissileSchrabidium::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_schrabidium"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileBHole>>       MISSILE_BHOLE =       ENTITY_TYPES.register("missile_bhole",       () -> EntityType.Builder.of(MissileBHole::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_bhole"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileTaint>>       MISSILE_TAINT =       ENTITY_TYPES.register("missile_taint",       () -> EntityType.Builder.of(MissileTaint::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_taint"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileEMP>>         MISSILE_EMP =         ENTITY_TYPES.register("missile_emp",         () -> EntityType.Builder.of(MissileEMP::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_emp"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileGeneric>>      MISSILE_GENERIC =    ENTITY_TYPES.register("missile_generic",    () -> EntityType.Builder.of(MissileGeneric::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_generic"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileIncendiary>>   MISSILE_INCENDIARY = ENTITY_TYPES.register("missile_incendiary", () -> EntityType.Builder.of(MissileIncendiary::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_incendiary"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileCluster>>      MISSILE_CLUSTER =    ENTITY_TYPES.register("missile_cluster",    () -> EntityType.Builder.of(MissileCluster::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_cluster"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileBunkerBuster>> MISSILE_BUSTER =     ENTITY_TYPES.register("missile_buster",     () -> EntityType.Builder.of(MissileBunkerBuster::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_bunker_buster"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDecoy>>        MISSILE_DECOY =      ENTITY_TYPES.register("missile_decoy",      () -> EntityType.Builder.of(MissileDecoy::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_decoy"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileStealth>> MISSILE_STEALTH = ENTITY_TYPES.register("missile_stealth",      () -> EntityType.Builder.of(MissileStealth::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_decoy"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileStrong>>           MISSILE_STRONG =            ENTITY_TYPES.register("missile_strong",            () -> EntityType.Builder.of(MissileStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileIncendiaryStrong>> MISSILE_INCENDIARY_STRONG = ENTITY_TYPES.register("missile_incendiary_strong", () -> EntityType.Builder.of(MissileIncendiaryStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_incendiary_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileClusterStrong>>    MISSILE_CLUSTER_STRONG =    ENTITY_TYPES.register("missile_cluster_strong",    () -> EntityType.Builder.of(MissileClusterStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_cluster_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileBusterStrong>>     MISSILE_BUSTER_STRONG =     ENTITY_TYPES.register("missile_buster_strong",     () -> EntityType.Builder.of(MissileBusterStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_buster_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileEMPStrong>>        MISSILE_EMP_STRONG =        ENTITY_TYPES.register("missile_emp_strong",     () -> EntityType.Builder.of(MissileEMPStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_emp_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileBurst>>   MISSILE_BURST =   ENTITY_TYPES.register("missile_burst",   () -> EntityType.Builder.of(MissileBurst::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_burst"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileInferno>> MISSILE_INFERNO = ENTITY_TYPES.register("missile_inferno", () -> EntityType.Builder.of(MissileInferno::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_inferno"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileRain>>    MISSILE_RAIN =    ENTITY_TYPES.register("missile_rain",    () -> EntityType.Builder.of(MissileRain::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_rain"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDrill>>   MISSILE_DRILL =   ENTITY_TYPES.register("missile_drill",   () -> EntityType.Builder.of(MissileDrill::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_drill"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileShuttle>> MISSILE_SHUTTLE = ENTITY_TYPES.register("missile_shuttle", () -> EntityType.Builder.of(MissileShuttle::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_shuttle"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileNuclear>>        MISSILE_NUCLEAR =         ENTITY_TYPES.register("missile_nuclear",         () -> EntityType.Builder.of(MissileNuclear::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_nuclear"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileMirv>>           MISSILE_NUCLEAR_CLUSTER = ENTITY_TYPES.register("missile_nulcear_cluster", () -> EntityType.Builder.of(MissileMirv::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_nulcear_cluster"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileVolcano>>        MISSILE_VOLCANO =         ENTITY_TYPES.register("missile_volcano",         () -> EntityType.Builder.of(MissileVolcano::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_volcano"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDoomsday>>       MISSILE_DOOMSDAY =        ENTITY_TYPES.register("missile_doomsday",        () -> EntityType.Builder.of(MissileDoomsday::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_doomsday"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDoomsdayRusted>> MISSILE_DOOMSDAY_RUSTED = ENTITY_TYPES.register("missile_doomsday_rusted", () -> EntityType.Builder.of(MissileDoomsdayRusted::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_doomsday"));

    public static final DeferredHolder<EntityType<?>, EntityType<BombletZeta>> BOMBLET_ZETA = ENTITY_TYPES.register(
            "bomblet_zeta",
            () -> EntityType.Builder.of(BombletZeta::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(250)
                    .build("bomblet_zeta"));

    public static final DeferredHolder<EntityType<?>, EntityType<BlackHole>> BLACK_HOLE = ENTITY_TYPES.register(
            "black_hole",
            () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(250)
                    .fireImmune()
                    .build("black_hole"));
    public static final DeferredHolder<EntityType<?>, EntityType<Vortex>> VORTEX = ENTITY_TYPES.register(
            "vortex",
            () -> EntityType.Builder.of(Vortex::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(250)
                    .fireImmune()
                    .build("vortex"));
    public static final DeferredHolder<EntityType<?>, EntityType<RagingVortex>> RAGING_VORTEX = ENTITY_TYPES.register(
            "raging_vortex",
            () -> EntityType.Builder.of(RagingVortex::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(250)
                    .fireImmune()
                    .build("raging_vortex"));
    public static final DeferredHolder<EntityType<?>, EntityType<BlackHole>> DIGAMMA_QUASAR = ENTITY_TYPES.register(
            "digamma_quasar",
            () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(250)
                    .fireImmune()
                    .build("digamma_quasar"));

    public static final DeferredHolder<EntityType<?>, EntityType<Meteor>> METEOR = ENTITY_TYPES.register(
            "meteor",
            () -> EntityType.Builder.of(Meteor::new, MobCategory.MISC)
                    .sized(4F, 4F)
                    .build("meteor"));

    public static final DeferredHolder<EntityType<?>, EntityType<EMP>> EMP = ENTITY_TYPES.register("emp", () -> EntityType.Builder.of(EMP::new, MobCategory.MISC).build("emp"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
