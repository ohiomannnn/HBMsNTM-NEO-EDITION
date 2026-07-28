package com.hbm.items.special;

import com.hbm.items.EnumMultiItem;
import com.hbm.items.ISatChip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SatelliteItem extends EnumMultiItem implements ISatChip {

    public SatelliteItem(Properties properties) {
        super(properties, SatType.class, true, true);
    }

    public enum SatType {
        SPY,
        SCANNER,
        RADAR,
        MINER_ASTRO,
        MINER_LUNAR,
        PRECISION_LASER,
        DEATH_RAY,
        XENIUM_RESONATOR,
        RELAY,
        DETECTOR
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item.hbmsntm.obj_sat_chip.frequency", this.getFreq(stack)).withStyle(ChatFormatting.GRAY));
    }
}
