package com.hbm.blocks.gas;

import com.hbm.blocks.ModBlocks;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.lib.ModEffect;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GasRadonTombBlock extends GasBaseBlock {

    /**
     * You should not have come here.
     * <p>
     * This is not a place of honor. No great deed is commemorated here.
     * <p>
     * Nothing of value is here.
     * <p>
     * What is here is dangerous and repulsive.
     * <p>
     * We considered ourselves a powerful culture. We harnessed the hidden fire,
     * and used it for our own purposes.
     * <p>
     * Then we saw the fire could burn within living things, unnoticed until it
     * destroyed them.
     * <p>
     * And we were afraid.
     * <p>
     * We built great tombs to hold the fire for one hundred thousand years,
     * after which it would no longer kill.
     * <p>
     * If this place is opened, the fire will not be isolated from the world,
     * and we will have failed to protect you.
     * <p>
     * Leave this place and never come back.
     */
    public GasRadonTombBlock(Properties properties) {
        super(properties, 0.1F, 0.3F, 0.1F);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.removeEffect(ModEffect.RADAWAY); //get fucked
            livingEntity.removeEffect(ModEffect.RADX);

            ContaminationUtil.contaminate(livingEntity, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 0.5F);
            HbmLivingAttachments.incrementAsbestos(livingEntity, 10);
        }
    }

    @Override
    public Direction getFirstDirection(Level level, BlockPos pos) {
        if (level.getRandom().nextInt(3) == 0) {
            return Direction.UP;
        }
        return Direction.DOWN;
    }

    @Override
    public Direction getSecondDirection(Level level, BlockPos pos) {
        return this.randomHorizontal(level.getRandom());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) {

            if (random.nextInt(10) == 0) {
                BlockPos below = pos.below();
                BlockState stateBelow = level.getBlockState(below);
                Block b = stateBelow.getBlock();

                if (b == Blocks.GRASS_BLOCK) {
                    if (random.nextInt(5) == 0) {
                        level.setBlock(below, Blocks.DIRT.defaultBlockState(), 3);
                    } else {
                        level.setBlock(below, ModBlocks.WASTE_EARTH.get().defaultBlockState(), 3);
                    }
                }

                boolean removable =
                        stateBelow.is(BlockTags.LEAVES)
                                || stateBelow.is(BlockTags.REPLACEABLE)
                                || stateBelow.is(BlockTags.MOSS_REPLACEABLE)
                                || stateBelow.is(BlockTags.CLIMBABLE);

                if (removable && !stateBelow.isCollisionShapeFullBlock(level, below)) {
                    level.removeBlock(below, false);
                }
            }

            if (random.nextInt(600) == 0) {
                level.removeBlock(pos, false);
                return;
            }
        }

        super.tick(state, level, pos, random);
    }
}
