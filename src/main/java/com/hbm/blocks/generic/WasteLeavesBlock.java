package com.hbm.blocks.generic;

import com.hbm.HBMsNTMClient;
import com.hbm.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WasteLeavesBlock extends Block {

    public WasteLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource rand) {
        if (rand.nextInt(30) == 0) {
            serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            if (serverLevel.getBlockState(pos.below()).isAir()) {
                FallingBlockEntity leaves = FallingBlockEntity.fall(serverLevel, pos, ModBlocks.LEAVES_LAYER.get().defaultBlockState());
                leaves.time = 2;
                leaves.dropItem = false;
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (rand.nextInt(7) == 0 && level.getBlockState(pos.below()).isAir()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "deadleaf");
            tag.putDouble("posX", pos.getX() + rand.nextDouble());
            tag.putDouble("posY", pos.getY() - 0.05);
            tag.putDouble("posZ", pos.getZ() + rand.nextDouble());
            HBMsNTMClient.effectNT(tag);
        }
    }
}