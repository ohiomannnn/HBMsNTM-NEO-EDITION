package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.DetCordBlockEntity;
import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.interfaces.IBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DetCordBlock extends DetonatableBlock implements IDetConnectible, IBomb, EntityBlock {

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
    public void explodeEntity(Level level, double x, double y, double z, EntityTNTPrimedBase entity) {
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
        return new DetCordBlockEntity(pos, state);
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
