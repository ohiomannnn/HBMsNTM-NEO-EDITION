package com.hbm.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IEquipReceiver {

    void onEquip(Player player, ItemStack stack);
}
