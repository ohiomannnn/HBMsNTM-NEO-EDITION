package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CrateTungstenBlockEntity extends CrateBaseBlockEntity {

    public CrateTungstenBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CRATE_TUNGSTEN.get(), pos, state, 27, "gui_crate_tungsten", 9, 3, 8, 18, 8, 86, 176, 168, 8, 10526880, 10526880);
    }

    @Override public Component getName() { return Component.translatable("container.crateTungsten"); }
}
