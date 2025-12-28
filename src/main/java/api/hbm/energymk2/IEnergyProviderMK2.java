package api.hbm.energymk2;

import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

/** If it sends energy, use this */
public interface IEnergyProviderMK2 extends IEnergyHandlerMK2 {

    /** Uses up available power, default implementation has no sanity checking, make sure that the requested power is lequal to the current power */
    default void usePower(long power) {
        this.setPower(this.getPower() - power);
    }

    default long getProviderSpeed() {
        return this.getMaxPower();
    }

    default void tryProvide(Level level, BlockPos pos, Direction dir) {

        BlockEntity be = Compat.getBlockEntityStandard(level, pos);
        boolean red = false;

        if (be instanceof IEnergyConductorMK2 con) {
            if (con.canConnect(dir.getOpposite())) {

                PowerNode node = Nodespace.getNode(level, pos);

                if (node != null && node.net != null) {
                    node.net.addProvider(this);
                    red = true;
                }
            }
        }

        if (be instanceof IEnergyReceiverMK2 rec && be != this) {
            if (rec.canConnect(dir.getOpposite()) && rec.allowDirectProvision()) {
                long provides = Math.min(this.getPower(), this.getProviderSpeed());
                long receives = Math.min(rec.getMaxPower() - rec.getPower(), rec.getReceiverSpeed());
                long toTransfer = Math.min(provides, receives);
                toTransfer -= rec.transferPower(toTransfer);
                this.usePower(toTransfer);
            }
        }

        if (particleDebug) {
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "network");
            tag.putString("mode", "power");
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
}
