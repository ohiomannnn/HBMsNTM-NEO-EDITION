package com.hbm.items;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

public interface ICustomItemModelRegister {

    void registerItemModel(ItemModelProvider provider, ResourceLocation modelLocation);
}
