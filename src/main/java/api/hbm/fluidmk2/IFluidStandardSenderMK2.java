package api.hbm.fluidmk2;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.uninos.GenNode;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * IFluidProviderMK2 with standard implementation for fluid provision and fluid removal.
 * @author hbm
 */
public interface IFluidStandardSenderMK2 extends IFluidProviderMK2 {

    default void tryProvide(FluidTank tank, Level level, DirPos pos) { tryProvide(tank.getTankType(), tank.getPressure(), level, pos.makeCompat(), pos.getDir()); }
    default void tryProvide(FluidType type, Level level, DirPos pos) { tryProvide(type, 0, level, pos.makeCompat(), pos.getDir()); }
    default void tryProvide(FluidType type, int pressure, Level level, DirPos pos) { tryProvide(type, pressure, level, pos.makeCompat(), pos.getDir()); }

    default void tryProvide(FluidTank tank, Level level, BlockPos pos, Direction dir) { tryProvide(tank.getTankType(), tank.getPressure(), level, pos, dir); }
    default void tryProvide(FluidType type, Level level, BlockPos pos, Direction dir) { tryProvide(type, 0, level,pos, dir); }

    default void tryProvide(FluidType type, int pressure, Level level, BlockPos pos, Direction dir) {

        BlockEntity be = TileAccessCache.getTileOrCache(level, pos);
        boolean red = false;

        if (be instanceof IFluidConnectorMK2 con) {
            if (con.canConnect(type, dir.getOpposite())) {

                GenNode<FluidNetMK2> node = UniNodespace.getNode(level, pos, type.getNetworkProvider());

                if (node != null && node.net != null) {
                    node.net.addProvider(this);
                    red = true;
                }
            }
        }

        if (be != this && be instanceof IFluidReceiverMK2 rec) {
            if (rec.canConnect(type, dir.getOpposite())) {
                long provides = Math.min(this.getFluidAvailable(type, pressure), this.getProviderSpeed(type, pressure));
                long receives = Math.min(rec.getDemand(type, pressure), rec.getReceiverSpeed(type, pressure));
                long toTransfer = Math.min(provides, receives);
                toTransfer -= rec.transferFluid(type, pressure, toTransfer);
                this.useUpFluid(type, pressure, toTransfer);
            }
        }

        if (particleDebug) {
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "network");
            tag.putString("mode", "fluid");
            tag.putInt("color", type.getColor());
            double posX = pos.getX() + 0.5 - dir.getStepX() * 0.5 + level.random.nextDouble() * 0.5 - 0.25;
            double posY = pos.getY() + 0.5 - dir.getStepY() * 0.5 + level.random.nextDouble() * 0.5 - 0.25;
            double posZ = pos.getZ() + 0.5 - dir.getStepZ() * 0.5 + level.random.nextDouble() * 0.5 - 0.25;
            tag.putDouble("mX", dir.getStepX() * (red ? 0.025 : 0.1));
            tag.putDouble("mY", dir.getStepY() * (red ? 0.025 : 0.1));
            tag.putDouble("mZ", dir.getStepZ() * (red ? 0.025 : 0.1));
            if (level instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, posX, posY, posZ, 25, new AuxParticle(tag, posX, posY, posZ));
            }
        }
    }

    FluidTank[] getSendingTanks();

    @Override
    default long getFluidAvailable(FluidType type, int pressure) {
        long amount = 0;
        for (FluidTank tank : getSendingTanks()) {
            if (tank.getTankType() == type && tank.getPressure() == pressure) amount += tank.getFill();
        }
        return amount;
    }

    @Override
    default void useUpFluid(FluidType type, int pressure, long amount) {
        int tanks = 0;
        for (FluidTank tank : getSendingTanks()) {
            if (tank.getTankType() == type && tank.getPressure() == pressure) tanks++;
        }
        if (tanks > 1) {
            int firstRound = (int) Math.floor((double) amount / (double) tanks);
            for (FluidTank tank : getSendingTanks()) {
                if( tank.getTankType() == type && tank.getPressure() == pressure) {
                    int toRem = Math.min(firstRound, tank.getFill());
                    tank.setFill(tank.getFill() - toRem);
                    amount -= toRem;
                }
            }
        }
        if (amount > 0) {
            for (FluidTank tank : getSendingTanks()) {
                if (tank.getTankType() == type && tank.getPressure() == pressure) {
                    int toRem = (int) Math.min(amount, tank.getFill());
                    tank.setFill(tank.getFill() - toRem);
                    amount -= toRem;
                }
            }
        }
    }

    @Override
    default int[] getProvidingPressureRange(FluidType type) {
        int lowest = HIGHEST_VALID_PRESSURE;
        int highest = 0;

        for (FluidTank tank : getSendingTanks()) {
            if (tank.getTankType() == type) {
                if (tank.getPressure() < lowest) lowest = tank.getPressure();
                if (tank.getPressure() > highest) highest = tank.getPressure();
            }
        }

        return lowest <= highest ? new int[] {lowest, highest} : DEFAULT_PRESSURE_RANGE;
    }
}
