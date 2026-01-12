package api.hbm.fluidmk2;

import api.hbm.energymk2.IEnergyReceiverMK2.ConnectionPriority;
import com.hbm.inventory.fluid.FluidType;
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

public interface IFluidReceiverMK2 extends IFluidUserMK2 {
    /** Sends fluid of the desired type and pressure to the receiver, returns the remainder */
    long transferFluid(FluidType type, int pressure, long amount);
    default long getReceiverSpeed(FluidType type, int pressure) { return 1_000_000_000; }
    long getDemand(FluidType type, int pressure);

    default int[] getReceivingPressureRange(FluidType type) { return DEFAULT_PRESSURE_RANGE; }

    default void trySubscribe(FluidType type, Level level, DirPos pos) { trySubscribe(type, level, pos, pos.getDir()); }

    default void trySubscribe(FluidType type,Level level, BlockPos pos, Direction dir) {
        BlockEntity be = TileAccessCache.getTileOrCache(level, pos);
        boolean red = false;

        if (be instanceof IFluidConnectorMK2 con) {
            if (!con.canConnect(type, dir.getOpposite())) return;

            GenNode node = UniNodespace.getNode(level, pos, type.getNetworkProvider());

            if (node != null && node.net != null) {
                node.net.addReceiver(this);
                red = true;
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
            tag.putDouble("mX", -dir.getStepX() * (red ? 0.025 : 0.1));
            tag.putDouble("mY", -dir.getStepY() * (red ? 0.025 : 0.1));
            tag.putDouble("mZ", -dir.getStepZ() * (red ? 0.025 : 0.1));
            if (level instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, posX, posY, posZ, 25, new AuxParticle(tag, posX, posY, posZ));
            }
        }
    }

    default ConnectionPriority getFluidPriority() {
        return ConnectionPriority.NORMAL;
    }
}
