package api.hbm.energymk2;

import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.interfaces.NotableComments;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.Compat;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

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

        BlockEntity be = Compat.getBlockEntityStandard(level, pos);
        boolean red = false;

        if (be instanceof IEnergyConductorMK2 con) {
            if (!con.canConnect(dir.getOpposite())) return;

            PowerNode node = Nodespace.getNode(level, pos);

            if (node != null && node.net != null) {
                node.net.addReceiver(this);
                red = true;
            }
        }

        if (particleDebug) {
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "network");
            tag.putString("mode", "power");
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

    default void tryUnsubscribe(Level level, BlockPos pos) {

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof IEnergyConductorMK2 con) {
            PowerNode node = con.createNode();

            if (node != null && node.net != null) {
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
