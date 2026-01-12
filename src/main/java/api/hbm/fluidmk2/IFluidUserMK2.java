package api.hbm.fluidmk2;

import api.hbm.blockentity.ILoadedTile;
import com.hbm.inventory.fluid.tank.FluidTank;

public interface IFluidUserMK2 extends IFluidConnectorMK2, ILoadedTile {

    int HIGHEST_VALID_PRESSURE = 5;
    int[] DEFAULT_PRESSURE_RANGE = new int[] {0, 0};

    boolean particleDebug = false;

    FluidTank[] getAllTanks();
}