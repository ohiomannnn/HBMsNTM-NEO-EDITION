package com.hbm.items.tools;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.config.MainConfig;
import com.hbm.interfaces.IBomb;
import com.hbm.interfaces.IBomb.BombReturnCode;
import com.hbm.lib.ModSounds;
import com.hbm.util.TagsUtilDegradation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();

        if (!level.isClientSide) {
            if (player.isCrouching()) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("x", context.getClickedPos().getX());
                tag.putInt("y", context.getClickedPos().getY());
                tag.putInt("z", context.getClickedPos().getZ());
                TagsUtilDegradation.putTag(stack, tag);

                level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                player.displayClientMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.translatable("detonator.pos_set").withStyle(ChatFormatting.GREEN)), false);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide) {
            if (!TagsUtilDegradation.containsAnyTag(stack)) {
                player.displayClientMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.translatable("detonator.no_pos").withStyle(ChatFormatting.RED)), false);
            } else {
                CompoundTag tag = TagsUtilDegradation.getTag(stack);
                int x = tag.getInt("x");
                int y = tag.getInt("y");
                int z = tag.getInt("z");

                BlockPos pos = new BlockPos(x, y, z);
                Block block = level.getBlockState(pos).getBlock();

                if (block instanceof IBomb bomb) {
                    level.playSound(null, player.blockPosition(), ModSounds.TECH_BLEEP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    BombReturnCode ret = bomb.explode(level, pos);

                    if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get()) {
                        HBMsNTM.LOGGER.info("[DETONATOR] {} detonated {} at {} / {} / {}!", player.getName().getString(), block.getName().getString(), x, y, z);
                    }

                    player.displayClientMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA)
                            .append(Component.translatable(ret.getUnlocalizedMessage()).withStyle(ret.wasSuccessful() ? ChatFormatting.YELLOW : ChatFormatting.RED)), false);
                } else {
                    player.displayClientMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA)
                            .append(Component.translatable(BombReturnCode.ERROR_NO_BOMB.getUnlocalizedMessage()).withStyle(ChatFormatting.RED)), false);
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for (String s : ITooltipProvider.getDescription(stack)) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
        if (TagsUtilDegradation.containsAnyTag(stack)) {
            CompoundTag tag = TagsUtilDegradation.getTag(stack);
            int x = tag.getInt("x");
            int y = tag.getInt("y");
            int z = tag.getInt("z");
            components.add(Component.translatable("detonator.set_to", x, y, z).withStyle(ChatFormatting.YELLOW));
        } else {
            components.add(Component.translatable("detonator.no_pos").withStyle(ChatFormatting.RED));
        }
    }
}
