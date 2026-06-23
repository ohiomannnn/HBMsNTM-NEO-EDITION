package com.hbm.items.special;

import com.hbm.inventory.MetaHelper;
import com.hbm.items.ICustomItemModelRegister;
import com.hbm.items.ICustomRarityItem;
import com.hbm.items.IMetaItem;
import com.hbm.main.NuclearTechMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;

public class SoyuzItem extends Item implements IMetaItem, ICustomRarityItem, ICustomItemModelRegister {

    public SoyuzItem(Properties properties) {
        super(properties);
    }

    @Override
    public void getSubItems(Item item, List<ItemStack> stacks) {
        for(int i = 0; i < 3; i++) {
            stacks.add(MetaHelper.newStack(item, 1, i));
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return switch(MetaHelper.getMeta(stack)) {
            case 0 -> Rarity.UNCOMMON;
            case 1 -> Rarity.RARE;
            case 2 -> Rarity.EPIC;
            default -> Rarity.COMMON;
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {

        components.add(Component.translatable("item.hbmsntm.obj_soyuz.skin").withStyle(ChatFormatting.GRAY));

        switch(MetaHelper.getMeta(stack)) {
            case 0 -> components.add(Component.translatable("item.hbmsntm.obj_soyuz.skin.original").withStyle(ChatFormatting.GOLD));
            case 1 -> components.add(Component.translatable("item.hbmsntm.obj_soyuz.skin.luna").withStyle(ChatFormatting.BLUE));
            case 2 -> components.add(Component.translatable("item.hbmsntm.obj_soyuz.skin.post").withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public void registerItemModel(ItemModelProvider provider, ResourceLocation modelLocation) {

        ItemModelBuilder builder = provider.getBuilder(modelLocation.toString());

        for(int i = 0; i < 3; i++) {

            builder.override()
                    .predicate(NuclearTechMod.withDefaultNamespace("item_meta"), i)
                    .model(provider.getBuilder(modelLocation.getPath() + "_" + i)
                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
                            .texture("layer0", ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), "item/soyuz_" + i)))
                    .end();
        }
    }
}
