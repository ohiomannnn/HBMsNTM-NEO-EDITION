package com.hbm.items.tools;

import com.hbm.HBMsNTM;
import com.hbm.items.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class ItemKeyPin extends Item {
    public ItemKeyPin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int pins = getPins(stack);
        if (pins != 0) {
            tooltipComponents.add(Component.literal("Pin configuration: " + pins));
        } else {
            tooltipComponents.add(Component.literal("Pins not set!"));
        }

        if (this == ModItems.KEY_FAKE.get()) {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.literal("Pins can neither be changed, nor copied."));
        }
    }

    public static int getPins(ItemStack stack) {
        CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
        if (custom != null) {
            CompoundTag tag = custom.copyTag();
            return tag.getInt("pins");
        }
        return 0;
    }

    public static void setPins(ItemStack stack, int pins) {
        CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
        tag.putInt("pins", pins);
    }

//    public boolean canTransfer() {
//        return this != ModItems.key_fake;
//    }
}
