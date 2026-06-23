package com.hbm.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public interface ICustomRarityItem {

    Rarity getRarity(ItemStack stack);
}
