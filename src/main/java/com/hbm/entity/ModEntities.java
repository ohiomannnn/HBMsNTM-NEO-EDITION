package com.hbm.entity;

import com.hbm.HBMsNTM;
import com.hbm.entity.effect.EntityFalloutRain;
import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.entity.logic.NukeExplosionBalefireEntity;
import com.hbm.entity.logic.NukeExplosionMK5Entity;
import com.hbm.entity.effect.EntityNukeTorex;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.entity.projectile.EntityRubble;
import com.hbm.entity.projectile.EntityShrapnel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HBMsNTM.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<NukeExplosionMK5Entity>> NUKE_MK5 =
            ENTITY_TYPES.register("nuke_explosion_mk5",
                    () -> EntityType.Builder.of(NukeExplosionMK5Entity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .build("nuke_explosion_mk5"));

    public static final DeferredHolder<EntityType<?>, EntityType<NukeExplosionBalefireEntity>> NUKE_BALEFIRE =
            ENTITY_TYPES.register("nuke_balefire",
                    () -> EntityType.Builder.of(NukeExplosionBalefireEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .build("nuke_balefire"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityNukeTorex>> NUKE_TOREX =
            ENTITY_TYPES.register("nuke_torex",
                    () -> EntityType.Builder.of(EntityNukeTorex::new, MobCategory.MISC)
                            .setTrackingRange(1000)
                            .sized(2.0F, 50.0F)
                            .build("nuke_torex"));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFalloutRain>> FALLOUT_RAIN =
            ENTITY_TYPES.register("fallout_rain",
                    () -> EntityType.Builder.of(EntityFalloutRain::new, MobCategory.MISC)
                            .setTrackingRange(1000)
                            .sized(2.0F, 20.0F)
                            .build("fallout_rain"));

    public static final DeferredHolder<EntityType<?>, EntityType<Duck>> DUCK =
            ENTITY_TYPES.register("duck",
                    () -> EntityType.Builder.of(Duck::new, MobCategory.CREATURE)
                            .sized(0.4F, 0.7F)
                            .build("duck"));
    public static final DeferredHolder<EntityType<?>, EntityType<CreeperNuclear>> CREEPER_NUCLEAR =
            ENTITY_TYPES.register("creeper_nuclear",
                    () -> EntityType.Builder.of(CreeperNuclear::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.7F)
                            .build("creeper_nuclear"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityTNTPrimedBase>> TNT_PRIMED_BASE =
            ENTITY_TYPES.register("tnt_primed_base",
                    () -> EntityType.Builder
                            //why
                            .of((EntityType<EntityTNTPrimedBase> type, Level level) -> new EntityTNTPrimedBase(type, level), MobCategory.MISC)
                            .sized(0.98F, 0.98F)
                            .clientTrackingRange(10)
                            .updateInterval(10)
                            .build("tnt_primed_base"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityShrapnel>> SHRAPNEL =
            ENTITY_TYPES.register("shrapnel",
                    () -> EntityType.Builder.of(EntityShrapnel::new, MobCategory.MISC)
                            .build("shrapnel"));
    public static final DeferredHolder<EntityType<?>, EntityType<EntityRubble>> RUBBLE =
            ENTITY_TYPES.register("rubble",
                    () -> EntityType.Builder.of(EntityRubble::new, MobCategory.MISC)
                            .build("rubble"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
