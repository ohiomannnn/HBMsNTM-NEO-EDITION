package com.hbm.items.special;

import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public class PolaroidItem extends Item {

    public static int polaroidID = 1;
    public static int generalOverride = 0;

    public static void rerollPal() {
        // Reroll Polaroid
        if(generalOverride > 0 && generalOverride < 19) {
            polaroidID = generalOverride;
        } else {
            polaroidID = RANDOM.nextInt(18) + 1;
            while (polaroidID == 4 || polaroidID == 9)
                polaroidID = RANDOM.nextInt(18) + 1;
        }
    }

    private static final Random RANDOM = new Random();

    public PolaroidItem(Properties properties) {
        super(properties);
        rerollPal();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(entity instanceof LivingEntity living) {
            if(living.getHealth() < 10F) {
                living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 2));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.hbmsntm.obj_polaroid.desc").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        for(String s : I18nUtil.resolveKeyArray("item.hbmsntm.obj_polaroid.fate" + PolaroidItem.polaroidID)) {
            tooltipComponents.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
    }
}