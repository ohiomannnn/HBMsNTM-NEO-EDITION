package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CrateDeshBlockEntity extends CrateBaseBlockEntity {

    public CrateDeshBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CRATE_DESH.get(), pos, state, 104);
    }

    @Override public Component getName() { return Component.translatable("container.crateDesh"); }
}
