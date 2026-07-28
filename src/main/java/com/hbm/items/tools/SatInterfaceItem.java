package com.hbm.items.tools;

import com.hbm.blockentity.IScreenProvider;
import com.hbm.inventory.screens.SatCoordScreen;
import com.hbm.items.IItemControlReceiver;
import com.hbm.items.machine.SatChipItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.saveddata.SatelliteSavedData;
import com.hbm.saveddata.satellite.SatelliteBase;
import com.hbm.util.TagsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SatInterfaceItem extends SatChipItem implements IScreenProvider, IItemControlReceiver {

    public static final String KEY_NBT_CONNECTED = "connected";

    public SatInterfaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        NuclearTechMod.proxy.openScreen(player, BlockPos.ZERO);

        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

        if(!(level instanceof ServerLevel serverLevel)) return;
        if(!isSelected) return;

        SatelliteBase sat = SatelliteSavedData.getData(serverLevel).getSatFromFreq(this.getFreq(stack));

        CompoundTag tag = TagsUtil.getCustomData(stack);
        tag.putBoolean(KEY_NBT_CONNECTED, sat != null);
        TagsUtil.putCustomData(stack, tag);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object provideScreen(Player player, BlockPos pos) {
        return new SatCoordScreen(player);
    }

    @Override
    public void receiveControl(Player player, ItemStack stack, CompoundTag tag) {
        if(!(player.level instanceof ServerLevel serverLevel)) return;
        SatelliteBase sat = SatelliteSavedData.getData(serverLevel).getSatFromFreq(this.getFreq(stack));
        if(sat != null) sat.onCoordAction(serverLevel, player, new BlockPos(tag.getInt("x"), tag.contains("y") ? tag.getInt("y") : -1, tag.getInt("z")));
    }
}
