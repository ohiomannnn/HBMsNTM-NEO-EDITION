package com.hbm.world.biome;

import com.hbm.HBMsNTM;
import com.hbm.particle.ModParticles;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES =
            DeferredRegister.create(Registries.BIOME, HBMsNTM.MODID);

    public static final ResourceKey<Biome> CRATER =
            ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "crater"));
    public static final ResourceKey<Biome> CRATER_INNER =
            ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "crater_inner"));
    public static final ResourceKey<Biome> CRATER_OUTER =
            ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "crater_outer"));

    public static void register(IEventBus eventBus) {
        BIOMES.register(eventBus);
    }

    public static void bootstrap(BootstrapContext<Biome> ctx) {
        HolderGetter<PlacedFeature> placed = ctx.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = ctx.lookup(Registries.CONFIGURED_CARVER);

        ctx.register(CRATER, makeCraterBiome(placed, carvers));
        ctx.register(CRATER_INNER, makeCraterInnerBiome(placed, carvers));
        ctx.register(CRATER_OUTER, makeCraterOuterBiome(placed, carvers));
    }

    private static Biome makeCraterBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers)
                .specialEffects(effects(0x10161C, 0x606060, 0x6A7039, 0x525A52, 0.01F))
                .build();
    }

    private static Biome makeCraterOuterBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers)
                .specialEffects(effects(0x10161C, 0x6F6752, 0x6A7039, 0x6B9189, 0))
                .build();
    }

    private static Biome makeCraterInnerBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers)
                .specialEffects(effects(0x10161C, 0x303030, 0x6A7039, 0x424A42, 0.01F))
                .build();
    }

    private static Biome.BiomeBuilder baseBuilder(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placed, carvers);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.8F)
                .downfall(0.0F)
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation.build());
    }

    private static BiomeSpecialEffects effects(int waterColor, int grassColor, int foliageColor, int skyColor, float particleCount) {
        return new BiomeSpecialEffects.Builder()
                .ambientParticle(new AmbientParticleSettings(ModParticles.AURA.get(), particleCount))
                .waterColor(waterColor)
                .waterFogColor(waterColor)
                .grassColorOverride(grassColor)
                .foliageColorOverride(foliageColor)
                .skyColor(skyColor)
                .fogColor(skyColor)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build();
    }
}
