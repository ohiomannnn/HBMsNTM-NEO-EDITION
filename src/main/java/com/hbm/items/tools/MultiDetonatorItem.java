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
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.List;

public class MultiDetonatorItem extends Item {
    public MultiDetonatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();

        if (!level.isClientSide) {
            if (player.isShiftKeyDown()) {
                addLocation(stack, context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ());

                level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                player.sendSystemMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.translatable("detonator.pos_set").withStyle(ChatFormatting.GREEN)));
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide) {
            if (!TagsUtilDegradation.containsAnyTag(stack) || getLocations(stack) == null) {
                player.sendSystemMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.translatable("detonator.no_pos").withStyle(ChatFormatting.RED)));
            } else {
                if (!player.isCrouching()) {
                    int[][] locs = getLocations(stack);

                    int success = 0;

                    for (int i = 0; i < locs[0].length; i++) {
                        int x = locs[0][i];
                        int y = locs[1][i];
                        int z = locs[2][i];

                        BlockPos pos = new BlockPos(x, y, z);
                        Block block = level.getBlockState(pos).getBlock();

                        if (block instanceof IBomb bomb) {
                            BombReturnCode ret = bomb.explode(level, pos);

                            if (ret.wasSuccessful()) success++;

                            if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get()) {
                                HBMsNTM.LOGGER.info("[MULTI DETONATOR] {} detonated {} at {} / {} / {}!", player.getName().getString(), block.getName().getString(), x, y, z);
                            }
                        }
                    }

                    level.playSound(null, player.blockPosition(), ModSounds.TECH_BLEEP.get(), SoundSource.PLAYERS, 2.0F, 1.0F);

                    player.sendSystemMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA).append(Component.literal("Triggered " + success + "/" + locs[0].length + "!").withStyle(ChatFormatting.YELLOW)));
                } else {
                    CompoundTag tag = TagsUtilDegradation.getTag(stack);
                    tag.putIntArray("xValues", new int[0]);
                    tag.putIntArray("yValues", new int[0]);
                    tag.putIntArray("zValues", new int[0]);
                    TagsUtilDegradation.putTag(stack, tag);

                    level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    player.sendSystemMessage(Component.literal("[" + this.getName(stack).getString() + "] ").withStyle(ChatFormatting.DARK_AQUA).append(Component.literal("Positions cleared!").withStyle(ChatFormatting.RED)));
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        for (String s : ITooltipProvider.getDescription(stack)) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
        if (!TagsUtilDegradation.containsAnyTag(stack) || getLocations(stack) == null) {
            components.add(Component.translatable("detonator.no_pos.multi").withStyle(ChatFormatting.RED));
        } else {
            components.add(Component.translatable("detonator.set_to.multi").withStyle(ChatFormatting.YELLOW));
            int[][] locs = getLocations(stack);
            for (int i = 0; i < locs[0].length; i++) {
                components.add(Component.literal(locs[0][i] + " / " + locs[1][i] + " / " + locs[2][i]).withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    private static void addLocation(ItemStack stack, int x, int y, int z) {
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        int[] xs = tag.getIntArray("xValues");
        int[] ys = tag.getIntArray("yValues");
        int[] zs = tag.getIntArray("zValues");

        tag.putIntArray("xValues", ArrayUtils.add(xs, x));
        tag.putIntArray("yValues", ArrayUtils.add(ys, y));
        tag.putIntArray("zValues", ArrayUtils.add(zs, z));

        TagsUtilDegradation.putTag(stack, tag);
    }

    @Nullable
    private static int[][] getLocations(ItemStack stack) {

        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        int[] xs = tag.getIntArray("xValues");
        int[] ys = tag.getIntArray("yValues");
        int[] zs = tag.getIntArray("zValues");

        if (xs.length == 0 || ys.length == 0 || zs.length == 0) {
            return null;
        }

        return new int[][] { xs, ys, zs };
    }
}
