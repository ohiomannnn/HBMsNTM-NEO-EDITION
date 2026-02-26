package com.hbm.entity;

import com.hbm.HBMsNTM;
import com.hbm.entity.effect.BlackHole;
import com.hbm.entity.effect.FalloutRain;
import com.hbm.entity.effect.RagingVortex;
import com.hbm.entity.effect.Vortex;
import com.hbm.entity.item.FallingBlockEntityNT;
import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.entity.logic.*;
import com.hbm.entity.missile.MissileShuttle;
import com.hbm.entity.missile.MissileStealth;
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

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HBMsNTM.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<NukeExplosionMK5>> NUKE_MK5 = REGISTER.register(
            "nuke_explosion_mk5",
            () -> EntityType.Builder.of(NukeExplosionMK5::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .build("nuke_explosion_mk5"));
    public static final DeferredHolder<EntityType<?>, EntityType<NukeExplosionBalefire>> NUKE_BALEFIRE = REGISTER.register(
            "nuke_balefire",
            () -> EntityType.Builder.of(NukeExplosionBalefire::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .build("nuke_balefire"));

    public static final DeferredHolder<EntityType<?>, EntityType<FalloutRain>> NUKE_FALLOUT_RAIN = REGISTER.register(
            "nuke_fallout_rain",
            () -> EntityType.Builder.of(FalloutRain::new, MobCategory.MISC)
                    .setTrackingRange(1000)
                    .sized(4F, 20F)
                    .fireImmune()
                    .build("nuke_fallout_rain"));

    public static final DeferredHolder<EntityType<?>, EntityType<FallingBlockEntityNT>> FALLING_BLOCK = REGISTER.register(
            "falling_block",
            () -> EntityType.Builder.of(FallingBlockEntityNT::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20)
                    .build("falling_block"));

    public static final DeferredHolder<EntityType<?>, EntityType<Duck>> DUCK =
            REGISTER.register("duck",
                    () -> EntityType.Builder.of(Duck::new, MobCategory.CREATURE)
                            .sized(0.4F, 0.7F)
                            .build("duck"));
    public static final DeferredHolder<EntityType<?>, EntityType<CreeperNuclear>> CREEPER_NUCLEAR =
            REGISTER.register("creeper_nuclear",
                    () -> EntityType.Builder.of(CreeperNuclear::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.7F)
                            .build("creeper_nuclear"));

    public static final DeferredHolder<EntityType<?>, EntityType<TNTPrimedBase>> TNT_PRIMED_BASE = REGISTER.register(
            "tnt_primed_base",
            () -> EntityType.Builder.<TNTPrimedBase>of(TNTPrimedBase::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .eyeHeight(0.15F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build("tnt_primed_base"));

    public static final DeferredHolder<EntityType<?>, EntityType<Shrapnel>> SHRAPNEL = REGISTER.register("shrapnel", () -> EntityType.Builder.of(Shrapnel::new, MobCategory.MISC).sized(0.25F, 0.25F).fireImmune().build("shrapnel"));
    public static final DeferredHolder<EntityType<?>, EntityType<Rubble>> RUBBLE = REGISTER.register("rubble", () -> EntityType.Builder.of(Rubble::new, MobCategory.MISC).sized(0.25F, 0.25F).build("rubble"));

    public static final DeferredHolder<EntityType<?>, EntityType<Rocket>> ROCKET = REGISTER.register("rocket", () -> EntityType.Builder.of(Rocket::new, MobCategory.MISC).sized(0.5F, 0.5F).build("rocket"));

    public static final DeferredHolder<EntityType<?>, EntityType<DeathBlast>> DEATH_BLAST = REGISTER.register(
            "death_blast",
            () -> EntityType.Builder.of(DeathBlast::new, MobCategory.MISC)
                    .setTrackingRange(1000)
                    .build("death_blast"));

    public static final DeferredHolder<EntityType<?>, EntityType<Bomber>> BOMBER = REGISTER.register(
            "bomber",
            () -> EntityType.Builder.of(Bomber::new, MobCategory.MISC)
                    .sized(8F, 4F)
                    .setTrackingRange(250)
                    .build("bomber"));

    public static final DeferredHolder<EntityType<?>, EntityType<MissileGeneric>>      MISSILE_GENERIC =    REGISTER.register("missile_generic",    () -> EntityType.Builder.of(MissileGeneric::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_generic"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileIncendiary>>   MISSILE_INCENDIARY = REGISTER.register("missile_incendiary", () -> EntityType.Builder.of(MissileIncendiary::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_incendiary"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileCluster>>      MISSILE_CLUSTER =    REGISTER.register("missile_cluster",    () -> EntityType.Builder.of(MissileCluster::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_cluster"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileBunkerBuster>> MISSILE_BUSTER =     REGISTER.register("missile_buster",     () -> EntityType.Builder.of(MissileBunkerBuster::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_bunker_buster"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDecoy>>        MISSILE_DECOY =      REGISTER.register("missile_decoy",      () -> EntityType.Builder.of(MissileDecoy::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_decoy"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileStealth>> MISSILE_STEALTH = REGISTER.register("missile_stealth",      () -> EntityType.Builder.of(MissileStealth::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_decoy"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileStrong>>           MISSILE_STRONG =            REGISTER.register("missile_strong",            () -> EntityType.Builder.of(MissileStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileIncendiaryStrong>> MISSILE_INCENDIARY_STRONG = REGISTER.register("missile_incendiary_strong", () -> EntityType.Builder.of(MissileIncendiaryStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_incendiary_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileClusterStrong>>    MISSILE_CLUSTER_STRONG =    REGISTER.register("missile_cluster_strong",    () -> EntityType.Builder.of(MissileClusterStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_cluster_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileBusterStrong>>     MISSILE_BUSTER_STRONG =     REGISTER.register("missile_buster_strong",     () -> EntityType.Builder.of(MissileBusterStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_buster_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileEMPStrong>>        MISSILE_EMP_STRONG =        REGISTER.register("missile_emp_strong",     () -> EntityType.Builder.of(MissileEMPStrong::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_emp_strong"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileBurst>>   MISSILE_BURST =   REGISTER.register("missile_burst",   () -> EntityType.Builder.of(MissileBurst::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_burst"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileInferno>> MISSILE_INFERNO = REGISTER.register("missile_inferno", () -> EntityType.Builder.of(MissileInferno::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_inferno"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileRain>>    MISSILE_RAIN =    REGISTER.register("missile_rain",    () -> EntityType.Builder.of(MissileRain::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_rain"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDrill>>   MISSILE_DRILL =   REGISTER.register("missile_drill",   () -> EntityType.Builder.of(MissileDrill::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_drill"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileShuttle>> MISSILE_SHUTTLE = REGISTER.register("missile_shuttle", () -> EntityType.Builder.of(MissileShuttle::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_shuttle"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileNuclear>>        MISSILE_NUCLEAR =         REGISTER.register("missile_nuclear",         () -> EntityType.Builder.of(MissileNuclear::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_nuclear"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileMirv>>           MISSILE_NUCLEAR_CLUSTER = REGISTER.register("missile_nulcear_cluster", () -> EntityType.Builder.of(MissileMirv::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_nulcear_cluster"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileVolcano>>        MISSILE_VOLCANO =         REGISTER.register("missile_volcano",         () -> EntityType.Builder.of(MissileVolcano::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_volcano"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDoomsday>>       MISSILE_DOOMSDAY =        REGISTER.register("missile_doomsday",        () -> EntityType.Builder.of(MissileDoomsday::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_doomsday"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileDoomsdayRusted>> MISSILE_DOOMSDAY_RUSTED = REGISTER.register("missile_doomsday_rusted", () -> EntityType.Builder.of(MissileDoomsdayRusted::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(500).build("missile_doomsday"));

    public static final DeferredHolder<EntityType<?>, EntityType<BombletZeta>> BOMBLET_ZETA = REGISTER.register(
            "bomblet_zeta",
            () -> EntityType.Builder.of(BombletZeta::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(250)
                    .build("bomblet_zeta"));

    public static final DeferredHolder<EntityType<?>, EntityType<BlackHole>> BLACK_HOLE = REGISTER.register(
            "black_hole",
            () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(1000)
                    .fireImmune()
                    .build("black_hole"));
    public static final DeferredHolder<EntityType<?>, EntityType<Vortex>> VORTEX = REGISTER.register(
            "vortex",
            () -> EntityType.Builder.of(Vortex::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(1000)
                    .fireImmune()
                    .build("vortex"));
    public static final DeferredHolder<EntityType<?>, EntityType<RagingVortex>> RAGING_VORTEX = REGISTER.register(
            "raging_vortex",
            () -> EntityType.Builder.of(RagingVortex::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(1000)
                    .fireImmune()
                    .build("raging_vortex"));
    public static final DeferredHolder<EntityType<?>, EntityType<BlackHole>> QUASAR = REGISTER.register(
            "quasar",
            () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .setTrackingRange(1000)
                    .fireImmune()
                    .build("quasar"));

    public static final DeferredHolder<EntityType<?>, EntityType<Meteor>> METEOR = REGISTER.register(
            "meteor",
            () -> EntityType.Builder.of(Meteor::new, MobCategory.MISC)
                    .sized(4F, 4F)
                    .build("meteor"));

    public static final DeferredHolder<EntityType<?>, EntityType<EMP>> EMP = REGISTER.register("emp", () -> EntityType.Builder.of(EMP::new, MobCategory.MISC).build("emp"));

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
