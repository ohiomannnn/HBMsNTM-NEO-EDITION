package com.hbm.blocks.gas;

import com.hbm.lib.ModDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GasMonoxideBlock extends GasBaseBlock {

    public GasMonoxideBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            DamageSource src = new DamageSource(
                    livingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.MONOXIDE)
            );
            livingEntity.hurt(src, 1);
        }
    }

    @Override
    public Direction getFirstDirection(Level level, BlockPos pos) {
        return Direction.DOWN;
    }

    @Override
    public Direction getSecondDirection(Level level, BlockPos pos) {
        return this.randomHorizontal(level.getRandom());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return;
        }
        super.tick(state, level, pos, random);
    }
}