package com.hbm.particle;

import com.hbm.HBMsNTM;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, HBMsNTM.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BASE_PARTICLE = REGISTER.register(
            "base_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RBMK_MUSH = REGISTER.register(
            "rbmkmush", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HAZE = REGISTER.register(
            "haze", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_CLOUD = REGISTER.register(
            "muke_cloud", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_CLOUD_BF = REGISTER.register(
            "muke_cloud_bf", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_WAVE = REGISTER.register(
            "muke_wave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUKE_FLASH = REGISTER.register(
            "muke_flash", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXPLOSION_SMALL = REGISTER.register(
            "explosion_small", () -> new SimpleParticleType(false));
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
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROCKET_FLAME = REGISTER.register(
            "rocket_flame", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEBRIS = REGISTER.register(
            "debris", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AMAT_FLASH = REGISTER.register(
            "amat_flash", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EX_SMOKE = REGISTER.register(
            "ex_smoke", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FOAM = REGISTER.register(
            "foam", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ASHES = REGISTER.register(
            "ashes", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GIBLET = REGISTER.register(
            "giblet", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HADRON = REGISTER.register(
            "hadron", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIGAMMA_SMOKE = REGISTER.register(
            "digamma_smoke", () -> new SimpleParticleType(true));

    public static final ResourceLocation BASE = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/base_particle.png");

    public static SpriteSet BASE_PARTICLE_SPRITES;
    public static SpriteSet RBMK_MUSH_SPRITES;
    public static SpriteSet HAZE_SPRITES;
    public static SpriteSet MUKE_FLASH_SPRITES;
    public static SpriteSet GIBLET_SPRITES;
    public static SpriteSet HADRON_SPITES;
    public static SpriteSet DEAD_LEAVES_SPRITES;
    public static SpriteSet MUKE_WAVE_SPRITES;
    public static SpriteSet AURA_SPITES;
    public static SpriteSet RAD_FOG_SPRITES;
    public static SpriteSet MUKE_CLOUD_SPRITES;
    public static SpriteSet MUKE_CLOUD_BF_SPRITES;

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}