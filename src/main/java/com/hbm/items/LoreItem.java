package com.hbm.items;

import com.hbm.blocks.ITooltipProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class LoreItem extends Item {

    public LoreItem(Properties properties) { super(properties); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for (String s : ITooltipProvider.getDescriptionWithP11(this.getDescriptionId())) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
    }
}
