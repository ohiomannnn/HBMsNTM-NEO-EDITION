package com.hbm.explosion.vanillant.standard;

import com.google.common.collect.Sets;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.IBlockAllocator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;

public class BlockAllocatorStandard implements IBlockAllocator {

    private final int resolution;

    public BlockAllocatorStandard() {
        this(16);
    }

    public BlockAllocatorStandard(int resolution) {
        this.resolution = resolution;
    }

    @Override
    public HashSet<BlockPos> allocate(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {
        HashSet<BlockPos> affectedBlocks = Sets.newHashSet();

        for (int i = 0; i < this.resolution; ++i) {
            for (int j = 0; j < this.resolution; ++j) {
                for (int k = 0; k < this.resolution; ++k) {
                    if (i == 0 || i == this.resolution - 1 || j == 0 || j == this.resolution - 1 || k == 0 || k == this.resolution - 1) {
                        double rayX = (double) i / (this.resolution - 1.0F) * 2.0F - 1.0F;
                        double rayY = (double) j / (this.resolution - 1.0F) * 2.0F - 1.0F;
                        double rayZ = (double) k / (this.resolution - 1.0F) * 2.0F - 1.0F;
                        double length = Math.sqrt(rayX * rayX + rayY * rayY + rayZ * rayZ);
                        rayX /= length;
                        rayY /= length;
                        rayZ /= length;

                        float currentPower = size * (0.7F + level.random.nextFloat() * 0.6F);
                        Vec3 currentPos = new Vec3(x, y, z);

                        for (float step = 0.3F; currentPower > 0.0F; currentPower -= step * 0.75F) {
                            BlockPos blockpos = BlockPos.containing(currentPos);
                            BlockState blockstate = level.getBlockState(blockpos);
                            FluidState fluidstate = level.getFluidState(blockpos);

                            if (!blockstate.isAir() || !fluidstate.isEmpty()) {
                                float resistance = Math.max(blockstate.getExplosionResistance(level, blockpos, null), fluidstate.getExplosionResistance(level, blockpos, null));
                                currentPower -= (resistance + 0.3F) * step;
                            }

                            if (currentPower > 0.0F) {
                                affectedBlocks.add(blockpos);
                            }

                            currentPos = currentPos.add(rayX * step, rayY * step, rayZ * step);
                        }
                    }
                }
            }
        }
        return affectedBlocks;
    }
}
