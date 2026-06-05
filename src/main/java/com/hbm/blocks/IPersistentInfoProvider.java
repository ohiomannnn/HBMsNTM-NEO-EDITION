package com.hbm.blocks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public interface IPersistentInfoProvider {

    void appendHoverText(ItemStack stack, CompoundTag tag, List<Component> components, TooltipContext context, TooltipFlag flag);
}
