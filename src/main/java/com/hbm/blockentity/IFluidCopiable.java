package com.hbm.blockentity;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import api.hbm.fluidmk2.IFluidUserMK2;
import com.hbm.interfaces.ICopiable;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.BobMathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;


public interface IFluidCopiable extends ICopiable {
    /**
     * @return First type for the normal paste, second type for the alt paste,
     *         none if there is no alt paste support
     */
    default int[] getFluidIDToCopy() {
        IFluidUserMK2 be = (IFluidUserMK2) this;
        ArrayList<Integer> types = new ArrayList<>();

        for (FluidTank tank : be.getAllTanks()) {
            if (!tank.getTankType().hasNoID()) types.add(tank.getTankType().getID());
        }

        return BobMathUtil.intCollectionToArray(types);
    }

    default FluidTank getTankToPaste() {
        BlockEntity be = (BlockEntity) this;
        if (be instanceof IFluidStandardTransceiverMK2 fst) { // why are we using the transceiver here?
            return fst.getReceivingTanks() != null ? fst.getReceivingTanks()[0] : null;
        }
        return null;
    }

    @Override
    default CompoundTag getSettings(Level level, BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        if (getFluidIDToCopy().length > 0) tag.putIntArray("FluidID", getFluidIDToCopy());
        return tag;
    }

    @Override
    default void pasteSettings(CompoundTag tag, int index, Level level, Player player, BlockPos pos) {
        if (getTankToPaste() != null) {
            int[] ids = tag.getIntArray("FluidID");
            if (ids.length > 0 && index < ids.length) {
                int id = ids[index];
                getTankToPaste().setTankType(Fluids.fromID(id));
            }
        }
    }

    @Override
    default String[] infoForDisplay(Level level, BlockPos pos) {
        int[] ids = getFluidIDToCopy();
        String[] names = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            names[i] = Fluids.fromID(ids[i]).getUnlocalizedName();
        }
        return names;
    }
}
