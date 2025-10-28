package com.hbm.hazard.type;

import com.hbm.config.MainConfig;
import com.hbm.extprop.LivingProperties;
import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HazardTypeCoal extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if (MainConfig.COMMON.DISABLE_COAL.get()) return;

//        if(!ArmorRegistry.hasProtection(target, 3, HazardClass.PARTICLE_COARSE)) {
//            HbmLivingProps.incrementBlackLung(target, (int) Math.min(level * stack.stackSize, 10));
//        } else {
//            if(target.getRNG().nextInt(Math.max(65 - stack.stackSize, 1)) == 0) {
//                ArmorUtil.damageGasMaskFilter(target, (int) level);
//            }
//        }
        LivingProperties.incrementBlackLung(target, (int) Math.min(level * stack.getCount(), 10));
    }

    @Override
    public void updateEntity(ItemEntity item, float level) { }

    @Override
    public void addHazardInformation(Player player, List<Component> components, float level, ItemStack stack, List<HazardModifier> modifiers) {
        components.add(Component.literal("[" + I18nUtil.resolveKey("trait.coal") + "]").withStyle(ChatFormatting.DARK_GRAY));
    }
}