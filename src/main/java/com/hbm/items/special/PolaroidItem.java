package com.hbm.items.special;

import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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

    public static void RerollPal() {
        // Reroll Polaroid
        if (generalOverride > 0 && generalOverride < 19) {
            polaroidID = generalOverride;
        } else {
            polaroidID = rand.nextInt(18) + 1;
            while (polaroidID == 4 || polaroidID == 9)
                polaroidID = rand.nextInt(18) + 1;
        }
    }

    private static Random rand = new Random();

    public PolaroidItem(Properties properties) {
        super(properties);
        RerollPal();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            if (player.getHealth() < 10F) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 2));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.hbmsntm.polaroid.fate").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        for (String s : I18nUtil.resolveKeyArray("item.hbmsntm.polaroid.fate" + PolaroidItem.polaroidID)) {
            tooltipComponents.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
    }
}