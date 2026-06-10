package com.hbm.items.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlastInfoBlockItem extends BlockItemBase {

    public BlastInfoBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);

        components.add(Component.translatable("block.hbmsntm.desc.blastRes", this.getBlock().getExplosionResistance()).withStyle(ChatFormatting.GOLD));
    }
}
