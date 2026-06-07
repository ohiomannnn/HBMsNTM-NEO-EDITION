package com.hbm.world.gen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;

import java.util.List;

public class NtmConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRASHED_BOMB = registerKey("crashed_bomb");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        register(context, CRASHED_BOMB, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                new RandomizedIntStateProvider(
                                        BlockStateProvider.simple(NtmBlocks.CRASHED_BOMB.get().defaultBlockState()),
                                        NtmBlockStateProperties.META,
                                        BiasedToBottomInt.of(0, 4)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.STONE, Blocks.SAND, Blocks.SANDSTONE),
                        1
                )
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, NuclearTechMod.withDefaultNamespace(path));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
