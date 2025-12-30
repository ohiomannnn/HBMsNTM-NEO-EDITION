package com.hbm.lib;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyConnectorBlock;
import api.hbm.energymk2.IEnergyConnectorMK2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

public class Library {
    public static final Direction POS_X = Direction.EAST;
    public static final Direction NEG_X = Direction.WEST;
    public static final Direction POS_Y = Direction.UP;
    public static final Direction NEG_Y = Direction.DOWN;
    public static final Direction POS_Z = Direction.SOUTH;
    public static final Direction NEG_Z = Direction.NORTH;

    /**
     * Is putting this into this trash can a good idea? No. Do I have a better idea? Not currently.
     * @param dir cable's connecting side
     */
    public static boolean canConnect(BlockGetter level, BlockPos pos, Direction dir) {
        if (pos.getY() > level.getMaxBuildHeight() || pos.getY() < level.getMinBuildHeight()) return false;

        Block b = level.getBlockState(pos).getBlock();

        if (b instanceof IEnergyConnectorBlock con) {

            if (con.canConnect(level, pos, dir.getOpposite() /* machine's connecting side */)) {
                return true;
            }
        }

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof IEnergyConnectorMK2 con) {

            if (con.canConnect(dir.getOpposite() /* machine's connecting side */)) {
                return true;
            }
        }

        return false;
    }

    public static long chargeTEFromItems(ItemStackHandler slots, int index, long power, long maxPower) {

        if (slots.getStackInSlot(index).getItem() instanceof IBatteryItem batteryItem) {

            long batCharge = batteryItem.getCharge(slots.getStackInSlot(index));
            long batRate = batteryItem.getDischargeRate(slots.getStackInSlot(index));
            long toDischarge = Math.min(Math.min((maxPower - power), batRate), batCharge);

            batteryItem.dischargeBattery(slots.getStackInSlot(index), toDischarge);
            power += toDischarge;
        }

        return power;
    }
}
