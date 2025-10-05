package com.hbm.hazard.type;

import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HazardTypeDigamma extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {
        ContaminationUtil.applyDigammaData(target, level / 20F);
    }

    @Override
    public void updateEntity(ItemEntity item, float level) { }

    @Override
    public void addHazardInformation(Player player, List<Component> components, float level, ItemStack stack, List<HazardModifier> modifiers) {
        level = HazardModifier.evalAllModifiers(stack, player, level, modifiers);

        float digamma = (float) (Math.floor(level * 10000F)) / 10F;
        components.add(Component.literal("[" + I18nUtil.resolveKey("trait.digamma") + "]").withStyle(ChatFormatting.RED));
        components.add(Component.literal(digamma + "mDRX/s"));

        if (stack.getCount() > 1) {
            components.add(Component.literal("Stack: " + ((Math.floor(level * 10000F * stack.getCount()) / 10F) + "mDRX/s")).withStyle(ChatFormatting.DARK_RED));
        }
    }
}