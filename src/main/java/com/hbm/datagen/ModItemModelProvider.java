package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HBMsNTM.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.GRENADE.get());
        basicItem(ModItems.NOTHING.get());
        basicItem(ModItems.GEIGER_COUNTER.get());
        basicItem(ModItems.REACHER.get());
    }
}