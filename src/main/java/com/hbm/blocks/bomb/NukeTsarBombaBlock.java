package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeTsarBombaBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeTsarBombaBlock extends NukeBaseBlock {

    public NukeTsarBombaBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeTsarBombaBlock> CODEC = simpleCodec(NukeTsarBombaBlock::new);
    @Override protected MapCodec<NukeTsarBombaBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeTsarBombaBlockEntity(pos, state);
    }
}
