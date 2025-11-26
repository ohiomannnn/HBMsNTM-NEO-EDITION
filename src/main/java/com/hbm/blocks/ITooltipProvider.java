package com.hbm.blocks;

import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface ITooltipProvider {

    default void addStandardInfo(List<Component> tooltip) {
        if (Screen.hasShiftDown()) {
            for (String s : I18nUtil.resolveKeyArray(((Block) this).getDescriptionId() + ".desc")) {
                tooltip.add(Component.literal(s).withStyle(ChatFormatting.YELLOW));
            }
        } else {
            tooltip.add(
                    Component.literal("Hold <")
                            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
                            .append(Component.literal("LSHIFT").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC))
                            .append(Component.literal("> to display more info").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC))
            );
        }
    }
}
