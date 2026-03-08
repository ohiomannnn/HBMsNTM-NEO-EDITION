package api.hbm.fluidmk2;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * IFluidConductorMK2 with added node creation method
 * @author hbm
 */
public interface IFluidPipeMK2 extends IFluidConnectorMK2 {

    default FluidNode createNode(FluidType type) {
        BlockEntity be = (BlockEntity) this;
        return new FluidNode(type.getNetworkProvider(), be.getBlockPos()).setConnections(
                new DirPos(be.getBlockPos().relative(Library.POS_X), Library.POS_X),
                new DirPos(be.getBlockPos().relative(Library.NEG_X), Library.NEG_X),
                new DirPos(be.getBlockPos().relative(Library.POS_Y), Library.POS_Y),
                new DirPos(be.getBlockPos().relative(Library.NEG_Y), Library.NEG_Y),
                new DirPos(be.getBlockPos().relative(Library.POS_Z), Library.POS_Z),
                new DirPos(be.getBlockPos().relative(Library.NEG_Z), Library.NEG_Z)
        );
    }
}
