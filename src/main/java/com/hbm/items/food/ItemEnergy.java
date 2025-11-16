package com.hbm.items.food;

import com.hbm.CommonEvents;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.items.ModItems;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class ItemEnergy extends PotionItem {

    private Item container = null;
    private Item cap = null;
    private boolean requiresOpener = false;

    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    public String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId();
    }

    public ItemEnergy(Properties properties) {
        super(properties);
    }

    public ItemEnergy makeBottle(Item bottle, Item cap) {
        this.container = bottle;
        this.cap = cap;
        this.requiresOpener = true;
        return this;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity drinker) {
        Player player = drinker instanceof Player ? (Player)drinker : null;
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        if (!level.isClientSide) {
            if (this == ModItems.CHOCOLATE_MILK.get()) {
                ExplosionLarge.explode(level, drinker.getX(), drinker.getY(), drinker.getZ(), 50, true, false, false);
            }
            if (this == ModItems.BOTTLE_NUKA.get()) {
                drinker.heal(4F);
                drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 1));
                drinker.addEffect(new MobEffectInstance(MobEffects.JUMP, 30 * 20, 1));
                ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 5.0F);
            }
            if (this == ModItems.BOTTLE_CHERRY.get()) {
                drinker.heal(6F);
                drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 0));
                drinker.addEffect(new MobEffectInstance(MobEffects.JUMP, 30 * 20, 2));
                ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 5.0F);
            }
            if (this == ModItems.BOTTLE_QUANTUM.get()) {
                drinker.heal(10F);
                drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 1));
                drinker.addEffect(new MobEffectInstance(MobEffects.JUMP, 30 * 20, 2));
                drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30 * 20, 1));
                ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 15.0F);
            }
            if (this == ModItems.BOTTLE_SPARKLE.get()) {
                drinker.heal(10F);
                drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 1));
                drinker.addEffect(new MobEffectInstance(MobEffects.JUMP, 30 * 20, 2));
                drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30 * 20, 1));
                drinker.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 30 * 20, 1));
                ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 5.0F);
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        if (player != null && !player.hasInfiniteMaterials()) {
            if (this.cap != null) {
                player.getInventory().add(new ItemStack(this.cap));
            }
            if (this.container != null) {
                player.getInventory().add(new ItemStack(this.container));
            }
        }

        drinker.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!this.requiresOpener) return super.use(level, player, hand);

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.BOTTLE_OPENER)) {
                return super.use(level, player, hand);
            }
        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (this == ModItems.BOTTLE_NUKA.get()) {
            tooltipComponents.add(Component.literal("Contains about 210 kcal and 1500 mSv."));
        }
        if (this == ModItems.BOTTLE_CHERRY.get()) {
            tooltipComponents.add(Component.literal("Now with severe radiation poisoning in every seventh bottle!"));
        }
        if (this == ModItems.BOTTLE_QUANTUM.get()) {
            tooltipComponents.add(Component.literal("Comes with a colorful mix of over 70 isotopes!"));
        }
        if (this == ModItems.BOTTLE_SPARKLE.get()) {
            if (CommonEvents.polaroidID == 11) {
                tooltipComponents.add(Component.literal("Contains trace amounts of taint."));
            } else {
                tooltipComponents.add(Component.literal("The most delicious beverage in the wasteland!"));
            }
        }
    }
}
