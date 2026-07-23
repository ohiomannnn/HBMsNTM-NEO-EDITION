package com.hbm.items.tools;

import com.hbm.registry.NtmSoundEvents;
import com.hbm.world.MeteorStrikeSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class MeteorRemoteItem extends Item {

    public MeteorRemoteItem(Properties properties) {
        super(properties.stacksTo(1).durability(2));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

        if (!level.isClientSide) {
            MeteorStrikeSystem.spawnMeteorAtPlayer(player, false);
        } else {
            player.displayClientMessage(Component.translatable("chat.meteorremote.watchhead"), true);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), NtmSoundEvents.TECH_BLEEP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        player.swing(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item.meteor_remote.desc").withStyle(ChatFormatting.GRAY));
    }
}
