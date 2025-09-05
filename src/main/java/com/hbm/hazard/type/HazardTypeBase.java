package com.hbm.hazard.type;

import com.hbm.hazard.modifier.HazardModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public abstract class HazardTypeBase {

    /**
     * Does the thing. Called by HazardEntry.applyHazard
     */
    public abstract void onUpdate(LivingEntity target, float level, ItemStack stack);

    /**
     * Updates the hazard for dropped items. Used for things like explosive and hydroactive items.
     */
    public abstract void updateEntity(ItemEntity item, float level);

    /**
     * Adds item tooltip info. Called by Item.addInformation
     */
    @OnlyIn(Dist.CLIENT)
    public abstract void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers);
}
