package com.hbm.items;

import com.hbm.handler.KeyHandler.EnumKeybind;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IKeybindReceiver {
    boolean canHandleKeybind(Player player, ItemStack stack, EnumKeybind keybind);
    void handleKeybind(Player player, ItemStack stack, EnumKeybind keybind, boolean state);
    default void handleKeybindClient(LocalPlayer player, ItemStack stack, EnumKeybind keybind, boolean state) { }
}