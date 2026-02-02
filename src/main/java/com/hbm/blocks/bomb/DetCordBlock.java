package com.hbm.blocks.bomb;

import com.hbm.blockentity.EmptyBlockEntity;
import com.hbm.blockentity.ModBlockEntities;
import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.interfaces.IBomb;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DetCordBlock extends DetonatableBlock implements IDetConnectible, IBomb, EntityBlock {

    private static final VoxelShape SHAPE_LINE_X = Block.box(0, 5.5, 5.5, 16, 10.5, 10.5);
    private static final VoxelShape SHAPE_LINE_Y = Block.box(5.5, 0, 5.5, 10.5, 16, 10.5);
    private static final VoxelShape SHAPE_LINE_Z = Block.box(5.5, 5.5, 0, 10.5, 10.5, 16);

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

    public DetCordBlock(Properties properties) {
        super(properties, 0, 0, 0, false, false);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5F, Level.ExplosionInteraction.BLOCK);
        }
        return BombReturnCode.DETONATED;
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {
        explode(level, BlockPos.containing(x, y, z));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            this.explode(level, pos);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EmptyBlockEntity(ModBlockEntities.DET_CORD.get(), pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean pX = IDetConnectible.isConnectible(level, pos.relative(Library.POS_X), Library.POS_X);
        boolean nX = IDetConnectible.isConnectible(level, pos.relative(Library.NEG_X), Library.NEG_X);
        boolean pY = IDetConnectible.isConnectible(level, pos.relative(Library.POS_Y), Library.POS_Y);
        boolean nY = IDetConnectible.isConnectible(level, pos.relative(Library.NEG_Y), Library.NEG_Y);
        boolean pZ = IDetConnectible.isConnectible(level, pos.relative(Library.POS_Z), Library.POS_Z);
        boolean nZ = IDetConnectible.isConnectible(level, pos.relative(Library.NEG_Z), Library.NEG_Z);

        int mask = (pX ? 32 : 0) + (nX ? 16 : 0) + (pY ? 8 : 0) + (nY ? 4 : 0) + (pZ ? 2 : 0) + (nZ ? 1 : 0);

        if (mask == 0b110000 || mask == 0b100000 || mask == 0b010000) {
            return SHAPE_LINE_X;
        } else if (mask == 0b001100 || mask == 0b001000 || mask == 0b000100) {
            return SHAPE_LINE_Y;
        } else if (mask == 0b000011 || mask == 0b000010 || mask == 0b000001) {
            return SHAPE_LINE_Z;
        }
        else {
            return SHAPE[getShapeIndex(pX, nX, pY, nY, pZ, nZ)];
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
