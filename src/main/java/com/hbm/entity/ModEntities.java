package com.hbm.entity;

import com.hbm.HBMsNTM;
import com.hbm.entity.mob.EntityDuck;
import com.hbm.entity.projectile.EntityRubble;
import com.hbm.entity.projectile.EntityShrapnel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HBMsNTM.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EntityDuck>> DUCK =
            ENTITY_TYPES.register("duck",
                    () -> EntityType.Builder.of(EntityDuck::new, MobCategory.CREATURE)
                            .sized(0.4F, 0.7F)
                            .build("duck"));
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
