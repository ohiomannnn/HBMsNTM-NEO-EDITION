package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.IBlockMutator;
import com.hbm.inventory.RecipesCommon.StateBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMutatorDebris implements IBlockMutator {

    protected StateBlock stateBlock;

    public BlockMutatorDebris(Block block) {
        this(block.defaultBlockState());
    }

    public BlockMutatorDebris(BlockState state) {
        this.stateBlock = new StateBlock(state);
    }

    @Override public void mutatePre(ExplosionVNT explosion, BlockState state, BlockPos pos) { }

    @Override
    public void mutatePost(ExplosionVNT explosion, BlockPos pos) {
        Level level = explosion.level;
        for (Direction dir : Direction.values()) {
            BlockPos posStep = new BlockPos(pos.getX() + dir.getStepX(), pos.getY() + dir.getStepY(), pos.getZ() + dir.getStepZ());
            BlockState state = level.getBlockState(posStep);
            if (state.isSolidRender(level, posStep) && (state != stateBlock.state || level.getBlockState(posStep) != stateBlock.state)) {
                level.setBlock(pos, stateBlock.state, 3);
                return;
            }
        }
    }
}
