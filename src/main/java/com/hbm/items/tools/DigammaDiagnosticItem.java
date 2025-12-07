package com.hbm.items.tools;

import com.hbm.lib.ModSounds;
import com.hbm.util.ContaminationUtil;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DigammaDiagnosticItem extends Item {

    public DigammaDiagnosticItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            ContaminationUtil.printDiagnosticData(player);
        }

        return InteractionResultHolder.pass(stack);
    }
}
