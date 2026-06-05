package com.hbm.blocks;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

public interface ICustomBlockModelRegister {

    void registerModel(BlockStateProvider provider, ResourceLocation modelLocation);
}
