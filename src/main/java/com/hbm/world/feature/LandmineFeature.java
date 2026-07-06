package com.hbm.world.feature;

import com.hbm.blockentity.bomb.LandmineBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.config.NtmConfig;
import com.hbm.main.NuclearTechMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LandmineFeature extends Feature<NoneFeatureConfiguration> {

    public LandmineFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        int x = origin.getX();
        int z = origin.getZ();
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);

        for(int g = y + 2; g >= y; g--) {
            BlockPos below = new BlockPos(x, g - 1, z);
            BlockState belowState = level.getBlockState(below);

            if(belowState.isFaceSturdy(level, below, Direction.UP)) {
                BlockPos minePos = new BlockPos(x, g, z);
                level.setBlock(minePos, NtmBlocks.MINE_AP.get().defaultBlockState(), 3);

                BlockEntity be = level.getBlockEntity(minePos);
                if(be instanceof LandmineBlockEntity landmine) {
                    landmine.waitingForPlayer = true;
                }

                if(NtmConfig.COMMON.ENABLE_DEBUG_MODE.get()) NuclearTechMod.LOGGER.info("[Debug] Successfully spawned landmine at {} {} {}", x, g, z);
                return true;
            }
        }

        return false;
    }
}
