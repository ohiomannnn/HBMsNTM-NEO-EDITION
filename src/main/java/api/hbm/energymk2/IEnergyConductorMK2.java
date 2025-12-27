package api.hbm.energymk2;

import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IEnergyConductorMK2 extends IEnergyConnectorMK2 {

    default PowerNode createNode() {
        BlockEntity be = (BlockEntity) this;
        int xCoord = be.getBlockPos().getX();
        int yCoord = be.getBlockPos().getY();
        int zCoord = be.getBlockPos().getZ();
        return new PowerNode(be.getBlockPos()).setConnections(
                new DirPos(xCoord + 1, yCoord, zCoord, Library.POS_X),
                new DirPos(xCoord - 1, yCoord, zCoord, Library.NEG_X),
                new DirPos(xCoord, yCoord + 1, zCoord, Library.POS_Y),
                new DirPos(xCoord, yCoord - 1, zCoord, Library.NEG_Y),
                new DirPos(xCoord, yCoord, zCoord + 1, Library.POS_Z),
                new DirPos(xCoord, yCoord, zCoord - 1, Library.NEG_Z)
        );
    }
}
