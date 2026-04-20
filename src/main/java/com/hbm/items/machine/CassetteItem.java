package com.hbm.items.machine;

import com.hbm.items.IMetaItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;

import java.util.List;

public class CassetteItem extends Item implements IMetaItem {

    public enum TrackType {

    }

    public enum SoundType {
        LOOP,
        PASS,
        SOUND
    }

    public CassetteItem(Properties properties) {
        super(properties);
    }

    @Override
    public void getSubItems(Item item, List<ItemStack> stacks) {

    }

    @Override
    public void registerModel(ModelProvider<ItemModelBuilder> provider, ResourceLocation modelLocation) {
        provider.getBuilder(modelLocation.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), "item/cassette"))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), "item/cassette_overlay"));
    }
}
