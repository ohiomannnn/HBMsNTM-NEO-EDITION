package com.hbm.items.armor;

import net.minecraft.world.item.Item;

public class ItemArmorMod extends Item {

    public final int type;
    public final boolean helmet;
    public final boolean chestplate;
    public final boolean leggings;
    public final boolean boots;

    public ItemArmorMod(Properties properties, int type, boolean helmet, boolean chestplate, boolean leggings, boolean boots) {
        super(properties);
        this.type = type;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }
}
