package com.hbm.hazard.type;

import java.util.List;

import com.hbm.config.ServerConfig;
import com.hbm.hazard.modifier.HazardModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HazardTypeHydroactive extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if(ServerConfig.DISABLE_HYDROACTIVE.getAsBoolean())
            return;

        if(target.isInWaterOrRain() && stack.getCount() > 0) {
            stack.setCount(0);
            target.level().explode(null, target.getX(), target.getY(2), target.getZ(), 1.0F,false, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void updateEntity(ItemEntity item, float level) {

        if(ServerConfig.DISABLE_HYDROACTIVE.getAsBoolean())
            return;

        if(item.isInWaterOrRain()) {
            item.discard();
            item.level().explode(null, item.getX(), item.getY(2), item.getZ(), 1.0F,false, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers) {

        @SuppressWarnings("unchecked") // no
        List<Component> components = (List<Component>) list;

        components.add(Component.literal("[" + Component.translatable("trait.hydro").getString() + "]")
                .withStyle(ChatFormatting.RED));
    }
}