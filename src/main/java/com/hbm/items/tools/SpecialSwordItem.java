package com.hbm.items.tools;

import com.hbm.blocks.ITooltipProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SpecialSwordItem extends SwordItem {

    public static final Consumer<LivingEntity> LAMBDA_OPENER_HURT_ENEMY = (target) -> {
        Level level = target.level;

        if(!level.isClientSide) {
            int i = level.random.nextInt(7);
            if(i == 0) target.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.BLINDNESS, 5 * 60 * 20, 0)));
            if(i == 1) target.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 60 * 20, 2)));
            if(i == 2) target.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 5 * 60 * 20, 2)));
            if(i == 3) target.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.CONFUSION, 1 * 60 * 20, 0)));
            level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ANVIL_LAND, SoundSource.AMBIENT, 3.0F, 1.0F);
        }
    };

    @Nullable
    private Consumer<LivingEntity> hurtEnemy;

    public SpecialSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public SpecialSwordItem setHurtEnemy(Consumer<LivingEntity> lambda) {
        this.hurtEnemy = lambda;
        return this;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if(hurtEnemy != null) this.hurtEnemy.accept(target);

        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for(String s : ITooltipProvider.getDescription(stack)) components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
    }
}
