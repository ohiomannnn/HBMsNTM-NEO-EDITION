package com.hbm.world.gen;

import com.hbm.main.NuclearTechMod;
import com.hbm.world.feature.NtmFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class NtmConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> LANDMINE = registerKey("landmine");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRASHED_BOMB = registerKey("crashed_bomb");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        register(context, LANDMINE, NtmFeatures.LANDMINE.get(), NoneFeatureConfiguration.INSTANCE);

        register(context, CRASHED_BOMB, NtmFeatures.CRASHED_BOMB.get(), NoneFeatureConfiguration.INSTANCE);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, NuclearTechMod.withDefaultNamespace(path));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
