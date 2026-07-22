package com.hbm.items.tools;

import com.hbm.blocks.NtmBlocks;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import java.util.List;

public class OilDetectorItem extends Item {

    public OilDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(!level.isClientSide) {
            scanAndReport(level, player);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), NtmSoundEvents.TECH_BLEEP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable(getDescriptionId(stack) + ".desc1").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable(getDescriptionId(stack) + ".desc2").withStyle(ChatFormatting.GRAY));
    }

    private void scanAndReport(Level level, Player player) {
        int startY = player.getBlockY() + 15;
        int playerX = player.getBlockX();
        int playerZ = player.getBlockZ();

        SearchResult direct = scanColumn(level, playerX, playerZ, startY);
        if(direct != SearchResult.NONE) {
            report(player, direct, true);
            return;
        }

        RandomSource random = level.getRandom();
        for(int i = 0; i < 50; i++) {
            int sampleX = playerX + (int) (random.nextGaussian() * 25.0D);
            int sampleZ = playerZ + (int) (random.nextGaussian() * 25.0D);
            SearchResult result = scanColumn(level, sampleX, sampleZ, startY);
            if(result != SearchResult.NONE) {
                report(player, result, false);
                return;
            }
        }

        player.displayClientMessage(Component.translatable(getDescriptionId() + ".noOil").withStyle(ChatFormatting.RED), false);
    }

    private SearchResult scanColumn(Level level, int x, int z, int startY) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for(int y = startY; y >= level.getMinBuildHeight(); y--) {
            pos.set(x, y, z);
            if(isBedrockOil(level, pos)) {
                return SearchResult.BEDROCK_OIL;
            }
            if(isOil(level, pos)) {
                return SearchResult.OIL;
            }
        }
        return SearchResult.NONE;
    }

    private void report(Player player, SearchResult result, boolean direct) {
        String base = getDescriptionId();
        String key = switch(result) {
            case BEDROCK_OIL -> direct ? base + ".bullseyeBedrock" : base + ".detectedBedrock";
            case OIL -> direct ? base + ".bullseye" : base + ".detected";
            default -> base + ".noOil";
        };

        ChatFormatting color = switch(result) {
            case BEDROCK_OIL -> direct ? ChatFormatting.DARK_GREEN : ChatFormatting.GOLD;
            case OIL -> direct ? ChatFormatting.GREEN : ChatFormatting.YELLOW;
            default -> ChatFormatting.RED;
        };

        player.displayClientMessage(Component.translatable(key).withStyle(color), false);
    }

    private static boolean isOil(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos).is(NtmBlocks.ORE_OIL.get());
    }

    private static boolean isBedrockOil(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos).is(NtmBlocks.ORE_BEDROCK_OIL.get());
    }

    private enum SearchResult {
        NONE,
        OIL,
        BEDROCK_OIL
    }
}
