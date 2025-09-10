package com.hbm.blocks.gas;

import com.hbm.extprop.HbmLivingProps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGasCoal extends GasBase {

    public BlockGasCoal(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() + random.nextDouble();
        double z = pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity livingEntity) {
//            if(!ArmorRegistry.hasProtection(living, 3, HazardClass.PARTICLE_COARSE)) {
            HbmLivingProps.incrementBlackLung(livingEntity, 10);
//            }
        }
    }

    @Override
    public Direction getFirstDirection(Level level, BlockPos pos) {
        if (level.getRandom().nextInt(5) == 0) {
            return Direction.DOWN;
        }
        return Direction.getRandom(level.getRandom());
    }

    @Override
    public Direction getSecondDirection(Level level, BlockPos pos) {
        return this.randomHorizontal(level.getRandom());
    }

    public Direction randomHorizontal(RandomSource random) {
        return Direction.Plane.HORIZONTAL.getRandomDirection(random);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(20) == 0) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return;
        }
        super.tick(state, level, pos, random);
    }
}