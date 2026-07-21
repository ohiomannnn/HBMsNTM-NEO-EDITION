package com.hbm.items;

import com.hbm.inventory.MetaHelper;
import com.hbm.items.component.NtmDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BoltItem extends Item implements IMetaItem {

    public BoltItem(Properties properties) {
        super(properties.component(NtmDataComponents.META.get(), Type.STEEL.meta));
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

    public int getLightColor(ItemStack stack) {
        return Type.byMeta(MetaHelper.getMeta(stack)).lightColor;
    }

    public int getDarkColor(ItemStack stack) {
        return Type.byMeta(MetaHelper.getMeta(stack)).darkColor;
    }

    public enum Type {
        TUNGSTEN(7400, "hbmmat.tungsten", 0x868686, 0x000000),
        LEAD(8200, "hbmmat.lead", 0xA6A6B2, 0x03030F),
        STEEL(30, "hbmmat.steel", 0xAFAFAF, 0x0F0F0F),
        DURA_STEEL(33, "hbmmat.durasteel", 0x82A0DC, 0x06281E);

        public final int meta;
        public final String materialTranslationKey;
        public final int lightColor;
        public final int darkColor;

        Type(int meta, String materialTranslationKey, int lightColor, int darkColor) {
            this.meta = meta;
            this.materialTranslationKey = materialTranslationKey;
            this.lightColor = lightColor;
            this.darkColor = darkColor;
        }

        public static Type byMeta(int meta) {
            for(Type type : values()) {
                if(type.meta == meta) return type;
            }
            return STEEL;
        }
    }
}
