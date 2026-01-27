package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeLittleBoyBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeLittleBoyBlock extends NukeBaseBlock {

    public NukeLittleBoyBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeLittleBoyBlock> CODEC = simpleCodec(NukeLittleBoyBlock::new);
    @Override protected MapCodec<NukeLittleBoyBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeLittleBoyBlockEntity(pos, state);
    }
}
