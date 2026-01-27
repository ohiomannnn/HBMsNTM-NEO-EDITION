package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeFatManBlock extends NukeBaseBlock {

    public NukeFatManBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeFatManBlock> CODEC = simpleCodec(NukeFatManBlock::new);
    @Override protected MapCodec<NukeFatManBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeFatManBlockEntity(pos, state);
    }
}
