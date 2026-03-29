package com.hbm.registry;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

@SuppressWarnings("removal")
public class NtmBiomes {

    public static final ResourceKey<Biome> CRATER = ResourceKey.create(Registries.BIOME, NuclearTechMod.withDefaultNamespace("crater"));
    public static final ResourceKey<Biome> CRATER_INNER = ResourceKey.create(Registries.BIOME, NuclearTechMod.withDefaultNamespace("crater_inner"));
    public static final ResourceKey<Biome> CRATER_OUTER = ResourceKey.create(Registries.BIOME, NuclearTechMod.withDefaultNamespace("crater_outer"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(CRATER, makeCraterBiome(placed, carvers));
        context.register(CRATER_INNER, makeCraterInnerBiome(placed, carvers));
        context.register(CRATER_OUTER, makeCraterOuterBiome(placed, carvers));
    }

    private static Biome makeCraterBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers).specialEffects(effects(0x10161C, 0x606060, 0x6A7039, 0x525A52, CRATER_COLOR_MOD.getValue())).build();
    }

    private static Biome makeCraterOuterBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers).specialEffects(effects(0x10161C, 0x6F6752, 0x6A7039, 0x6B9189, CRATER_OUTER_COLOR_MOD.getValue())).build();
    }

    private static Biome makeCraterInnerBiome(HolderGetter<PlacedFeature> placed, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return baseBuilder(placed, carvers).specialEffects(effects(0x10161C, 0x303030, 0x6A7039, 0x424A42, CRATER_INNER_COLOR_MOD.getValue())).build();
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

    private static BiomeSpecialEffects effects(int waterColor, int grassColor, int foliageColor, int skyColor, GrassColorModifier grassMod) {
        return new BiomeSpecialEffects.Builder()
                .waterColor(waterColor)
                .waterFogColor(waterColor)
                .grassColorOverride(grassColor)
                .grassColorModifier(grassMod)
                .foliageColorOverride(foliageColor)
                .skyColor(skyColor)
                .fogColor(skyColor)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build();
    }

    public static final EnumProxy<GrassColorModifier> CRATER_COLOR_MOD = new EnumProxy<>(
            GrassColorModifier.class,
            "hbmsntm:crater_color_mod",
            (GrassColorModifier.ColorModifier) (x, z, color) -> {
                double noise = Biome.BIOME_INFO_NOISE.getValue(x * 0.225, z * 0.225, false);
                return noise < -0.1 ? 0x606060 : 0x505050;
            });

    public static final EnumProxy<GrassColorModifier> CRATER_OUTER_COLOR_MOD = new EnumProxy<>(
            GrassColorModifier.class,
            "hbmsntm:crater_outer_color_mod",
            (GrassColorModifier.ColorModifier) (x, z, color) -> {
                double noise = Biome.BIOME_INFO_NOISE.getValue(x * 0.225, z * 0.225, false);
                return noise < -0.1 ? 0x776F59 : 0x6F6752;
            });

    public static final EnumProxy<GrassColorModifier> CRATER_INNER_COLOR_MOD = new EnumProxy<>(
            GrassColorModifier.class,
            "hbmsntm:crater_inner_color_mod",
            (GrassColorModifier.ColorModifier) (x, z, color) -> {
                double noise = Biome.BIOME_INFO_NOISE.getValue(x * 0.225, z * 0.225, false);
                return noise < -0.1 ? 0x404040 : 0x303030;
            });
}
