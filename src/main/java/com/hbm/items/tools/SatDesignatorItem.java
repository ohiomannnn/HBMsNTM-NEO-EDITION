package com.hbm.items.tools;

import com.hbm.items.machine.SatChipItem;
import com.hbm.lib.Library;
import com.hbm.saveddata.SatelliteSavedData;
import com.hbm.saveddata.satellite.SatelliteBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class SatDesignatorItem extends SatChipItem {

    public SatDesignatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack stack = player.getItemInHand(usedHand);

        if(level instanceof ServerLevel serverLevel) {
            SatelliteBase sat = SatelliteSavedData.getData(serverLevel).getSatFromFreq(this.getFreq(stack));

            if(sat != null) {
                BlockHitResult pos = Library.rayTrace(player, 300, 1);

                sat.onCoordAction(level, player, pos.getBlockPos().relative(pos.getDirection()));
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}
