package com.hbm.blockentity.machine.heater;

import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class HeaterOvenBlockEntity extends HeaterFireboxBlockEntity {

    public static int baseHeat = 500;
    public static double timeMult = 0.125D;
    public static int maxHeatEnergy = 500_000;
    public static double heatEff = 0.5D;

    public HeaterOvenBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.HEATER_OVEN.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.heaterOven");
    }

    @Override
    protected int getBaseHeat() {
        return baseHeat;
    }

    @Override
    protected double getTimeMult() {
        return timeMult;
    }

    @Override
    protected int getMaxHeat() {
        return maxHeatEnergy;
    }

    @Override
    public void updateEntity() {
        if(this.level != null && !this.level.isClientSide) {
            this.tryPullHeat();
        }

        super.updateEntity();
    }

    private void tryPullHeat() {
        if(this.level == null) return;
        BlockEntity te = this.level.getBlockEntity(this.getBlockPos().below());
        if(!(te instanceof IHeatSource source)) return;

        int toPull = Math.max(Math.min(source.getHeatStored(), this.getMaxHeat() - this.heatEnergy), 0);
        if(toPull <= 0) return;

        this.heatEnergy += (int) (toPull * heatEff);
        source.useUpHeat(toPull);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new com.hbm.inventory.menus.HeaterOvenMenu(id, inventory, this);
    }
}
