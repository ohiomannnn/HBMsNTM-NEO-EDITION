package com.hbm.items.tools;

import com.hbm.HBMsNTM;
import com.hbm.config.ModConfigs;
import com.hbm.interfaces.IBomb;
import com.hbm.lib.ModSounds;
import com.hbm.util.TagsUtil;
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
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class MultiDetonatorItem extends Item {
    public MultiDetonatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if (player.isCrouching()) {
            addLocation(stack, context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ());

            if (!level.isClientSide) {
                player.sendSystemMessage(Component.literal("[Multi Detonator] ").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.literal("Position added!").withStyle(ChatFormatting.GREEN)));
            }

            level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!TagsUtil.hasTag(stack) || getLocations(stack) == null) {
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.literal("[Multi Detonator] ").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.literal("No position set!").withStyle(ChatFormatting.RED)));
            }
        } else {
            if (!player.isCrouching()) {
                int[][] locs = getLocations(stack);

                int success = 0;

                for (int i = 0; i < locs[0].length; i++) {
                    int x = locs[0][i];
                    int y = locs[1][i];
                    int z = locs[2][i];

                    BlockPos pos = new BlockPos(x,y,z);
                    Block block = level.getBlockState(pos).getBlock();

                    if (block instanceof IBomb) {
                        IBomb.BombReturnCode ret = (((IBomb) block).explode(level, x, y, z));

                        if (ret.wasSuccessful()) success++;

                        if (ModConfigs.COMMON.ENABLE_EXTENDED_LOGGING.get()) {
                            HBMsNTM.LOGGER.info("[MULTI DETONATOR] {} detonated {} at {} / {} / {}!", player.getName().getString(), block.getName().getString(), x, y, z);
                        }
                    }
                }

                level.playSound(null, player.blockPosition(), ModSounds.TECH_BLEEP.get(), SoundSource.PLAYERS, 2.0F, 1.0F);

                if (!level.isClientSide) {
                    player.sendSystemMessage(Component.literal("[Multi Detonator] ").withStyle(ChatFormatting.DARK_AQUA)
                            .append(Component.literal("Triggered " + success + "/" + locs[0].length + "!").withStyle(ChatFormatting.YELLOW)));
                }
            } else {
                TagsUtil.setIntArray(stack, "xValues", new int[0]);
                TagsUtil.setIntArray(stack, "yValues", new int[0]);
                TagsUtil.setIntArray(stack, "zValues", new int[0]);

                level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                if (!level.isClientSide) {
                    player.sendSystemMessage(Component.literal("[Multi Detonator] ").withStyle(ChatFormatting.DARK_AQUA)
                            .append(Component.literal("Locations cleared!").withStyle(ChatFormatting.RED)));
                }
            }
        }

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("Shift right-click block to add position,"));
        tooltipComponents.add(Component.literal("right-click to detonate!"));
        tooltipComponents.add(Component.literal("Shift right-click in the air to clear positions."));

        if (!TagsUtil.hasTag(stack) || getLocations(stack) == null) {
            tooltipComponents.add(Component.literal("No positions set!").withStyle(ChatFormatting.RED));
        } else {
            int[][] locs = getLocations(stack);

            for (int i = 0; i < locs[0].length; i++) {
                tooltipComponents.add(Component.literal(locs[0][i] + " / " + locs[1][i] + " / " + locs[2][i]).withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    private static void addLocation(ItemStack stack, int x, int y, int z) {
        int[] xs = TagsUtil.getIntArray(stack, "xValues");
        int[] ys = TagsUtil.getIntArray(stack, "yValues");
        int[] zs = TagsUtil.getIntArray(stack, "zValues");

        TagsUtil.setIntArray(stack, "xValues", ArrayUtils.add(xs, x));
        TagsUtil.setIntArray(stack, "yValues", ArrayUtils.add(ys, y));
        TagsUtil.setIntArray(stack, "zValues", ArrayUtils.add(zs, z));
    }

    private static int[][] getLocations(ItemStack stack) {

        int[] xs = TagsUtil.getIntArray(stack, "xValues");
        int[] ys = TagsUtil.getIntArray(stack, "yValues");
        int[] zs = TagsUtil.getIntArray(stack, "zValues");

        if(xs.length == 0 || ys.length == 0 || zs.length == 0) {
            return null;
        }

        return new int[][] {
                xs, ys, zs
        };
    }
}
