package api.hbm.conveyor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IEnterableBlock {

    /**
     * Returns true of the moving item can enter from the given side. When this happens, the IConveyorItem will call onEnter and despawn
     */
    boolean canItemEnter(Level level, BlockPos pos, Direction dir, IConveyorItem entity);
    void onItemEnter(Level level, BlockPos pos, Direction dir, IConveyorItem entity);

    boolean canPackageEnter(Level level, BlockPos pos, Direction dir, IConveyorPackage entity);
    void onPackageEnter(Level level, BlockPos pos, Direction dir, IConveyorPackage entity);
}
