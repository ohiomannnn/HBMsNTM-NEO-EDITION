package com.hbm.items;

import com.hbm.inventory.MetaHelper;
import com.hbm.items.component.NtmDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WireDenseItem extends Item implements IMetaItem {

    public WireDenseItem(Properties properties) {
        super(properties.component(NtmDataComponents.META.get(), Type.GOLD.meta));
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
        GOLD(7900, "hbmmat.gold", 0xFFFF8B),
        SCHRABIDIUM(12626, "hbmmat.schrabidium", 0x32FFFF),
        SCHRABIDATE(12600, "hbmmat.schrabidate", 0x77C0D7),
        TITANIUM(2200, "hbmmat.titanium", 0xF7F3F2),
        COPPER(2900, "hbmmat.copper", 0xFDCA88),
        TUNGSTEN(7400, "hbmmat.tungsten", 0x868686),
        NEODYMIUM(6000, "hbmmat.neodymium", 0xE6E6B6),
        NIOBIUM(4100, "hbmmat.niobium", 0xB76EC9),
        RED_COPPER(31, "hbmmat.red_copper", 0xFFBA7D),
        STARMETAL(35, "hbmmat.starmetal", 0xCCCCEA),
        BSCCO(48, "hbmmat.bscco", 0x767BF1),
        MAGNETIZED_TUNGSTEN(38, "hbmmat.magnetized_tungsten", 0x22A2A2),
        DINEUTRONIUM(45, "hbmmat.dineutronium", 0x7582B9);

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
            return GOLD;
        }
    }
}
