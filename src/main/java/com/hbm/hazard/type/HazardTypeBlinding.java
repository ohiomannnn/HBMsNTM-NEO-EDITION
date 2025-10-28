package com.hbm.hazard.type;

import com.hbm.config.MainConfig;
import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HazardTypeBlinding extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if (MainConfig.COMMON.DISABLE_BLINDING.get()) return;

        if (!ArmorRegistry.hasProtection(target, 3, ArmorRegistry.HazardClass.LIGHT)) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, (int)Math.ceil(level) * 20, 0, true, false));
        }
    }

    @Override
    public void updateEntity(ItemEntity item, float level) { }

    @Override
    public void addHazardInformation(Player player, List<Component> components, float level, ItemStack stack, List<HazardModifier> modifiers) {
        components.add(Component.literal("[" + I18nUtil.resolveKey("trait.blinding") + "]").withStyle(ChatFormatting.DARK_AQUA));
    }
}