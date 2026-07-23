package com.hbm.world.feature;

import com.hbm.blocks.NtmBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BedrockOilDepositFeature extends Feature<NoneFeatureConfiguration> {

    public BedrockOilDepositFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        int minY = level.getMinBuildHeight();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean placed = false;

        for(int dx = -4; dx <= 4; dx++) {
            for(int dz = -4; dz <= 4; dz++) {
                for(int dy = 0; dy <= 4; dy++) {
                    if(Math.abs(dx) + Math.abs(dz) + dy > 6) {
                        continue;
                    }

                    pos.set(origin.getX() + dx, minY + dy, origin.getZ() + dz);
                    if(level.getBlockState(pos).is(Blocks.BEDROCK)) {
                        level.setBlock(pos, NtmBlocks.ORE_BEDROCK_OIL.get().defaultBlockState(), 3);
                        placed = true;
                    }
                }
            }
        }

        if(placed) {
            OilSpot.generateOilSpot(level, origin.getX(), origin.getZ(), 5, 50, true);
        }

        return placed;
    }
}
