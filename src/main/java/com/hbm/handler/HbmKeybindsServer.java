package com.hbm.handler;

import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.items.IKeybindReceiver;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HbmKeybindsServer {

    /** Can't put this in HbmKeybinds because it's littered with clientonly stuff */
    public static void onPressedServer(Player player, EnumKeybind key, boolean state) {

        // EXTPROP HANDLING
        HbmPlayerAttachments.setKeyPressed(player, key, state);

        // ITEM HANDLING
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (held.getItem() instanceof IKeybindReceiver rec) {
            if (rec.canHandleKeybind(player, held, key)) rec.handleKeybind(player, held, key, state);
        }
    }
}
