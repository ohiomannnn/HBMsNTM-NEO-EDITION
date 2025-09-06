package com.hbm.hazard.type;

import java.util.List;

import com.hbm.config.ServerConfig;
import com.hbm.hazard.modifier.HazardModifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HazardTypeBlinding extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if(ServerConfig.DISABLE_BLINDING.getAsBoolean())
            return;

//        if(!ArmorRegistry.hasProtection(target, 3, HazardClass.LIGHT)) {
//            target.addEffect(new PotionEffect(Potion.blindness.id, (int)Math.ceil(level), 0));
//        }
        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, (int)Math.ceil(level) * 20, 0, true, false));
    }

    @Override
    public void updateEntity(ItemEntity item, float level) { }

    @Override
    public void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers) {
        @SuppressWarnings("unchecked") // no
        List<Component> components = (List<Component>) list;

        components.add(Component.literal("[" + Component.translatable("trait.blinding").getString() + "]")
                .withStyle(ChatFormatting.DARK_AQUA));
    }
}