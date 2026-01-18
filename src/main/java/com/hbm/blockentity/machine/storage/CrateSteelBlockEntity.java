package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CrateSteelBlockEntity extends CrateBaseBlockEntity {

    public CrateSteelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRATE_STEEL.get(), pos, state, 54);
    }

    @Override public Component getName() { return Component.translatable("container.crateSteel"); }
}
