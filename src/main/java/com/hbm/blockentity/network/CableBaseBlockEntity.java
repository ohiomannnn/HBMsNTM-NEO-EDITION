package com.hbm.blockentity.network;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.Nodespace;
import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CableBaseBlockEntity extends LoadedBaseBlockEntity implements IEnergyConductorMK2, Tickable {

    public PowerNode node;

    public CableBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NETWORK_CABLE.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if (level == null) return;

        if (!level.isClientSide) {
            if (this.node == null || this.node.expired) {

                if (this.shouldCreateNode()) {
                    this.node = Nodespace.getNode(level, this.getBlockPos());

                    if (this.node == null || this.node.expired) {
                        this.node = this.createNode();
                        Nodespace.createNode(level, this.node);
                    }
                }
            }
        }
    }

    public boolean shouldCreateNode() {
        return true;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (this.level != null && !this.level.isClientSide) {
            if (this.node != null) {
                Nodespace.destroyNode(level, this.getBlockPos());
            }
        }
    }
}
