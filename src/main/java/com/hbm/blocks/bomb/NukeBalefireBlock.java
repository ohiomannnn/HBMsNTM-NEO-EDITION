package com.hbm.blocks.bomb;

import com.hbm.blockentity.Tickable;
import com.hbm.blockentity.bomb.NukeBalefireBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NukeBalefireBlock extends NukeBaseBlock {

    public NukeBalefireBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeBalefireBlock> CODEC = simpleCodec(NukeBalefireBlock::new);
    @Override protected MapCodec<NukeBalefireBlock> codec() { return CODEC; }

    @Override protected void explode(Level level, double x, double y, double z) { }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {

        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be == null) return BombReturnCode.UNDEFINED;
            if (be instanceof NukeBalefireBlockEntity nuke) {
                if (nuke.isLoaded()) {
                    nuke.explode();
                    return BombReturnCode.DETONATED;
                }
            }

            return BombReturnCode.ERROR_MISSING_COMPONENT;
        }

        return BombReturnCode.UNDEFINED;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof Tickable tickable) tickable.updateEntity(); };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeBalefireBlockEntity(pos, state);
    }
}
