package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeIvyMikeBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeIvyMikeBlock extends NukeBaseBlock {

    public NukeIvyMikeBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeIvyMikeBlock> CODEC = simpleCodec(NukeIvyMikeBlock::new);
    @Override protected MapCodec<NukeIvyMikeBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeIvyMikeBlockEntity(pos, state);
    }
}
