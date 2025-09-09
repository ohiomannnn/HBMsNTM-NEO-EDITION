package com.hbm.particle;

import com.hbm.HBMsNTM;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, HBMsNTM.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOME_PART =
            PARTICLE_TYPES.register("some_part",
                    () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_CLOUD =
            PARTICLE_TYPES.register("muke_cloud",
                    () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_WAVE =
            PARTICLE_TYPES.register("muke_wave",
                    () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXPLOSION_SMALL =
            PARTICLE_TYPES.register("explosion_small",
                    () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COOLING_TOWER =
            PARTICLE_TYPES.register("cooling_tower",
                    () -> new SimpleParticleType(false));
    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}