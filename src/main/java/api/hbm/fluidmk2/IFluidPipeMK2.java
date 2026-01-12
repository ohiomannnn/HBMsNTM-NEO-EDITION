package api.hbm.fluidmk2;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * IFluidConductorMK2 with added node creation method
 * @author hbm
 */
public interface IFluidPipeMK2 {

    default FluidNode createNode(FluidType type) {
        BlockEntity be = (BlockEntity) this;
        int x = be.getBlockPos().getX();
        int y = be.getBlockPos().getY();
        int z = be.getBlockPos().getZ();
        return new FluidNode(type.getNetworkProvider(), be.getBlockPos()).setConnections(
                new DirPos(x + 1, y, z, Library.POS_X),
                new DirPos(x - 1, y, z, Library.NEG_X),
                new DirPos(x, y + 1, z, Library.POS_Y),
                new DirPos(x, y - 1, z, Library.NEG_Y),
                new DirPos(x, y, z + 1, Library.POS_Z),
                new DirPos(x, y, z - 1, Library.NEG_Z)
        );
    }
}
