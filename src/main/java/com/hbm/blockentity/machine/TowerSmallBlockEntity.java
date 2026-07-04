package com.hbm.blockentity.machine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TowerSmallBlockEntity extends CondenserBaseBlockEntity {

    // todo make Configurable values
    public static int inputTankSizeTL = 10_000;
    public static int outputTankSizeTL = 10_000;

    public TowerSmallBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.TOWER_SMALL.get(), pos, state);

        this.tanks = new FluidTank[2];
        this.tanks[0] = new FluidTank(Fluids.SPENTSTEAM, inputTankSizeTL);
        this.tanks[1] = new FluidTank(Fluids.WATER, outputTankSizeTL);
    }
}
