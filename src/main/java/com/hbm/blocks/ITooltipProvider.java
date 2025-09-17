package com.hbm.blocks;

import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface ITooltipProvider {

    void addInformation(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag);

    default void addStandardInfo(ItemStack stack, Player player, List<Component> tooltip, boolean ext) {
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

    default Rarity getRarity(ItemStack stack) {
        return Rarity.COMMON;
    }
}
