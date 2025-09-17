package com.hbm.items.tools;

import com.hbm.blockentity.machine.LockableBaseBlockEntity;
import com.hbm.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class CounterfitKeysItem extends KeyItem {
    public CounterfitKeysItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.PASS;

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof LockableBaseBlockEntity locked) {
            ItemStack stack = new ItemStack(ModItems.KEY_FAKE.get());
            KeyPinItem.setPins(stack, locked.getPins());

            player.setItemInHand(context.getHand(), stack.copy());

            if (!player.getInventory().add(stack.copy())) {
                player.drop(stack.copy(), false);
            }

            player.containerMenu.broadcastChanges();

            player.swing(context.getHand(), true);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("Use on a locked container to create two counterfeit keys!"));
    }
}
