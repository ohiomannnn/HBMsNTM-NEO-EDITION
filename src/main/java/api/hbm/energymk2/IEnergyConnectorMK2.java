package api.hbm.energymk2;

import net.minecraft.core.Direction;

public interface IEnergyConnectorMK2 {
    /**
     * Whether the given side can be connected to
     * dir refers to the side of this block, not the connecting block doing the check
     */
    default boolean canConnect(Direction dir) {
        return dir != null;
    }
}
