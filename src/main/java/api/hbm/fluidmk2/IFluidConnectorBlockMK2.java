package api.hbm.fluidmk2;

import com.hbm.inventory.fluid.FluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public interface IFluidConnectorBlockMK2 {

    /** dir is the face that is connected to, the direction going outwards from the block */
    boolean canConnect(FluidType type, BlockGetter level, BlockPos pos, Direction dir);
}