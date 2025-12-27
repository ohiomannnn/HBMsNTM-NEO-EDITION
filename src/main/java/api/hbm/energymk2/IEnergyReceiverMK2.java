package api.hbm.energymk2;

import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.interfaces.NotableComments;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/** If it receives energy, use this */
@NotableComments
public interface IEnergyReceiverMK2 extends IEnergyHandlerMK2 {

    default long transferPower(long power) {
        if (power + this.getPower() <= this.getMaxPower()) {
            this.setPower(power + this.getPower());
            return 0;
        }
        long capacity = this.getMaxPower() - this.getPower();
        long overshoot = power - capacity;
        this.setPower(this.getMaxPower());
        return overshoot;
    }

    default long getReceiverSpeed() {
        return this.getMaxPower();
    }

    /** Whether a provider can provide power by touching the block (i.e. via proxies), bypassing the need for a network entirely */
    default boolean allowDirectProvision() { return true; }

    default void trySubscribe(Level level, DirPos pos) { trySubscribe(level, pos, pos.getDir()); }

    default void trySubscribe(Level level, BlockPos pos, Direction dir) {

        BlockEntity be = IEnergyProviderMK2.getBlockEntityStandard(level, pos);
        boolean red = false;

        if (be instanceof IEnergyConductorMK2 con) {
            if (!con.canConnect(dir.getOpposite())) return;

            PowerNode node = Nodespace.getNode(level, pos);

            if(node != null && node.net != null) {
                node.net.addReceiver(this);
                red = true;
            }
        }

//        if (particleDebug) {
//            NBTTagCompound data = new NBTTagCompound();
//            data.setString("type", "network");
//            data.setString("mode", "power");
//            double posX = x + 0.5 + dir.offsetX * 0.5 + world.rand.nextDouble() * 0.5 - 0.25;
//            double posY = y + 0.5 + dir.offsetY * 0.5 + world.rand.nextDouble() * 0.5 - 0.25;
//            double posZ = z + 0.5 + dir.offsetZ * 0.5 + world.rand.nextDouble() * 0.5 - 0.25;
//            data.setDouble("mX", -dir.offsetX * (red ? 0.025 : 0.1));
//            data.setDouble("mY", -dir.offsetY * (red ? 0.025 : 0.1));
//            data.setDouble("mZ", -dir.offsetZ * (red ? 0.025 : 0.1));
//            PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(data, posX, posY, posZ), new TargetPoint(world.provider.dimensionId, posX, posY, posZ, 25));
//        }
    }

    default void tryUnsubscribe(Level level, BlockPos pos) {

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof IEnergyConductorMK2 con) {
            PowerNode node = con.createNode();

            if(node != null && node.net != null) {
                node.net.removeReceiver(this);
            }
        }
    }

    enum ConnectionPriority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST
    }

    default ConnectionPriority getPriority() {
        return ConnectionPriority.NORMAL;
    }

}
