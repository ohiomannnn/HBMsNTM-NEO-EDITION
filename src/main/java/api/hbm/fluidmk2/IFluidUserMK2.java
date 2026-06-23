package api.hbm.fluidmk2;

import api.hbm.blockentity.ILoadedBE;
import com.hbm.inventory.fluid.tank.FluidTank;

public interface IFluidUserMK2 extends IFluidConnectorMK2, ILoadedBE {

    int HIGHEST_VALID_PRESSURE = 5;
    int[] DEFAULT_PRESSURE_RANGE = new int[] {0, 0};

    boolean particleDebug = false;

    FluidTank[] getAllTanks();
}