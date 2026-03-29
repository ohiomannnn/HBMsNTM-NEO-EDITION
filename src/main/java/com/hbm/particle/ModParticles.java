package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, NuclearTechMod.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BASE_PARTICLE = REGISTER.register(
            "base_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RBMK_MUSH = REGISTER.register(
            "rbmkmush", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HAZE = REGISTER.register(
            "haze", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COOLING_TOWER = REGISTER.register(
            "cooling_tower", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAS_FLAME = REGISTER.register(
            "gas_flame", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEAD_LEAF = REGISTER.register(
            "dead_leaf", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AURA = REGISTER.register(
            "aura", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAD_FOG = REGISTER.register(
            "rad_fog", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEBRIS = REGISTER.register(
            "debris", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AMAT_FLASH = REGISTER.register(
            "amat_flash", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FOAM = REGISTER.register(
            "foam", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ASHES = REGISTER.register(
            "ashes", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HADRON = REGISTER.register(
            "hadron", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIGAMMA_SMOKE = REGISTER.register(
            "digamma_smoke", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SKELETON = REGISTER.register(
            "skeleton", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLUID_DEBUG = REGISTER.register(
            "fluid_debug", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POWER_DEBUG = REGISTER.register(
            "power_debug", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARK = REGISTER.register(
            "spark", () -> new SimpleParticleType(true));

    // Why we cant just use make one constructor public...
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VANILLA_CLOUD = REGISTER.register(
            "vanilla_cloud", () -> new SimpleParticleType(true));

    public static final ResourceLocation BASE = ResourceLocation.fromNamespaceAndPath(NuclearTechMod.MODID, "textures/particle/base_particle.png");

    public static SpriteSet BASE_PARTICLE_SPRITES;
    public static SpriteSet RBMK_MUSH_SPRITES;
    public static SpriteSet HAZE_SPRITES;
    public static SpriteSet HADRON_SPITES;
    public static SpriteSet DEAD_LEAVES_SPRITES;
    public static SpriteSet AURA_SPITES;
    public static SpriteSet RAD_FOG_SPRITES;
    public static SpriteSet GAS_FLAME_PARTICLES;

    public static SpriteSet POWER_SPRITES;
    public static SpriteSet FLUID_SPRITES;

    public static SpriteSet VANILLA_CLOUD_SPRITES;

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}