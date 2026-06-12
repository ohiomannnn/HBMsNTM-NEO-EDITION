package com.hbm.blocks.generic;

import com.hbm.blocks.NtmBlocks;
import com.hbm.util.i18n.I18nUtil;
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
        for(String s : I18nUtil.resolveKeyArray("block.hbmsntm.obj_no_spawn.desc")) {
            components.add(Component.literal(s).withStyle(ChatFormatting.RED));
        }
    }
}
