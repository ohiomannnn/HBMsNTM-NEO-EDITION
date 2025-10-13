package com.hbm.items.special;

import com.hbm.CommonEvents;
import com.hbm.explosion.ExplosionLarge;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;


public class PolaroidItem extends Item {
    public PolaroidItem(Properties properties) {
        super(properties);
        CommonEvents.RerollPal();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (!level.isClientSide) {
            ExplosionLarge.spawnParticles(level, player.getX(), player.getY(), player.getZ(), 23);
        }

        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
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
        tooltipComponents.add(Component.literal("Fate chosen"));
        tooltipComponents.add(Component.empty());
        switch (CommonEvents.polaroidID) {
            case 1:
                tooltipComponents.add(Component.literal("..."));
                break;
            case 2:
                tooltipComponents.add(Component.literal("Clear as glass."));
                break;
            case 3:
                tooltipComponents.add(Component.literal("'M"));
                break;
            case 4:
                tooltipComponents.add(Component.literal("It's about time."));
                break;
            case 5:
                tooltipComponents.add(Component.literal("If you stare long into the abyss, the abyss stares back."));
                break;
            case 6:
                tooltipComponents.add(Component.literal("public Party celebration = new Party();"));
                break;
            case 7:
                tooltipComponents.add(Component.literal("V urnerq lbh yvxr EBG13!"));
                break;
            case 8:
                tooltipComponents.add(Component.literal("11011100"));
                break;
            case 9:
                tooltipComponents.add(Component.literal("Vg'f nobhg gvzr."));
                break;
            case 10:
                tooltipComponents.add(Component.literal("Schrabidium dislikes the breeding reactor."));
                break;
            case 11:
                tooltipComponents.add(Component.literal("yss stares back.6public Party cel"));
                break;
            case 12:
                tooltipComponents.add(Component.literal("Red streaks."));
                break;
            case 13:
                tooltipComponents.add(Component.literal("Q1"));
                break;
            case 14:
                tooltipComponents.add(Component.literal("Q2"));
                break;
            case 15:
                tooltipComponents.add(Component.literal("Q3"));
                break;
            case 16:
                tooltipComponents.add(Component.literal("Q4"));
                break;
            case 17:
                tooltipComponents.add(Component.literal("Two friends before christmas."));
                break;
            case 18:
                tooltipComponents.add(Component.literal("Duchess of the boxcars."));
                tooltipComponents.add(Component.empty());
                tooltipComponents.add(Component.literal("\"P.S.: Thirty-one.\""));
                tooltipComponents.add(Component.literal("\"Huh, what does thirty-one mean?\""));
                break;
        }
    }
}