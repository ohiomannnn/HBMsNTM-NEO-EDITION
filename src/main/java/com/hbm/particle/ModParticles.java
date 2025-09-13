package com.hbm.particle;

import com.hbm.HBMsNTM;
import net.minecraft.client.particle.SpriteSet;
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
    /// Muke stuff
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_CLOUD =
            PARTICLE_TYPES.register("muke_cloud",
                    () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_CLOUD_BF =
            PARTICLE_TYPES.register("muke_cloud_bf",
                    () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_WAVE =
            PARTICLE_TYPES.register("muke_wave",
                    () -> new SimpleParticleType(true));
    /// Muke explosion itself
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_FLASH =
            PARTICLE_TYPES.register("muke_flash",
                    () -> new SimpleParticleType(true));
    /// end
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXPLOSION_SMALL =
            PARTICLE_TYPES.register("explosion_small",
                    () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COOLING_TOWER =
            PARTICLE_TYPES.register("cooling_tower",
                    () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAS_FLAME =
            PARTICLE_TYPES.register("gas_flame",
                    () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEAD_LEAF =
            PARTICLE_TYPES.register("dead_leaf",
                    () -> new SimpleParticleType(false));

    public static SpriteSet COOLING_TOWER_SPRITES;

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}