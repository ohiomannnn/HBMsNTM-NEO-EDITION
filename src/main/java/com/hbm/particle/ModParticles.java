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

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_CLOUD =
            PARTICLE_TYPES.register("muke_cloud", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_CLOUD_BF =
            PARTICLE_TYPES.register("muke_cloud_bf", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_WAVE =
            PARTICLE_TYPES.register("muke_wave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_FLASH =
            PARTICLE_TYPES.register("muke_flash", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXPLOSION_SMALL =
            PARTICLE_TYPES.register("explosion_small", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COOLING_TOWER =
            PARTICLE_TYPES.register("cooling_tower", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAS_FLAME =
            PARTICLE_TYPES.register("gas_flame", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEAD_LEAF =
            PARTICLE_TYPES.register("dead_leaf", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AURA =
            PARTICLE_TYPES.register("aura", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAD_FOG =
            PARTICLE_TYPES.register("rad_fog", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROCKET_FLAME =
            PARTICLE_TYPES.register("rocket_flame", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEBRIS =
            PARTICLE_TYPES.register("debris", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AMAT_FLASH =
            PARTICLE_TYPES.register("amat_flash", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EX_SMOKE =
            PARTICLE_TYPES.register("ex_smoke", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FOAM =
            PARTICLE_TYPES.register("foam", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ASHES =
            PARTICLE_TYPES.register("ashes", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GIBLET =
            PARTICLE_TYPES.register("giblet", () -> new SimpleParticleType(false));


    public static SpriteSet COOLING_TOWER_SPRITES;
    public static SpriteSet ROCKET_FLAME_SPRITES;
    public static SpriteSet MUKE_FLASH_SPRITES;
    public static SpriteSet GIBLET_SPRITES;
    public static SpriteSet DEAD_LEAVES_SPRITES;
    public static SpriteSet MUKE_WAVE_SPRITES;
    public static SpriteSet EXPLOSION_SMALL_SPRITES;
    public static SpriteSet AURA_SPITES;
    public static SpriteSet RAD_FOG_SPRITES;
    public static SpriteSet MUKE_CLOUD_SPRITES;
    public static SpriteSet MUKE_CLOUD_BF_SPRITES;

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}