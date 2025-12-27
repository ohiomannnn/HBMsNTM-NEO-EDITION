package api.hbm.energymk2;

import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

/**
 * Interface for all blocks that should visually connect to cables without having an IEnergyConnector tile entity.
 * This is meant for BLOCKS
 * @author hbm
 *
 */
public interface IEnergyConnectorBlock {

    /**
     * Same as IEnergyConnector's method but for regular blocks that might not even have TEs. Used for rendering only!
     */
    boolean canConnect(BlockGetter level, int x, int y, int z, Direction dir);
}
