package com.hbm.items.tools;

import com.hbm.blockentity.IGUIProvider;
import com.hbm.inventory.screens.SatelliteInterfaceScreen;
import com.hbm.items.machine.SatChipItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toclient.SatellitePanel;
import com.hbm.saveddata.SatelliteSavedData;
import com.hbm.saveddata.satellite.Satellite;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class SatelliteInterfaceItem extends SatChipItem implements IGUIProvider {

    @OnlyIn(Dist.CLIENT) @Nullable public Satellite satellite = null;

    public SatelliteInterfaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if(level instanceof ServerLevel serverLevel) {
            Satellite sat = SatelliteSavedData.getData(serverLevel).getSatFromFreq(this.getFreq(stack));

            if(sat != null) {
                CompoundTag tag = new CompoundTag();
                sat.saveAdditional(tag);

                if(player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new SatellitePanel(sat.getID(), tag));
                }
            }
        }

        NuclearTechMod.proxy.openScreen(player, BlockPos.ZERO);

        return InteractionResultHolder.pass(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object provideScreen(Player player, BlockPos pos) {
        return new SatelliteInterfaceScreen(player, this.satellite);
    }
}
