package com.hbm.datagen;

import com.hbm.fluids.NtmFluids;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class NtmFluidTagsProvider extends FluidTagsProvider {

    public NtmFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, NuclearTechMod.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(Tags.Fluids.LAVA).add(
                NtmFluids.VOLCANIC_LAVA.get(),
                NtmFluids.VOLCANIC_LAVA_FLOWING.get()
        );

        this.tag(FluidTags.LAVA).add(
                NtmFluids.VOLCANIC_LAVA.get(),
                NtmFluids.VOLCANIC_LAVA_FLOWING.get()
        );
    }
}
