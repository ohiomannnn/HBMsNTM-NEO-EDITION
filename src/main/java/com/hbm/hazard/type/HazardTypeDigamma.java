package com.hbm.hazard.type;

import java.util.List;

import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.util.ContaminationUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HazardTypeDigamma extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {
        ContaminationUtil.applyDigammaData(target, level / 20F);
    }

    @Override
    public void updateEntity(ItemEntity item, float level) { }

    @Override
    public void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers) {
        level = HazardModifier.evalAllModifiers(stack, player, level, modifiers);

        if (level < 1e-5) {
            return;
        }

        @SuppressWarnings("unchecked") // no
        List<Component> components = (List<Component>) list;

        components.add(Component.literal("[" + Component.translatable("trait.digamma").getString() + "]")
                .withStyle(ChatFormatting.RED));
        String dig = "" + (float)(Math.floor(level * 10000F)) / 10F;
        components.add(Component.literal(dig + "mDRX/s").withStyle(ChatFormatting.RED));

        if (stack.getCount() > 1) {
            components.add(Component.literal("Stack: " + ((Math.floor(level * 10000F * stack.getCount()) / 10F) + "mDRX/s")).withStyle(ChatFormatting.DARK_RED));
        }
    }
}