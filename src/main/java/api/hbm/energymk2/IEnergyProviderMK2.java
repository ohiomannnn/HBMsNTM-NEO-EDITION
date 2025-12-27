package api.hbm.energymk2;

import api.hbm.energymk2.Nodespace.PowerNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/** If it sends energy, use this */
public interface IEnergyProviderMK2 extends IEnergyHandlerMK2 {

    /** Uses up available power, default implementation has no sanity checking, make sure that the requested power is lequal to the current power */
    default void usePower(long power) {
        this.setPower(this.getPower() - power);
    }

    default long getProviderSpeed() {
        return this.getMaxPower();
    }

    /** A standard implementation of safely grabbing a tile entity without loading chunks, might have more fluff added to it later on. */
    static BlockEntity getBlockEntityStandard(Level level, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        if (!level.hasChunk(x >> 4, z >> 4)) return null;
        return level.getBlockEntity(pos);
    }

    default void tryProvide(Level level, BlockPos pos, Direction dir) {

        BlockEntity be = getBlockEntityStandard(level, pos);
        boolean red = false;

        if (be instanceof IEnergyConductorMK2) {
            IEnergyConductorMK2 con = (IEnergyConductorMK2) be;
            if(con.canConnect(dir.getOpposite())) {

                PowerNode node = Nodespace.getNode(level, pos);

                if (node != null && node.net != null) {
                    node.net.addProvider(this);
                    red = true;
                }
            }
        }

        if (be instanceof IEnergyReceiverMK2 && be != this) {
            IEnergyReceiverMK2 rec = (IEnergyReceiverMK2) be;
            if (rec.canConnect(dir.getOpposite()) && rec.allowDirectProvision()) {
                long provides = Math.min(this.getPower(), this.getProviderSpeed());
                long receives = Math.min(rec.getMaxPower() - rec.getPower(), rec.getReceiverSpeed());
                long toTransfer = Math.min(provides, receives);
                toTransfer -= rec.transferPower(toTransfer);
                this.usePower(toTransfer);
            }
        }

//        if (particleDebug) {
//            NBTTagCompound data = new NBTTagCompound();
//            data.setString("type", "network");
//            data.setString("mode", "power");
//            double posX = x + 0.5 - dir.offsetX * 0.5 + world.rand.nextDouble() * 0.5 - 0.25;
//            double posY = y + 0.5 - dir.offsetY * 0.5 + world.rand.nextDouble() * 0.5 - 0.25;
//            double posZ = z + 0.5 - dir.offsetZ * 0.5 + world.rand.nextDouble() * 0.5 - 0.25;
//            data.setDouble("mX", dir.offsetX * (red ? 0.025 : 0.1));
//            data.setDouble("mY", dir.offsetY * (red ? 0.025 : 0.1));
//            data.setDouble("mZ", dir.offsetZ * (red ? 0.025 : 0.1));
//            PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(data, posX, posY, posZ), new TargetPoint(world.provider.dimensionId, posX, posY, posZ, 25));
//        }
    }
}
