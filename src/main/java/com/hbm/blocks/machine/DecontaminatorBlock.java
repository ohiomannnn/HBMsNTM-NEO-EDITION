package com.hbm.blocks.machine;

import com.hbm.blockentity.Tickable;
import com.hbm.blockentity.machine.DecontaminatorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DecontaminatorBlock extends Block implements EntityBlock {

    public DecontaminatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DecontaminatorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof Tickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<DecontaminatorBlock> CODEC = simpleCodec(DecontaminatorBlock::new);
    @Override protected MapCodec<DecontaminatorBlock> codec() { return CODEC; }
}
