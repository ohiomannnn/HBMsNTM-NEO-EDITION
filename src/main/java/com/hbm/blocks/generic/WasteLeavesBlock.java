package com.hbm.blocks.generic;

import com.hbm.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WasteLeavesBlock extends Block {

    public WasteLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource rand) {
        if (rand.nextInt(30) == 0) {
            serverLevel.removeBlock(pos, false);

//            if (serverLevel.getBlockState(pos.below()).isAir()) {
//                FallingBlockEntity leaves = FallingBlockEntity.fall(serverLevel, pos, ModBlocks.LEAVES_LAYER.defaultBlockState());
//                leaves.time = 2;
//                leaves.dropItem = false;
//                serverLevel.addFreshEntity(leaves);
//            }
        }
        super.randomTick(state, serverLevel, pos, rand);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        super.animateTick(state, level, pos, rand);

        if (rand.nextInt(7) == 0 && level.getBlockState(pos.below()).isAir()) {
                level.addParticle(
                        ModParticles.DEAD_LEAF.get(),
                        false,
                        pos.getX() + rand.nextDouble(),
                        pos.getY() - 0.05,
                        pos.getZ() + rand.nextDouble(),
                        1, 0.0,0.0
                );
        }
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return false;
    }
}