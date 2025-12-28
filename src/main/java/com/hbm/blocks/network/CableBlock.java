package com.hbm.blocks.network;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.network.CableBlockEntityBaseNT;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlock extends BaseEntityBlock {

    public CableBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<CableBlock> CODEC = simpleCodec(CableBlock::new);
    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, ModBlockEntities.NETWORK_CABLE.get(), CableBlockEntityBaseNT::serverTick);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBlockEntityBaseNT(pos, state);
    }
}
