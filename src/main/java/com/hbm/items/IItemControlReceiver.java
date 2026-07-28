package com.hbm.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IItemControlReceiver {

    void receiveControl(Player player, ItemStack stack, CompoundTag tag);
}
