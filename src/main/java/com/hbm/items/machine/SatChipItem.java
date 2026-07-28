package com.hbm.items.machine;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.items.ISatChip;
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
        components.add(Component.translatable("item.hbmsntm.obj_sat_chip.frequency", this.getFreq(stack)).withStyle(ChatFormatting.GRAY));

        String[] lines = ITooltipProvider.getDescriptionOrNull(stack);
        if(lines != null) {
            for(String line : lines) {
                components.add(Component.translatable(line).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
