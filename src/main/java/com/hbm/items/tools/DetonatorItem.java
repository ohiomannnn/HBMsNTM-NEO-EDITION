package com.hbm.items.tools;

import com.hbm.HBMsNTM;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntities;
import com.hbm.entity.missile.MissileTier1;
import com.hbm.interfaces.IBomb;
import com.hbm.interfaces.IBomb.BombReturnCode;
import com.hbm.lib.ModSounds;
import com.hbm.util.TagsUtil;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DetonatorItem extends Item {
    public DetonatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();

        if (player.isCrouching()) {
            TagsUtil.setInt(stack, "x", context.getClickedPos().getX());
            TagsUtil.setInt(stack, "y", context.getClickedPos().getY());
            TagsUtil.setInt(stack, "z", context.getClickedPos().getZ());

            if (!context.getLevel().isClientSide) {
                player.sendSystemMessage(Component.literal("[Detonator] ").withStyle(ChatFormatting.DARK_AQUA).append(Component.literal("Position set!").withStyle(ChatFormatting.GREEN)));
            }

            context.getLevel().playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 2.0F, 1.0F);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!TagsUtil.hasTag(stack)) {
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.literal("[Detonator] ").withStyle(ChatFormatting.DARK_AQUA).append(Component.literal("No position set!").withStyle(ChatFormatting.RED)));
            }
        } else {
            int x = TagsUtil.getInt(stack, "x", 0);
            int y = TagsUtil.getInt(stack, "y", 0);
            int z = TagsUtil.getInt(stack, "z", 0);

            BlockPos pos = new BlockPos(x,y,z);
            Block block = level.getBlockState(pos).getBlock();

            if (!level.isClientSide) {
                if (block instanceof IBomb bomb) {
                    level.playSound(null, player.blockPosition(), ModSounds.TECH_BLEEP.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
                    BombReturnCode ret = bomb.explode(level, pos);

                    if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get()) {
                        HBMsNTM.LOGGER.info("[DETONATOR] {} detonated {} at {} / {} / {}!", player.getName().getString(), block.getName().getString(), x, y, z);
                    }

                    player.sendSystemMessage(Component.literal("[Detonator] ").withStyle(ChatFormatting.DARK_AQUA).append(Component.translatable(ret.getUnlocalizedMessage()).withStyle(ret.wasSuccessful() ? ChatFormatting.YELLOW : ChatFormatting.RED)));
                } else {
                    player.sendSystemMessage(Component.literal("[Detonator] ").withStyle(ChatFormatting.DARK_AQUA).append(Component.translatable(BombReturnCode.ERROR_NO_BOMB.getUnlocalizedMessage()).withStyle(ChatFormatting.RED)));
                }
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        for (String s : I18nUtil.resolveKeyArray(this.getDescriptionId() + ".desc")) {
            tooltipComponents.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
        if (TagsUtil.hasTag(stack)) {
            tooltipComponents.add(Component.literal(I18nUtil.resolveKey("detonator.setto")
                    + TagsUtil.getInt(stack, "x", 0) + ", "
                    + TagsUtil.getInt(stack, "y", 0) + ", "
                    + TagsUtil.getInt(stack, "z", 0)
            ).withStyle(ChatFormatting.YELLOW));
        } else {
            tooltipComponents.add(Component.translatable("detonator.nopos").withStyle(ChatFormatting.RED));
        }
    }
}
