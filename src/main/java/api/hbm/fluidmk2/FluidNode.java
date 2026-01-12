package api.hbm.fluidmk2;

import com.hbm.uninos.GenNode;
import com.hbm.uninos.INetworkProvider;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;

public class FluidNode extends GenNode<FluidNetMK2> {

    public FluidNode(INetworkProvider<FluidNetMK2> provider, BlockPos... positions) {
        super(provider, positions);
    }

    @Override
    public FluidNode setConnections(DirPos... connections) {
        super.setConnections(connections);
        return this;
    }
}
