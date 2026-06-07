package com.hbm.datagen;

import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmDamageTypes;
import com.hbm.registry.NtmBiomes;
import com.hbm.world.gen.NtmBiomeModifiers;
import com.hbm.world.gen.NtmConfiguredFeatures;
import com.hbm.world.gen.NtmPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = NuclearTechMod.MODID)
public class NtmDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        // Datapack things
        RegistrySetBuilder builder = new RegistrySetBuilder();
        builder.add(Registries.DAMAGE_TYPE, NtmDamageTypes::bootstrap);
        builder.add(Registries.BIOME, NtmBiomes::bootstrap);
        builder.add(Registries.CONFIGURED_FEATURE, NtmConfiguredFeatures::bootstrap);
        builder.add(Registries.PLACED_FEATURE, NtmPlacedFeatures::bootstrap);
        builder.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NtmBiomeModifiers::bootstrap);
        DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(output, lookup, builder, Set.of(NuclearTechMod.MODID));
        generator.addProvider(event.includeServer(), datapackProvider);

        // Client things
        generator.addProvider(event.includeClient(), new NtmItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new NtmBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new NtmSoundDefinitionsProvider(output, helper));
        generator.addProvider(event.includeClient(), new NtmLanguageProvider(output));

        // Server things
        LootTableProvider.SubProviderEntry blockLootTableSubProvider = new LootTableProvider.SubProviderEntry(NtmBlockLootTableProvider::new, LootContextParamSets.BLOCK);
        generator.addProvider(event.includeServer(), (DataProvider.Factory<LootTableProvider>) lootTableOutput -> new LootTableProvider(lootTableOutput, Collections.emptySet(), List.of(blockLootTableSubProvider), event.getLookupProvider()));

        BlockTagsProvider blockTagsProvider = new NtmBlockTagProvider(output, lookup, helper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new NtmItemTagProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new NtmDamageTypeTagsProvider(output, lookup, helper));
        generator.addProvider(event.includeServer(), new NtmFluidTagsProvider(output, lookup, helper));
        generator.addProvider(event.includeServer(), new NtmRecipeProvider(output, lookup));
    }
}
