package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CrateSteelBlockEntity extends CrateBaseBlockEntity {

    public CrateSteelBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CRATE_STEEL.get(), pos, state, 54, "gui_crate_steel", 9, 6, 8, 18, 8, 140, 176, 222, 8, 1842204, 1842204);
    }

    @Override public Component getName() { return Component.translatable("container.crateSteel"); }
}
