package com.hbm.hazard.type;

import java.util.List;

import com.hbm.config.ServerConfig;
import com.hbm.extprop.LivingProperties;
import com.hbm.hazard.modifier.HazardModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HazardTypeCoal extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if(ServerConfig.DISABLE_COAL.getAsBoolean())
            return;

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
    public void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers) {
        @SuppressWarnings("unchecked") // no
        List<Component> components = (List<Component>) list;

        components.add(Component.literal("[" + Component.translatable("trait.coal").getString() + "]")
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}