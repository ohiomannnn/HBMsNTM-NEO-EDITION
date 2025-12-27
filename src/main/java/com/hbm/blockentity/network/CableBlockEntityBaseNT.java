package com.hbm.blockentity.network;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.Nodespace;
import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntityBaseNT extends LoadedBaseBlockEntity implements IEnergyConductorMK2 {

    protected PowerNode node;

    public CableBlockEntityBaseNT(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public boolean shouldCreateNode() {
        return true;
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();

        if (this.level != null && !this.level.isClientSide) {
            if(this.node != null) {
                Nodespace.destroyNode(level, this.getBlockPos());
            }
        }
    }
}
