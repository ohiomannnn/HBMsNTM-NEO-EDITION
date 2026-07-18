package com.hbm.world.gen;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NtmBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_OIL_BUBBLE = registerKey("add_oil_bubble");

    public static final ResourceKey<BiomeModifier> ADD_LANDMINE = registerKey("add_landmine");

    public static final ResourceKey<BiomeModifier> ADD_CRASHED_BOMB = registerKey("add_crashed_bomb");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(ADD_OIL_BUBBLE, new AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.OIL_BUBBLE_PLACED)), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_LANDMINE, new AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.LANDMINE_PLACED)), GenerationStep.Decoration.UNDERGROUND_DECORATION));

        context.register(ADD_CRASHED_BOMB, new AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), HolderSet.direct(placedFeatures.getOrThrow(NtmPlacedFeatures.CRASHED_BOMB_PLACED)), GenerationStep.Decoration.UNDERGROUND_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NuclearTechMod.withDefaultNamespace(name));
    }
}
