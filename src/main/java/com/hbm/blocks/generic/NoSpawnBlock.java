package com.hbm.blocks.generic;

import com.hbm.blocks.NtmBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class NoSpawnBlock extends Block {

    public NoSpawnBlock(Properties properties) {
        super(properties.isValidSpawn(NtmBlocks::noSpawn));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("block.hbmsntm.desc.nospawn").withStyle(ChatFormatting.RED));
    }
}
