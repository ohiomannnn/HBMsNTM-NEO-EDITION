package com.hbm.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public interface IBlockMulti {

    int getSubCount();

    default String getUnlocalizedName(ItemStack stack) {
        return ((Block) this).getDescriptionId();
    }

    default String getOverrideDisplayName(ItemStack stack) {
        return null;
    }

    default int rectify(int index) {
        return Math.abs(index % getSubCount());
    }
}
