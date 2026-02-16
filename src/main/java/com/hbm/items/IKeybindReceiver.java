package com.hbm.items;

import com.hbm.handler.HbmKeybinds.EnumKeybind;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IKeybindReceiver {
    boolean canHandleKeybind(Player player, ItemStack stack, EnumKeybind keybind);
    void handleKeybind(Player player, ItemStack stack, EnumKeybind keybind, boolean state);
    @OnlyIn(Dist.CLIENT) default void handleKeybindClient(LocalPlayer player, ItemStack stack, EnumKeybind keybind, boolean state) { }
}