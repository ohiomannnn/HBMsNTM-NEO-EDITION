package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CrateDeshBlockEntity extends CrateBaseBlockEntity {

    public CrateDeshBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CRATE_DESH.get(), pos, state, 104, "gui_crate_desh", 13, 8, 8, 18, 44, 174, 248, 256, 44, 4134165, 4134165);
    }

    @Override public Component getName() { return Component.translatable("container.crateDesh"); }
}
