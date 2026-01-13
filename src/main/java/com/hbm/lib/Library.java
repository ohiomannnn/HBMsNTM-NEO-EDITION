package com.hbm.lib;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyConnectorBlock;
import api.hbm.energymk2.IEnergyConnectorMK2;
import com.hbm.interfaces.Spaghetti;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.items.ItemStackHandler;

@Spaghetti("this whole class")
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

    //not great either but certainly better
    public static long chargeItemsFromTE(NonNullList<ItemStack> slots, int index, long power, long maxPower) {

        if (power < 0) return 0;
        if (power > maxPower) return maxPower;

        if (slots.get(index).getItem() instanceof IBatteryItem batteryItem) {

            long batMax = batteryItem.getMaxCharge(slots.get(index));
            long batCharge = batteryItem.getCharge(slots.get(index));
            long batRate = batteryItem.getChargeRate(slots.get(index));
            long toCharge = Math.min(Math.min(power, batRate), batMax - batCharge);

            power -= toCharge;

            batteryItem.chargeBattery(slots.get(index), toCharge);
        }

        return power;
    }

    public static long chargeTEFromItems(NonNullList<ItemStack> slots, int index, long power, long maxPower) {

        if (slots.get(index).getItem() instanceof IBatteryItem batteryItem) {

            long batCharge = batteryItem.getCharge(slots.get(index));
            long batRate = batteryItem.getDischargeRate(slots.get(index));
            long toDischarge = Math.min(Math.min((maxPower - power), batRate), batCharge);

            batteryItem.dischargeBattery(slots.get(index), toDischarge);
            power += toDischarge;
        }

        return power;
    }

    public static HitResult rayTrace(Player player, double length, float interpolation) {
        Vec3 vec3 = getPosition(interpolation, player);
        vec3 = vec3.add(0, player.getEyeHeight(), 0);
        Vec3 vec31 = player.getLookAngle();
        Vec3 vec32 = vec3.add(vec31.x * length, vec31.y * length, vec31.z * length);
        return player.level().clip(new ClipContext(vec3, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
    }

    public static Vec3 getPosition(float interpolation, Player player) {
        if (interpolation == 1.0F) {
            return new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        } else {
            double d0 = player.xo + (player.getX() - player.xo) * interpolation;
            double d1 = player.yo + (player.getY() - player.yo) * interpolation + player.getEyeHeight();
            double d2 = player.zo + (player.getZ() - player.zo) * interpolation;
            return new Vec3(d0, d1, d2);
        }
    }

    public static boolean isObstructed(Level level, double x, double y, double z, double a, double b, double c) {
        return level.clip(new ClipContext(new Vec3(x, y, z), new Vec3(a, b, c), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getType() != HitResult.Type.MISS;
    }
}
