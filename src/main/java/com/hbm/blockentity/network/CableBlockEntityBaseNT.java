package com.hbm.blockentity.network;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.Nodespace;
import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntityBaseNT extends LoadedBaseBlockEntity implements IEnergyConductorMK2 {

    public PowerNode node;

    public CableBlockEntityBaseNT(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NETWORK_CABLE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState ignored, CableBlockEntityBaseNT be) {
        if (!level.isClientSide) {
            if (be.node == null || be.node.expired) {

                if (be.shouldCreateNode()) {
                    be.node = Nodespace.getNode(level, pos);

                    if (be.node == null || be.node.expired) {
                        be.node = be.createNode();
                        Nodespace.createNode(level, be.node);
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
            //if (level.getBlockEntity(this.getBlockPos()) instanceof CableBlockEntityBaseNT be) { FUCK
                if (this.node != null) {
                    Nodespace.destroyNode(level, this.getBlockPos());
                }
            //}
        }
    }
}
