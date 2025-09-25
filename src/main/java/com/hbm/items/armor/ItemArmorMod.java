package com.hbm.items.armor;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;

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

    public void modUpdate(LivingEntity entity, ItemStack armor) { }

    public void modDamage(ArmorHurtEvent event, ItemStack armor) { }

    public Multimap getModifiers(ItemStack armor) { return null; }

}
