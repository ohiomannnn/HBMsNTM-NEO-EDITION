package com.hbm.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IItemControlReceiver {
    void receiveControl(ItemStack stack, CompoundTag tag);
}
