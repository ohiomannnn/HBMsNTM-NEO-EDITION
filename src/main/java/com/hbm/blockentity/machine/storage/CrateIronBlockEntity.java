package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CrateIronBlockEntity extends CrateBaseBlockEntity {

    public CrateIronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRATE_IRON.get(), pos, state, 36);
    }

    @Override public Component getName() { return Component.translatable("container.crateIron"); }
}
