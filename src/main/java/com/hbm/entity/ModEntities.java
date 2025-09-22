package com.hbm.entity;

import com.hbm.HBMsNTM;
import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.entity.logic.EntityNukeExplosionMK5;
import com.hbm.entity.effect.EntityNukeTorex;
import com.hbm.entity.mob.EntityDuck;
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

    public static final DeferredHolder<EntityType<?>, EntityType<EntityNukeExplosionMK5>> NUKE_MK5 =
            ENTITY_TYPES.register("nuke_explosion_mk5",
                    () -> EntityType.Builder.of(EntityNukeExplosionMK5::new, MobCategory.MISC)
                            .setTrackingRange(1000)
                            .sized(1.0F, 1.0F)
                            .build("nuke_explosion_mk5"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityNukeTorex>> BIG_NUKE_TOREX =
            ENTITY_TYPES.register("nuke_torex",
                    () -> EntityType.Builder.of(EntityNukeTorex::new, MobCategory.MISC)
                            .setTrackingRange(1000)
                            .sized(2.0F, 50.0F)
                            .build("nuke_torex"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityDuck>> DUCK =
            ENTITY_TYPES.register("duck",
                    () -> EntityType.Builder.of(EntityDuck::new, MobCategory.CREATURE)
                            .sized(0.4F, 0.7F)
                            .build("duck"));

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
