package com.hbm.items.armor;

import com.hbm.handler.ArmorModHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemModCladding extends ItemArmorMod {

    public double rad;

    public ItemModCladding(Properties properties, double rad) {
        super(properties.stacksTo(1), ArmorModHandler.CLADDING, true, true, true, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("armorMod.radRes", rad).withStyle(ChatFormatting.YELLOW));
        tooltipComponents.add(Component.empty());
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
