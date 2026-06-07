package com.hbm.blocks;

import com.hbm.inventory.MetaHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IMultiBlock {

    int getMeta(BlockState state);
    int getSubCount();

    default void getSubItems(Item item, List<ItemStack> stacks) {
        for(int i = 0; i < this.getSubCount(); i++) {
            stacks.add(MetaHelper.newStack(item, i));
        }
    }

    default String getItemDescriptionId(ItemStack stack) {
        return null;
    }

    default int rectify(int meta) {
        return Math.abs(meta % getSubCount());
    }
}
