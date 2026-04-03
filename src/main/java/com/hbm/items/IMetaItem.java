package com.hbm.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * We do not need to have Item.setHasSubtypes in here, because if item has this interface it means it have meta things
 */
public interface IMetaItem extends ICustomModelRegister {

    void getSubItems(Item item, List<ItemStack> stacks);
}
