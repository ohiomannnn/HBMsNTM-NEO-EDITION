package com.hbm.hazard.type;

import com.hbm.config.ModConfigs;
import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class HazardTypeHydroactive extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if (ModConfigs.COMMON.DISABLE_HYDROACTIVE.get()) return;

        if (target.isInWaterOrRain() && stack.getCount() > 0) {
            stack.setCount(0);
            target.level().explode(null, target.getX(), target.getY() + 2, target.getZ(), level,false, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void updateEntity(ItemEntity item, float level) {

        if (ModConfigs.COMMON.DISABLE_HYDROACTIVE.get()) return;

        if (item.isInWaterOrRain()) {
            item.discard();
            item.level().explode(null, item.getX(), item.getY(), item.getZ(), level, false, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    public void addHazardInformation(Player player, List<Component> components, float level, ItemStack stack, List<HazardModifier> modifiers) {
        components.add(Component.literal("[" + I18nUtil.resolveKey("trait.hydro") + "]").withStyle(ChatFormatting.RED));
    }
}