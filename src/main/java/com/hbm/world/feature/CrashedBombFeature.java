package com.hbm.world.feature;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.bomb.CrashedBombBlock;
import com.hbm.blocks.bomb.CrashedBombBlock.DudType;
import com.hbm.config.NtmConfig;
import com.hbm.inventory.NtmTags;
import com.hbm.main.NuclearTechMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CrashedBombFeature extends Feature<NoneFeatureConfiguration> {

    public CrashedBombFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        int i = level.getRandom().nextInt(1);

        if(i == 0) {
            if(!locationValid(level, origin)) return false;
            level.setBlock(origin, NtmBlocks.CRASHED_BOMB.get().defaultBlockState().setValue(CrashedBombBlock.SUBTYPE, level.getRandom().nextInt(DudType.values().length)), 3);
            if(NtmConfig.COMMON.ENABLE_DEBUG_MODE.get()) NuclearTechMod.LOGGER.info("[Debug] Successfully spawned dud at {}", origin);
            return true;
        }

        return false;
    }

    protected boolean locationValid(WorldGenLevel level, BlockPos pos) {

        BlockState checkBlock = level.getBlockState(pos.below());
        BlockState blockAbove = level.getBlockState(pos);
        BlockState blockBelow = level.getBlockState(pos.below(2));

        for(Block b : getValidSpawnBlocks()) {
            if(!blockAbove.isAir()) return false;
            if(checkBlock.is(b)) return true;
            if(checkBlock.is(Blocks.SNOW) && blockBelow.is(b)) return true;
            if(checkBlock.is(NtmTags.Blocks.PLANTS) && blockBelow.is(b)) return true;
        }

        return false;
    }

    protected Block[] getValidSpawnBlocks() {
        return new Block[] {
                Blocks.GRASS_BLOCK,
                Blocks.DIRT,
                Blocks.STONE,
                Blocks.SAND,
                Blocks.SANDSTONE,
        };
    }

}
