package com.hbm.items;

import com.hbm.handler.HbmKeybinds.EnumKeybind;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IKeybindReceiver {

    boolean canHandleKeybind(Player player, ItemStack stack, EnumKeybind keybind);
    void handleKeybind(Player player, ItemStack stack, EnumKeybind keybind, boolean state);
    default void handleKeybindClient(Player player, ItemStack stack, EnumKeybind keybind, boolean state) { }
}