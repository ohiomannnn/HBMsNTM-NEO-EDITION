package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeFatManBlockEntity extends BlockEntity {

    public NukeFatManBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.NUKE_FATMAN.get(), pos, blockState);
    }
}
