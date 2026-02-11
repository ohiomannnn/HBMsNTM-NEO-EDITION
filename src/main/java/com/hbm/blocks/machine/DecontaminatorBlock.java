package com.hbm.blocks.machine;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.machine.DecontaminatorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DecontaminatorBlock extends BaseEntityBlock {

    public DecontaminatorBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<DecontaminatorBlock> CODEC = simpleCodec(DecontaminatorBlock::new);

    protected RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DecontaminatorBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return BaseEntityBlock.createTickerHelper(type, ModBlockEntityTypes.DECONTAMINATOR.get(), DecontaminatorBlockEntity::tick);
    }
}
