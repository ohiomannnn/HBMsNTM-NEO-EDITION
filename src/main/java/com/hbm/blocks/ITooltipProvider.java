package com.hbm.blocks;

import com.hbm.items.special.PolaroidItem;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
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

    static String[] getDescription(ItemStack stack) {
        return I18nUtil.resolveKeyArray(stack.getDescriptionId() + ".desc");
    }

    @Nullable
    static String[] getDescriptionOrNull(ItemStack stack) {
        if (I18nUtil.resolveKey(stack.getDescriptionId() + ".desc").equals(" ")) return null;
        return I18nUtil.resolveKeyArray(stack.getDescriptionId() + ".desc");
    }

    static String[] getDescriptionWithP11(String unlocalizedName) {
        String wDesc = unlocalizedName + ".desc";

        if (PolaroidItem.polaroidID == 11) {
            String keyP11 = wDesc + ".P11";
            String[] arrP11 = I18nUtil.resolveKeyArray(keyP11);

            if (!arrP11[0].equals(keyP11)) {
                return arrP11;
            }
        }
        return I18nUtil.resolveKeyArray(wDesc);
    }
}
