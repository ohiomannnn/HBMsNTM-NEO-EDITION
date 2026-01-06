package com.hbm.blocks.network;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.network.CableBlockEntityBaseNT;
import com.hbm.lib.Library;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableBlock extends BaseEntityBlock {

    private static final VoxelShape CORE = Block.box(5.5, 5.5, 5.5, 10.5, 10.5, 10.5);
    private static final VoxelShape SHAPE_POS_X = Block.box(10.5, 5.5, 5.5, 16, 10.5, 10.5);
    private static final VoxelShape SHAPE_NEG_X = Block.box(0, 5.5, 5.5, 5.5, 10.5, 10.5);
    private static final VoxelShape SHAPE_POS_Y = Block.box(5.5, 10.5, 5.5, 10.5, 16, 10.5);
    private static final VoxelShape SHAPE_NEG_Y = Block.box(5.5, 0, 5.5, 10.5, 5.5, 10.5);
    private static final VoxelShape SHAPE_POS_Z = Block.box(5.5, 5.5, 10.5, 10.5, 10.5, 16);
    private static final VoxelShape SHAPE_NEG_Z = Block.box(5.5, 5.5, 0, 10.5, 10.5, 5.5);

    private static final VoxelShape[] SHAPE = new VoxelShape[64];
    static {
        for (int i = 0; i < 64; i++) {
            SHAPE[i] = createShape(
                    (i & 1) != 0,   // posX
                    (i & 2) != 0,   // negX
                    (i & 4) != 0,   // posY
                    (i & 8) != 0,   // negY
                    (i & 16) != 0,  // posZ
                    (i & 32) != 0   // negZ
            );
        }
    }

    private static VoxelShape createShape(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        VoxelShape shape = CORE;

        if (posX) shape = Shapes.or(shape, SHAPE_POS_X);
        if (negX) shape = Shapes.or(shape, SHAPE_NEG_X);
        if (posY) shape = Shapes.or(shape, SHAPE_POS_Y);
        if (negY) shape = Shapes.or(shape, SHAPE_NEG_Y);
        if (posZ) shape = Shapes.or(shape, SHAPE_POS_Z);
        if (negZ) shape = Shapes.or(shape, SHAPE_NEG_Z);

        return shape.optimize();
    }

    private static int getShapeIndex(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {
        int index = 0;
        if (posX) index |= 1;
        if (negX) index |= 2;
        if (posY) index |= 4;
        if (negY) index |= 8;
        if (posZ) index |= 16;
        if (negZ) index |= 32;
        return index;
    }

    public CableBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<CableBlock> CODEC = simpleCodec(CableBlock::new);
    @Override protected MapCodec<CableBlock> codec() { return CODEC; }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, ModBlockEntities.NETWORK_CABLE.get(), CableBlockEntityBaseNT::serverTick);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBlockEntityBaseNT(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean pX = Library.canConnect(level, pos.relative(Library.POS_X), Library.POS_X);
        boolean nX = Library.canConnect(level, pos.relative(Library.NEG_X), Library.NEG_X);
        boolean pY = Library.canConnect(level, pos.relative(Library.POS_Y), Library.POS_Y);
        boolean nY = Library.canConnect(level, pos.relative(Library.NEG_Y), Library.NEG_Y);
        boolean pZ = Library.canConnect(level, pos.relative(Library.POS_Z), Library.POS_Z);
        boolean nZ = Library.canConnect(level, pos.relative(Library.NEG_Z), Library.NEG_Z);

        return SHAPE[getShapeIndex(pX, nX, pY, nY, pZ, nZ)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }
}
