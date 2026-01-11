package com.hbm.datagen;

import com.hbm.HBMsNTM;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

public class ModAtlasProvider extends SpriteSourceProvider {
    public ModAtlasProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HBMsNTM.MODID, existingFileHelper);
    }

    @Override
    protected void gather() {
        SourceList blocksAtlas = atlas(BLOCKS_ATLAS);

        blocksAtlas.addSource(new DirectoryLister("models/machines", "models/machines/"));
    }
}
