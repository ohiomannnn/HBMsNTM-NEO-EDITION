package com.hbm.world.gen;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import java.util.List;

public class NtmPlacedFeatures {

    public static final ResourceKey<PlacedFeature> OIL_BUBBLE_PLACED = registerKey("oil_bubble_placed");

    public static final ResourceKey<PlacedFeature> LANDMINE_PLACED = registerKey("landmine_placed");

    public static final ResourceKey<PlacedFeature> CRASHED_BOMB_PLACED = registerKey("crashed_bomb_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, OIL_BUBBLE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.OIL_BUBBLE), List.of(InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24))));
        register(context, LANDMINE_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.LANDMINE), List.of(RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG)));
        register(context, CRASHED_BOMB_PLACED, configuredFeatures.getOrThrow(NtmConfiguredFeatures.CRASHED_BOMB), List.of(RarityFilter.onAverageOnceEvery(500), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG)));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, NuclearTechMod.withDefaultNamespace(name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
