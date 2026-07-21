package com.hbm.items;

import com.hbm.inventory.MetaHelper;
import com.hbm.items.component.NtmDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RawIngotItem extends Item implements IMetaItem {

    public RawIngotItem(Properties properties) {
        super(properties.component(NtmDataComponents.META.get(), Type.REDSTONE.meta));
    }

    @Override
    public void getSubItems(Item item, List<ItemStack> stacks) {
        for(Type type : Type.values()) {
            stacks.add(MetaHelper.newStack(item, 1, type.meta));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Type type = Type.byMeta(MetaHelper.getMeta(stack));
        return Component.translatable(this.getDescriptionId(), Component.translatable(type.materialTranslationKey));
    }

    public int getColor(ItemStack stack) {
        return Type.byMeta(MetaHelper.getMeta(stack)).color;
    }

    public enum Type {
        REDSTONE(1, "hbmmat.redstone", 0xE3330C),
        NEODYMIUM(6000, "hbmmat.neodymium", 0xE6E6F6),
        BORAX(501, "hbmmat.borax", 0xFFFFFF),
        SODIUM(1100, "hbmmat.sodium", 0xD3C81E),
        STRONTIUM(3800, "hbmmat.strontium", 0xF1E03A),
        SLAG(41, "hbmmat.slag", 0x554A00);

        public final int meta;
        public final String materialTranslationKey;
        public final int color;

        Type(int meta, String materialTranslationKey, int color) {
            this.meta = meta;
            this.materialTranslationKey = materialTranslationKey;
            this.color = color;
        }

        public static Type byMeta(int meta) {
            for(Type type : values()) {
                if(type.meta == meta) return type;
            }
            return REDSTONE;
        }
    }
}
