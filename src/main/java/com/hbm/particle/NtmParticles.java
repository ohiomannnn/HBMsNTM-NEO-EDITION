package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.particle.vanilla.NbtParticleOption;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class NtmParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, NuclearTechMod.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAS_FLAME = register("gas_flame", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEAD_LEAF = register("dead_leaf", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AURA = register("aura", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEBRIS = register("debris", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AMAT_FLASH = register("amat_flash", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FOAM = register("foam", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ASHES = register("ashes", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIGAMMA_SMOKE = register("digamma_smoke", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SKELETON = register("skeleton", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLUID_DEBUG = register("fluid_debug", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POWER_DEBUG = register("power_debug", true);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARK = register("spark", true);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VANILLA_CLOUD = register("vanilla_cloud", false);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ABM_CONTRAIL = register("abm_contrail", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RADIATION_FOG = register("radiation_fog", false);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LAUNCH_SMOKE = register("launch_smoke", false);
    public static final DeferredHolder<ParticleType<?>, ParticleType<NbtParticleOption>> SWEAT = register("sweat", false, NbtParticleOption::codec, NbtParticleOption::streamCodec);
    public static final DeferredHolder<ParticleType<?>, ParticleType<NbtParticleOption>> VOMIT_NORMAL = register("vomit_normal", false, NbtParticleOption::codec, NbtParticleOption::streamCodec);
    public static final DeferredHolder<ParticleType<?>, ParticleType<NbtParticleOption>> VOMIT_BLOOD =  register("vomit_blood",  false, NbtParticleOption::codec, NbtParticleOption::streamCodec);
    public static final DeferredHolder<ParticleType<?>, ParticleType<NbtParticleOption>> VOMIT_SMOKE =  register("vomit_smoke",  false, NbtParticleOption::codec, NbtParticleOption::streamCodec);
    public static final DeferredHolder<ParticleType<?>, ParticleType<NbtParticleOption>> COOLING_TOWER =  register("cooling_tower",  false, NbtParticleOption::codec, NbtParticleOption::streamCodec);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TOM_BLAST = register("tom_blast", true);

    private static DeferredHolder<ParticleType<?>, SimpleParticleType> register(String name, boolean overrideLimitter) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(overrideLimitter));
    }

    private static <T extends ParticleOptions> DeferredHolder<ParticleType<?>, ParticleType<T>> register(
            String name,
            boolean overrideLimiter,
            final Function<ParticleType<T>, MapCodec<T>> codecGetter,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter
    ) {
        return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter) {
            @Override public MapCodec<T> codec() { return codecGetter.apply(this); }
            @Override public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() { return streamCodecGetter.apply(this); }
        });
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    // move ts
    public static SpriteSet BASE_PARTICLE_SPRITES;
    public static SpriteSet DEAD_LEAVES_SPRITES;
    public static SpriteSet AURA_SPITES;
    public static SpriteSet GAS_FLAME_PARTICLES;
    public static SpriteSet POWER_SPRITES;
    public static SpriteSet FLUID_SPRITES;
    public static SpriteSet VANILLA_CLOUD_SPRITES;
}