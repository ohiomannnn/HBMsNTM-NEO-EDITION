package com.hbm.hazard.type;

import com.hbm.config.MainConfig;
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

public class HazardTypeExplosive extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {
        if (MainConfig.COMMON.DISABLE_EXPLOSIVE.get()) return;

        if (!target.level().isClientSide && (target.isOnFire() || target.isInLava()) && stack.getCount() > 0) {
            stack.setCount(0);
            target.level().explode(null, target.getX(), target.getY() + 2, target.getZ(), level, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    public void updateEntity(ItemEntity item, float lvl) {

        if (MainConfig.COMMON.DISABLE_EXPLOSIVE.get()) return;

        if (item.isOnFire() || item.isInLava()) {
            item.discard();
            item.level().explode(null, item.getX(), item.getY(), item.getZ(), lvl, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    public void addHazardInformation(Player player, List<Component> components, float lvl, ItemStack stack, List<HazardModifier> modifiers) {
        components.add(Component.literal("[" + I18nUtil.resolveKey("trait.explosive") + "]").withStyle(ChatFormatting.RED));
    }
}