package com.hbm.handler;

import com.hbm.HBMsNTM;
import com.hbm.extprop.PlayerProperties;
import com.hbm.items.IKeybindReceiver;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HbmKeybindsServer {
    public static void onPressedServer(Player player, HbmKeybinds.EnumKeybind key, boolean state) {

        HBMsNTM.LOGGER.info("KEYBIND {}, state = {}", key, state);

        // EXTPROP HANDLING
        PlayerProperties props = PlayerProperties.getData(player);
        props.setKeyPressed(key, state);

        // ITEM HANDLING
        ItemStack held = player.getMainHandItem();
        if (held != null && held.getItem() instanceof IKeybindReceiver rec) {
            if (rec.canHandleKeybind(player, held, key)) rec.handleKeybind(player, held, key, state);
        }
    }
}
