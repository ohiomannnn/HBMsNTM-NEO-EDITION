package com.hbm.items.machine;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.items.ISatChip;
import com.hbm.items.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SatChipItem extends Item implements ISatChip {

    public SatChipItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("satchip.frequency", this.getFreq(stack)).withStyle(ChatFormatting.GRAY));

        if (this != ModItems.SATELLITE_INTERFACE.get()) {
            for (String s : ITooltipProvider.getDescription(stack)) {
                components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
