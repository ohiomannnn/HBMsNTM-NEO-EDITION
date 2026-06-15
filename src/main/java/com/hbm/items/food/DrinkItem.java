package com.hbm.items.food;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.inventory.MetaHelper;
import com.hbm.items.EnumMultiItem;
import com.hbm.items.ItemEnums.CapType;
import com.hbm.items.NtmItems;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import com.hbm.util.EnumUtil;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class DrinkItem extends EnumMultiItem {

    public enum DrinkType {
        COFFEE(LAMBDA_COFFEE, ItemStack.EMPTY, ItemStack.EMPTY, false),
        COFFEE_RADIUM(LAMBDA_COFFEE_RADIUM, ItemStack.EMPTY, ItemStack.EMPTY, false),
        EMPTY(null, ItemStack.EMPTY, ItemStack.EMPTY, false),
        NUKA(LAMBDA_NUKA, MetaHelper.newStack(NtmItems.DRINK, EMPTY), MetaHelper.newStack(NtmItems.CAP, CapType.NUKA), true),
        CHERRY(LAMBDA_CHERRY, MetaHelper.newStack(NtmItems.DRINK, EMPTY), MetaHelper.newStack(NtmItems.CAP, CapType.NUKA), true),
        QUANTUM(LAMBDA_QUANTUM, MetaHelper.newStack(NtmItems.DRINK, EMPTY), MetaHelper.newStack(NtmItems.CAP, CapType.QUANTUM), true),
        SPARKLE(LAMBDA_SPARKLE, MetaHelper.newStack(NtmItems.DRINK, EMPTY), MetaHelper.newStack(NtmItems.CAP, CapType.SPARKLE), true),
        RAD(LAMBDA_RAD, MetaHelper.newStack(NtmItems.DRINK, EMPTY), MetaHelper.newStack(NtmItems.CAP, CapType.RAD), true),
        EMPTY2(null, ItemStack.EMPTY, ItemStack.EMPTY, false),
        KORL(LAMBDA_KORL, MetaHelper.newStack(NtmItems.DRINK, EMPTY2), MetaHelper.newStack(NtmItems.CAP, CapType.KORL), true),
        FRITZ(LAMBDA_FRITZ, MetaHelper.newStack(NtmItems.DRINK, EMPTY2), MetaHelper.newStack(NtmItems.CAP, CapType.FRITZ), true),
        ;

        public final @Nullable Consumer<LivingEntity> finishUsingItem;

        public final ItemStack container;
        public final ItemStack cap;
        public final boolean requiresOpener;

        DrinkType(@Nullable Consumer<LivingEntity> lambda, ItemStack container, ItemStack cap, boolean requiresOpener) {
            this.finishUsingItem = lambda;
            this.container = container;
            this.cap = cap;
            this.requiresOpener = requiresOpener;
        }
    }

    public static final Consumer<LivingEntity> LAMBDA_COFFEE = (drinker) -> {
        drinker.heal(10F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60 * 20, 2));
    };
    public static final Consumer<LivingEntity> LAMBDA_COFFEE_RADIUM = (drinker) -> {
        drinker.heal(10F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60 * 20, 2));
        HbmLivingAttachments.incrementRadiation(drinker, 500F);
    };
    public static final Consumer<LivingEntity> LAMBDA_NUKA = (drinker) -> {
        drinker.heal(4F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 1));
        drinker.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 30 * 20, 1));
        ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 5.0F);
    };
    public static final Consumer<LivingEntity> LAMBDA_CHERRY = (drinker) -> {
        drinker.heal(6F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 0));
        drinker.addEffect(new MobEffectInstance(MobEffects.JUMP, 30 * 20, 2));
        ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 5.0F);
    };
    public static final Consumer<LivingEntity> LAMBDA_QUANTUM = (drinker) -> {
        drinker.heal(10F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 1));
        drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30 * 20, 2));
        drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30 * 20, 1));
        ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 15.0F);
    };
    public static final Consumer<LivingEntity> LAMBDA_SPARKLE = (drinker) -> {
        drinker.heal(10F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120 * 20, 1));
        drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120 * 20, 2));
        drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120 * 20, 2));
        drinker.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 120 * 20, 1));
        ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 5.0F);
    };
    public static final Consumer<LivingEntity> LAMBDA_RAD = (drinker) -> {
        drinker.heal(10F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120 * 20, 1));
        drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120 * 20, 2));
        drinker.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120 * 20, 0));
        drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120 * 20, 4));
        drinker.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 120 * 20, 1));
        ContaminationUtil.contaminate(drinker, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 15.0F);
    };
    public static final Consumer<LivingEntity> LAMBDA_KORL = (drinker) -> {
        drinker.heal(6F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 1));
        drinker.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 30 * 20, 2));
        drinker.addEffect(new MobEffectInstance(MobEffects.JUMP, 30 * 20, 2));
    };
    public static final Consumer<LivingEntity> LAMBDA_FRITZ = (drinker) -> {
        drinker.heal(6F);
        drinker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 1));
        drinker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30 * 20, 2));
        drinker.addEffect(new MobEffectInstance(MobEffects.JUMP, 30 * 20, 2));
    };

    public DrinkItem(Properties properties) {
        super(properties, DrinkType.class, true, true);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity drinker) {

        Player player = drinker instanceof Player ? (Player) drinker : null;
        if(player instanceof ServerPlayer serverPlayer) CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);

        DrinkType type = EnumUtil.grabEnumSafely(DrinkType.class, MetaHelper.getMeta(stack));
        if(!level.isClientSide) if(type.finishUsingItem != null) type.finishUsingItem.accept(drinker);

        if(player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        if(player != null && !player.hasInfiniteMaterials()) {
            if(!type.cap.isEmpty()) player.getInventory().add(type.cap);
            if(!type.container.isEmpty()) player.getInventory().add(type.container);
        }

        drinker.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override public int getUseDuration(ItemStack stack, LivingEntity entity) { return 32; }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.DRINK; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack inHand = player.getItemInHand(hand);
        DrinkType type = EnumUtil.grabEnumSafely(DrinkType.class, MetaHelper.getMeta(inHand));

        if(type.finishUsingItem == null) return InteractionResultHolder.pass(inHand);
        if(!type.requiresOpener) return ItemUtils.startUsingInstantly(level, player, hand);

        for(ItemStack stack : player.getInventory().items) {
            if(stack.is(NtmItems.BOTTLE_OPENER)) return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResultHolder.pass(inHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for(String s : ITooltipProvider.getDescriptionWithP11(this.getDescriptionId())) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }

        DrinkType type = EnumUtil.grabEnumSafely(DrinkType.class, MetaHelper.getMeta(stack));
        if(type.requiresOpener) components.add(Component.translatable("requires.opener").withStyle(ChatFormatting.GRAY));
    }
}
