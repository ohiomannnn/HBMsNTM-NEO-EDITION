package com.hbm.items.food;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.items.NtmItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

@Deprecated
public class EnergyItem extends PotionItem {

    public EnergyItem(Properties properties) {
        super(properties);
    }

    public ItemStack getDefaultInstance() { return new ItemStack(this); }
    public String getDescriptionId(ItemStack stack) { return this.getDescriptionId(); }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity drinker) {
        Player player = drinker instanceof Player ? (Player)drinker : null;
        if(player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        if(this == NtmItems.CHOCOLATE_MILK.get()) {
            if(level instanceof ServerLevel serverLevel) {
                ExplosionLarge.explode(serverLevel, drinker.getX(), drinker.getY(), drinker.getZ(), 50, true, false, false, drinker);
            }
        }

        if(player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        drinker.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for(String s : ITooltipProvider.getDescription(stack)) components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
    }
}
