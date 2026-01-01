package com.hbm.entity;

import com.hbm.HBMsNTM;
import com.hbm.entity.effect.*;
import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.entity.logic.DeathBlast;
import com.hbm.entity.logic.NukeExplosionBalefire;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.entity.missile.MissileTier1.MissileGeneric;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.entity.projectile.Rubble;
import com.hbm.entity.projectile.Shrapnel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
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

    public static final DeferredHolder<EntityType<?>, EntityType<NukeTorex>> NUKE_TOREX = REGISTER.register(
            "nuke_torex",
            () -> EntityType.Builder.of(NukeTorex::new, MobCategory.MISC)
                    .setTrackingRange(1000)
                    .sized(1F, 50F)
                    .fireImmune()
                    .build("nuke_torex"));
    public static final DeferredHolder<EntityType<?>, EntityType<FalloutRain>> NUKE_FALLOUT_RAIN = REGISTER.register(
            "nuke_fallout_rain",
            () -> EntityType.Builder.of(FalloutRain::new, MobCategory.MISC)
                    .setTrackingRange(1000)
                    .sized(4F, 20F)
                    .fireImmune()
                    .build("nuke_fallout_rain"));

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

    public static final DeferredHolder<EntityType<?>, EntityType<EntityTNTPrimedBase>> TNT_PRIMED_BASE = REGISTER.register(
            "tnt_primed_base",
            () -> EntityType.Builder.<EntityTNTPrimedBase>of(EntityTNTPrimedBase::new, MobCategory.MONSTER)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build("tnt_primed_base"));

    public static final DeferredHolder<EntityType<?>, EntityType<Shrapnel>> SHRAPNEL = REGISTER.register(
            "shrapnel",
            () -> EntityType.Builder.of(Shrapnel::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .fireImmune()
                    .build("shrapnel"));
    public static final DeferredHolder<EntityType<?>, EntityType<Rubble>> RUBBLE = REGISTER.register(
            "rubble",
            () -> EntityType.Builder.of(Rubble::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .build("rubble"));

    public static final DeferredHolder<EntityType<?>, EntityType<DeathBlast>> DEATH_BLAST = REGISTER.register(
            "death_blast",
            () -> EntityType.Builder.of(DeathBlast::new, MobCategory.MISC)
                    .setTrackingRange(1000)
                    .build("death_blast"));

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

    public static final DeferredHolder<EntityType<?>, EntityType<Entity>> MISSILE_HE = REGISTER.register(
            "missile_he",
            () -> EntityType.Builder.of(MissileGeneric::new, MobCategory.MISC)
                    .sized(1.5F, 1.5F)
                    .build("missile_he"));

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
