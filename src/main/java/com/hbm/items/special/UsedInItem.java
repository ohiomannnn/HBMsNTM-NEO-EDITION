package com.hbm.items.special;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class UsedInItem extends Item {

    private List<Block> blocks;

    public UsedInItem(Properties properties, List<Block> blocks) {
        super(properties);
        this.blocks = blocks;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item.hbmsntm.used_in").withStyle(ChatFormatting.GRAY));
        blocks.forEach(block -> components.add(Component.literal("  " + block.getName().getString()).withStyle(ChatFormatting.GRAY)));
    }
}
