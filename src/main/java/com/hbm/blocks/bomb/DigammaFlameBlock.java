package com.hbm.blocks.bomb;

import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DigammaFlameBlock extends Block {

    public DigammaFlameBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity living) {
            ContaminationUtil.contaminate(living, HazardType.DIGAMMA, ContaminationType.DIGAMMA, 0.05F);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos posBelow = pos.below();
        return level.getBlockState(posBelow).isFaceSturdy(level, posBelow, Direction.UP);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if(!canSurvive(state, level, pos)) level.removeBlock(pos, false);
    }
}
