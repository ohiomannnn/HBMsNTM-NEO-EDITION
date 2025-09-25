package com.hbm.world.biome;

import com.hbm.HBMsNTM;
import net.minecraft.core.Holder;
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
import net.neoforged.neoforge.registries.RegisterEvent;

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
                .specialEffects(effects(0x505020, 0x606060, 0x6A7039, 0x525A52))
                .build();
    }

    private static Biome makeCraterInnerBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers)
                .specialEffects(effects(0x505020, 0x303030, 0x6A7039, 0x424A42))
                .build();
    }

    private static Biome makeCraterOuterBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers)
                .specialEffects(effects(0x505020, 0x6F6752, 0x6A7039, 0x6B9189))
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

    private static BiomeSpecialEffects effects(int waterColor, int grassColor, int foliageColor, int skyColor) {
        return new BiomeSpecialEffects.Builder()
                .waterColor(waterColor)
                .waterFogColor(0xE0FFAE)
                .grassColorOverride(grassColor)
                .foliageColorOverride(foliageColor)
                .skyColor(skyColor)
                .fogColor(0xC0D8FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build();
    }
}
