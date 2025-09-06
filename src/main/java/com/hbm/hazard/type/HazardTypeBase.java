package com.hbm.hazard.type;

import com.hbm.hazard.modifier.HazardModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public abstract class HazardTypeBase {
    public abstract void onUpdate(LivingEntity target, float level, ItemStack stack);

    public abstract void updateEntity(ItemEntity item, float level);

    @OnlyIn(Dist.CLIENT)
    public abstract void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers);
}
