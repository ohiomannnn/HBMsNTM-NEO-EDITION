package com.hbm.blocks.special;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ConcreteBrickMarked extends Block {
    public ConcreteBrickMarked(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult useWithoutItem(BlockState state,
                                            Level level,
                                            BlockPos pos,
                                            Player player,
                                            BlockHitResult hit) {
        if (!level.isClientSide) {
            player.sendSystemMessage(Component.literal("You should not have come here.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("This is not a place of honor. No great deed is commemorated here.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("Nothing of value is here.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("What is here is dangerous and repulsive.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("We considered ourselves a powerful culture. We harnessed the hidden fire, and used it for our own purposes.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("Then we saw the fire could burn within living things, unnoticed until it destroyed them.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("And we were afraid.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("We built great tombs to hold the fire for one hundred thousand years, after which it would no longer kill.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("If this place is opened, the fire will not be isolated from the world, and we will have failed to protect you.").withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("Leave this place and never come back.").withStyle(ChatFormatting.RED));
        }
        return InteractionResult.SUCCESS;
    }
}
