package com.hbm.blocks.network;

import com.hbm.blockentity.Tickable;
import com.hbm.blockentity.network.CableBaseBlockEntity;
import com.hbm.lib.Library;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableBlock extends Block implements EntityBlock {

    public CableBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<CableBlock> CODEC = simpleCodec(CableBlock::new);
    @Override protected MapCodec<CableBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBaseBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof Tickable tickable) tickable.updateEntity(); };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        boolean posX = Library.canConnect(level, pos.relative(Library.POS_X), Library.POS_X);
        boolean negX = Library.canConnect(level, pos.relative(Library.NEG_X), Library.NEG_X);
        boolean posY = Library.canConnect(level, pos.relative(Library.POS_Y), Library.POS_Y);
        boolean negY = Library.canConnect(level, pos.relative(Library.NEG_Y), Library.NEG_Y);
        boolean posZ = Library.canConnect(level, pos.relative(Library.POS_Z), Library.POS_Z);
        boolean negZ = Library.canConnect(level, pos.relative(Library.NEG_Z), Library.NEG_Z);

        return this.getBlockBounds(posX, negX, posY, negY, posZ, negZ);
    }

    private VoxelShape getBlockBounds(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {

        double pixel = 0.0625D;
        double min = pixel * 5.5D;
        double max = pixel * 10.5D;

        double minX = negX ? 0D : min;
        double maxX = posX ? 1D : max;
        double minY = negY ? 0D : min;
        double maxY = posY ? 1D : max;
        double minZ = negZ ? 0D : min;
        double maxZ = posZ ? 1D : max;

        return Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
