package com.hbm.items.special;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.items.ModItems;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class CigaretteItem extends Item {

    public CigaretteItem(Properties properties) { super(properties); }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 30;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity drinker) {
        Player player = drinker instanceof Player ? (Player)drinker : null;
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        if (!level.isClientSide) {
            if (this == ModItems.CIGARETTE.get()) {
                HbmLivingAttachments.incrementBlackLung(drinker, 2000);
                HbmLivingAttachments.incrementAsbestos(drinker, 2000);
                HbmLivingAttachments.incrementRadiation(drinker, 100F);
            }
            if (this == ModItems.CRACKPIPE.get()) {
                HbmLivingAttachments.incrementBlackLung(drinker, 500);
                drinker.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                drinker.heal(10F);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.COUGH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            CompoundTag tag = new CompoundTag();
            tag.putString("type", "vomit");
            tag.putString("mode", "smoke");
            tag.putInt("count", 30);
            tag.putInt("entity", player.getId());
            if (level instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, player.getX(), player.getY(), player.getZ(), 25, new AuxParticle(tag, 0, 0, 0));
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        drinker.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (this == ModItems.CIGARETTE.get()) {
            for (String s : ITooltipProvider.getDescription(stack)) {
                components.add(Component.translatable(s).withStyle(ChatFormatting.RED));
            }
        }
        if (this == ModItems.CRACKPIPE.get()) {
            ChatFormatting[] formattings = new ChatFormatting[] {
                    ChatFormatting.RED,
                    ChatFormatting.GOLD,
                    ChatFormatting.YELLOW,
                    ChatFormatting.GREEN,
                    ChatFormatting.AQUA,
                    ChatFormatting.BLUE,
                    ChatFormatting.DARK_PURPLE,
                    ChatFormatting.LIGHT_PURPLE,
            };
            int len = 2000;
            components.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable(this.getDescriptionId() + ".desc1").withStyle(formattings[(int)(System.currentTimeMillis() % len * formattings.length / len)])));
        }
    }
}
