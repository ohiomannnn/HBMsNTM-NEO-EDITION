package com.hbm.items;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;

public interface ICustomModelRegister {

    void registerModel(ModelProvider<ItemModelBuilder> provider, ResourceLocation loc);
}
