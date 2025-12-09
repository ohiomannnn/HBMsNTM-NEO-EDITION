package com.hbm.hazard.type;

import com.hbm.config.MainConfig;
import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.items.ModItems;
import com.hbm.util.BobMathUtil;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HazardTypeRadiation extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {
        boolean reacher = false;

        if (target instanceof Player player) {
            reacher = player.getInventory().contains(new ItemStack(ModItems.REACHER.get()));
        }

        level *= stack.getCount();

        if (level > 0) {
            float rad = level / 20F;

            if (MainConfig.COMMON.ENABLE_528.get() && reacher) {
                rad = rad / 49F;
            } else if (reacher) {
                rad = (float) BobMathUtil.squirt(rad);
            }

            ContaminationUtil.contaminate(target, HazardType.RADIATION, ContaminationType.CREATIVE, rad);
        }
    }

    @Override
    public void updateEntity(ItemEntity item, float level) {}

    @Override
    public void addHazardInformation(Player player, List<Component> components, float level, ItemStack stack, List<HazardModifier> modifiers) {
        level = HazardModifier.evalAllModifiers(stack, player, level, modifiers);

        components.add(Component.literal("[" + I18nUtil.resolveKey("trait.radioactive") + "]").withStyle(ChatFormatting.GREEN));

        String rad = "" + (Math.floor(level * 1000) / 1000);
        components.add(Component.literal(rad + " RAD/s").withStyle(ChatFormatting.YELLOW));

        if (stack.getCount() > 1) {
            double total = Math.floor(level * 1000 * stack.getCount()) / 1000;
            components.add(Component.literal("Stack: " + total + " RAD/s").withStyle(ChatFormatting.YELLOW));
        }
    }
}