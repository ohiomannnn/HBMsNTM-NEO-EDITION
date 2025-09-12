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


public class HazardTypeAsbestos extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if(ServerConfig.DISABLE_ASBESTOS.getAsBoolean())
            return;

//        if(!ArmorRegistry.hasProtection(target, 3, HazardClass.PARTICLE_FINE))
//            HbmLivingProps.incrementAsbestos(target, (int) Math.min(level, 10));
//        else
//            ArmorUtil.damageGasMaskFilter(target, (int) level);
        LivingProperties.incrementAsbestos(target, (int) Math.min(level, 10));
    }

    @Override
    public void updateEntity(ItemEntity item, float level) { }

    @Override
    public void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers) {
        @SuppressWarnings("unchecked") // no
        List<Component> components = (List<Component>) list;

        components.add(Component.literal("[" + Component.translatable("trait.asbestos").getString() + "]")
                .withStyle(ChatFormatting.WHITE));
    }
}