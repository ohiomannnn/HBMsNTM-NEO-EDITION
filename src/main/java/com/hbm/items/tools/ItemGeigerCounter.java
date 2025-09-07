package com.hbm.items.tools;

import com.hbm.lib.ModSounds;
import com.hbm.util.ContaminationUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemGeigerCounter extends Item {
    public ItemGeigerCounter(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ContaminationUtil.printGeigerData((ServerPlayer) player);
        }
        player.playSound(ModSounds.TECH_BOOP.get(), 1.0f, 1.0f);

        return InteractionResultHolder.pass(stack);
    }
}