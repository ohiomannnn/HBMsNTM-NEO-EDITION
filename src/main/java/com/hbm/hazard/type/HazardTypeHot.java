package com.hbm.hazard.type;

import java.util.List;

import com.hbm.config.ServerConfig;
import com.hbm.hazard.modifier.HazardModifier;

import com.hbm.items.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HazardTypeHot extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if(ServerConfig.DISABLE_HOT.getAsBoolean())
            return;

        boolean reacher = false;

        if(target instanceof Player && !ServerConfig.ENABLE_528.getAsBoolean())
            if (target.getMainHandItem().is(ModItems.REACHER.get()) || target.getOffhandItem().is(ModItems.REACHER.get())) {
                reacher = true;
            }

        if(!reacher && !target.isInWaterOrRain() && level > 0)
            target.setRemainingFireTicks((int) Math.ceil(level) * 20);
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

        components.add(Component.literal("[" + Component.translatable("trait.hot").getString() + "]")
                .withStyle(ChatFormatting.GOLD));
    }
}