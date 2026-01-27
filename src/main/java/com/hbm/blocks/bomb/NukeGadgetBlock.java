package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeGadgetBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeGadgetBlock extends NukeBaseBlock {

    public NukeGadgetBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeGadgetBlock> CODEC = simpleCodec(NukeGadgetBlock::new);
    @Override protected MapCodec<NukeGadgetBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeGadgetBlockEntity(pos, state);
    }
}
