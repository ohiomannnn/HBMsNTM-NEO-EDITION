package com.hbm.world.feature;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NtmFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, NuclearTechMod.MODID);

    public static final DeferredHolder<Feature<?>, OilBubbleFeature> OIL_BUBBLE = FEATURES.register("oil_bubble", () -> new OilBubbleFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, LandmineFeature> LANDMINE = FEATURES.register("landmine", () -> new LandmineFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, CrashedBombFeature> CRASHED_BOMB = FEATURES.register("crashed_bomb", () -> new CrashedBombFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
