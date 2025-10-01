package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.world.biome.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldgenData extends DatapackBuiltinEntriesProvider {

    public ModWorldgenData(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries,
                new RegistrySetBuilder().add(Registries.BIOME, ModBiomes::bootstrap),
                Set.of(HBMsNTM.MODID));
    }
}
