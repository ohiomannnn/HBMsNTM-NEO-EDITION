package com.hbm.items.tools;

import com.hbm.blockentity.IGUIProvider;
import com.hbm.inventory.screens.SatelliteInterfaceScreen;
import com.hbm.items.machine.SatChipItem;
import com.hbm.network.toclient.SatellitePanel;
import com.hbm.saveddata.SatelliteSavedData;
import com.hbm.saveddata.satellite.Satellite;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

public class SatelliteInterfaceItem extends SatChipItem implements IGUIProvider {

    @OnlyIn(Dist.CLIENT)
    public static Satellite currentSat;

    public SatelliteInterfaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            if (isSelected) {
                if (level instanceof ServerLevel serverLevel) {
                    Satellite sat = SatelliteSavedData.get(serverLevel).getSatFromFreq(this.getFreq(stack));

                    if (entity instanceof ServerPlayer player) {
                        if (sat != null && entity.tickCount % 2 == 0) {
                            CompoundTag tag = new CompoundTag();
                            sat.writeToNBT(tag);
                            SatellitePanel packet = new SatellitePanel(sat.getID(), tag);

                            PacketDistributor.sendToPlayer(player, packet);
                        }
                    }
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Screen provideScreenOnRightClick(Player player, BlockPos pos) {
        return new SatelliteInterfaceScreen(player);
    }
}
